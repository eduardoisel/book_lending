# Project description

The project consists of an API build with java spring-boot, along with a typescript client of the API for the browser.

## Backend service

This service is about various people part of the app being able to share the books they own with others, somewhat as an extension of libraries.
The lending would theoretically be done in person, without any authorative administration. As such, any punishment possibly given by the server
would be to block the user from any further interactions after multiple complaints of late returns or complete failure of giving back books.

## Frontend

Base structure was created with <mark>npx create-react-router@latest</mark> command for a
[react router](https://reactrouter.com/start/framework/installation) project. <mark>npx openapi-typescript</mark> along with 
[its client](https://github.com/openapi-ts/openapi-typescript/tree/main/packages/openapi-fetch). 



