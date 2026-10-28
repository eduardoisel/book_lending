package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.http.data.UserLogin;
import backend.bookSharing.http.returns.LoginToken;
import backend.bookSharing.services.user.UserService;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userAuth")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final UserService service;

    /**
     * @param body information necessary to create user
     * @return login token
     */
    @PostMapping("createUser")
    @ResponseStatus(code = HttpStatus.CREATED)
    public LoginToken createUser(@RequestBody @Validated @Valid UserCreation body)
            throws UserCreationError {

        return new LoginToken(
                service.createUser(body.email(), body.password(), body.x(), body.y()));
    }

    /**
     *
     * @param body information necessary to login
     * @return login token
     * @throws UserAuthenticationError
     */
    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LoginToken login(@Validated @Valid @RequestBody UserLogin body)
            throws UserAuthenticationError {
        return new LoginToken(service.login(body.email(), body.password()));
    }

    @DeleteMapping("logout")
    @ResponseStatus(value = HttpStatus.OK)
    public void logout() {
        try {

            String token =
                    (String)
                            Objects.requireNonNull(
                                            SecurityContextHolder.getContext().getAuthentication())
                                    .getCredentials();

            assert token != null;
            service.logout(token);

        } catch (LogoutError error) {
            // should not happen as token was retrieved on authentication
            throw new RuntimeException(error);
        }
    }
}
