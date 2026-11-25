package backend.bookSharing.utils;

public class IsbnUtils {

    /**
     * Ensures valid ISBN is of type 13. Created due to being useful to have only one stored in database
     * @param isbn ISBN 10 or 13
     * @return ISBN 13
     */
    public static String getIsbn13(String isbn) {
        if (isbn.length() != 13) {
            return isbn10To13(isbn);
        }
        return isbn;
    }

    /**
     * @see <a href="https://en.wikipedia.org/wiki/ISBN#ISBN-10_to_ISBN-13_conversion">conversion</a>
     * @param isbn ISBN 10, assumes it is valid
     * @return ISBN 13, with the prefix and calculated checkSum
     */
    public static String isbn10To13(String isbn) {
        String isbnWithoutCheckSum = "978" + isbn.substring(0, 9);

        return isbnWithoutCheckSum + isbn13ChecksumCalculation(isbnWithoutCheckSum);
    }

    /**
     * @param isbn13WithoutChecksum should have a length of 12
     * @return remainder, a number from 0 to 9.
     */
    public static int isbn13ChecksumCalculation(String isbn13WithoutChecksum) {
        // isbn 13 checksum
        int sum = 0;
        for (int i = 0; i < isbn13WithoutChecksum.length(); i++) {
            sum += (isbn13WithoutChecksum.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        return 10 - (sum % 10);
    }

    public static boolean isIsbn13ConvertibleTo10(String isbn) {
        return isbn.startsWith("978");
    }

    /**
     * @param isbn ISBN 13
     * @return ISBN 10
     * @throws IllegalArgumentException if it is not convertible to ISBN 10. see <a href="https://en.wikipedia.org/wiki/ISBN#ISBN-10_to_ISBN-13_conversion">conversion</a>
     * @see IsbnUtils#isIsbn13ConvertibleTo10(String)
     */
    public static String isbn13To10(String isbn) throws IllegalArgumentException {
        if (!isIsbn13ConvertibleTo10(isbn)) {
            throw new IllegalArgumentException();
        }
        // remove first three and checksum
        String isbnWithoutCheckSum = isbn.substring(3, 12);

        int checkSum = isbn10ChecksumCalculation(isbnWithoutCheckSum);

        String checkSumString = checkSum == 11 ? "X" : String.valueOf(checkSum);

        return isbnWithoutCheckSum + checkSumString;
    }

    /**
     * @param isbn10WithoutChecksum should have a length of 9
     * @return remainder, a number from 0 to 10. When it is of value 10 it should be transformed to an X
     */
    public static int isbn10ChecksumCalculation(String isbn10WithoutChecksum) {
        int sum = 0;
        for (int i = 0; i < isbn10WithoutChecksum.length(); i++) {
            sum += (isbn10WithoutChecksum.charAt(i) - '0') * (10 - i);
        }

        return 11 - (sum % 11);
    }
}
