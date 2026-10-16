FROM postgis/postgis

USER postgres
WORKDIR /app

#base directory is the same as gradle (defined by docker compose yml)
COPY sql/creation.sql /docker-entrypoint-initdb.d/1_create.sql

COPY sql/insert.sql /docker-entrypoint-initdb.d/2_insert-test-data.sql

COPY --chown=postgres:postgres ./wait-for-postgres.sh ./bin/wait-for-postgres.sh
RUN chmod +x ./bin/wait-for-postgres.sh

EXPOSE 5432