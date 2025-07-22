-- CREACION DE BASE DE DATOS
CREATE DATABASE BD_SOPORTE_DIGITAL;

-- CREACION DE ESQUEMA PRINCIPAL
CREATE SCHEMA DigitalSuport;

--CREACION DE LAS TABLAS PARA EL PROYECTO
CREATE TABLE DigitalSuport.TBL_TIPO_CLIENTE (
    nIdTipoCliente INT PRIMARY KEY IDENTITY,
    sDescripcion VARCHAR(100) NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_CLIENTE (
    nIdCliente INT PRIMARY KEY IDENTITY,
    sNombre VARCHAR(100) NOT NULL,
    sApellido VARCHAR(100) NULL,
    nEdad INT NULL,
    dFechaNacimiento DATE NULL,
    sEmail VARCHAR(100) NOT NULL,
    sContrasena VARCHAR(100) NOT NULL,
    nIdTipoCliente INT NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_USUARIO_CLIENTE (
    nIdUsuarioCliente INT PRIMARY KEY IDENTITY,
	nIdRolUsuario INT NOT NULL,
    sNombre VARCHAR(100) NOT NULL,
    sApellido VARCHAR(100) NOT NULL,
    sEmail VARCHAR(100) NOT NULL,
    sContrasena VARCHAR(100) NOT NULL,
    nIdCliente INT NOT NULL,
	nIdRolUsuario INT NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_APLICATIVO (
    nIdAplicativo INT PRIMARY KEY IDENTITY,
    sNombreAplicativo VARCHAR(50) NOT NULL,
    sDescripcion VARCHAR(100) NOT NULL,
    dFechaLanzamiento DATE NOT NULL,
    dFechaModificacion DATE NOT NULL,
    sVersion VARCHAR(50) NOT NULL,
    nIdCliente INT NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_TIPO_SOLICITUD (
    nIdTipoSolicitud INT PRIMARY KEY IDENTITY,
    sDescripcion VARCHAR(100) NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_SOLICITUD (
    nIdSolicitud INT PRIMARY KEY IDENTITY,
	nIdUsuarioCliente INT NOT NULL,
    nIdAplicativo INT NOT NULL,
    nIdTipoSolicitud INT NOT NULL,
	sMotivo VARCHAR(MAX) NOT NULL,
    dFechaCreacion DATETIME NOT NULL,
    dFechaFinalizacion DATETIME,
    sEstado VARCHAR(20) DEFAULT 'Pendiente', -- Pendiente, En Proceso, Finalizado
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_ASIGNACION_SOLICITUD (
    nIdAsignacionSolicitud INT PRIMARY KEY IDENTITY,
    nIdSolicitud INT NOT NULL,
    nIdColaborador INT NOT NULL,
    bEsCoordinador BIT DEFAULT 0,
    bEstado BIT,
);

CREATE TABLE DigitalSuport.TBL_ROL_COLABORADOR (
    nIdRolColaborador INT PRIMARY KEY IDENTITY,
    sDescripcion VARCHAR(50) NOT NULL,
    bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_COLABORADOR (
	nIdColaborador INT PRIMARY KEY IDENTITY,
	sNombre VARCHAR(50) NOT NULL,
	sApellido VARCHAR(50) NOT NULL,
	sEmail VARCHAR(100) NOT NULL,
	sContrasena VARCHAR(100) NOT NULL,
	nIdRolColaborador INT NOT NULL,
	bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_NOTIFICACION (
    nIdNotificacion INT PRIMARY KEY IDENTITY,
    nIdSolicitud INT NOT NULL,
    sDescripcion VARCHAR(100),
    dFechaEnvio DATETIME NOT NULL,
    bLeido BIT DEFAULT 0,
    bEstado BIT,
);

CREATE TABLE DigitalSuport.TBL_REGISTRO_TRABAJO (
	nIdRegistroTrabajo INT PRIMARY KEY IDENTITY,
	nIdSolicitud INT NOT NULL,
	nIdColaborador INT NOT NULL,
	sDescripcion VARCHAR(150) NOT NULL,
	dFechaRegistro DATE NOT NULL,
	nHorasTrabajadas DECIMAL(4,2) NOT NULL,
	sObservacion VARCHAR(MAX) NOT NULL,
	bEstado BIT
);

CREATE TABLE DigitalSuport.TBL_ROL_USUARIO(
	nIdRolUsuario INT PRIMARY KEY IDENTITY,
	sNombreRol VARCHAR(100) NOT NULL,
);


-- CREAR RELACIONES FK (FOREIGN KEY)
-- CLIENTE → TIPO DE CLIENTE
ALTER TABLE DigitalSuport.TBL_CLIENTE
ADD CONSTRAINT FK_Cliente_TipoCliente
FOREIGN KEY (nIdTipoCliente) REFERENCES DigitalSuport.TBL_TIPO_CLIENTE(nIdTipoCliente);

-- USUARIO DEL CLIENTE → CLIENTE
ALTER TABLE DigitalSuport.TBL_USUARIO_CLIENTE
ADD CONSTRAINT FK_UsuarioCliente_Cliente
FOREIGN KEY (nIdCliente) REFERENCES DigitalSuport.TBL_CLIENTE(nIdCliente);

-- APLICATIVO → CLIENTE
ALTER TABLE DigitalSuport.TBL_APLICATIVO
ADD CONSTRAINT FK_Aplicativo_Cliente
FOREIGN KEY (nIdCliente) REFERENCES DigitalSuport.TBL_CLIENTE(nIdCliente);

-- SOLICITUD → USUARIO DEL CLIENTE 
ALTER TABLE DigitalSuport.TBL_SOLICITUD
ADD CONSTRAINT FK_Solicitud_UsuarioCliente
FOREIGN KEY (nIdUsuarioCliente) REFERENCES DigitalSuport.TBL_USUARIO_CLIENTE(nIdUsuarioCliente);

-- SOLICITUD → APLICATIVO
ALTER TABLE DigitalSuport.TBL_SOLICITUD
ADD CONSTRAINT FK_Solicitud_Aplicativo
FOREIGN KEY (nIdAplicativo) REFERENCES DigitalSuport.TBL_APLICATIVO(nIdAplicativo);

-- SOLICITUD → TIPO DE SOLICITUD
ALTER TABLE DigitalSuport.TBL_SOLICITUD
ADD CONSTRAINT FK_Solicitud_TipoSolicitud
FOREIGN KEY (nIdTipoSolicitud) REFERENCES DigitalSuport.TBL_TIPO_SOLICITUD(nIdTipoSolicitud);

-- ASIGNACION DE SOLICITUD → SOLICITUD 
ALTER TABLE DigitalSuport.TBL_ASIGNACION_SOLICITUD
ADD CONSTRAINT FK_Asignacion_Solicitud
FOREIGN KEY (nIdSolicitud) REFERENCES DigitalSuport.TBL_SOLICITUD(nIdSolicitud);

-- ASIGNACION DE SOLICITUD → COLABORADOR
ALTER TABLE DigitalSuport.TBL_ASIGNACION_SOLICITUD
ADD CONSTRAINT FK_Asignacion_Colaborador
FOREIGN KEY (nIdColaborador) REFERENCES DigitalSuport.TBL_COLABORADOR(nIdColaborador);

-- COLABORADOR → ROL DEL COLABORADOR
ALTER TABLE DigitalSuport.TBL_COLABORADOR
ADD CONSTRAINT FK_Colaborador_Rol
FOREIGN KEY (nIdRolColaborador) REFERENCES DigitalSuport.TBL_ROL_COLABORADOR(nIdRolColaborador);

-- NOTIFICACION → SOLICITUD
ALTER TABLE DigitalSuport.TBL_NOTIFICACION
ADD CONSTRAINT FK_Notificacion_Solicitud
FOREIGN KEY (nIdSolicitud) REFERENCES DigitalSuport.TBL_SOLICITUD(nIdSolicitud);

-- REGISTRO DE TRABAJO → SOLICITUD
ALTER TABLE DigitalSuport.TBL_REGISTRO_TRABAJO
ADD CONSTRAINT FK_RegistroTrabajo_Solicitud
FOREIGN KEY (nIdSolicitud) REFERENCES DigitalSuport.TBL_SOLICITUD(nIdSolicitud);

-- REGISTRO DE TRABAJO → COLABORADOR
ALTER TABLE DigitalSuport.TBL_REGISTRO_TRABAJO
ADD CONSTRAINT FK_RegistroTrabajo_Colaborador
FOREIGN KEY (nIdColaborador) REFERENCES DigitalSuport.TBL_COLABORADOR(nIdColaborador);

-- ROL USUARIO → USUARIO CLIENTE
ALTER TABLE DigitalSuport.TBL_USUARIO_CLIENTE
ADD CONSTRAINT FK_RolUsuario_UsuarioCliente
FOREIGN KEY (nIdRolUsuario) REFERENCES DigitalSuport.TBL_ROL_USUARIO(nIdRolUsuario);


-- CREAR RESTRICCIONES (UNIQUE AND CHECK)
-- Validar si el cliente es una empresa (sApellido es null) o si es una persona (ambos campos deben tener valores)
ALTER TABLE DigitalSuport.TBL_CLIENTE
ADD CONSTRAINT CK_TBL_CLIENTE_Apellido_Segun_Tipo CHECK (
    (nIdTipoCliente = 1 AND sApellido IS NULL) OR
    (nIdTipoCliente = 2 AND sApellido IS NOT NULL)
);
-- Validar si el cliente es una empresa no puede tener fecha de nacimiento
ALTER TABLE DigitalSuport.TBL_CLIENTE
ADD CONSTRAINT CK_TBL_CLIENTE_FechaNacimiento_Segun_Tipo CHECK (
    (nIdTipoCliente = 1 AND dFechaNacimiento IS NULL) OR
    (nIdTipoCliente = 2 AND dFechaNacimiento IS NOT NULL)
);
-- Validar Correos Electronicos Unicos
ALTER TABLE DigitalSuport.TBL_CLIENTE
ADD CONSTRAINT UC_TBL_CLIENTE_Email UNIQUE (sEmail);

ALTER TABLE DigitalSuport.TBL_COLABORADOR
ADD CONSTRAINT UC_TBL_COLABORADOR_Email UNIQUE (sEmail);

-- Validar si el cliente es una Empresa no tenga Edad y si es un cliente persona que sea su edad mayor igual a 18
ALTER TABLE DigitalSuport.TBL_CLIENTE
ADD CONSTRAINT CK_TBL_CLIENTE_Edad_Segun_Tipo CHECK (
    (nIdTipoCliente = 1 AND nEdad IS NULL) OR
    (nIdTipoCliente = 2 AND nEdad >= 18)
);

-- Horas trabajadas tiene que ser mayor igual de 0
ALTER TABLE DigitalSuport.TBL_REGISTRO_TRABAJO
ADD CONSTRAINT CK_TBL_REGISTRO_TRABAJO_Horas CHECK (nHorasTrabajadas >= 0);

-- Validar que estado sea 'Pendiente', 'En Proceso' o 'Finalizado'
ALTER TABLE DigitalSuport.TBL_SOLICITUD
ADD CONSTRAINT CK_Estado_Solicitud CHECK (sEstado IN ('Pendiente', 'En Proceso', 'Finalizado'));

-- SELECTS DE LAS TABLAS
SELECT * FROM DigitalSuport.TBL_CLIENTE
SELECT * FROM DigitalSuport.TBL_TIPO_CLIENTE
SELECT * FROM DigitalSuport.TBL_USUARIO_CLIENTE
SELECT * FROM DigitalSuport.TBL_APLICATIVO
SELECT * FROM DigitalSuport.TBL_SOLICITUD
SELECT * FROM DigitalSuport.TBL_TIPO_SOLICITUD
SELECT * FROM DigitalSuport.TBL_NOTIFICACION
SELECT * FROM DigitalSuport.TBL_COLABORADOR
SELECT * FROM DigitalSuport.TBL_ROL_COLABORADOR
SELECT * FROM DigitalSuport.TBL_ASIGNACION_SOLICITUD
SELECT * FROM DigitalSuport.TBL_REGISTRO_TRABAJO

-- INSERCCIONES DE LAS TABLAS
-- TIPO DE CLIENTE
INSERT INTO DigitalSuport.TBL_TIPO_CLIENTE(sDescripcion, bEstado)
VALUES ('Empresa', 1)
INSERT INTO DigitalSuport.TBL_TIPO_CLIENTE(sDescripcion, bEstado)
VALUES ('Persona Natural con Negocio', 1)

--CLIENTE
SELECT * FROM DigitalSuport.TBL_CLIENTE
--(TIPO EMPRESA):
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Grupo NovaTech', NULL, NULL, NULL, 'gnt@novatech.com', 'Empresax5362', 1, 1);
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Tech Solutions', NULL, NULL, NULL, 'contact@techsolutions.com', 'Solutec@2025', 1, 1);
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Digital Innovators', NULL, NULL, NULL, 'info@digitalinnovators.com', 'Innovate20u0j73', 1, 1);
--(TIPO PERSONA):
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Camila', 'Morales', 27, '1997-02-18', 'cmorales@gmail.com', 'EstefCami97!', 2, 1)
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Juan', 'Perez', 35, '1989-07-12', 'jperez@gmail.com', 'PJzuan3434$00', 2, 1);
INSERT INTO DigitalSuport.TBL_CLIENTE(sNombre, sApellido, nEdad, dFechaNacimiento, sEmail, sContrasena, nIdTipoCliente, bEstado)
VALUES ('Eduard', 'Nighbord', 36, '1989-02-25', 'enighbord@gmail.com', 'KezmanHerodes02', 2, 1)

--USUARIO DE CLIENTE
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Valentina', 'Lopez', 'gnt@novatech.com', 'F37%Ygn', 1, 1);
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Mateo', 'Ramirez', 'gnt@novatech.com', 'Pas2025!', 1, 1);
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Diego', 'Salas', 'contact@techsolutions.com', 'TechPass@2025', 2, 1);
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Lucia', 'Gomez', 'contact@techsolutions.com', 'MariaTechyu2025$', 2, 1);
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Isabella', 'Navarro', 'info@digitalinnovators.com', 'IsNav?003', 3, 1);
INSERT INTO DigitalSuport.TBL_USUARIO_CLIENTE(sNombre, sApellido, sEmail, sContrasena, nIdCliente, bEstado)
VALUES ('Victor', 'Barrios', 'vbarrios@gmail.com', 'drFrankzVec05', 6, 1);

--APLICATIVO
INSERT INTO DigitalSuport.TBL_APLICATIVO(sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('NovaERP', 'Sistema de gestión empresarial desarrollado internamente', '2022-01-15', '2023-12-01', 'v2.3.1', 1, 1);
INSERT INTO DigitalSuport.TBL_APLICATIVO(sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('NovaCRM', 'CRM interno para gestión de clientes y ventas', '2021-06-10', '2024-03-22', 'v4.1.0', 1, 1);
INSERT INTO DigitalSuport.TBL_APLICATIVO(sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('SolutecPOS', 'Sistema de punto de venta personalizado', '2020-09-01', '2023-11-05', 'v3.5.2', 2, 1);
INSERT INTO DigitalSuport.TBL_APLICATIVO(sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('SolutecPay', 'Módulo de pagos en línea', '2021-03-25', '2024-01-10', 'v1.9.0', 2, 1);
INSERT INTO DigitalSuport.TBL_APLICATIVO (sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('InnovaWeb', 'Plataforma web de e-comercio', '2019-07-12', '2023-12-20', 'v5.0.7', 3, 1);
INSERT INTO DigitalSuport.TBL_APLICATIVO (sNombreAplicativo, sDescripcion, dFechaLanzamiento, dFechaModificacion, sVersion, nIdCliente, bEstado)
VALUES ('LuckyERP', 'Sistema ERP para gestión integral de recursos empresariales.', '2022-03-25', '2025-04-22', '2.0.0', 6, 1);

--TIPO SOLICITUD
INSERT INTO DigitalSuport.TBL_TIPO_SOLICITUD (sDescripcion, bEstado)
VALUES ('Error de Software', 1);
INSERT INTO DigitalSuport.TBL_TIPO_SOLICITUD (sDescripcion, bEstado)
VALUES ('Capacitación sobre uso del software', 1);
INSERT INTO DigitalSuport.TBL_TIPO_SOLICITUD (sDescripcion, bEstado)
VALUES ('Requerimiento de Software', 1);

--SOLICITUD
SELECT * FROM DigitalSuport.TBL_SOLICITUD
-- Solicitud de error de software por parte de Valentina (usuaria de NovaTech) para el aplicativo NovaERP
INSERT INTO DigitalSuport.TBL_SOLICITUD (nIdUsuarioCliente, nIdAplicativo, nIdTipoSolicitud, sMotivo, dFechaCreacion, dFechaFinalizacion, sEstado, bEstado)
VALUES (1, 1, 1, 'El módulo de facturación presenta errores al calcular el IGV.', '2025-04-10 09:15:00', '2025-04-12 14:30:00', 'Finalizado', 1);
-- Solicitud de capacitación de Mateo (NovaTech) para el CRM
INSERT INTO DigitalSuport.TBL_SOLICITUD(nIdUsuarioCliente, nIdAplicativo, nIdTipoSolicitud, sMotivo, dFechaCreacion, dFechaFinalizacion, sEstado, bEstado)
VALUES (3, 2, 2, 'Requiere capacitación para el equipo de ventas sobre el uso de NovaCRM.', '2025-04-15 10:00:00', NULL, 'En Proceso', 1);
-- Requerimiento de nueva funcionalidad por parte de Diego (Tech Solutions) en SolutecPOS
INSERT INTO DigitalSuport.TBL_SOLICITUD(nIdUsuarioCliente, nIdAplicativo, nIdTipoSolicitud, sMotivo, dFechaCreacion, dFechaFinalizacion, sEstado, bEstado)
VALUES (4, 3, 3, 'Solicita agregar función para emitir boletas electrónicas desde el POS.', '2025-04-17 11:30:00', NULL, 'Pendiente', 1);
-- Solicitud de error por parte de Isabella (Digital Innovators) para la plataforma e-comercio InnovaWeb
INSERT INTO DigitalSuport.TBL_SOLICITUD(nIdUsuarioCliente, nIdAplicativo, nIdTipoSolicitud, sMotivo, dFechaCreacion, dFechaFinalizacion, sEstado, bEstado)
VALUES (6, 5, 1, 'La sección de pago muestra un error al finalizar la compra.', '2025-04-20 08:45:00', '2025-04-21 16:00:00', 'Finalizado', 1);
-- Solicitud de Error por parte de Victor (persona natural) para LuckyERP
INSERT INTO DigitalSuport.TBL_SOLICITUD(nIdUsuarioCliente, nIdAplicativo, nIdTipoSolicitud, sMotivo, dFechaCreacion, dFechaFinalizacion, sEstado, bEstado)
VALUES (7, 6, 1, 'LUCKY ERP presenta lentitud al generar reportes de inventario.', '2025-04-22 09:00:00', NULL, 'En Proceso', 1);

-- NOTIFICACION 
-- Notificación para solicitud 1 (Valentina - NovaERP)
INSERT INTO DigitalSuport.TBL_NOTIFICACION (nIdSolicitud, sDescripcion, dFechaEnvio, bLeido, bEstado)
VALUES (1, 'Tu solicitud ha sido finalizada. Revisa el módulo de facturación.', '2025-04-12 15:00:00', 1, 1);
-- Notificación para solicitud 2 (Mateo - NovaCRM)
INSERT INTO DigitalSuport.TBL_NOTIFICACION (nIdSolicitud, sDescripcion, dFechaEnvio, bLeido, bEstado)
VALUES (2, 'Capacitación agendada para el equipo de ventas.', '2025-04-16 09:00:00', 0, 1);
-- Notificación para solicitud 3 (Diego - SolutecPOS)
INSERT INTO DigitalSuport.TBL_NOTIFICACION (nIdSolicitud, sDescripcion, dFechaEnvio, bLeido, bEstado)
VALUES (3, 'Funcionalidad en revisión por el equipo de desarrollo.', '2025-04-18 10:00:00', 0, 1);
-- Notificación para solicitud 4 (Isabella - InnovaWeb)
INSERT INTO DigitalSuport.TBL_NOTIFICACION (nIdSolicitud, sDescripcion, dFechaEnvio, bLeido, bEstado)
VALUES (4, 'Se solucionó el error en la sección de pagos.', '2025-04-21 16:10:00', 1, 1);
-- Notificación para solicitud 5 (Victor - LuckyERP)
INSERT INTO DigitalSuport.TBL_NOTIFICACION (nIdSolicitud, sDescripcion, dFechaEnvio, bLeido, bEstado)
VALUES (5, 'El equipo técnico está revisando el problema de lentitud en reportes.', '2025-04-22 10:00:00', 0, 1);

--ROL DEL COLABORADOR
INSERT INTO DigitalSuport.TBL_ROL_COLABORADOR(sDescripcion, bEstado)
VALUES ('Analistas', 1);
INSERT INTO DigitalSuport.TBL_ROL_COLABORADOR(sDescripcion, bEstado)
VALUES('Diseñadores', 1);
INSERT INTO DigitalSuport.TBL_ROL_COLABORADOR(sDescripcion, bEstado)
VALUES('Programadores', 1);
INSERT INTO DigitalSuport.TBL_ROL_COLABORADOR(sDescripcion, bEstado)
VALUES('Soporte Tecnico', 1);
INSERT INTO DigitalSuport.TBL_ROL_COLABORADOR(sDescripcion, bEstado)
VALUES('Tester QA', 1);

-- COLABORADOR
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Pedro', 'Martinez', 'pmartinez@123digit.com', 'pedropazzz17', 1, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES('Lucia', 'Gomez', 'lgomez@123digit.com', 'luc&art22', 1, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Jorge', 'Fernandez', 'jfernandez@123digit.com', 'georgefer03', 1, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Ana', 'Quispe', 'aquispe@123digit.com', 'anniechambvus02', 2, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Marco', 'Velarde', 'mvelarde@123digit.com', 'Velarde153453', 3, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Luciana', 'Meza', 'lmeza@123digit.com', 'LuciMez2024', 2, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Jorge', 'Reyna', 'jreyna@123digit.com', 'JRey23$$', 3, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Tatiana', 'Silva', 'tsilva@123digit.com', 'TatSilv89*', 4, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Carlos', 'Aranda', 'caranda@123digit.com', 'AranCarlo01#', 4, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Leyla', 'Rangel', 'lrangel@123digit.com', 'kimrang$216', 5, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Analucia', 'Vargas', 'avargas@digitalsupport.com', 'micaluu0924', 5, 1)
INSERT INTO DigitalSuport.TBL_COLABORADOR(sNombre, sApellido, sEmail, sContrasena, nIdRolColaborador, bEstado)
VALUES ('Korrina', 'Rapzel', 'krapzel@123digit.com', 'RapsFlower432*', 2, 1)

-- ASIGNACION DE LA SOLICITUD
SELECT * FROM DigitalSuport.TBL_ASIGNACION_SOLICITUD
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (1, 5, 0, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (1, 4, 1, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (2, 2, 1, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (2, 1, 0, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (3, 7, 1, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (3, 5, 0, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (4, 3, 1, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (4, 8, 0, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (5, 10, 0, 1)
INSERT INTO DigitalSuport.TBL_ASIGNACION_SOLICITUD(nIdSolicitud, nIdColaborador, bEsCoordinador, bEstado)
VALUES (5, 12, 1, 1)

--REGISTRO DE TRABAJO
-- Registro de trabajo de Marco Velarde (id 5) en la solicitud 1 (Error de software en NovaERP)
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (1, 5, 'Se revisó el cálculo del IGV en el módulo de facturación y se corrigió el error de redondeo.', '2025-04-10 11:30:00', 3, 'Código actualizado en producción.', 1)
-- Registro de Ana Quispe (id 4) como coordinadora en la solicitud 1
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (1, 4, 'Supervisión del equipo y validación de corrección en NovaERP.', '2025-04-11 10:00:00', 2, 'Se validó con la usuario Valentina.', 1);
-- Registro de Lucia Gomez (id 2) en la solicitud 2 (Capacitación CRM)
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (2, 2, 'Se diseñó material de capacitación para NovaCRM y se envió al cliente.', '2025-04-16 09:00:00', 4, 'Se espera validación por parte de Mateo.', 1);
-- Registro de Pedro Martinez (id 1) en la solicitud 2
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (2, 1, 'Se brindó sesión de capacitación virtual para el equipo de ventas.', '2025-04-17 14:00:00', 2, 'Capacitación completada en 2 sesiones.', 1);
-- Registro de Jorge Reyna (id 7) en la solicitud 3 (Funcionalidad para SolutecPOS)
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (3, 7, 'Análisis de requerimiento y diseño preliminar de emisión de boletas.', '2025-04-18 10:30:00', 5, 'Se entregó mockup al cliente.', 1);
-- Registro de Leyla Rangel  (id 10) en la solicitud 5 (LuckyERP)
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (5, 10, 'Pruebas de rendimiento realizadas en el módulo de reportes de inventario.', '2025-04-23 08:00:00', 4, 'Detectada ineficiencia en la consulta SQL.', 1);
-- Registro de Korrina Rapzel (id 12) como coordinadora en solicitud 5
INSERT INTO DigitalSuport.TBL_REGISTRO_TRABAJO(nIdSolicitud, nIdColaborador, sDescripcion, dFechaRegistro, nHorasTrabajadas, sObservacion, bEstado)
VALUES (5, 12, 'Coordinación con equipo técnico para mejorar performance.', '2025-04-23 09:00:00', 1, 'Se programó reunión con cliente para avances.', 1);
