import React, { useReducer } from 'react';
import { Navigate, useLocation } from 'react-router';
import { useAuthenticationContext, type AuthenticationState } from '../context/userAuthentication';
import {userCreationZod, zodCreateUser} from '../api/requests';
import { ZodError } from 'zod';
import type { QuickGeneralAction, QuickGeneralState } from '~/utils/reducerTypes';
import type { JSONSchema } from 'zod/v4/core';
import { JsonSchemaToInputRestrictions, numberValidate, stringValidate } from '~/utils/inputRestrictions';
import { post_CreateUser } from '~/api/typed-openapi/schema';

//import {useQuery} from '@tanstack/react-query';

type Input = {
    username: string;
    password: string;
    x: string; 
    y: string; 
}

type InputWithoutSensitive = Omit<Input, "password">;

type State = QuickGeneralState<Input>;

type Action = QuickGeneralAction<Input>;

function logUnexpectedAction(state: State, action: Action) {
    console.log(`Unexpected action '${action.type}' on state '${state.tag}'`);
}
/**
 * 
 * As discussed {@link https://ux.stackexchange.com/questions/39999/why-do-most-create-account-forms-clear-the-password-fields-upon-wrong-validation here}, 
 * some inputs are sensitive and should not be stored.
 */
function reduce(state: State, action: Action): State {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                //console.log("Action edit ", action.propertySet.propertyName, " with value ", action.propertySet.inputValue, " of type ", typeof action.propertySet.inputValue)

                const newState: State = {tag: 'editing', error: undefined, inputs: state.inputs, validationErrors: state.validationErrors}
                newState.inputs[action.propertySet.propertyName] = action.propertySet.inputValue;
                newState.validationErrors[action.propertySet.propertyName] = undefined;

                return newState;
            } else if (action.type === 'submit') {
                return { tag: 'submitting', inputs: state.inputs };
            } else if(action.type === 'validationErrors'){
                return {tag: 'editing', error: "Validation error", inputs: {...state.inputs, password: ""}, validationErrors: action.errors}
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'submitting':
            if (action.type === 'success') {
                return { tag: 'done' };
            } else if (action.type === 'error') {

                const newInputs = state.inputs;
                newInputs.password = ''

                return { tag: 'editing', error: action.message, inputs: newInputs , validationErrors: {
                    username: undefined,
                    password: undefined,
                    x: undefined,
                    y: undefined
                } };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'done':
            logUnexpectedAction(state, action);
            return state;
    }
}


const bodyParameters: Record<string, JSONSchema._JSONSchema> = post_CreateUser.parameters.body.toJSONSchema().properties!!;
const usernameRestrictions = JsonSchemaToInputRestrictions(bodyParameters["email"]);
const passwordRestrictions = JsonSchemaToInputRestrictions(bodyParameters["password"]);
const xRestrictions = JsonSchemaToInputRestrictions(bodyParameters["x"]);
const yRestrictions = JsonSchemaToInputRestrictions(bodyParameters["y"]);


// zodcreateUser({ email: "john@example.com", password: "password", x: 0, y: 0 });
/**
 * 
 * @returns React gui object
 */
export default function CreateUser() {
    const authState: AuthenticationState = useAuthenticationContext();
    const [state, dispatch] = useReducer(reduce, { tag: 'editing', inputs: { username: '', password: '', x: '0', y: '0' }, 
        validationErrors: {
            username: undefined,
            password: undefined,
            x: undefined,
            y: undefined
    } });
    const location = useLocation();

    // Handle redirection after login
    if (state.tag === 'done') {
        //return React.createElement(Navigate, {to: location.state?.source || '/'})
        return (<Navigate to={location.state?.source || '/'} />);
    }

    async function handleSubmit(ev: React.SubmitEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }

        const username = state.inputs.username;
        const password = state.inputs.password;
        const x = Number(state.inputs.x);
        const y = Number(state.inputs.y);

        let usernameValidation = stringValidate(username, usernameRestrictions);
        let passwordValidation = stringValidate(password, passwordRestrictions);
        let xValidation = numberValidate(x, xRestrictions);
        let yValidation = numberValidate(y, yRestrictions);


        if(usernameValidation !== undefined || passwordValidation !== undefined || xValidation !== undefined || yValidation !== undefined){
            return dispatch({type: "validationErrors", errors: {
                username: usernameValidation,
                password: passwordValidation,
                x: xValidation,
                y: yValidation
            }})
        }

        
        dispatch({ type: 'submit' });
        try {
            const result = await zodCreateUser({ email: username, password, x, y });

            //if (result.type === 'failure') {
            //    // Handle error case
            //    dispatch({ type: 'error', message: result.info.detail});
            //    return;
            //}

            // On successful login, update the context state
            authState.auth(username, result.token); //result.info.token

            // Transition to the done state
            dispatch({ type: 'success' });
        } catch (error) {
            console.error('Error during user creation:');
            console.log(error)
            dispatch({ type: 'error', message: 'Operation could not be completed' });
        }
    }

    const username = state.inputs.username;
    const password = state.tag === 'submitting' ? '' : state.inputs.password;

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
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({type: "edit", propertySet: { propertyName: "username", inputValue: ev.currentTarget.value}})}
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.username}</span>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        name="password"
                        value={password}
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({type: "edit", propertySet: { propertyName: "password", inputValue: ev.currentTarget.value}})}
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.password}</span>
                </div>
                <div>
                    <label htmlFor="x">X Coordinate</label>
                    <input
                        id="x"
                        type="number"
                        name="x"
                        value={state.inputs.x}
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({type: "edit", propertySet: { propertyName: "x", inputValue: ev.currentTarget.value}})}
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.x}</span>
                </div>
                <div>
                    <label htmlFor="y">Y Coordinate</label>
                    <input
                        id="y"
                        type="number"
                        name="y"
                        value={state.inputs.y}
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({type: "edit", propertySet: { propertyName: "y", inputValue: ev.currentTarget.value}})}
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.y}</span>
                </div>
                <div>
                    <button type="submit">Create user</button>
                </div>
            </fieldset>
            {state.tag === 'editing' && state.error && <p>{state.error}</p>}
        </form>
    );
}

