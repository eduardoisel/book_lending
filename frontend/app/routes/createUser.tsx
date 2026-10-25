import React, { useReducer } from 'react';
import { Navigate, useLocation } from 'react-router';
import { useAuthenticationContext, type AuthenticationState } from '../context/userAuthentication';
import {createUser} from '../api/requests';

type State =
    | { tag: 'editing'; error?: string; inputs: { username: string; password: string, x: number, y: number } }
    | { tag: 'submitting'; inputs: { username: string,  x: number, y: number } }
    | { tag: 'done' };

type Action =
    | { type: 'edit'; inputName: string; inputValue: string }
    | { type: 'submit' }
    | { type: 'error'; message: string }
    | { type: 'success' };

function logUnexpectedAction(state: State, action: Action) {
    console.log(`Unexpected action '${action.type}' on state '${state.tag}'`);
}

function reduce(state: State, action: Action): State {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                return { tag: 'editing', error: undefined, inputs: { ...state.inputs, [action.inputName]: action.inputValue } };
            } else if (action.type === 'submit') {
                return { tag: 'submitting', inputs: state.inputs };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'submitting':
            if (action.type === 'success') {
                return { tag: 'done' };
            } else if (action.type === 'error') {
                return { tag: 'editing', error: action.message, inputs: { ...state.inputs, password: '' } };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'done':
            logUnexpectedAction(state, action);
            return state;
    }
}

/**
 * 
 * @returns React gui object
 */
export default function CreateUser() {
    const authState: AuthenticationState = useAuthenticationContext();
    const [state, dispatch] = useReducer(reduce, { tag: 'editing', inputs: { username: '', password: '', x: 0, y: 0 } });
    const location = useLocation();

    // Handle redirection after login
    if (state.tag === 'done') {
        //return React.createElement(Navigate, {to: location.state?.source || '/'})
        return (<Navigate to={location.state?.source || '/'} />);
    }

    function handleChange(ev: React.ChangeEvent<HTMLInputElement>) {
        dispatch({ type: 'edit', inputName: ev.currentTarget.name, inputValue: ev.currentTarget.value });
    }

    async function handleSubmit(ev: React.SubmitEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }

        dispatch({ type: 'submit' });

        const username = state.inputs.username;
        const password = state.inputs.password;
        const x = state.inputs.x;
        const y = state.inputs.y;

        try {
            const result = await createUser(username, password, x, y);

            if (result.type === 'failure') {
                // Handle error case
                dispatch({ type: 'error', message: result.info.detail});
                return;
            }

            // On successful login, update the context state
            authState.auth(username, result.info.token);

            // Transition to the done state
            dispatch({ type: 'success' });
        } catch (error) {
            console.error('Error during login:', error);
            dispatch({ type: 'error', message: 'Operation could not be completed' });
        }
    }

    const username = state.inputs.username;
    const password = state.tag === 'submitting' ? '' : state.inputs.password;
    const x = state.inputs.x;
    const y = state.inputs.y;

    return (
        <form onSubmit={handleSubmit}>
            <fieldset disabled={state.tag !== 'editing'}>
                <div>
                    <label htmlFor="username">Username</label>
                    <input
                        id="username"
                        type="text"
                        name="username"
                        value={username}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        name="password"
                        value={password}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="x">X Coordinate</label>
                    <input
                        id="x"
                        type="number"
                        name="x"
                        value={x}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="y">Y Coordinate</label>
                    <input
                        id="y"
                        type="number"
                        name="y"
                        value={y}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <button type="submit">Create user</button>
                </div>
            </fieldset>
            {state.tag === 'editing' && state.error && <p>{state.error}</p>}
        </form>
    );
}

