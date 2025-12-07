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
		UPDATE vaga
		SET status = true
		WHERE id_vaga = NEW.id_vaga;
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
	

INSERT INTO vaga (status) VALUES (false);
INSERT INTO vaga (status) VALUES (false);
INSERT INTO vaga (status) VALUES (false);
INSERT INTO vaga (status) VALUES (false);
INSERT INTO vaga (status) VALUES (false);

select * from cliente;
select * from veiculo;
select id_cliente, nome, email, telefone from cliente;