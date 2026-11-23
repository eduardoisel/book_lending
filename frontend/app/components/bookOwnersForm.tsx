import React, {useState } from 'react';
import { Link} from 'react-router';
import {zodBookOwners, getBookOwners} from '../api/requests';
import type { JSONSchema } from 'zod/v4/core';
import {type ObjectToString } from '~/utils/reducerTypes';
import { JsonSchemaToInputRestrictions, stringValidate } from '~/utils/inputRestrictions';
import { useFormStatus } from 'react-dom';
import { IsbnCheckSum } from '~/utils/isbn';

type Input = {
    ISBN: string;
};

const PathParameters: Record<string, JSONSchema._JSONSchema> = getBookOwners.parameters.path.toJSONSchema().properties!!; 
const ISBNRestrictions = JsonSchemaToInputRestrictions(PathParameters["ISBN"]);

type S = { inputs: Input, validationErrors: ObjectToString<Input> };


export namespace BookOwners {

    export function isbnValidation(isbn: string): string | undefined{

        let isbnValidation = stringValidate(isbn, ISBNRestrictions);

        if(isbnValidation === undefined && !IsbnCheckSum(isbn)) {
            return `ISBN value does not respect checksum`;
        }

        return isbnValidation;
    }

    /**
     * Retrieves information for {@link zodBookOwners}. Does not include paging, element may be used outside of 
     * page url and paging is done better with buttons for each page
     * @returns React gui object. Affects url on submission.
     */
    export function OwnersOfBookForm() {

        const [s, setS] = useState<S>({inputs: {ISBN: ''}, validationErrors: {ISBN: undefined}});


        /**
         * Form action param is incapable of putting inputs in url path instead of query, unadvisable for required
         * parameters. If acceptable to send all by query, remove prevent default unless validation error and
         * add action path string
         * @param ev event info prevent default
         */
        async function handleSubmit(ev: React.SubmitEvent<HTMLFormElement>) {
            ev.preventDefault();

            let isbnValidate = isbnValidation(s.inputs.ISBN);

            if(isbnValidate !== undefined){
                const copy = {...s}; copy.validationErrors.ISBN = isbnValidate;
                return setS(copy);
            }

            window.location.href = `/ownersOfBook/${s.inputs.ISBN}`
        }

        //using on action formData should let one not save inputs, letting useState for validation error messages
        //on change would stay to erase validation errors
        return (
            <div>
                <form onSubmit={handleSubmit} noValidate>
                <fieldset>
                    <div>
                        <label htmlFor="ISBN">ISBN</label>
                        <input
                            id="ISBN"
                            type="text"
                            name="ISBN"
                            value={s.inputs.ISBN}
                            onChange={(ev: React.ChangeEvent<HTMLInputElement>) =>{ 
                                const copy = {...s}; 
                                copy.inputs.ISBN = ev.currentTarget.value; 
                                s.validationErrors.ISBN = undefined; 
                                setS(copy)
                            }}
                            autoFocus required title=""
                            data-error="hhh"
                        />
                        <span className="error-message">    {s.validationErrors.ISBN}</span>
                    </div>
                    <div>
                        <button type="submit">
                            Search owners
                        </button>
                    </div>
                </fieldset>
            </form>
                        
            </div>

        );
        //  <form action={(data: FormData) => data.}/>
    }

}

