package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.UserCreation;
import backend.bookSharing.services.user.UserService;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "410", description = "Password not up to requirement"),
            @ApiResponse(responseCode = "400", description = "Repeat email")
    })
    @PostMapping("createUser")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody UserCreation body) {
        try {
            Integer result = service.createUser(body.email, body.password);

            return ResponseEntity.status(201).body(String.format("Id of user: %s \n", result));

        } catch (UserCreationError error) {

            return switch (error) {
                case UserCreationError.WeakPassword weakPassword ->
                        ResponseEntity.status(400).body("Password is weak. Todo add ways to inform requirements");
                case UserCreationError.EmailInUse emailInUse ->
                        ResponseEntity.status(400).body("Email is already in use for this service.");
            };

        }

    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> login(@RequestBody UserCreation body) {
        try {
            String result = service.login(body.email, body.password);

            return ResponseEntity.status(201).body(result);

        } catch (UserAuthenticationError error) {

            return switch (error) {
                case UserAuthenticationError.UserOrPasswordAreInvalid userOrPasswordAreInvalid ->
                        ResponseEntity.status(400).body("User not recognized");
            };

        }

    }

    @DeleteMapping("logout")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> logout() {
        try {
            service.logout((String) SecurityContextHolder.getContext().getAuthentication().getCredentials());

            return ResponseEntity.status(200).body("Deleted token");

        } catch (LogoutError error) {

            return switch (error) {
                case LogoutError.TokenInvalidForAuthentication tokenInvalidForAuthentication ->
                        ResponseEntity.status(400).body("User not recognized");
            };

        }

    }

}
