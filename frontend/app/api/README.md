# API connection

## Yaml to typescript

Run the command below

```
npx openapi-typescript http://localhost:8080/v3/api-docs.yaml -o api-types.ts
```

It will extract all paths and schemas from the API. One could also use 

```
npx openapi-typescript-codegen --input http://localhost:8080/v3/api-docs.yaml --output api
```

to generate API fetch methods directly on the output directory