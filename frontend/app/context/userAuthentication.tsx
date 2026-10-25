import React, { createContext, useState, useContext, useEffect } from 'react';
import Cookies from 'js-cookie';

export type AuthenticationState = {
    username: string | undefined;
    token: string | undefined;
    auth: (username: string, token: string) => void;
    unsetAuth: () => void;
}

const AuthenticationContext = createContext<AuthenticationState>({
    username: undefined,
    token: undefined,
    auth: () => {},
    unsetAuth: () => {}
});

export function AuthenticationProvider({ children }: { children: React.ReactNode }) {
    const [username, setUsernameState] = useState<string | undefined>(undefined);
    const [token, setTokenState] = useState<string | undefined>(undefined);
    

    useEffect(() => {
        const savedToken = Cookies.get('token');
        const savedUsername = Cookies.get('username');

        if (savedToken !== undefined && savedUsername !== undefined) {
            setTokenState(savedToken);
            setUsernameState(savedUsername);
        } else {
            setTokenState(undefined);
            setUsernameState(undefined);
        }

    }, []);

    const auth = (username: string, token: string) => {
        setUsernameState(username);
        Cookies.set('username', username, { expires: 6 });
        setTokenState(token);
        Cookies.set('token', token, { expires: 6 });
    }

    const unsetAuth = () => {
        setUsernameState(undefined);
        setTokenState(undefined);
        Cookies.remove('username');
        Cookies.remove('token');
    }

    const value = {
        username,
        token,
        auth,
        unsetAuth
    };

    return <AuthenticationContext.Provider value={value}>{children}</AuthenticationContext.Provider>;
}
/**
 * Note: Context is applicable to all components.
 * @returns user authentication state
 */
export function useAuthenticationContext() {
    const context = useContext(AuthenticationContext);
    if (!context) {
        throw new Error('useAuthentication must be used within an AuthenticationProvider');
    }
    return context;
}
