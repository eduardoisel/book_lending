import ReactPaginateModule from "react-paginate";
import { Link, Navigate } from "react-router";


//I do not know why this is necessary. Importing ReactPaginate directly gives weird errors. Solution came from AI
const ReactPaginate =
  (typeof ReactPaginateModule === "function"
    ? ReactPaginateModule
    : (ReactPaginateModule as { default?: React.ComponentType<any> })
        ?.default)!!;

type PageInfo = {
    onClick: (page: number) => void;
    currentPage: number;
    pageCount: number;
    /**
     * Typescript has no actual power to check it is an li element, use with care
     */
    data?: React.ReactElement<React.JSX.IntrinsicElements['li']>[];
    //hasPreviousPage: boolean;
    //hasNextPage: boolean;
}

//https://github.com/AdeleD/react-paginate/blob/master/react_components/PaginationBoxView.js#L527
export function Paging({onClick, currentPage, data, pageCount}: PageInfo): React.JSX.Element{
  return <div id="paging">
    <Link to={"Hello"}> AAAAA</Link>
    <ul children={data}></ul>
      <div id="container">
        <ReactPaginate
          pageCount={pageCount}
          forcePage={0} 
          previousLabel="< previous" 
          nextLabel="next >" 
          breakLabel="..." 
          onPageChange={( selected:{selected: number}) => onClick(selected.selected)}
          hrefBuilder={(page, pageCount, selected) => page >= 1 && page <= pageCount ? `/ownersOfBook?page=${page}` : '#'}
          
        ></ReactPaginate>
      </div>
        
  </div>

}
