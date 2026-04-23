-- ================================================================
-- Script de inicialización para PostgreSQL
-- Ejecutar UNA SOLA VEZ como usuario 'postgres' (o un superusuario):
--     psql -U postgres -f docs/init-db.sql
-- ================================================================

-- Crear la base de datos (si no existe)
CREATE DATABASE notificaciones_db;

-- (Opcional) crear un usuario dedicado para la aplicación:
-- CREATE USER notif_app WITH PASSWORD 'cambia_esta_clave';
-- GRANT ALL PRIVILEGES ON DATABASE notificaciones_db TO notif_app;

-- Hibernate (spring.jpa.hibernate.ddl-auto=update) se encargará de
-- crear automáticamente las tablas la primera vez que arranque la app:
--   * notificaciones                (tabla padre, atributos comunes)
--   * notificaciones_email          (atributos específicos del email)
--   * notificaciones_sms            (atributos específicos del SMS)
--   * notificaciones_app_movil      (atributos de la app móvil)
