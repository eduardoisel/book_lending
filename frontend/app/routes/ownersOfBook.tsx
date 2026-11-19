import React, { useActionState, useReducer } from 'react';
import { Navigate, useLocation } from 'react-router';
import { useAuthenticationContext, type AuthenticationState } from '../context/userAuthentication';
import {zodBookOwners, getBookOwners} from '../api/requests';
import { keyof, object, string, ZodError, ZodObject, ZodString } from 'zod';
import type { Schemas } from '~/api/typed-openapi/schema.types';
import type { ZodStandardJSONSchemaPayload, $strict, JSONSchema } from 'zod/v4/core';
import {type GeneralAction, type GeneralState } from '~/utils/reducerTypes';
import { JsonSchemaToInputRestrictions, numberValidate, stringValidate, type InputRestrictions } from '~/utils/inputRestrictions';
import { useFormStatus } from 'react-dom';
import { IsbnCheckSum } from '~/utils/isbn';

//import {useQuery} from '@tanstack/react-query';

type Input = {
    ISBN: string;
    page: string;
};


type State = GeneralState<Input, Schemas.ListedDataUser>;

type Action = GeneralAction<Input, Schemas.ListedDataUser>;

function logUnexpectedAction(state: State, action: Action) {
    console.log(`Unexpected action '${action.type}' on state '${state.tag}'`);
}

function reduce(state: State, action: Action): State {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                state.error = undefined; 
                /*
                state.inputs[action.propertySet.propertyName] = action.propertySet.inputValue;

                let tttt = state.inputs[action.propertySet.propertyName]; 
                
                let property = action.propertySet.propertyName

                if(action.propertySet.propertyName === "ISBN"){
                    const typeofValue = action.propertySet.inputValue;
                    const fard: string | number = 2
                    state.inputs[action.propertySet.propertyName] = action.propertySet.inputValue
                }
                if(action.propertySet.propertyName === "page"){
                    const typeofValue = action.propertySet.inputValue;
                    state.inputs[action.propertySet.propertyName] = action.propertySet.inputValue
                }
                */

                //console.log("changing ", action.propertySet.propertyName, " to ", action.propertySet.inputValue, " of type ", typeof action.propertySet.inputValue);

                const newState: State = {tag: 'editing', error: undefined, inputs: state.inputs, validationErrors: state.validationErrors}
                newState.inputs[action.propertySet.propertyName] = action.propertySet.inputValue;
                newState.validationErrors[action.propertySet.propertyName] = undefined;
                
                return newState;
            } else if (action.type === 'submit') {
                //console.log("Changing from state editing to submitting");
                return { tag: 'submitting', inputs: state.inputs };
            } else if(action.type === 'validationErrors'){
                return {tag: 'editing', error: "Validation error", inputs: state.inputs, validationErrors: action.errors}
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'submitting':
            if (action.type === 'success') {
                return { 
                    tag: 'editing',
                    inputs: { ...state.inputs }, 
                    data: action.data, 
                    validationErrors: {
                        ISBN: undefined,
                        page: undefined
                }};//, validationErrors: undefined };
            } else if (action.type === 'error') {
                return { 
                    tag: 'editing', 
                    error: action.message, 
                    inputs: { ...state.inputs },
                    validationErrors: {
                        ISBN: undefined,
                        page: undefined
                    }
                };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }
    }
}

const PathParameters: Record<string, JSONSchema._JSONSchema> = getBookOwners.parameters.path.toJSONSchema().properties!!; 
const ISBNRestrictions = JsonSchemaToInputRestrictions(PathParameters["ISBN"]);

const queryParameters = getBookOwners.parameters.query.toJSONSchema().properties!!; // page paramater
const pageRestrictions = JsonSchemaToInputRestrictions(queryParameters["page"]);


/**
 * 
 * @returns React gui object
 */
export default function OwnersOfBook() {
    const [state, dispatch] = useReducer(reduce, { tag: 'editing', inputs: { ISBN: '', page: "0"}, data: undefined, validationErrors: {ISBN: undefined,page: undefined} });

    async function handleSubmit(ev: React.SubmitEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }

        console.log("BBBBB ", ISBNRestrictions);

        const isbn = state.inputs.ISBN;
        const page = Number(state.inputs.page); //

        let isbnValidation = stringValidate(isbn, ISBNRestrictions);

        let pageValidation = numberValidate(page, pageRestrictions);

        if(isbnValidation === undefined && !IsbnCheckSum(isbn)) {
            isbnValidation = `ISBN value does not respect checksum`;
        }

        if(isbnValidation !== undefined || pageValidation !== undefined){
            console.log("Hello val error")
            return dispatch({type: "validationErrors", errors: {ISBN: isbnValidation, page: pageValidation}})
        }


        //console.log("Changing to submitting");
        dispatch({ type: 'submit' }); //only happens later? uncomment logs to check

        //console.log("Should be on state submitting; On state ", state.tag);

        try {
            const result = await zodBookOwners(isbn, page);

            if (result.status === 200) {
                // Transition to the done state
                dispatch({ type: 'success', data: result.data });
            }
            else {
                console.error(`Unexpected status code ${result.status} from zodBookOwners`);
                dispatch({ type: 'error', message: `Unexpected status code ${result.status}` });
            }

            
        } catch (error) {
            console.error('Error during book owner search:');
            if (error instanceof ZodError) {
                console.error('Zod validation error:', error);
                dispatch({ type: 'error', message: 'Validation error' });
            } 
            /*
            else if (error instanceof TypedStatusError && error.status === 422) {
                const errorData = await error.response.json() as Schemas.ValidationProblemDetail;
                console.error('Validation error from API:', errorData);    
            }
            */
            else {
                console.error('Unexpected error:', error);
                dispatch({ type: 'error', message: 'Operation could not be completed' });
            }
            dispatch({ type: 'error', message: 'Operation could not be completed' });
        }
    }

    //to be placed on all inputs
    // <span className="error-message">    {state.tag === "editing" && state?.validationErrors?.ISBN && {state.error}}</span>
    return (
        <div>
            <form onSubmit={handleSubmit} noValidate>
            <fieldset disabled={state.tag !== 'editing'}>
                <div>
                    <label htmlFor="ISBN">ISBN</label>
                    <input
                        id="ISBN"
                        type="text"
                        name="ISBN"
                        value={state.inputs.ISBN}
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({type: "edit", propertySet: { propertyName: "ISBN", inputValue: ev.currentTarget.value}})}
                        autoFocus required title=""
                        data-error="hhh"
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.ISBN}</span>
                    <div>
                    <label htmlFor="page">page</label>
                    <input
                        id="page"
                        type="number"
                        name="page"
                        value={state.inputs.page}
                        onChange={(ev: React.ChangeEvent<HTMLInputElement>) => dispatch({ type: 'edit', propertySet: { propertyName: "page", inputValue: ev.currentTarget.value} })}
                    />
                    <span className="error-message">    {state.tag === "editing" && state.validationErrors.page}</span>
                </div>
                </div>
                <div>
                    <button type="submit" disabled={state.tag !== 'editing'}>
                        Search owners
                    </button>
                </div>
            </fieldset>
            {state.tag === 'editing' && state.error && <p>{state.error}</p>}
        </form>

        {state.data && state.data.data.length > 0 && 
            <ul>
                {state.data.data.map((owner) => (
                    <li key={owner.id}>{owner.email}</li>
                ))}
            </ul>}
        </div>
        
    );
}


/*
function Button() {
  const { pending } = useFormStatus();
  return (
    <button disabled={pending} type="submit">
      Submit
    </button>
  );
}


function Form(action: (formData: FormData) => void | Promise<void>):React.JSX.Element {
  async function increment(n: number) {
    return n + 1;
  }
  const [count, incrementFormAction] = useActionState(increment, 0);
  return (
    <form action={action}>
      <button formAction={incrementFormAction}>Count: {count}</button>
      <Button />
    </form>
  );
}
const sad = (form: FormData) => {console.log("hello"); form.get("")}

Form(sad)
*/