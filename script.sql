DROP DATABASE prueba_tecnica;
CREATE DATABASE IF NOT EXISTS prueba_tecnica;
SHOW DATABASES;
USE prueba_tecnica;
CREATE TABLE IF NOT EXISTS articulo (
    id_articulo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL
);
describe articulo;
INSERT INTO articulo (nombre, descripcion, precio) VALUES ('articulo prueba', 'Es solo un articulo de descripción', 19.9);
INSERT INTO articulo (nombre, descripcion, precio) VALUES ('articulos prueba 2', 'Es solo un articulo de descripción 2', 2000);
INSERT INTO articulo (nombre, descripcion, precio) VALUES ('articulo prueba 3', 'Es solo un articulo de descripción 3', 2000);
select * from articulo;
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre_cliente VARCHAR(70) NOT NULL,
    rfc VARCHAR(255) NOT NULL UNIQUE
);
describe cliente;
INSERT INTO cliente (nombre_cliente, rfc) VALUES ('Cliente 1', 'RFC1234567890');
CREATE TABLE IF NOT EXISTS pedido (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    descripcion_pedido VARCHAR(255),
    CONSTRAINT id_cliente_foreign FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
);
describe pedido;
INSERT INTO pedido (id_cliente, descripcion_pedido) VALUES ( (SELECT id_cliente FROM cliente WHERE nombre_cliente = 'Cliente 1' LIMIT 1), 'Pedido prueba');
CREATE TABLE IF NOT EXISTS partidas_pedido (
    id_partida INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    id_articulo INT NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT id_pedido_foreign FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido),
    CONSTRAINT id_articulo_foreign FOREIGN KEY (id_articulo)
        REFERENCES articulo (id_articulo)
);
describe partidas_pedido;
INSERT INTO partidas_pedido (id_pedido, id_articulo, cantidad) VALUES (
	(SELECT pedido.id_pedido FROM cliente, pedido WHERE cliente.id_cliente = pedido.id_cliente LIMIT 1),
	 (SELECT articulo.id_articulo FROM articulo WHERE articulo.nombre = 'articulo prueba'),
     20
 );
show tables;
describe partidas_pedido;
