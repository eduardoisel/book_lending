package backend.bookSharing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import backend.bookSharing.utils.IsbnUtils;
import org.junit.jupiter.api.Test;

/**
 * Check <a href="https://www.loc.gov/programs/preassigned-control-number/isbn-converter/">this site</a> for valid
 * values
 */
public class IsbnUtilsTest {

    @Test
    public void testIsbn10To13() {
        assertEquals("9780195052435", IsbnUtils.isbn10To13("0195052439"));
        assertEquals("9788702347036", IsbnUtils.isbn10To13("8702347032"));
        assertEquals("9780316312486", IsbnUtils.isbn10To13("0316312487"));
        assertEquals(
                "9780306406157",
                IsbnUtils.isbn10To13("0306406152")); // Wikipedia isbn 10 checksum ex.
    }

    @Test
    public void testIsbn13To10() {
        assertEquals("0195052439", IsbnUtils.isbn13To10("9780195052435"));
        assertEquals("8702347032", IsbnUtils.isbn13To10("9788702347036"));
        assertEquals("0316312487", IsbnUtils.isbn13To10("9780316312486"));
        assertEquals("0306406152", IsbnUtils.isbn13To10("9780306406157"));

        assertThrowsExactly(
                IllegalArgumentException.class, () -> IsbnUtils.isbn13To10("9798604654095"));
    }
}
