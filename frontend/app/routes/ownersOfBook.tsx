import React, { useReducer } from 'react';
import { Navigate, useLocation } from 'react-router';
import { useAuthenticationContext, type AuthenticationState } from '../context/userAuthentication';
import {zodBookOwners, zodCreateUser} from '../api/requests';
import { ZodError } from 'zod';
import type { Schemas } from '~/api/typed-openapi/schema.types';
import { TypedStatusError } from '~/api/typed-openapi/schema';

//import {useQuery} from '@tanstack/react-query';

type State =
    | { tag: 'editing'; error?: string; inputs: { ISBN: string, data?: Schemas.ListedDataUser } }
    | { tag: 'submitting'; inputs: { ISBN: string, data?: Schemas.ListedDataUser } };

type Action =
    | { type: 'edit'; inputName: string; inputValue: string }
    | { type: 'submit' }
    | { type: 'error'; message: string }
    | { type: 'success', data: Schemas.ListedDataUser };

function logUnexpectedAction(state: State, action: Action) {
    console.log(`Unexpected action '${action.type}' on state '${state.tag}'`);
}

function reduce(state: State, action: Action): State {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                return { tag: 'editing', error: undefined, inputs: { ...state.inputs, [action.inputName]: action.inputValue } };
            } else if (action.type === 'submit') {
                //console.log("Changing from state editing to submitting");
                return { tag: 'submitting', inputs: state.inputs };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'submitting':
            if (action.type === 'success') {
                return { tag: 'editing', inputs: { ...state.inputs, data: action.data } };
            } else if (action.type === 'error') {
                return { tag: 'editing', error: action.message, inputs: { ...state.inputs } };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }
    }
}


/**
 * 
 * @returns React gui object
 */
export default function OwnersOfBook() {
    const [state, dispatch] = useReducer(reduce, { tag: 'editing', inputs: { ISBN: '', data: undefined } });


    function handleChange(ev: React.ChangeEvent<HTMLInputElement>) {
        dispatch({ type: 'edit', inputName: ev.currentTarget.name, inputValue: ev.currentTarget.value });
    }

    async function handleSubmit(ev: React.SubmitEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }

        //console.log("Changing to submitting");
        dispatch({ type: 'submit' }); //only happens later? uncomment logs to check

        //console.log("Should be on state submitting; On state ", state.tag);

        const isbn = state.inputs.ISBN;
        const page = 0; // TODO add input

        try {
            const result = await zodBookOwners(isbn, page);

            // Transition to the done state
            dispatch({ type: 'success', data: result });
        } catch (error) {
            console.error('Error during book owner search:');
            if (error instanceof ZodError) {
                console.error('Zod validation error:', error);
                dispatch({ type: 'error', message: 'Validation error' });
            } else if (error instanceof TypedStatusError && error.status === 422) {
                const errorData = await error.response.json() as Schemas.ValidationProblemDetail;
                console.error('Validation error from API:', errorData);
                
            } else {
                console.error('Unexpected error:', error);
                dispatch({ type: 'error', message: 'Operation could not be completed' });
            }
            dispatch({ type: 'error', message: 'Operation could not be completed' });
        }
    }

    const isbn = state.inputs.ISBN;

    return (
        <div>
            <form onSubmit={handleSubmit}>
            <fieldset disabled={state.tag !== 'editing'}>
                <div>
                    <label htmlFor="ISBN">ISBN</label>
                    <input
                        id="ISBN"
                        type="text"
                        name="ISBN"
                        value={isbn}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <button type="submit" disabled={state.tag !== 'editing'}>
                        Search owners
                    </button>
                </div>
            </fieldset>
            {state.tag === 'editing' && state.error && <p>{state.error}</p>}
        </form>

        {state.inputs.data && state.inputs.data.data.length > 0 && 
            <ul>
                {state.inputs.data.data.map((owner) => (
                    <li key={owner.id}>{owner.email}</li>
                ))}
            </ul>}
        </div>
        
    );
}

