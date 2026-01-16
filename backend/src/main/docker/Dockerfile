FROM postgres

USER postgres
WORKDIR /app

COPY src/main/resources/sql/creation.sql /docker-entrypoint-initdb.d/1_create.sql

# COPY src/main/resources/sql/insert.sql /docker-entrypoint-initdb.d/2_insert-test-data.sql

COPY --chown=postgres:postgres ./wait-for-postgres.sh ./bin/wait-for-postgres.sh
RUN chmod +x ./bin/wait-for-postgres.sh

EXPOSE 5432