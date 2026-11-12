# Personal changes

Below is the auto generated readme for react router.

## Backend API access

[typed-openapi](https://github.com/astahmer/typed-openapi) is being used to generate the base typescript code to access the API.

The base information about the api should come from the open-api doc, and the extraction of the information should be automatized.
To get this information one should run the prebuild script defined in the project's [package.json](./package.json) with the command


```bash
npm run prebuild
```
The command above uses a yaml file found in the project, but it can be substituted by url of localhost.

One can extract the zod restrictions with [toJsonSchema function](https://zod.dev/json-schema#ztojsonschema) so they can be applied on the inputs.

The prebuild can also have the --default-fetcher option to automatically create a client.

### Why this openapi generator

Started by using [openapi-typescript](https://github.com/openapi-ts/openapi-typescript/tree/main) along with its client openapi-fetch, but this did not 
include validation restrictions or security schema.


# Welcome to React Router!

A modern, production-ready template for building full-stack React applications using React Router.

[![Open in StackBlitz](https://developer.stackblitz.com/img/open_in_stackblitz.svg)](https://stackblitz.com/github/remix-run/react-router-templates/tree/main/default)

## Features

- 🚀 Server-side rendering
- ⚡️ Hot Module Replacement (HMR)
- 📦 Asset bundling and optimization
- 🔄 Data loading and mutations
- 🔒 TypeScript by default
- 🎉 TailwindCSS for styling
- 📖 [React Router docs](https://reactrouter.com/)

## Getting Started

### Installation

Install the dependencies:

```bash
npm install
```

### Development

Start the development server with HMR:

```bash
npm run dev
```

Your application will be available at `http://localhost:5173`.

## Building for Production

Create a production build:

```bash
npm run build
```

## Deployment

### Docker Deployment

To build and run using Docker:

```bash
docker build -t my-app .

# Run the container
docker run -p 3000:3000 my-app
```

The containerized application can be deployed to any platform that supports Docker, including:

- AWS ECS
- Google Cloud Run
- Azure Container Apps
- Digital Ocean App Platform
- Fly.io
- Railway

### DIY Deployment

If you're familiar with deploying Node applications, the built-in app server is production-ready.

Make sure to deploy the output of `npm run build`

```
├── package.json
├── package-lock.json (or pnpm-lock.yaml, or bun.lockb)
├── build/
│   ├── client/    # Static assets
│   └── server/    # Server-side code
```

## Styling

This template comes with [Tailwind CSS](https://tailwindcss.com/) already configured for a simple default starting experience. You can use whatever CSS framework you prefer.

---

Built with ❤️ using React Router.
