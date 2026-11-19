const isDigitPattern = /^[0-9]$/;

function isbn10CheckSum(isbnTen: string): number{
    let sum = 0;
    for(let i = 0; i < isbnTen.length - 1; i++){
        const current : number = Number(isbnTen.charAt(i));
        sum += current * 10 - i;
    }
    return sum % 11;
}

function isbn13CheckSum(isbn13: string): number{
    let sum = 0;
    for(let i = 0; i < isbn13.length - 1; i++){
        const current : number = Number(isbn13.charAt(i));
        sum += current * (i % 2 === 0 ? 1 : 3 );
    }
    return 10 - (sum % 10);
}


/**
 * Check https://en.wikipedia.org/wiki/ISBN#ISBN-10_check_digit_calculation
 * @param isbn assumes to be valid ISBN not counting checksum
 * @returns 
 */
export function IsbnCheckSum(isbn: string): boolean{

    let calculatedCheck: number;
    if(isbn.length == 13){
        calculatedCheck = isbn13CheckSum(isbn);
        return calculatedCheck === Number(isbn.charAt(12));

    } 
    // assumes length is 10
    calculatedCheck = isbn10CheckSum(isbn)

    const check = isDigitPattern.test(isbn[9]) ? Number(isbn[9]) : 11;

    return calculatedCheck === check;
}
