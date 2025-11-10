Postgres service names in DB connection strings are updated to reference container names used in the consolidated compose (e.g., pg_trip_management) where applicable.

Notes and required manual checks:
- Ensure the `ny-travel-network` external docker network exists. Create it if missing:
  docker network create --subnet=172.16.238.0/24 ny-travel-network

- Ensure the files under `./docker/secrets` and the root `.env` exist and contain correct values for variables like POSTGRES_USER, POSTGRES_PASSWORD, JWT_KEY, HOTEL_SEARCH_PROVIDER_URL, etc.
