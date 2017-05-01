CREATE TABLE IF NOT EXISTS users (
  `id` int(20) NOT NULL,
  `username` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id)
) 

INSERT INTO `users` (`id`, `username`, `password`, `name`, `address`, `latitude`, `longitude`) VALUES
(61,'moises', 'moises', 'Moisés Evaristo Bueno','Rodriguez 500', -37.3136, -59.0981),
(142, 'oyhanart.matias.ezequiel', 'matias', 'oyhanart matias ezequiel', 'aristeguy 1346', -37.3136, -59.0981),
(141, 'andrea.veronica.roldan', 'andrea', 'andrea veronica roldan', 'loberia 1214', -37.3136, -59.0981),
(140, 'myrian.fernandez', 'myrian', 'Myrian Fernandez', 'Sinka 101', -37.3136, -59.0981),
(119, 'fabián.alejandro.gerez', 'fabian', 'Fabián Alejandro Gerez', 'San Francisco 2148', -37.3288, -59.1367),
(136, 'evaristo.bueno', 'evaristo', 'Evaristo Bueno', 'salceda 1995', -37.3136, -59.0981),
(137, 'claudia.serafini', 'claudia', 'CLAUDIA SERAFINI', 'ARANA 22', -37.3136, -59.0981),
(138, 'natalia.bellver', 'natalia', 'Natalia Bellver',  'mitre 1512',  -37.3136, -59.0981),
(139, 'maria.guillermina', 'guillermina', 'Maria Guillermina', 'Mosconi 438', -37.3136, -59.0981)
(143, 'omar.alejandro', 'omarale', 'OMAR ALEJANDRO','Mosconi 438', -37.3136, -59.0981)
(144, 'carlosalfredo', 'carlitos', 'CARLOS ALFREDO','Mosconi 438', -37.3136, -59.0981)




SELECT id, name
FROM users
WHERE  username = "username-ingresado" AND password = "pass-ingresada";



SELECT donante, cod_producto, receptor
FROM asignacion
WHERE cod_producto = "cod_escaneado";



CREATE TABLE IF NOT EXISTS people (
  `id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `user_id` int NOT NULL,
  CONSTRAINT pk_people PRIMARY KEY (id)
) 

ALTER TABLE `people`
  ADD FOREIGN KEY (`user_id`) REFERENCES users (`id`) 
--
-- Volcado de datos para la tabla `people`
--

INSERT INTO `people` (`id`, `name`, `email`) VALUES
(901, 'moises bueno', 'moisesbueno@gmail.com', 61),
(898, 'OMAR ALEJANDRO', 'omaralejandro@gmail.com', 143),
(899, 'CARLOS ALFREDO', 'carlosalfredo@gmail.com', 144),
(900, 'oyhanart matias ezequiel', 'ezequiel@gmail.com', 142),
(905, 'MOISES', 'moises@gmail.com', 136),
(906, 'andrea veronica roldan', 'angelayolanda@gmail.com',141)



user_id clave extranjera ?? no lo tengo en cuenta y fue, no cambia nada aca
-- --------------------------------------------------------



CREATE TABLE IF NOT EXISTS envio (
 id serial NOT NULL,
 donante int NOT NULL,
 receptor int NOT NULL,
 entregado boolean NOT NULL,
 latitude_donante float DEFAULT NULL,
 longitude_donante float DEFAULT NULL,
 latitude_receptor float DEFAULT NULL,
 longitude_receptor float DEFAULT NULL,
 address_donante varchar(45) COLLATE utf8_unicode_ci NOT NULL,
 address_receptor varchar(45) COLLATE utf8_unicode_ci NOT NULL, 
 fecha_envio date NOT NULL,
 fecha_recepcion date 
 CONSTRAINT pk_envio PRIMARY KEY (id)
)


