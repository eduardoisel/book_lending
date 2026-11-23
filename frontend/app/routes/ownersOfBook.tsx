import { useEffect, useState } from 'react';
import { Link, Navigate, useLocation, useParams, useSearchParams, type URLSearchParamsInit } from 'react-router';
import {zodBookOwners, getBookOwners} from '../api/requests';
import { ZodError } from 'zod';
import { useFormStatus } from 'react-dom';
import { Paging } from '~/components/Paging';
import { BookOwners } from '~/components/bookOwnersForm';
import type { Schemas } from '~/api/typed-openapi/schema.types';

/*
type Input = {
    ISBN: string;
    page: string;
};
*/


type DataOrError = {type: 'error'; error: string} | {type: 'success'; data: Schemas.ListedDataUser};

/**
 * @returns React gui object, provides paging for owners of specified book. 
 * If required isbn is invalid (always possible user can change url manually) message indicating it shows up along with {@link BookOwners.OwnersOfBookForm}.
 * If all is well, form still shows up along with the pagination.
 */
export default function OwnersOfBook() {
    const params = useParams();// console.log("Params: ", params);
    const isbn =  params["ISBN"]!!

    const [searchParams, setSearchParams] = useSearchParams();
    // may need offset by 1, backend starts at 0 and frontend at 1?
    const page = searchParams.get("page") !== undefined ? Number(searchParams.get("page")) : 0;

    const isbnValidate = BookOwners.isbnValidation(isbn);
    if(isbnValidate !== undefined){
        return <div>
            <p>Crucial information invalid; {isbnValidate}</p>
            <BookOwners.OwnersOfBookForm/>
        </div>
    }

    const [resp, setResp] = useState<DataOrError | null>(null);

    //necessary for api call
    //https://medium.com/@vinaykumarbr07/debouncing-api-calls-in-react-with-useeffect-and-abortcontroller-d60a16716c7f
    useEffect(() => {
        const controller = new AbortController();

        async function fetchData() {
            try {
                const result = await zodBookOwners(isbn, page, controller.signal);
                console.log(`Request; isbn: ${isbn}, page ${page} `);

                if (result.status === 200) {
                    // Transition to the done state
                    console.log("Success, ", result.data);

                    setResp({type: 'success' ,data: result.data});
                } else{
                    setResp({type: 'error' , error: `Fetch failed with status ${result.status}`})
                }

            } catch (error) {
                console.error('Error during book owner search:');
                if (error instanceof ZodError) {
                    console.error('Zod validation error; SHOULD NOT HAPPEN. Value: ', error);
                    setResp(  {type: 'error' , error: "Zod error. Bad"} );
                } 
        /*
        else if (error instanceof TypedStatusError && error.status === 422) {
            const errorData = await error.response.json() as Schemas.ValidationProblemDetail;
            console.error('Validation error from API:', errorData);    
        }
        */
                else if(error instanceof TypeError){ //fetch failure
                    console.log("Fetch failed; API unaccessible: ", error);
                    setResp(  {type: 'error' , error: "Api unreachable"} );
                    //setError("Api unreachable")
                } else {
                    console.error('Unexpected error:', error);
                    setResp(  {type: 'error' , error: "Unexpected error" } );
                    //setError("Unexpected error");
                }
            }
        }

        fetchData();

        return () => controller.abort(); // cancel on unmount or re-run
    }, [searchParams]); //also having params causes 2 requests?




    if(resp === null){
        return <div>Loading</div>
    } else if(resp.type === 'error'){
        return <div> <BookOwners.OwnersOfBookForm/> <p>{resp.error}</p></div>
    }
    else{
        return (
            <div>
                <BookOwners.OwnersOfBookForm/>

                <Paging 
                    onClick={ (page) => {setSearchParams(prev => {prev.set("page", `${page}`); return prev;}); console.log("Clicked")} }
                    currentPage={page}
                    pageCount={resp.data.totalPages}
                    data={resp.data && resp.data.data.map((owner) => <li key={owner.id}>{owner.email}</li>)}/>
            
            </div>);
        
    }
}
