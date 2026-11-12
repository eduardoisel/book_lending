import {EndpointByMethod, RequestCreation, UserCreation, type EndpointParameters, type EndpointParameterStyles, type Fetcher, type FetcherResponse, type Method, type RequestFormat, type SecurityRequirements} from "./typed-openapi/schema"

import { createApiClient } from "./typed-openapi/schema";
import { z } from "zod"; // if runtime=zod
import type { Schemas } from "./typed-openapi/schema.types";
import Cookies from 'js-cookie';


// https://github.com/astahmer/typed-openapi/blob/main/example/api-client-example.ts base example
// i believe validation is done on other part of class if activated, so it was removed
//see --default-fetcher generation for authentication setting, current solution is the first that came to my head
const fetchMethod : Fetcher["fetch"] = 
    (input: {
      method: Method, 
      url: URL, 
      urlSearchParams?: URLSearchParams | undefined, 
      requestFormat: RequestFormat,
      parameters?: EndpointParameters | undefined,
      security?: SecurityRequirements | undefined, //todo use this to add auth headers
      overrides?: RequestInit | undefined
    }) =>{
      if (input.urlSearchParams) {
        input.url.search = input.urlSearchParams.toString();
      }
    
      const headers = new Headers();

      // Add custom headers
      if (input.parameters?.header) {
        Object.entries(input.parameters.header).forEach(([key, value]) => {
          if (value != null) {
            headers.set(key, String(value));
          }
        });
      }

      const body = JSON.stringify(input.parameters?.body);

      if (body) {
        headers.set("Content-Type", "application/json");
      }

      if (input.security && input.security.length > 0) {
        console.log("Security requirements exist for request:", input.security);
        headers.set("Authorization", "Bearer " + Cookies.get('token')); //assumes is bearer
      }
    
      console.log(`Making ${input.method} request to ${input.url.toString()}`);

      return fetch(input.url, { method: input.method, body: body, headers: headers, ...input.overrides }) 

    };

const api = createApiClient({fetch: fetchMethod}, "http://localhost:8080", { validate: "input" });


//TEndPoint["/users/ownedBy/{userId}/requests/{bookId}"]
export async function zodgetBookRequests(userId: number, bookId: number, page?: number) {

  return await api.get("/users/ownedBy/{userId}/requests/{bookId}", {
    path: {
      userId: userId,
      bookId: bookId
    },
    query: {page: page}
  });

}



//RequestCreation; // TypeScript type for request creation
//const temp: string = z.toJSONSchema(RequestCreation); // JSON Schema for request creation
export async function zodpostBookRequest(requestCreation: RequestCreation) {

  //const requestCreation: RequestCreation = RequestCreation.parse({ isbn, ownerEmail, timeInDays });

  return await api.post("/books/request", {
    body: requestCreation
  });

}



export function zodCreateUser(userCreation: UserCreation) {
  return api.post("/userAuth/createUser", {
    body: userCreation  // {email, password, x, y}
  });
}

/**
 * {@link EndpointByMethod.get get methods}, does not seem to be able to point to specific path
 * @param ISBN 
 * @param page 
 * @returns 
 */
export function zodBookOwners(ISBN: string, page: number = 0): Promise<Schemas.ListedDataUser> {
  console.log('Hello from zodBookOwners');
  return api.get("/books/owners/{ISBN}", {
    path: { ISBN: ISBN },
    query: { page }
  });
}


export function loginUser(email: string, password: string): Promise<Schemas.LoginToken> {
  return api.post("/userAuth/login", {
    body: { email, password }
  });

}

export function logoutUser(token: string): Promise<unknown> {
  return api.delete("/userAuth/logout", 
    { overrides: { headers: { Authorization: `Bearer ${token}` } } });
  }