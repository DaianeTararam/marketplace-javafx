CREATE DATABASE db_ArteCRAFT
GO
USE db_ArteCRAFT
GO
create user usuario for login usuario
go
alter role db_owner add member usuario
go
CREATE TABLE usuario (
email    VARCHAR(40)  NOT NULL UNIQUE,
username VARCHAR(20)  NOT NULL,
senha    VARCHAR(15)  NOT NULL,
nome     VARCHAR(50)  NOT NULL,
PRIMARY KEY (username)
)
GO
 
CREATE TABLE telefone (
ddd    CHAR(2) NOT NULL,
numero CHAR(9) NOT NULL,
PRIMARY KEY (ddd, numero)
)
GO
CREATE TABLE endereco (
cep         CHAR(8)       NOT NULL,
numero      VARCHAR(5)    NOT NULL,
cidade      VARCHAR(100)  NOT NULL,
logradouro  VARCHAR(100)  NOT NULL,
complemento VARCHAR(200),
bairro      VARCHAR(100)  NOT NULL,
estado      VARCHAR(100)  NOT NULL,
PRIMARY KEY (cep, numero, cidade)
)
GO
CREATE TABLE vendedor (
username VARCHAR(20) NOT NULL,
cnpj     CHAR(14)    NOT NULL UNIQUE,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES usuario(username)
)
GO
CREATE TABLE comprador (
username      VARCHAR(20) NOT NULL,
cep           CHAR(8)     NOT NULL,
numero        VARCHAR(5)  NOT NULL,
cidade        VARCHAR(100) NOT NULL,
ddd           CHAR(2)     NOT NULL,
num_telefone  CHAR(9)     NOT NULL,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES usuario(username),
FOREIGN KEY (cep, numero, cidade) REFERENCES endereco(cep, numero, cidade),
FOREIGN KEY (ddd, num_telefone) REFERENCES telefone(ddd, numero)
)
GO

CREATE TABLE loja (
id            INT IDENTITY(2000, 1) NOT NULL,
nome          VARCHAR(30)  NOT NULL,
user_vendedor VARCHAR(20)  NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (user_vendedor) REFERENCES vendedor(username)
)
GO
 
CREATE TABLE produto (
id        INT IDENTITY(4100, 1) NOT NULL,
nome      VARCHAR(100)  NOT NULL,
preco     DECIMAL(7,2) NOT NULL,
descricao VARCHAR(200),
categoria VARCHAR(100),
id_loja   INT           NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id_loja) REFERENCES loja(id)
)
GO
 
CREATE TABLE pedido (
id             INT IDENTITY(3100, 1) NOT NULL,
data_pedido    DATE          NOT NULL,
status         VARCHAR(20)   NOT NULL,
valor_total    DECIMAL(10,2) NOT NULL,
user_comprador VARCHAR(20)   NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (user_comprador) REFERENCES comprador(username)
)
GO
 
CREATE TABLE item_pedido (
id             INT IDENTITY(1, 1) NOT NULL,
quantidade     INT           NOT NULL,
valor_unitario DECIMAL(7, 2) NOT NULL,
id_pedido      INT           NOT NULL,
id_produto     INT           NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id_pedido)  REFERENCES pedido(id),
FOREIGN KEY (id_produto) REFERENCES produto(id)
)
GO

--Insercao de dados ficticios para os meus testes
INSERT INTO usuario (email, username, senha, nome) VALUES
('maria@artcraft.com', 'maria_crochet', 'senha123', 'Maria Oliveira'),
('joao@artcraft.com', 'joao_bordado', 'senha123', 'João Pereira'),
('ana@artcraft.com','ana_ceramica', 'senha123', 'Ana Souza'),
('carlos@artcraft.com','carlos_compra', 'senha123', 'Carlos Mendes'),
('lucia@artcraft.com',  'lucia_compra', 'senha123', 'Lucia Ferreira')
GO
INSERT INTO telefone (ddd, numero) VALUES
('11', '991234567'),
('21', '998765432'),
('31', '997654321')
GO
INSERT INTO endereco (cep, numero, cidade, logradouro, complemento, bairro, estado) VALUES
('01310100', '100', 'São Paulo', 'Av. Paulista', 'Apto 42', 'Bela Vista', 'SP'),
('20040020', '200', 'Rio de Janeiro', 'Rua do Ouvidor', NULL, 'Centro', 'RJ'),
('30130010', '50',  'Belo Horizonte', 'Rua da Bahia', 'Casa', 'Centro', 'MG')
GO
INSERT INTO vendedor (username, cnpj) VALUES
('maria_crochet', '12345678000195'),
('joao_bordado',  '98765432000188'),
('ana_ceramica',  '11222333000144')
GO
INSERT INTO comprador (username, cep, numero, cidade, ddd, num_telefone) VALUES
('carlos_compra', '01310100', '100', 'São Paulo', '11', '991234567'),
('lucia_compra', '20040020', '200', 'Rio de Janeiro', '21', '998765432')
GO
INSERT INTO loja (nome, user_vendedor) VALUES
('Ateliê da Maria', 'maria_crochet'),
('Casa do Bordado', 'joao_bordado'),
('Cerâmica da Ana', 'ana_ceramica')
GO
INSERT INTO produto (nome, preco, descricao, categoria, id_loja) VALUES
('Bolsa de Crochê Bege', 89.90,  'Bolsa feita à mão com fio 100% algodão', 'Crochê', 2000),
('Toalha de Mesa Crochê', 59.90,  'Toalha redonda para mesa de 4 lugares', 'Crochê', 2000),
('Tapete Oval Colorido', 120.00, 'Tapete em fio de malha, lavável','Crochê', 2000),
('Xale de Tricô Azul', 75.00, 'Xale em tricô ponto rendado, tamanho único', 'Tricô', 2000),
('Quadro Bordado Floral', 149.90,'Bordado em ponto cruz, moldura inclusa', 'Bordado', 2001),
('Almofada Bordada Rosa', 69.90, 'Almofada 40x40cm com enchimento', 'Bordado',2001),
('Caminho de Mesa Bordado', 85.00, 'Caminho 1,5m com bordado manual nas bordas', 'Bordado',  2001),
('Vaso Cerâmica Rústico', 110.00, 'Vaso artesanal queimado em forno a lenha', 'Cerâmica', 2002),
('Xícara Cerâmica Pintada', 45.00, 'Xícara 200ml pintada à mão, peça única','Cerâmica', 2002),
('Prato Decorativo Cerâmica', 95.00, 'Prato 25cm com esmalte artesanal azul', 'Cerâmica', 2002),
('Bowl para Salada', 130.00, 'Bowl grande 30cm, ideal para saladas', 'Cerâmica', 2002),
('Brinco Macramê Bege', 35.00, 'Brinco artesanal em macramê natural', 'Macramê',  2001),
('Painel Macramê Parede', 199.90, 'Painel 60x80cm para decoração', 'Macramê',  2001),
('Pulseira Trançada', 25.00, 'Pulseira em couro e fio encerado', 'Bijuteria',2000),
('Colar Pedras Naturais', 79.90,  'Colar com pedras semi-preciosas naturais', 'Bijuteria',2002);
GO
INSERT INTO pedido (data_pedido, status, valor_total, user_comprador) VALUES
('2024-05-10', 'ENTREGUE', 149.80, 'carlos_compra'),
('2024-05-22', 'ENTREGUE', 110.00, 'carlos_compra'),
('2024-06-01', 'PENDENTE', 204.90, 'lucia_compra'),
('2024-06-03', 'ENVIADO', 70.00, 'lucia_compra')
GO
INSERT INTO item_pedido (quantidade, valor_unitario, id_pedido, id_produto) VALUES
(1, 89.90, 3100, 4100),
(1, 59.90, 3100, 4101),
(1, 110.00, 3101, 4107),
(1, 149.90, 3102, 4104),
(1,  55.00, 3102, 4111),
(2, 45.00, 3103, 4108)
GO
