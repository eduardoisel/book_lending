package backend.bookSharing.data;

import backend.bookSharing.repository.entities.PasswordValidationInfo;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.services.PasswordValidation;
import org.locationtech.jts.geom.Point;

public record ClearPasswordUser(Point location, String email, String clearPassword) {

    private static final PasswordValidation passwordValidation = new PasswordValidation();

    public User toUser() {
        String salt = passwordValidation.getSalt();
        return new User(
                this.location,
                this.email,
                new PasswordValidationInfo(
                        passwordValidation.passwordEncoding(this.clearPassword, salt), salt));
    }
}
