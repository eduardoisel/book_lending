package backend.bookSharing.utils;

public class IsbnUtils {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/ISBN#ISBN-10_to_ISBN-13_conversion">conversion</a>
     * @param isbn ISBN 10, assumes it is valid
     * @return ISBN 13, with the prefix and calculated checkSum
     */
    public static String isbn10To13(String isbn) {
        String isbnWithoutCheckSum = "978" + isbn.substring(0, 9);

        // isbn 13 checksum
        int sum = 0;
        for (int i = 0; i < isbnWithoutCheckSum.length(); i++) {
            sum += (isbnWithoutCheckSum.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        return isbnWithoutCheckSum + (10 - (sum % 10));
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

        // isbn 10 checksum
        int sum = 0;
        for (int i = 0; i < isbnWithoutCheckSum.length(); i++) {
            sum += (isbnWithoutCheckSum.charAt(i) - '0') * (10 - i);
        }

        int checkSum = 11 - (sum % 11);

        String checkSumString = checkSum == 11 ? "X" : String.valueOf(checkSum);

        return isbnWithoutCheckSum + checkSumString;
    }
}
