# API connection

## Yaml to typescript

Run the command below, which starts a script defined on the package.json.

```
npm run prebuild
```

This will create the files found on the typed-openapi folder. It is recommended not to change its contents, as running the script would
override them.

## Organization

The requests.ts file initializes both the api client and contains the base functions for each API.

## Various

Each http method is represented by three typescript methods declared, and actually implemented by one. Depending on whether one wants
the http failures to be thrown as exceptions or simply returned as one possible response, set the bool value withResponse on the params.

Responses with no content such as with not modified show as being unknown.

## To see

Find ways to automatically retrieve all related data to a request, including zod restrictions and failure responses when they are being thrown.
If possible would require researching more about typescript.