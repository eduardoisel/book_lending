# API connection

## Yaml to typescript

Run the command below

```
npx openapi-typescript http://localhost:8080/v3/api-docs.yaml -o api-types.ts
```

It will extract all paths and schemas from the API