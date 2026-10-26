import createClient, { type Client } from "openapi-fetch"
import type { components, paths} from "./api-types"

// second argument is content type
const client: Client<paths> = createClient<paths>({baseUrl: "http://localhost:8080"});

/**
 * Base return type for all fetches. Union of success and failure types, with success containing the expected data and the failure type contains ProblemDetail.
 */
export type FetchReturn<T> = {type: 'success'; info: T} | {type: 'failure'; info: components["schemas"]["ProblemDetail"]}

async function bookRequests(userId: number, bookId: number, page?: number): Promise<FetchReturn<components["schemas"]["ListedDataRequest"]>> {
    const { data, error } = await client.GET("/users/owned/{userId}/requests/{bookId}", {
    params: {
      path: { userId, bookId },
      query: { page }
    }
  });

  if (error) {
    console.error("Error getting list:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}


async function booksOwned(userId: number, page?: number): Promise<FetchReturn<components["schemas"]["ListedDataBook"]>> {
const { data, error } = await client.GET("/users/owned/{userId}", {
    params: {
      path: { userId },
      query: { page }
    }
  });

  if (error) {
    console.error("Error getting list:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}

export async function addBookOwned(ISBN : string, token: string): Promise<FetchReturn<undefined>> {
const { data, error } = await client.POST("/users/owned/{ISBN}", {
    params: { 
      path: { ISBN },
      header: { Authorization: `Bearer ${token}` }
    }
  });

  if (error) {
    console.error("Error getting list:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}

export async function loginUser(email: string, password: string): Promise<FetchReturn<paths["/userAuth/login"]["post"]["responses"]["201"]["content"]["*/*"]>> {
  const { data, error } = await client.POST("/userAuth/login", {
    body: { email, password }
  });

  if (error) {
    console.error("Error logging in:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}

export async function createUser(email: string, password: string, x: number, y: number): Promise<FetchReturn<paths["/userAuth/createUser"]["post"]["responses"]["201"]["content"]["*/*"]>> {
  const { data, error } = await client.POST("/userAuth/createUser", {
    body: { email, password, x, y }
  });

  if (error) {
    console.error("Error creating user:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}

export async function logoutUser(token: string): Promise<FetchReturn<undefined>> {
  const { data, error } = await client.DELETE("/userAuth/logout", { params: { header: { Authorization: `Bearer ${token }` } } });
    
  if (error) {
    console.error("Error logging out:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
  }