import createClient from "openapi-fetch"
import type { components, paths} from "./api-types"

const client = createClient<paths>({baseUrl: "http://localhost:8080"});

/**
 * Base return type for all fetches. Union of success and failure types, with success containing the expected data and the failure type contains ProblemDetail.
 */
export type FetchReturn<T> = {type: 'success'; info: T} | {type: 'failure'; info: components["schemas"]["ProblemDetail"]}

async function bookRequests(userId: number, bookId: number, page?: number): Promise<FetchReturn<components["schemas"]["ListedData"]>> {
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


async function booksOwned(userId: number, page?: number): Promise<FetchReturn<components["schemas"]["ListedData"]>> {
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

async function addBookOwned(ISBN : string): Promise<FetchReturn<undefined>> {
const { data, error } = await client.POST("/users/owned/{ISBN}", {
    params: { 
      path: { ISBN }
    }
  });

  if (error) {
    console.error("Error getting list:", error);
    return {type: 'failure', info: error};
  }

  return {type: 'success', info: data};
}
