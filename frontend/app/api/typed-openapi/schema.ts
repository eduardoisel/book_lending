// @ts-nocheck
import type * as __TypedOpenapi from "./schema.types.js";

  import { z } from "zod";

// <Schemas>
export type ProblemDetail = __TypedOpenapi.Schemas.ProblemDetail;
export const ProblemDetail = z.strictObject({ type: z.url().optional(), title: z.string().optional(), status: z.number().int(), detail: z.string().optional(), instance: z.url().optional(), properties: z.record(z.string(), z.unknown()).optional() });

export type FieldError = __TypedOpenapi.Schemas.FieldError;
export const FieldError = z.strictObject({ field: z.string(), message: z.string() }).partial();

export type ValidationProblemDetail = __TypedOpenapi.Schemas.ValidationProblemDetail;
export const ValidationProblemDetail = z.strictObject({ type: z.url().optional(), title: z.string().optional(), status: z.number().int(), detail: z.string().optional(), instance: z.url().optional(), properties: z.record(z.string(), z.unknown()).optional(), validationIssues: z.array(FieldError).optional() });

export type UserLogin = __TypedOpenapi.Schemas.UserLogin;
export const UserLogin = z.strictObject({ email: z.string(), password: z.string() });

export type LoginToken = __TypedOpenapi.Schemas.LoginToken;
export const LoginToken = z.strictObject({ token: z.string() });

export type UserCreation = __TypedOpenapi.Schemas.UserCreation;
export const UserCreation = z.strictObject({ email: z.string(), password: z.string().min(8).max(2147483647).regex(new RegExp(".*(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-z0-9A-Z]).*")), x: z.number().min(-180).max(180), y: z.number().min(-90).max(90) });

export type Book = __TypedOpenapi.Schemas.Book;
export const Book = z.strictObject({ id: z.number().int(), isbnTen: z.string(), isbnThirteen: z.string(), title: z.string(), language: z.enum(["English", "Portuguese", "Spanish", "French", "German", "Russian", "Japanese", "Italian", "Hebrew", "Hungarian", "Danish"]) }).partial();

export type RequestCreation = __TypedOpenapi.Schemas.RequestCreation;
export const RequestCreation = z.strictObject({ isbn: z.string().min(10).max(13).regex(new RegExp("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)")), ownerEmail: z.string(), timeInDays: z.number().int().min(1) });

export type LendCreation = __TypedOpenapi.Schemas.LendCreation;
export const LendCreation = z.strictObject({ isbn: z.string().min(10).max(13).regex(new RegExp("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)")), receiverEmail: z.string().min(1) });

export type ListedDataBook = __TypedOpenapi.Schemas.ListedDataBook;
export const ListedDataBook = z.strictObject({ data: z.array(Book), hasNextPage: z.boolean(), hasPreviousPage: z.boolean() });

export type OwnedId = __TypedOpenapi.Schemas.OwnedId;
export const OwnedId = z.strictObject({ userId: z.number().int(), bookId: z.number().int() }).partial();

export type RequestId = __TypedOpenapi.Schemas.RequestId;
export const RequestId = z.strictObject({ requested: OwnedId, userId: z.number().int() }).partial();

export type User = __TypedOpenapi.Schemas.User;
export const User = z.strictObject({ id: z.number().int().optional(), email: z.string().min(0).max(70).optional(), isAdmin: z.boolean().optional(), enabled: z.boolean() });

export type Owned = __TypedOpenapi.Schemas.Owned;
export const Owned = z.strictObject({ id: OwnedId, user: User, book: Book }).partial();

export type Request = __TypedOpenapi.Schemas.Request;
export const Request = z.strictObject({ requestId: RequestId, owned: Owned, date: z.iso.datetime(), duration: z.number().int() }).partial();

export type ListedDataRequest = __TypedOpenapi.Schemas.ListedDataRequest;
export const ListedDataRequest = z.strictObject({ data: z.array(Request), hasNextPage: z.boolean(), hasPreviousPage: z.boolean() });

export type ListedDataUser = __TypedOpenapi.Schemas.ListedDataUser;
export const ListedDataUser = z.strictObject({ data: z.array(User), hasNextPage: z.boolean(), hasPreviousPage: z.boolean() });

// </Schemas>

// <Endpoints>
export type post_AddBookOwned = __TypedOpenapi.Endpoints.post_AddBookOwned;
export const post_AddBookOwned = {
  method: z.literal("POST"),
  path: z.literal("/users/owned/{ISBN}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { path: z.strictObject({ ISBN: z.string().min(10).max(13).regex(new RegExp("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)")) }) },
  responses: { 200: z.unknown(), 400: ProblemDetail, 401: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_AddBlacklist = __TypedOpenapi.Endpoints.post_AddBlacklist;
export const post_AddBlacklist = {
  method: z.literal("POST"),
  path: z.literal("/users/blacklist/{email}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { path: z.strictObject({ email: z.string() }) },
  responses: { 200: z.unknown(), 401: ProblemDetail, 403: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_Login = __TypedOpenapi.Endpoints.post_Login;
export const post_Login = {
  method: z.literal("POST"),
  path: z.literal("/userAuth/login"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { body: UserLogin },
  responses: { 201: LoginToken, 400: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_CreateUser = __TypedOpenapi.Endpoints.post_CreateUser;
export const post_CreateUser = {
  method: z.literal("POST"),
  path: z.literal("/userAuth/createUser"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { body: UserCreation },
  responses: { 201: LoginToken, 400: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_PostBook = __TypedOpenapi.Endpoints.post_PostBook;
export const post_PostBook = {
  method: z.literal("POST"),
  path: z.literal("/books/{ISBN}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { path: z.strictObject({ ISBN: z.string().min(10).max(13).regex(new RegExp("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)")) }) },
  responses: { 201: Book, 400: ProblemDetail, 401: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_RequestBook = __TypedOpenapi.Endpoints.post_RequestBook;
export const post_RequestBook = {
  method: z.literal("POST"),
  path: z.literal("/books/request"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { body: RequestCreation },
  responses: { 200: z.unknown(), 400: ProblemDetail, 401: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type post_LendBook = __TypedOpenapi.Endpoints.post_LendBook;
export const post_LendBook = {
  method: z.literal("POST"),
  path: z.literal("/books/lend"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { body: LendCreation },
  responses: { 200: z.unknown(), 401: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type get_BooksOwned = __TypedOpenapi.Endpoints.get_BooksOwned;
export const get_BooksOwned = {
  method: z.literal("GET"),
  path: z.literal("/users/ownedBy/{userId}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { query: z.strictObject({ page: z.coerce.number().int().default(0) }).partial().optional(), path: z.strictObject({ userId: z.coerce.number().int() }), header: z.strictObject({ ETag: z.string() }).partial().optional() },
  responses: { 200: ListedDataBook, 304: z.unknown(), 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type get_BookRequests = __TypedOpenapi.Endpoints.get_BookRequests;
export const get_BookRequests = {
  method: z.literal("GET"),
  path: z.literal("/users/ownedBy/{userId}/requests/{bookId}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { query: z.strictObject({ page: z.coerce.number().int().default(0) }).partial().optional(), path: z.strictObject({ userId: z.coerce.number().int(), bookId: z.coerce.number().int() }), header: z.strictObject({ ETag: z.string() }).partial().optional() },
  responses: { 200: ListedDataRequest, 304: z.unknown(), 400: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type get_GetBookOwners = __TypedOpenapi.Endpoints.get_GetBookOwners;
export const get_GetBookOwners = {
  method: z.literal("GET"),
  path: z.literal("/books/owners/{ISBN}"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { query: z.strictObject({ page: z.coerce.number().int().default(0) }).partial().optional(), path: z.strictObject({ ISBN: z.string().min(10).max(13).regex(new RegExp("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)")) }), header: z.strictObject({ ETag: z.string() }).partial().optional() },
  responses: { 200: ListedDataUser, 304: z.unknown(), 401: ProblemDetail, 404: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type get_GetBooks = __TypedOpenapi.Endpoints.get_GetBooks;
export const get_GetBooks = {
  method: z.literal("GET"),
  path: z.literal("/books/"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: { query: z.strictObject({ page: z.coerce.number().int().default(0) }).partial().optional(), header: z.strictObject({ ETag: z.string() }).partial().optional() },
  responses: { 200: ListedDataBook, 304: z.unknown(), 422: ValidationProblemDetail, 500: ProblemDetail },
};

export type delete_Logout = __TypedOpenapi.Endpoints.delete_Logout;
export const delete_Logout = {
  method: z.literal("DELETE"),
  path: z.literal("/userAuth/logout"),
  requestFormat: z.literal("json"),
  responseFormat: z.literal("json"),
  parameters: z.never(),
  responses: { 200: z.unknown(), 401: ProblemDetail, 422: ValidationProblemDetail, 500: ProblemDetail },
};

// </Endpoints>

  
     // <EndpointByMethod>
     export const EndpointByMethod = {
     post: {
           "/users/owned/{ISBN}": post_AddBookOwned,
"/users/blacklist/{email}": post_AddBlacklist,
"/userAuth/login": post_Login,
"/userAuth/createUser": post_CreateUser,
"/books/{ISBN}": post_PostBook,
"/books/request": post_RequestBook,
"/books/lend": post_LendBook
         },
get: {
           "/users/ownedBy/{userId}": get_BooksOwned,
"/users/ownedBy/{userId}/requests/{bookId}": get_BookRequests,
"/books/owners/{ISBN}": get_GetBookOwners,
"/books/": get_GetBooks
         },
delete: {
           "/userAuth/logout": delete_Logout
         }
     } satisfies { [M in keyof __TypedOpenapi.EndpointByMethod]: { [P in keyof __TypedOpenapi.EndpointByMethod[M]]: unknown } }
     export type EndpointByMethod = __TypedOpenapi.EndpointByMethod;
     // </EndpointByMethod>
     

    // <EndpointByMethod.Shorthands>
    export type PostEndpoints = EndpointByMethod["post"]
export type GetEndpoints = EndpointByMethod["get"]
export type DeleteEndpoints = EndpointByMethod["delete"]
    // </EndpointByMethod.Shorthands>
    
  
// <ApiClientTypes>
export type EndpointParameters = {
  body?: unknown;
  query?: unknown;
  header?: unknown;
  path?: unknown;
  cookie?: unknown;
};

export type MutationMethod = "post" | "put" | "patch" | "delete";
export type Method = "get" | "head" | "options" | "trace" | MutationMethod;

export type RequestFormat = "json" | "form-data" | "form-url" | "binary" | "text";
export type ResponseFormat = "json" | "sse";
export type SecurityRequirements = readonly (readonly string[])[];


    // <EndpointRequestFormats>
    /** Non-json request body encodings; missing entries default to `"json"`. */
    export const endpointRequestFormats = {
    
    } as Partial<{ [M in keyof EndpointByMethod]: Partial<{ [P in keyof EndpointByMethod[M]]: RequestFormat }> }>;
    // </EndpointRequestFormats>
    

    // <EndpointParameterStyles>
    export type ParameterSerialization = { style: string; explode: boolean; allowReserved: boolean };
    export type EndpointParameterStyles = Partial<Record<"query" | "path" | "header" | "cookie", Record<string, ParameterSerialization>>>;
    /** OpenAPI parameter styles used by the built-in encoders. */
    export const endpointParameterStyles = {"post":{"/users/owned/{ISBN}":{"path":{"ISBN":{"style":"simple","explode":false,"allowReserved":false}}},"/users/blacklist/{email}":{"path":{"email":{"style":"simple","explode":false,"allowReserved":false}}},"/books/{ISBN}":{"path":{"ISBN":{"style":"simple","explode":false,"allowReserved":false}}}},"get":{"/users/ownedBy/{userId}":{"query":{"page":{"style":"form","explode":true,"allowReserved":false}},"path":{"userId":{"style":"simple","explode":false,"allowReserved":false}},"header":{"ETag":{"style":"simple","explode":false,"allowReserved":false}}},"/users/ownedBy/{userId}/requests/{bookId}":{"query":{"page":{"style":"form","explode":true,"allowReserved":false}},"path":{"userId":{"style":"simple","explode":false,"allowReserved":false},"bookId":{"style":"simple","explode":false,"allowReserved":false}},"header":{"ETag":{"style":"simple","explode":false,"allowReserved":false}}},"/books/owners/{ISBN}":{"query":{"page":{"style":"form","explode":true,"allowReserved":false}},"path":{"ISBN":{"style":"simple","explode":false,"allowReserved":false}},"header":{"ETag":{"style":"simple","explode":false,"allowReserved":false}}},"/books/":{"query":{"page":{"style":"form","explode":true,"allowReserved":false}},"header":{"ETag":{"style":"simple","explode":false,"allowReserved":false}}}}} as Partial<Record<string, Partial<Record<string, EndpointParameterStyles>>>>;
    // </EndpointParameterStyles>
    

    // <EndpointResponseFormats>
    /** Non-json response body modes; missing entries default to `"json"`. SSE skips JSON parse + output validation. */
    export const endpointResponseFormats = {
    
    } as Partial<{ [M in keyof EndpointByMethod]: Partial<{ [P in keyof EndpointByMethod[M]]: ResponseFormat }> }>;
    // </EndpointResponseFormats>
    

    // <EndpointSecurityRequirements>
    /** OpenAPI security requirements applied when an endpoint has no explicit entry. */
    export const defaultSecurityRequirements = [["bearer"]] as SecurityRequirements;
    /** Endpoint-specific security requirements that differ from the default. */
    export const endpointSecurityRequirements = {
    post: { "/userAuth/login": [],
"/userAuth/createUser": [] },
get: { "/users/ownedBy/{userId}": [],
"/users/ownedBy/{userId}/requests/{bookId}": [],
"/books/": [] }
    } as Partial<{ [M in keyof EndpointByMethod]: Partial<{ [P in keyof EndpointByMethod[M]]: SecurityRequirements }> }>;
    // </EndpointSecurityRequirements>
    

export type DefaultEndpoint = {
  parameters?: EndpointParameters | undefined;
  responses?: Record<string, unknown>;
  responseHeaders?: Record<string, unknown>;
};

export type Endpoint<TConfig extends DefaultEndpoint = DefaultEndpoint> = {
  operationId: string;
  method: Method;
  path: string;
  requestFormat: RequestFormat;
  responseFormat: ResponseFormat;
  parameters?: TConfig["parameters"];
  meta: {
    alias: string;
    hasParameters: boolean;
    areParametersRequired: boolean;
  };
  responses?: TConfig["responses"];
  responseHeaders?: TConfig["responseHeaders"]
};

/**
 * Minimal response surface used by ApiClient — avoids depending on the DOM `Response`
 * global (helpful for Node without DOM lib). Structural typing accepts fetch Response.
 */
export interface FetcherResponse {
  ok: boolean;
  status: number;
  statusText: string;
  headers: {
    get(name: string): string | null;
    getSetCookie?: () => string[];
  };
  /** Present on fetch Response; used for SSE / streaming bodies. */
  body?: ReadableStream<Uint8Array> | null;
  json(): Promise<unknown>;
  text(): Promise<string>;
  arrayBuffer(): Promise<ArrayBuffer>;
  clone(): FetcherResponse;
}

export interface Fetcher {
    decodePathParams?: (path: string, pathParams: unknown, styles?: Record<string, ParameterSerialization>) => string
  encodeSearchParams?: (searchParams: unknown, styles?: Record<string, ParameterSerialization>) => URLSearchParams | undefined
  /** Merge cookie params into request headers (default: Cookie header). */
  encodeCookies?: (cookies: unknown, headers: Headers) => void
    //
    fetch: (input: {
      method: Method;
      url: URL;
      urlSearchParams?: URLSearchParams | undefined;
      parameters?: EndpointParameters | undefined;
      path: string;
      /** How to encode `parameters.body` (from OpenAPI requestBody content type). */
      requestFormat: RequestFormat;
      /** OpenAPI parameter serialization metadata for the current endpoint. */
      parameterStyles?: EndpointParameterStyles;
      /** OpenAPI security requirements for this operation. Empty means no credentials are required. */
      security?: SecurityRequirements;
      overrides?: RequestInit;
      throwOnStatusError?: boolean
    }) => Promise<FetcherResponse>;
    parseResponseData?: (response: FetcherResponse) => Promise<unknown>
}

export const successStatusCodes = [200,201,202,203,204,205,206,207,208,226,300,301,302,303,304,305,306,307,308] as const;
export type SuccessStatusCode = typeof successStatusCodes[number];

export const errorStatusCodes = [400,401,402,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,421,422,423,424,425,426,428,429,431,451,500,501,502,503,504,505,506,507,508,510,511] as const;
export type ErrorStatusCode = typeof errorStatusCodes[number];

// Taken from https://github.com/unjs/fetchdts/blob/ec4eaeab5d287116171fc1efd61f4a1ad34e4609/src/fetch.ts#L3
export interface TypedHeaders<TypedHeaderValues = unknown> extends Omit<Headers, 'append' | 'delete' | 'get' | 'getSetCookie' | 'has' | 'set' | 'forEach'> {
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/append) */
  append: <Name extends Extract<keyof TypedHeaderValues, string> | string & {}> (name: Name, value: Lowercase<Name> extends keyof TypedHeaderValues ? TypedHeaderValues[Lowercase<Name>] : string) => void
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/delete) */
  delete: <Name extends Extract<keyof TypedHeaderValues, string> | string & {}> (name: Name) => void
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/get) */
  get: <Name extends Extract<keyof TypedHeaderValues, string> | string & {}> (name: Name) => (Lowercase<Name> extends keyof TypedHeaderValues ? TypedHeaderValues[Lowercase<Name>] : string) | null
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/getSetCookie) */
  getSetCookie: () => string[]
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/has) */
  has: <Name extends Extract<keyof TypedHeaderValues, string> | string & {}> (name: Name) => boolean
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/Headers/set) */
  set: <Name extends Extract<keyof TypedHeaderValues, string> | string & {}> (name: Name, value: Lowercase<Name> extends keyof TypedHeaderValues ? TypedHeaderValues[Lowercase<Name>] : string) => void
  forEach: (callbackfn: (value: TypedHeaderValues[keyof TypedHeaderValues] | string & {}, key: Extract<keyof TypedHeaderValues, string> | string & {}, parent: TypedHeaders<TypedHeaderValues>) => void, thisArg?: unknown) => void
}

/** @see https://developer.mozilla.org/en-US/docs/Web/API/Response */
export interface TypedSuccessResponse<TSuccess, TStatusCode, THeaders> extends Omit<FetcherResponse, "ok" | "status" | "json" | "headers"> {
  ok: true;
  status: TStatusCode;
  headers: never extends THeaders ? FetcherResponse["headers"] : TypedHeaders<THeaders>;
  data: TSuccess;
  /** [MDN Reference](https://developer.mozilla.org/en-US/docs/Web/API/Response/json) */
  json: () => Promise<TSuccess>;
}

/** @see https://developer.mozilla.org/en-US/docs/Web/API/Response */
export interface TypedErrorResponse<TData, TStatusCode, THeaders> extends Omit<FetcherResponse, "ok" | "status" | "json" | "headers"> {
  ok: false;
  status: TStatusCode;
  headers: never extends THeaders ? FetcherResponse["headers"] : TypedHeaders<THeaders>;
  data: TData;
  /** [MDN Reference](https://developer.mozilla.org/en-US/docs/Web/API/Response/json) */
  json: () => Promise<TData>;
}

type StatusCodeFromKey<TKey> = TKey extends `${infer TStatusCode extends number}`
  ? TStatusCode
  : TKey extends number
    ? TKey
    : never;

export type TypedApiResponse<TAllResponses = {}, THeaders = {}> = {
  [K in keyof TAllResponses]: StatusCodeFromKey<K> extends infer TStatusCode extends number
    ? TStatusCode extends SuccessStatusCode
      ? TypedSuccessResponse<TAllResponses[K], TStatusCode, K extends keyof THeaders ? THeaders[K] : never>
      : TypedErrorResponse<TAllResponses[K], TStatusCode, K extends keyof THeaders ? THeaders[K] : never>
    : never;
}[keyof TAllResponses];

type __TypedOpenapiSchema<TOutput, TInput = TOutput> = {
  readonly __typedOpenapiOutput: TOutput;
  readonly __typedOpenapiInput: TInput;
};
type OptionalUndefinedKeys<T> = {
  [K in keyof T as undefined extends T[K] ? never : K]: T[K];
} & {
  [K in keyof T as undefined extends T[K] ? K : never]?: Exclude<T[K], undefined>;
};
export type InferSchemaValue<T> = T extends string | number | boolean | bigint | symbol | null | undefined ? T : T extends __TypedOpenapiSchema<infer O> ? O : T extends z.ZodType ? z.infer<T> : T extends (...args: never[]) => unknown ? T : T extends object ? { [K in keyof T]: InferSchemaValue<T[K]> } : T;
type InferSchemaInputRaw<T> = T extends string | number | boolean | bigint | symbol | null | undefined ? T : T extends __TypedOpenapiSchema<infer _O, infer I> ? I : T extends z.ZodType ? z.input<T> : T extends (...args: never[]) => unknown ? T : T extends object ? { [K in keyof T]: InferSchemaInputRaw<T[K]> } : T;
type InferSchemaInput<T> = OptionalUndefinedKeys<InferSchemaInputRaw<T>>;

export type SafeApiResponse<TEndpoint> = TEndpoint extends { responses: infer TResponses }
  ? TResponses extends Record<string | number, unknown>
    ? TypedApiResponse<InferSchemaValue<TResponses>, TEndpoint extends { responseHeaders: infer THeaders } ? InferSchemaValue<THeaders> : never>
    : never
  : never

export type InferResponseByStatus<TEndpoint, TStatusCode> = Extract<SafeApiResponse<TEndpoint>, { status: TStatusCode }>

/**
 * Success-body payload — InferSchemaValue only on success statuses.
 * Filter with extends {} like the old Extract { data: {} } so unknown bodies (e.g. 304) drop out.
 */
export type InferSuccessData<TEndpoint> = TEndpoint extends { responses: infer TResponses }
  ? {
      [K in keyof TResponses]: StatusCodeFromKey<K> extends infer TStatusCode extends number
        ? TStatusCode extends SuccessStatusCode
          ? Extract<InferSchemaValue<TResponses[K]>, {}>
          : never
        : never;
    }[keyof TResponses]
  : never;

type RequiredKeys<T> = {
  [P in keyof T]-?: undefined extends T[P] ? never : P;
}[keyof T];

type MaybeOptionalArg<T> = RequiredKeys<T> extends never ? [config?: T] : [config: T];
type NotNever<T> = [T] extends [never] ? false : true;

export type ApiQueryOptions = {
  /** Override whether a generated TanStack Query consumes TanStack Query's AbortSignal. */
  consumeQuerySignal?: boolean;
};

/** Call options merged onto inferred endpoint parameters. */
type ApiRequestOptions = {
  overrides?: RequestInit;
  queryOptions?: ApiQueryOptions;
  withResponse?: boolean;
  throwOnStatusError?: boolean;
  validate?: ValidateSide;
};

/** Parameter bag for an endpoint + request options. */
export type ApiCallParams<TEndpoint> = TEndpoint extends { parameters: infer UParams }
  ? NotNever<InferSchemaInput<UParams>> extends true
    ? InferSchemaInput<UParams> & ApiRequestOptions
    : ApiRequestOptions
  : ApiRequestOptions;

/** Resolve response type from withResponse flag on the call config. */
export type ApiCallResult<TEndpoint, TParams> = TParams extends { withResponse: true }
  ? SafeApiResponse<TEndpoint>
  : InferSuccessData<TEndpoint>;

export type ValidateSide = "none" | "input" | "output" | "both";
export type OnValidate = (ctx: {
  side: "input" | "output";
  method: string;
  path: string;
  schema: unknown;
  value: unknown;
}) => unknown | Promise<unknown>;

// </ApiClientTypes>

// <TypedStatusError>
export class TypedStatusError<TData = unknown> extends Error {
  response: TypedErrorResponse<TData, ErrorStatusCode, unknown>;
  status: number;
  constructor(response: TypedErrorResponse<TData, ErrorStatusCode, unknown>) {
    super(`HTTP ${response.status}: ${response.statusText}`);
    this.name = 'TypedStatusError';
    this.response = response;
    this.status = response.status;
  }
}
// </TypedStatusError>


// <ValidateHelpers>
const defaultParse = (schema: unknown, value: unknown): unknown => {
  return (schema as { parse: (v: unknown) => unknown }).parse(value);
};

const runValidate = async (ctx: {
  side: "input" | "output";
  method: string;
  path: string;
  schema: unknown;
  value: unknown;
  onValidate?: OnValidate;
}): Promise<unknown> => {
  if (ctx.onValidate) return ctx.onValidate(ctx);
  return defaultParse(ctx.schema, ctx.value);
};
// </ValidateHelpers>



// <ApiClient>
const isPlainObject = (value: unknown): value is Record<string, unknown> => {
  if (value === null || typeof value !== "object") return false;
  const prototype = Object.getPrototypeOf(value);
  return prototype === Object.prototype || prototype === null;
};

export class ApiClient {
  baseUrl: string = "";
  successStatusCodes = successStatusCodes;
  errorStatusCodes = errorStatusCodes;
  validate: ValidateSide = "both";
  onValidate?: OnValidate;

  constructor(
    public fetcher: Fetcher,
    options?: { validate?: ValidateSide; onValidate?: OnValidate },
  ) {
    if (options?.validate !== undefined) this.validate = options.validate;
    if (options?.onValidate) this.onValidate = options.onValidate;
  }

  setBaseUrl(baseUrl: string) {
    this.baseUrl = baseUrl;
    return this;
  }

  setValidate(validate: ValidateSide) {
    this.validate = validate;
    return this;
  }

  setOnValidate(onValidate: OnValidate | undefined) {
    if (onValidate === undefined) {
      delete this.onValidate;
    } else {
      this.onValidate = onValidate;
    }
    return this;
  }

  /**
   * Replace path parameters in URL
   * Supports both OpenAPI format {param} and Express format :param
   */
  defaultDecodePathParams = (url: string, params: unknown, styles?: Record<string, ParameterSerialization>): string => {
    const record = (params ?? {}) as Record<string, unknown>;
    const encode = (value: unknown) => encodeURIComponent(String(value));
    const serialize = (key: string, value: unknown): string => {
      const parameterStyle = styles?.[key];
      const style = parameterStyle?.style ?? "simple";
      const explode = parameterStyle?.explode ?? false;
      if (style === "label") {
        if (Array.isArray(value)) return "." + value.filter((item) => item != null).map(encode).join(explode ? "." : ",");
        if (isPlainObject(value)) {
          const entries = Object.entries(value as Record<string, unknown>).filter(([, item]) => item != null);
          return "." + (explode ? entries.map(([name, item]) => encode(name) + "=" + encode(item)).join(".") : entries.flatMap(([name, item]) => [encode(name), encode(item)]).join(","));
        }
        return "." + encode(value);
      }
      if (style === "matrix") {
        if (Array.isArray(value)) return explode ? value.filter((item) => item != null).map((item) => ";" + key + "=" + encode(item)).join("") : ";" + key + "=" + value.filter((item) => item != null).map(encode).join(",");
        if (isPlainObject(value)) {
          const entries = Object.entries(value as Record<string, unknown>).filter(([, item]) => item != null);
          return explode ? entries.map(([name, item]) => ";" + encode(name) + "=" + encode(item)).join("") : ";" + key + "=" + entries.flatMap(([name, item]) => [encode(name), encode(item)]).join(",");
        }
        return ";" + key + "=" + encode(value);
      }
      if (Array.isArray(value)) return value.filter((item) => item != null).map(encode).join(",");
      if (isPlainObject(value)) {
        return Object.entries(value as Record<string, unknown>)
          .filter(([, item]) => item != null)
          .map(([name, item]) => explode ? encode(name) + "=" + encode(item) : [encode(name), encode(item)])
          .flat()
          .join(",");
      }
      return encode(value);
    };
    return url
      .replace(/{([^}]+)}/g, (_, key: string) =>
        record[key] != null ? serialize(key, record[key]) : `{${key}}`,
      )
      .replace(/:([a-zA-Z0-9_]+)/g, (_, key: string) =>
        record[key] != null ? serialize(key, record[key]) : `:${key}`,
      );
  }

  /** Uses URLSearchParams, skips null/undefined values */
  defaultEncodeSearchParams = (queryParams: unknown, styles?: Record<string, ParameterSerialization>): URLSearchParams | undefined => {
    if (!queryParams || typeof queryParams !== "object") return;

    const searchParams = new URLSearchParams();
    const rawEntries: Array<{ key: string; value: string; allowReserved: boolean }> = [];
    const append = (key: string, value: unknown, allowReserved = false) => {
      const stringValue = String(value);
      searchParams.append(key, stringValue);
      rawEntries.push({ key, value: stringValue, allowReserved });
    };
    const encodeQueryComponent = (value: string, allowReserved: boolean) => {
      const encoded = encodeURIComponent(value);
      return allowReserved
        ? encoded.replace(/%3A|%2F|%3F|%40|%21|%24|%26|%27|%28|%29|%2A|%2B|%2C|%3B|%3D|%5B|%5D/gi, (part) => decodeURIComponent(part))
        : encoded;
    };
    Object.defineProperty(searchParams, "toString", {
      value: () => rawEntries.map(({ key, value, allowReserved }) => `${encodeQueryComponent(key, false)}=${encodeQueryComponent(value, allowReserved)}`).join("&"),
    });
    Object.entries(queryParams as Record<string, unknown>).forEach(([key, value]) => {
      if (value != null) {
        // Skip null/undefined values
        const parameterStyle = styles?.[key];
        const style = parameterStyle?.style ?? "form";
        const explode = parameterStyle?.explode ?? true;
        const allowReserved = parameterStyle?.allowReserved === true;
        if (Array.isArray(value)) {
          if (style === "spaceDelimited") append(key, value.filter((item) => item != null).map(String).join(" "), allowReserved);
          else if (style === "pipeDelimited") append(key, value.filter((item) => item != null).map(String).join("|"), allowReserved);
          else if (explode) value.forEach((val) => val != null && append(key, val, allowReserved));
          else append(key, value.filter((item) => item != null).map(String).join(","), allowReserved);
        } else if (isPlainObject(value)) {
          const entries = Object.entries(value as Record<string, unknown>).filter(([, nestedValue]) => nestedValue != null);
          if (style === "deepObject") {
            for (const [nestedKey, nestedValue] of entries) {
              if (Array.isArray(nestedValue)) nestedValue.forEach((item) => item != null && append(`${key}[${nestedKey}]`, item, allowReserved));
              else append(`${key}[${nestedKey}]`, nestedValue, allowReserved);
            }
          } else if (explode) {
            for (const [nestedKey, nestedValue] of entries) {
              if (Array.isArray(nestedValue)) nestedValue.forEach((item) => item != null && append(nestedKey, item, allowReserved));
              else append(nestedKey, nestedValue, allowReserved);
            }
          } else {
            append(key, entries.flatMap(([nestedKey, nestedValue]) => [nestedKey, ...(Array.isArray(nestedValue) ? nestedValue : [nestedValue])]).map(String).join(","), allowReserved);
          }
        } else {
          append(key, value, allowReserved);
        }
      }
    });

    return searchParams;
  }

  /** Append cookie params as a Cookie header (or merge into existing). */
  defaultEncodeCookies = (cookies: unknown, headers: Headers): void => {
    if (!cookies || typeof cookies !== "object") return;
    const parts = Object.entries(cookies as Record<string, unknown>)
      .filter(([, value]) => value != null)
      .map(([key, value]) => `${key}=${String(value)}`);
    if (!parts.length) return;
    const existing = headers.get("cookie");
    headers.set("cookie", existing ? `${existing}; ${parts.join("; ")}` : parts.join("; "));
  }

  defaultParseResponseData = async (response: FetcherResponse): Promise<unknown> => {
    const contentType = response.headers.get("content-type") ?? "";
    const normalizedContentType = contentType.toLowerCase();
    if (normalizedContentType.includes("text/event-stream")) {
      return response.body ?? null;
    }
    if (normalizedContentType.startsWith("text/")) {
      return (await response.text())
    }

    if (normalizedContentType.startsWith("application/octet-stream")) {
      return new Blob([await response.arrayBuffer()])
    }

    if (
      normalizedContentType.includes("application/json") ||
      (normalizedContentType.includes("application/") && normalizedContentType.includes("json")) ||
      normalizedContentType === "*/*"
      ) {
      try {
        return await response.json();
      } catch {
        return undefined
      }
    }

    return
  }

  // <ApiClient.post>
    post<Path extends keyof PostEndpoints, TEndpoint extends PostEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<SafeApiResponse<TEndpoint>>;

    post<Path extends keyof PostEndpoints, TEndpoint extends PostEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<InferSuccessData<TEndpoint>>;

    post<Path extends keyof PostEndpoints>(
      path: Path,
      ...params: [config?: unknown]
    ): Promise<unknown> {
        return this.request("post", path, params[0] as never) as Promise<unknown>;
    }
    // </ApiClient.post>
    
// <ApiClient.get>
    get<Path extends keyof GetEndpoints, TEndpoint extends GetEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<SafeApiResponse<TEndpoint>>;

    get<Path extends keyof GetEndpoints, TEndpoint extends GetEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<InferSuccessData<TEndpoint>>;

    get<Path extends keyof GetEndpoints>(
      path: Path,
      ...params: [config?: unknown]
    ): Promise<unknown> {
        return this.request("get", path, params[0] as never) as Promise<unknown>;
    }
    // </ApiClient.get>
    
// <ApiClient.delete>
    delete<Path extends keyof DeleteEndpoints, TEndpoint extends DeleteEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<SafeApiResponse<TEndpoint>>;

    delete<Path extends keyof DeleteEndpoints, TEndpoint extends DeleteEndpoints[Path]>(
      path: Path,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<InferSuccessData<TEndpoint>>;

    delete<Path extends keyof DeleteEndpoints>(
      path: Path,
      ...params: [config?: unknown]
    ): Promise<unknown> {
        return this.request("delete", path, params[0] as never) as Promise<unknown>;
    }
    // </ApiClient.delete>
    

    // <ApiClient.request>
    /**
     * Generic request method with full type-safety for any endpoint
     */
    request<
      TMethod extends keyof EndpointByMethod,
      TPath extends keyof EndpointByMethod[TMethod],
      TEndpoint extends EndpointByMethod[TMethod][TPath]
    >(
      method: TMethod,
      path: TPath,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse: true; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<SafeApiResponse<TEndpoint>>;

    request<
      TMethod extends keyof EndpointByMethod,
      TPath extends keyof EndpointByMethod[TMethod],
      TEndpoint extends EndpointByMethod[TMethod][TPath]
    >(
      method: TMethod,
      path: TPath,
      ...params: MaybeOptionalArg<
        (TEndpoint extends { parameters: infer UParams }
          ? NotNever<InferSchemaInput<UParams>> extends true ? InferSchemaInput<UParams> & { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide } : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide }
          : { overrides?: RequestInit; queryOptions?: ApiQueryOptions; withResponse?: false; throwOnStatusError?: boolean; validate?: ValidateSide })
      >
    ): Promise<InferSuccessData<TEndpoint>>;

    request<
      TMethod extends keyof EndpointByMethod,
      TPath extends keyof EndpointByMethod[TMethod],
      TEndpoint extends EndpointByMethod[TMethod][TPath]
    >(
      method: TMethod,
      path: TPath,
      ...params: [config?: unknown]
    ): Promise<unknown> {
      return (async () => {
      const requestParams = params[0] as
        | (EndpointParameters & {
            overrides?: RequestInit;
            queryOptions?: ApiQueryOptions;
            withResponse?: boolean;
            throwOnStatusError?: boolean;
            validate?: ValidateSide;
          })
        | undefined;
      const withResponse = requestParams?.withResponse;
      const throwOnStatusError = requestParams?.throwOnStatusError ?? (withResponse ? false : true);
      let overrides = requestParams?.overrides;
      const validateSide: ValidateSide = requestParams?.validate ?? this.validate;

      const parametersToSend: EndpointParameters = {};
      if (requestParams?.body !== undefined) parametersToSend.body = requestParams.body;
      if (requestParams?.query !== undefined) parametersToSend.query = requestParams.query;
      if (requestParams?.header !== undefined) parametersToSend.header = requestParams.header;
      if (requestParams?.path !== undefined) parametersToSend.path = requestParams.path;
      if (requestParams?.cookie !== undefined) parametersToSend.cookie = requestParams.cookie;

      type RuntimeEndpoint = {
        parameters?: Partial<Record<"body" | "query" | "header" | "path" | "cookie", unknown>>;
        responses?: Record<string, unknown>;
      };
      const endpointSchema = EndpointByMethod[method][path] as RuntimeEndpoint;
      const shouldValidateInput = validateSide === "input" || validateSide === "both";
      if (shouldValidateInput && endpointSchema.parameters) {
        const paramSchema = endpointSchema.parameters;
        for (const key of ["body", "query", "header", "path", "cookie"] as const) {
          const schema = paramSchema[key];
          const value = parametersToSend[key];
          if (schema !== undefined && value !== undefined) {
            parametersToSend[key] = await runValidate({
              side: "input",
              method: String(method),
              path: String(path),
              schema,
              value,
              ...(this.onValidate ? { onValidate: this.onValidate } : {}),
            });
          }
        }
      }

      const resolvedPath = (this.fetcher.decodePathParams ?? this.defaultDecodePathParams)(
        this.baseUrl + (path as string),
        parametersToSend.path ?? {},
        endpointParameterStyles[method]?.[path]?.path,
      );
      const url = new URL(resolvedPath);
      const urlSearchParams = (this.fetcher.encodeSearchParams ?? this.defaultEncodeSearchParams)(
        parametersToSend.query,
        endpointParameterStyles[method]?.[path]?.query,
      );

      if (parametersToSend.cookie) {
        const headers = new Headers((overrides as RequestInit | undefined)?.headers);
        (this.fetcher.encodeCookies ?? this.defaultEncodeCookies)(parametersToSend.cookie, headers);
        overrides = { ...overrides, headers };
      }

      const parameterStyles = endpointParameterStyles[method]?.[path as string];
      const response = await this.fetcher.fetch({
        method: method,
        path: (path as string),
        url,
        ...(urlSearchParams ? { urlSearchParams } : {}),
        ...(Object.keys(parametersToSend).length ? { parameters: parametersToSend } : {}),
        requestFormat: endpointRequestFormats[method]?.[path] ?? "json",
        ...(parameterStyles ? { parameterStyles } : {}),
        security: endpointSecurityRequirements[method]?.[path] ?? defaultSecurityRequirements,
        ...(overrides ? { overrides } : {}),
        throwOnStatusError
      });
          const responseFormat = endpointResponseFormats[method]?.[path] ?? "json";
          let data =
            responseFormat === "sse"
              ? (response.body ?? null)
              : await (this.fetcher.parseResponseData ?? this.defaultParseResponseData)(response);
          const shouldValidateOutput = validateSide === "output" || validateSide === "both";
          if (
            shouldValidateOutput &&
            responseFormat !== "sse" &&
            (response.ok || !(errorStatusCodes as readonly number[]).includes(response.status)) &&
            endpointSchema?.responses
          ) {
            const responseSchema =
              endpointSchema.responses[String(response.status)] ??
              endpointSchema.responses[String(Math.floor(response.status / 100)) + "xx"] ??
              endpointSchema.responses[String(Math.floor(response.status / 100)) + "XX"] ??
              endpointSchema.responses["default"];
            if (responseSchema) {
              data = await runValidate({
                side: "output",
                method: String(method),
                path: String(path),
                schema: responseSchema,
                value: data,
                ...(this.onValidate ? { onValidate: this.onValidate } : {}),
              });
            }
          }
          const typedResponse = Object.assign(response, {
            data: data,
            json: () => Promise.resolve(data)
          }) as SafeApiResponse<TEndpoint>;

          if (throwOnStatusError && (errorStatusCodes as readonly number[]).includes(response.status)) {
            throw new TypedStatusError(typedResponse as TypedErrorResponse<unknown, ErrorStatusCode, unknown>);
          }

          return withResponse ? typedResponse : data;
      })()
    }
    // </ApiClient.request>
}

export function createApiClient(
  fetcher: Fetcher,
  baseUrl?: string,
  options?: { validate?: ValidateSide; onValidate?: OnValidate },
) {
  return new ApiClient(fetcher, options).setBaseUrl(baseUrl ?? "");
}


/**
 Example usage:
 const api = createApiClient((method, url, params) =>
   fetch(url, { method, body: JSON.stringify(params) }).then((res) => res.json()),
 );
 api.get("/users").then((users) => console.log(users));
 api.post("/users", { body: { name: "John" } }).then((user) => console.log(user));
 api.put("/users/:id", { path: { id: 1 }, body: { name: "John" } }).then((user) => console.log(user));

 // With error handling
 const result = await api.get("/users/{id}", { path: { id: "123" }, withResponse: true });
 if (result.ok) {
   // Access data directly
   const user = result.data;
   console.log(user);

   // Or use the json() method for compatibility
   const userFromJson = await result.json();
   console.log(userFromJson);
 } else {
   const error = result.data;
   console.error(`Error ${result.status}:`, error);
 }
*/

// </ApiClient>

  