import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),

    route("login", "routes/login.tsx"),
    route("createUser", "routes/createUser.tsx"),
    route("ownersOfBook/:ISBN", "routes/ownersOfBook.tsx"),
    //route("test", "routes/testingArea.tsx"),
    //route("test/:param", "routes/testingAreaWithParam.tsx"),

    

] satisfies RouteConfig;
