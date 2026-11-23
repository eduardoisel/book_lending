/**
 * Transforms every end property into being of type string (can be undefined)
 */
export type ObjectToString<O extends object> = {
    [K in keyof O]: O[K] extends object?
        ObjectToString<O[K]>  :
        string | undefined
    };

/**
 * 
 */
export type PropertyPathAndType<T extends Record<string, string>> = {
        [K in keyof T]: T[K] extends object
            ? PropertyPathAndType<T[K]>
            : { propertyName: K, inputValue: T[K]};
      }[keyof T];

/*
 * Input in state and action only accepts type {@link Record<string, string>}. This is due to type inference. On reduce, even if one
 * uses the same input on state and action, the information on action edit propertySet will not be recognized as of the required type,
 * so it is circumvented by making them all of type string. 
 * String is is what is read from the input fields in html anyways, so the conversion is just done later when checking the value. 
 */

/**
 * Only includes the ones where you stay in the same screen, no done state
 * property error in editing is currnetly used to display the message coming from the API response, a bit crude and to see if is changed.
 */
export type GeneralState<Input extends Record<string, string>, Data extends object> =  
    { tag: 'editing'; error?: string; inputs: Input, data?: Data | undefined, validationErrors: ObjectToString<Input> }
    | { tag: 'submitting'; inputs: Input, data?: Data | undefined };

//type pP = GeneralState<typeof b, never>

//type GeneralState<Input extends Record<string, string>>;

/**
 * action of type error is for the property error in GeneralState, validationErrors is for validationErrors
 */
export type GeneralAction<Input extends Record<string, string>, Data extends object> =
    { type: 'edit'; propertySet: PropertyPathAndType<Input> }
    | { type: 'submit' }
    | { type: 'error'; message: string }
    | { type: 'validationErrors'; errors: ObjectToString<Input>}
    | { type: 'success', data: Data };


// Attempted to save the  most basic reduce function, unfortunately Record<string, string> could be empty, meaning  edit action propertySet could be empty
// I have not found a workaround to this issue
/*
function logUnexpectedAction<Input extends Record<string, string>, Data extends object>(state: GeneralState<Input, Data>, action: GeneralAction<Input, Data>) {
    console.log(`Unexpected action '${action.type}' on state '${state.tag}'`);
}

export function reduce<Input extends Record<string, string>, Data extends Record<string, string>>(state: GeneralState<Input, Data>, action: GeneralAction<Input, Data>): GeneralState<Input, Data>{// asserts state is NonEmptyRecord<string, string> {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                
                const newState: GeneralState<Input, Data> = {tag: 'editing', error: undefined, inputs: state.inputs, data: state.data, validationErrors: state.validationErrors}

                //not enough to recognizr has having it
                if(Object.getOwnPropertyNames(action.propertySet).includes("propertyName") && Object.getOwnPropertyNames(action.propertySet).includes("inputValue")){}                

                
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
                return { tag: 'editing', inputs: { ...state.inputs }, data: action.data};//, validationErrors: undefined };
            } else if (action.type === 'error') {
                return { tag: 'editing', error: action.message, inputs: { ...state.inputs } };
            } else {
                logUnexpectedAction(state, action);
                return state;
            }
    }
}
*/


//Was unable to merge the state and action types types by indicating even though they are similar. Todo see if possible later
/**
 * Includes extra state to indicate the ending.
 */
export type QuickGeneralState<Input extends Record<string, string>> = //, InputWithoutSensitive extends Record<string, string> = Input> =  
    { tag: 'editing'; error?: string; inputs: Input, validationErrors: ObjectToString<Input> }
    | { tag: 'submitting'; inputs: Input }
    | { tag: 'done' };

/**
 * Essencially the same as GeneralAction, but does not need Data.
 */
export type QuickGeneralAction<Input extends Record<string, string>> =
    | { type: 'edit'; propertySet: PropertyPathAndType<Input> }
    | { type: 'submit' }
    | { type: 'error'; message: string }
    | { type: 'validationErrors'; errors: ObjectToString<Input> }
    | { type: 'success' };