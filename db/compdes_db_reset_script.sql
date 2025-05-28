-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS compdes;

-- Crear la base de datos
CREATE DATABASE compdes;

-- Usar la base de datos
USE compdes;

-- Verificar si el usuario no existe y crearlo
CREATE USER IF NOT EXISTS 'compdes_user'@'localhost' IDENTIFIED BY 'compdes@app';

-- Otorgar permisos específicos sobre la base de datos
GRANT ALL PRIVILEGES ON compdes.* TO 'compdes_user'@'localhost';

-- Aplicar cambios de permisos
FLUSH PRIVILEGES;