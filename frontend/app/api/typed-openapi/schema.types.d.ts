
  export namespace Schemas {
    // <Schemas>
  export type ProblemDetail = { type?: string, title?: string, status: number, detail?: string, instance?: string, properties?: Record<string, unknown> }
export type FieldError = Partial<{ field: string, message: string }>
export type ValidationProblemDetail = { type?: string, title?: string, status: number, detail?: string, instance?: string, properties?: Record<string, unknown>, validationIssues?: Array<FieldError> }
export type UserLogin = { email: string, password: string }
/**
 * Information necessary to authenticate
 */
export type LoginToken = {
  /**
   * the bearer token
   */
  token: string;
}
/**
 * data class with information necessary to create user
 */
export type UserCreation = {
  /**
   * email to be associated to user, cannot be in use for another account at the same time
   */
  email: string;
  /**
   * clear password to use on login
   */
  password: string;
  /**
   * x coordinate of user location
   */
  x: number;
  /**
   * y coordinate of user location
   */
  y: number;
}
/**
 * Since this service is about physical books, it assumes the existence of the ISBN. It may be either ISBN 10 or
 *  ISBN 13 or even both, but never neither.
 *  <p>
 *  Due to this restriction, this class can only one of {@link Book#isbnTen Book#isbnTen}
 *  or {@link Book#isbnThirteen Book#isbnThirteen} fields as null
 */
export type Book = Partial<{ id: number, isbnTen: string, isbnThirteen: string, title: string, language: ("English" | "Portuguese" | "Spanish" | "French" | "German" | "Russian" | "Japanese" | "Italian" | "Hebrew" | "Hungarian" | "Danish") }>
export type RequestCreation = {
  /**
   * book identifier, either 10 or 13 numbers. Should also respect checksum
   */
  isbn: string;
  /**
   * unique identifier of book's owner
   */
  ownerEmail: string;
  /**
   * Duration of request
   */
  timeInDays: number;
}
export type LendCreation = {
  /**
   * book identifier, either 10 or 13 numbers
   */
  isbn: string;
  /**
   * unique identifier of book's requester
   */
  receiverEmail: string;
}
/**
 * Listed data, possibly not complete.
 */
export type ListedDataBook = {
  /**
   * This slice of the data
   */
  data: Array<Book>;
  /**
   * has more data to retrieve
   */
  hasNextPage: boolean;
  hasPreviousPage: boolean;
}
export type OwnedId = Partial<{ userId: number, bookId: number }>
export type RequestId = Partial<{ requested: OwnedId, userId: number }>
export type User = { id?: number, email?: string, isAdmin?: boolean, enabled: boolean }
export type Owned = Partial<{ id: OwnedId, user: User, book: Book }>
export type Request = Partial<{ requestId: RequestId, owned: Owned, date: string, duration: number }>
/**
 * Listed data, possibly not complete.
 */
export type ListedDataRequest = {
  /**
   * This slice of the data
   */
  data: Array<Request>;
  /**
   * has more data to retrieve
   */
  hasNextPage: boolean;
  hasPreviousPage: boolean;
}
/**
 * Listed data, possibly not complete.
 */
export type ListedDataUser = {
  /**
   * This slice of the data
   */
  data: Array<User>;
  /**
   * has more data to retrieve
   */
  hasNextPage: boolean;
  hasPreviousPage: boolean;
}

    // </Schemas>
    }
  
  export namespace Endpoints {
  // <Endpoints>
  
  /**
 * Add book identified by ISBN to list of owned books
 */
export type post_AddBookOwned = {
      method: "POST",
      path: "/users/owned/{ISBN}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        path:  {
  /**
   * ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum
   */
  ISBN: string;
},
        
        
        
          }
      responses: {200: unknown,
400: Schemas.ProblemDetail,
401: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * Block blacklist email from the API
 */
export type post_AddBlacklist = {
      method: "POST",
      path: "/users/blacklist/{email}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        path:  { email: string },
        
        
        
          }
      responses: {200: unknown,
401: Schemas.ProblemDetail,
403: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
export type post_Login = {
      method: "POST",
      path: "/userAuth/login",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        
        
        
        body:  Schemas.UserLogin,
          }
      responses: {201: Schemas.LoginToken,
400: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
export type post_CreateUser = {
      method: "POST",
      path: "/userAuth/createUser",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        
        
        
        body:  Schemas.UserCreation,
          }
      responses: {201: Schemas.LoginToken,
400: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * todo: to remove, service should check and add the book to the database by itself whenever specifically requested
 *  Add a book to the system so it can be recognized, by giving an id and letting the server check if it exists
 */
export type post_PostBook = {
      method: "POST",
      path: "/books/{ISBN}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        path:  {
  /**
   * ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum
   */
  ISBN: string;
},
        
        
        
          }
      responses: {201: Schemas.Book,
400: Schemas.ProblemDetail,
401: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
export type post_RequestBook = {
      method: "POST",
      path: "/books/request",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        
        
        
        body:  Schemas.RequestCreation,
          }
      responses: {200: unknown,
400: Schemas.ProblemDetail,
401: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
export type post_LendBook = {
      method: "POST",
      path: "/books/lend",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            
        
        
        
        body:  Schemas.LendCreation,
          }
      responses: {200: unknown,
401: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * Get list of books owned by a user
 */
export type get_BooksOwned = {
      method: "GET",
      path: "/users/ownedBy/{userId}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            query?:  Partial<{ page: number }>,
        path:  { userId: number },
        header?:  Partial<{ ETag: string }>,
        
        
          }
      responses: {200: Schemas.ListedDataBook,
304: unknown,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * Gets requests of book owned by user
 */
export type get_BookRequests = {
      method: "GET",
      path: "/users/ownedBy/{userId}/requests/{bookId}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            query?:  Partial<{ page: number }>,
        path:  { userId: number, bookId: number },
        header?:  Partial<{ ETag: string }>,
        
        
          }
      responses: {200: Schemas.ListedDataRequest,
304: unknown,
400: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * Get owners of a specific book
 */
export type get_GetBookOwners = {
      method: "GET",
      path: "/books/owners/{ISBN}",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            query?:  Partial<{ page: number }>,
        path:  {
  /**
   * ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum
   */
  ISBN: string;
},
        header?:  Partial<{ ETag: string }>,
        
        
          }
      responses: {200: Schemas.ListedDataUser,
304: unknown,
401: Schemas.ProblemDetail,
404: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
/**
 * General list book search by segments
 */
export type get_GetBooks = {
      method: "GET",
      path: "/books/",
      requestFormat: "json",
      responseFormat: "json",
      parameters: {
            query?:  Partial<{ page: number }>,
        
        header?:  Partial<{ ETag: string }>,
        
        
          }
      responses: {200: Schemas.ListedDataBook,
304: unknown,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }
export type delete_Logout = {
      method: "DELETE",
      path: "/userAuth/logout",
      requestFormat: "json",
      responseFormat: "json",
      parameters: never,
      responses: {200: unknown,
401: Schemas.ProblemDetail,
422: Schemas.ValidationProblemDetail,
500: Schemas.ProblemDetail,
},
      
    }

  // </Endpoints>
  }
  
  
     // <EndpointByMethod>
     export type EndpointByMethod = {
     post: {
           "/users/owned/{ISBN}": Endpoints.post_AddBookOwned,
"/users/blacklist/{email}": Endpoints.post_AddBlacklist,
"/userAuth/login": Endpoints.post_Login,
"/userAuth/createUser": Endpoints.post_CreateUser,
"/books/{ISBN}": Endpoints.post_PostBook,
"/books/request": Endpoints.post_RequestBook,
"/books/lend": Endpoints.post_LendBook
         },
get: {
           "/users/ownedBy/{userId}": Endpoints.get_BooksOwned,
"/users/ownedBy/{userId}/requests/{bookId}": Endpoints.get_BookRequests,
"/books/owners/{ISBN}": Endpoints.get_GetBookOwners,
"/books/": Endpoints.get_GetBooks
         },
delete: {
           "/userAuth/logout": Endpoints.delete_Logout
         }
     }
     
     // </EndpointByMethod>
     

    // <EndpointByMethod.Shorthands>
    export type PostEndpoints = EndpointByMethod["post"]
export type GetEndpoints = EndpointByMethod["get"]
export type DeleteEndpoints = EndpointByMethod["delete"]
    // </EndpointByMethod.Shorthands>
    