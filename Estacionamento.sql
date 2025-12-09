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

SELECT * FROM vw_informacoes_vaga;
	
	

INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 
INSERT INTO vaga (status) VALUES (false); 

INSERT INTO cliente (nome, email, telefone) VALUES 
('Luiz Francisco', 'luiz@ifpr.edu.br', '46999010101'),
('Maria Silva', 'maria@gmail.com', '46999020202'),
('João Souza', 'joao@hotmail.com', '41988030303'),
('Ana Pereira', 'ana.p@bol.com.br', '11977040404'),
('Carlos Lima', 'carlos@empresa.com', '46991050505');

INSERT INTO veiculo (modelo, cor, placa, id_cliente) VALUES 
('Honda Civic', 'Prata', 'ABC1234', 1),
('VW Gol', 'Branco', 'XYZ9876', 2),
('Fiat Palio', 'Vermelho', 'DEF4567', 3),
('Toyota Corolla', 'Preto', 'GHI7890', 4),
('Chevrolet Onix', 'Azul', 'JKL3210', 5),
('Ford Ka', 'Branco', 'MNO6543', 1);

INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '1 day 2 hours', NOW() - INTERVAL '1 day', 10.00, 20.00, 1, 1);

INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '1 day 6 hours', NOW() - INTERVAL '1 day 1 hour', 10.00, 50.00, 2, 2);

INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '4 hours', NOW() - INTERVAL '3 hours 30 minutes', 10.00, 10.00, 3, 3);

INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '15 hours', NOW() - INTERVAL '3 hours', 10.00, 120.00, 4, 4);


INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '2 hours', NOW() - INTERVAL '50 minutes', 10.00, 20.00, 5, 5);


INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '1 hour', NULL, 10.00, NULL, 6, 8);


INSERT INTO operacao (horario_entrada, horario_saida, valor_hora, valor_total, id_veiculo, id_vaga) 
VALUES (NOW() - INTERVAL '15 minutes', NULL, 10.00, NULL, 1, 9);

select * from cliente;
select * from veiculo;
select * from vaga;
select * from operacao;
