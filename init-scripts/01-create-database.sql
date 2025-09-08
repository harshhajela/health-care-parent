-- Grant necessary privileges to the postgres user
-- Note: The health-care-parent database is created by POSTGRES_DB environment variable
GRANT ALL PRIVILEGES ON DATABASE "health-care-parent" TO postgres;