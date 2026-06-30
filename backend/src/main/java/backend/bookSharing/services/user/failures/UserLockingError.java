package backend.bookSharing.services.user.failures;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public sealed abstract class UserLockingError extends Exception permits UserLockingError.UserDoesNotExist{

    private UserLockingError(){}

    public static final class UserDoesNotExist extends UserLockingError {

        public final UsernameNotFoundException userNotFound;

        public UserDoesNotExist(UsernameNotFoundException usernameNotFoundException){
            this.userNotFound = usernameNotFoundException;
        }
    };

}
