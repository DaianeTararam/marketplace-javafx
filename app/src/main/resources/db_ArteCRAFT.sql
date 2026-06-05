CREATE DATABASE db_ArteCRAFT
GO
USE db_ArteCRAFT
GO
create user usuario for login usuario
go
alter role db_owner add member usuario
go
CREATE TABLE usuario(
email		VARCHAR(40) UNIQUE NOT NULL,
username	VARCHAR(20) NOT NULL,
senha		VARCHAR(15) NOT NULL,
nome		VARCHAR(50) NOT NULL
PRIMARY KEY(username)
)
GO
CREATE TABLE telefone(
ddd			CHAR(2) NOT NULL,
numero		CHAR(9) NOT NULL
PRIMARY KEY(ddd, numero)
)
GO 
CREATE TABLE endereco(
logradouro	VARCHAR(100) NOT NULL,
numero		VARCHAR(5) NOT NULL,
complemento	VARCHAR(200),
bairro		VARCHAR(100) NOT NULL,
cidade		VARCHAR(100) NOT NULL,
estado		VARCHAR(100) NOT NULL,
cep			CHAR(8) NOT NULL
PRIMARY KEY(cep, numero, cidade)
)
GO
CREATE TABLE comprador(
username	VARCHAR(20) NOT NULL,
numero		VARCHAR(5) NOT NULL,
cidade		VARCHAR(100) NOT NULL,
cep			CHAR(8) NOT NULL,
ddd			CHAR(2) NOT NULL,
num_telefone	CHAR(9) NOT NULL
PRIMARY KEY(username)
FOREIGN KEY(username) REFERENCES usuario(username),
FOREIGN KEY(ddd, num_telefone) REFERENCES telefone(ddd, numero),
FOREIGN KEY(cep, numero, cidade) REFERENCES endereco(cep, numero, cidade)
)
GO
CREATE TABLE vendedor(
username	VARCHAR(20) NOT NULL,
cnpj		CHAR(14) UNIQUE NOT NULL,
PRIMARY KEY(username),
FOREIGN KEY(username) REFERENCES usuario(username)
)
GO
CREATE TABLE loja(
id		INT IDENTITY(2000, 1) NOT NULL,
nome	VARCHAR(30) NOT NULL,
user_vendedor VARCHAR(20) NOT NULL
PRIMARY KEY(id),
FOREIGN KEY(user_vendedor) REFERENCES vendedor(username)
)
GO
CREATE TABLE produto(
id		INT IDENTITY(4100,1) NOT NULL,
nome	VARCHAR(100) NOT NULL,
preco	DECIMAL(7,2) NOT NULL,	
descricao	VARCHAR(200),
categoria	VARCHAR(100),
id_loja	INT
PRIMARY KEY(id)
FOREIGN KEY(id_loja) REFERENCES loja(id)
)
GO
CREATE TABLE pedido(
id		INT IDENTITY(3100, 1) NOT NULL,
data_pedido DATE NOT NULL,
status		VARCHAR(20) NOT NULL,
valor_total DECIMAL(10,2) NOT NULL,
user_comprador VARCHAR(20) NOT NULL
PRIMARY KEY(id)
FOREIGN KEY(user_comprador) REFERENCES comprador(username)
)
GO
CREATE TABLE item_pedido(
id		INT IDENTITY(1, 1) NOT NULL,
quantidade	INT NOT NULL,
valor_unitario DECIMAL(7,2) NOT NULL,
id_pedido	INT NOT NULL,
id_produto  INT NOT NULL
PRIMARY KEY(id)
FOREIGN KEY(id_pedido) REFERENCES pedido(id),
FOREIGN KEY(id_produto) REFERENCES produto(id)
)
GO
select * from usuario


-- Filtro de busca por categoria:
SELECT * FROM produto 
WHERE categoria = 'Crochê'

--Soma do valor total de pedidos de um cliente:
SELECT SUM(valor_total) AS total_gasto FROM pedido 
WHERE user_comprador = 'ana_artes'

--Média de preço dos produtos de uma loja específica:
SELECT AVG(preco) AS preco_medio FROM produto 
WHERE id_loja = 1

--Contagem total de usuários cadastrados no sistema:
SELECT COUNT(*) AS total_usuarios 
FROM usuario;

--Buscar usuário específico pelo e-mail (usado em recuperação de senha):
SELECT * FROM usuario 
WHERE email = 'contato@loja.com'

--5 Avançadas (Com INNER JOIN)
--Trazer os dados cadastrais + CNPJ dos vendedores (Herança):
SELECT u.nome, u.email, v.cnpj 
FROM vendedor v INNER JOIN usuario u 
ON v.username = u.username

--Listar produtos trazendo junto o nome da Loja à qual ele pertence:
SELECT p.nome AS nome_produto, p.preco, l.nome AS nome_loja 
FROM produto p INNER JOIN loja l 
ON p.id_loja = l.id

--Histórico de pedidos de um comprador (trazendo o nome dele da tabela de usuário):
SELECT ped.id, ped.data_pedido, ped.valor_total, u.nome 
FROM pedido ped INNER JOIN usuario u 
ON ped.user_comprador = u.username 
WHERE ped.user_comprador = 'comprador_username'

--Listar os itens de dentro de um pedido com o nome do produto real:
SELECT item.quantidade, item.valor_unitario, prod.nome 
FROM item_pedido item INNER JOIN produto prod 
ON item.id_produto = prod.id 
WHERE item.id_pedido = 1

--Identificar o nome do Vendedor responsável por uma Loja específica:
SELECT l.nome AS nome_loja, u.nome AS nome_vendedor 
FROM loja l INNER JOIN usuario u 
ON l.user_vendedor = u.username

use master
DROP DATABASE db_ArteCRAFT

select * from usuario
select * from vendedor
