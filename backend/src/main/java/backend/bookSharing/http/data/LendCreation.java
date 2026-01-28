package backend.bookSharing.http.data;

/**
 * @param isbn book identifier, either 10 or 13 numbers
 * @param receiverEmail unique identifier of book's requester
 * */
public record LendCreation(String isbn, String receiverEmail) {
}
