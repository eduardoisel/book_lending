import type { JSONSchema } from "zod/v4/core";

/**
 * 
 * @param max max number value
 * @param exclusiveMaximum the maximum value found in max is not valid. Only used if max is not undefined
 * @param min min number value
 * @param exclusiveMinimum the minimum value found in min is not valid. Only used if min is not undefined
 * @param pattern string regex restriction
 * @param step for numbers, smallest difference a valid value can have. Called step so it may be used directly on input field step. Indicates either if is integer or not
 */
type NumberRestriction = {
    type: "number";
    max?: number | undefined;
    exclusiveMaximum: boolean;
    min?: number | undefined;
    exclusiveMinimum: boolean;
    step: "1" | "any"; 
}

/**
 * @param maxLength max text length
 * @param minLength min text length
 * @param pattern string regex restriction
 * @param enum list of accepted values. accepts values other than string, TODO investigate
 */
type StringRestriction = {
    type: "text"; //same as html input
    maxLength?: number | undefined;
    minLength?: number | undefined;
    pattern?: string | undefined;
    enum?: Array<string | number | boolean | null> // to restrict types
}

/**
 * Has no restrictions
 */
type BooleanRestriction = {
    type: "boolean"
}

/**
 * Seeing https://swagger.io/docs/specification/v3_0/data-models/data-types/#arrays for usable restrictions
 * @param minItems minimum number of items it can have
 * @param maxItems max number of items it can have
 * @param uniqueItems all items in the array must be unique
 * @param items the type each item can have
 */
type ArrayRestriction = {
    type: "array";
    minItems?: number | undefined;
    maxItems?: number | undefined;
    uniqueItems: boolean;
    items: InputRestrictions
}

/**
 * @param properties array of properties containing the property name and its restriction. Maybe change to be {@link Record} type
 */
type ObjectRestriction = {
    type: "object";
    properties: {
        key: string,
        schema: InputRestrictions
    }[];
}

export type InputRestrictions = NumberRestriction | StringRestriction | BooleanRestriction | ArrayRestriction | ObjectRestriction

/**
 * SchemaType array probably in use for OpenApi OneOf, AllOf or AnyOf. Not being used here.
 * Null is probably for when field is missing on OpenApi. Just do not miss it.
 * 
 */
type AcceptedTypes = Exclude<JSONSchema.SchemaType, JSONSchema.SchemaType[] | "null">


export function JsonSchemaToInputRestrictions(schemaOrBool: JSONSchema._JSONSchema): InputRestrictions{
    /*
    Apparently boolean serves to indicated if the object is always valid or invalid? Ignored here
    */
    const schema = schemaOrBool as JSONSchema.JSONSchema; 

    // "object" | "array" | "string" | "number" | "boolean" | "null" | "integer"
    switch (schema.type as AcceptedTypes){
        case "integer":
        case "number":
            let exclusiveMaximum: boolean = false;// stays if schema's value undefined
            let max: number | undefined;
            if(typeof schema.exclusiveMaximum === "number"){
                exclusiveMaximum = true;
                max = schema.exclusiveMaximum;
            } else if(typeof schema.exclusiveMaximum === "boolean"){
                exclusiveMaximum = schema.exclusiveMaximum;
                max = schema.maximum;
            }

            let exclusiveMinimum: boolean = false;// stays if schema's value undefined
            let min: number | undefined;
            if(typeof schema.exclusiveMinimum === "number"){
                exclusiveMinimum = true;
                min = schema.exclusiveMinimum;
            } else if(typeof schema.exclusiveMinimum === "boolean"){
                exclusiveMinimum = schema.exclusiveMinimum;
                min = schema.minimum;
            }
            
            return {
                type: "number",
                exclusiveMaximum: exclusiveMaximum,
                max: max,
                min: min,
                exclusiveMinimum: exclusiveMinimum,
                step: schema.type === "integer" ? "1" : "any" 
            }
        case "array":
            return {
                type: "array",
                minItems: schema.minItems,
                maxItems: schema.maxItems,
                uniqueItems: schema.uniqueItems === undefined ? false : schema.uniqueItems,
                items: JsonSchemaToInputRestrictions(schema.items as JSONSchema._JSONSchema)
            }
        case "boolean":
            return {type: "boolean"}
        case "string":
            return {
                type: "text",
                maxLength: schema.maxLength,
                minLength: schema.minLength,
                pattern: schema.pattern,
                enum: schema.enum
            };
        case "object":
            let properties: Record<string, JSONSchema._JSONSchema> =  schema.properties!!;properties.array;
            const arr = Object.entries(properties).map(([propName, schema]) => { return {key: propName, schema: JsonSchemaToInputRestrictions(schema)} });
            return {
                type: "object",
                properties: arr
            };
    }

}

export function numberValidate(input: number, restriction: InputRestrictions): string | undefined{

    if(restriction.type !== "number"){
        throw "wrong validation"
    }

    if(restriction.min && restriction.exclusiveMinimum && input <= restriction.min){
        return `Field must be bigger than ${restriction.min}`;
    }

    if(restriction.min && !restriction.exclusiveMinimum && input < restriction.min){
        return `Field must be at least ${restriction.min}`;
    }

    if(restriction.max && restriction.exclusiveMaximum && input > restriction.max){
        return `Field must be smaller than ${restriction.max}`;
    }

    if(restriction.max && !restriction.exclusiveMaximum && input <= restriction.max){
        return `Field must be at most than ${restriction.max}`;
    }

    if(restriction.step === "1" && !Number.isInteger(input)){
        return `Field is supposed to be a whole number`
    }

    return undefined;
    
}

/**
 * todo change so pattern message is more specific
 * @param input 
 * @param restriction 
 * @returns 
 */
export function stringValidate(input: string, restriction: InputRestrictions): string | undefined{

    if(restriction.type !== "text"){
        throw "wrong validation"
    }

    if(restriction.enum && !restriction.enum.includes(input)){
        return `Field must be one of the following values: ${restriction.enum}`;
    }

    if(restriction.maxLength && input.length > restriction.maxLength){
        return `Field must be at most ${restriction.maxLength} characters`;
    }

    if(restriction.minLength && restriction.minLength && input.length < restriction.minLength){
        return `Field must be at least ${restriction.maxLength} characters`;
    }

    if(restriction.pattern && !new RegExp(restriction.pattern).test(input)){
        return `Field does not follow required pattern`; 
    }

    return undefined;
    
}

// Source - https://stackoverflow.com/q/67666857
// Posted by AGZ, modified by community. See post 'Timeline' for change history
// Retrieved 2026-11-16, License - CC BY-SA 4.0

const data: Record<string, any> = { "Option 1": {"id": "123"}, "Option 2": {"id": "987"}}
const data_two: Record<string, any> = { "Option 1": {"id": "567"}, "Option 2": {"id": "376"}}
