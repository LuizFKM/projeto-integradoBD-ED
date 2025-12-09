DROP TABLE IF EXISTS operacao, vaga, veiculo, cliente CASCADE;

CREATE TABLE cliente (
	id_cliente SERIAL PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	email VARCHAR(100)NOT NULL,
	telefone VARCHAR(20)NOT NULL
);

CREATE TABLE veiculo(
	id_veiculo SERIAL PRIMARY KEY,
	modelo VARCHAR(100) NOT NULL,
	cor VARCHAR(12) NOT NULL,
	placa VARCHAR(7) NOT NULL UNIQUE,
	id_cliente INT NOT NULL,
	CONSTRAINT fk_veiculo_cliente 
		FOREIGN KEY (id_cliente) 
		REFERENCES cliente(id_cliente) 
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE vaga(
	id_vaga SERIAL PRIMARY KEY,
	status BOOLEAN DEFAULT FALSE
);

CREATE table operacao(
	id_operacao SERIAL PRIMARY KEY,
	horario_entrada TIMESTAMP NOT NULL,
	horario_saida TIMESTAMP,
	valor_hora DECIMAL(10,2) NOT NULL DEFAULT 0.00,
	valor_total DECIMAL(10, 2),
	id_veiculo INT NOT NULL,
	id_vaga INT NOT NULL,
	CONSTRAINT  fk_operacao_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo (id_veiculo)
	ON DELETE RESTRICT
	ON UPDATE CASCADE,
	CONSTRAINT fk_operacao_vaga FOREIGN KEY (id_vaga) REFERENCES vaga (id_vaga) 
	ON DELETE RESTRICT
	ON UPDATE CASCADE
);

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    role VARCHAR(20) CHECK (role in ('ADMIN', 'COLABORADOR')) NOT NULL
);

CREATE ROLE admin LOGIN PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE estacionamento TO admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin;

CREATE ROLE colaborador LOGIN PASSWORD 'colab123';
GRANT CONNECT ON DATABASE estacionamento TO colaborador;
GRANT UPDATE, SELECT, INSERT ON operacao, cliente, veiculo TO colaborador;

CREATE OR REPLACE FUNCTION fn_atualizar_status_da_vaga()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.horario_saida IS NULL THEN
            UPDATE vaga
            SET status = true
            WHERE id_vaga = NEW.id_vaga;
        END IF;  
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.horario_saida IS NULL AND NEW.horario_saida IS NOT NULL THEN
            UPDATE vaga SET status = false WHERE id_vaga = NEW.id_vaga;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_atualiza_status_da_vaga
AFTER INSERT OR UPDATE ON OPERACAO
FOR EACH ROW
EXECUTE FUNCTION fn_atualizar_status_da_vaga();

CREATE OR REPLACE FUNCTION fn_bloquear_vaga_ocupada()
RETURNS TRIGGER AS $$
BEGIN
	IF EXISTS(SELECT 1 FROM vaga WHERE id_vaga = NEW.id_vaga AND status = true) THEN
		RAISE EXCEPTION 'A vaga % já está ocupada!', NEW.id_vaga;
	END IF;

	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_impedir_duplicidade_vaga
BEFORE INSERT ON operacao
FOR EACH ROW
EXECUTE FUNCTION fn_bloquear_vaga_ocupada();

CREATE OR REPLACE VIEW vw_informacoes_vaga AS SELECT v.id_vaga, 
	COUNT(o.id_operacao) AS quantidade_operacoes,
	SUM(o.valor_total) AS valor_total_acumulado,
	CAST(SUM(o.horario_saida - o.horario_entrada) AS INTERVAL(0)) AS tempo_total_acumulado
FROM vaga AS v
LEFT JOIN operacao AS o ON v.id_vaga = o.id_vaga
GROUP BY v.id_vaga
ORDER BY v.id_vaga;


CREATE VIEW vw_informacoes_vaga_jasper AS 
SELECT 
    v.id_vaga, 
    COUNT(o.id_operacao) AS quantidade_operacoes,
    COALESCE(SUM(o.valor_total), 0) AS valor_total_acumulado,
    (
        FLOOR(EXTRACT(EPOCH FROM SUM(o.horario_saida - o.horario_entrada)) / 3600)::text 
        || ':' || 
        TO_CHAR(EXTRACT(MINUTE FROM SUM(o.horario_saida - o.horario_entrada)), 'FM00') 
        || ':' || 
        TO_CHAR(EXTRACT(SECOND FROM SUM(o.horario_saida - o.horario_entrada)), 'FM00')
    ) AS tempo_total_acumulado

FROM vaga AS v
LEFT JOIN operacao AS o ON v.id_vaga = o.id_vaga
GROUP BY v.id_vaga
ORDER BY v.id_vaga;

CREATE MATERIALIZED VIEW mv_faturamento_mensal AS
SELECT 
    TO_CHAR(horario_saida, 'YYYY-MM') AS mes_referencia,
    COUNT(id_operacao) AS total_veiculos,
    SUM(valor_total) AS faturamento_total,
    AVG(valor_total)::DECIMAL(10,2) AS ticket_medio
FROM operacao
WHERE horario_saida IS NOT NULL
GROUP BY TO_CHAR(horario_saida, 'YYYY-MM')
ORDER BY mes_referencia DESC
WITH DATA;



CREATE OR REPLACE PROCEDURE pr_registrar_entrada(
    p_id_veiculo INT, 
    p_id_vaga INT,
    p_valor_hora DECIMAL
)
LANGUAGE plpgsql
AS $$
BEGIN
   
    INSERT INTO operacao (horario_entrada, valor_hora, id_veiculo, id_vaga)
    VALUES (NOW(), p_valor_hora, p_id_veiculo, p_id_vaga);

    COMMIT;
END;
$$;

CREATE OR REPLACE PROCEDURE pr_registrar_saida_manual(
    p_id_operacao INT,
    p_valor_total DECIMAL
)
LANGUAGE plpgsql
AS $$
BEGIN   
    UPDATE operacao
    SET horario_saida = NOW(),
        valor_total = p_valor_total
    WHERE id_operacao = p_id_operacao;
    COMMIT;
END;
$$;

CREATE OR REPLACE FUNCTION fn_calcular_valor_total(p_id_operacao INT)
RETURNS DECIMAL(10,2)
LANGUAGE plpgsql
AS $$
DECLARE
    v_entrada TIMESTAMP;
    v_valor_hora DECIMAL(10,2);
    v_horas_permanencia DECIMAL;
    v_total DECIMAL(10,2);
BEGIN
   
    SELECT horario_entrada, valor_hora INTO v_entrada, v_valor_hora
    FROM operacao 
    WHERE id_operacao = p_id_operacao;

   
    IF v_entrada IS NULL THEN
        RETURN 0.00;
    END IF;


    v_horas_permanencia := EXTRACT(EPOCH FROM (NOW() - v_entrada)) / 3600;

   
    v_horas_permanencia := CEIL(v_horas_permanencia);
    
    IF v_horas_permanencia < 1 THEN 
        v_horas_permanencia := 1; 
    END IF;


    v_total := v_horas_permanencia * v_valor_hora;

    RETURN v_total;
END;
$$;

CREATE OR REPLACE PROCEDURE pr_realizar_checkout(p_id_operacao INT)
LANGUAGE plpgsql
AS $$
DECLARE
    v_valor_final DECIMAL(10,2);
BEGIN

    v_valor_final := fn_calcular_valor_total(p_id_operacao);

    UPDATE operacao
    SET horario_saida = NOW(),
        valor_total = v_valor_final
    WHERE id_operacao = p_id_operacao;
    
    COMMIT;
END;
$$;

CREATE OR REPLACE FUNCTION fn_relatorio_periodo(
    p_data_inicio TIMESTAMP, 
    p_data_fim TIMESTAMP
)
RETURNS TABLE (
    cod_operacao INT,
    nome_cliente VARCHAR,
    placa_veiculo VARCHAR,
    data_entrada TIMESTAMP,
    data_saida TIMESTAMP,
    valor_cobrado DECIMAL
) 
LANGUAGE plpgsql
AS $$
BEGIN
    
    RETURN QUERY 
    SELECT 
        o.id_operacao,
        c.nome,
        v.placa,
        o.horario_entrada,
        o.horario_saida,
        o.valor_total
    FROM operacao o
    INNER JOIN veiculo v ON o.id_veiculo = v.id_veiculo
    INNER JOIN cliente c ON v.id_cliente = c.id_cliente
    WHERE o.horario_saida BETWEEN p_data_inicio AND p_data_fim
    ORDER BY o.horario_saida DESC;
END;
$$;




