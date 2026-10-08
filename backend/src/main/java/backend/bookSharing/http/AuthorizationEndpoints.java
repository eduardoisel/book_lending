package backend.bookSharing.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;

/**
 * Makes the user authentication access available to share on the project
 * <p>
 * There does not seem to be a way to either automate the authentication requirements from spring
 * security for springdoc.
 * Extracting the information related to the authentication from spring security seems to be impossible without
 * reflection (currently should require using bean to get {@link DefaultSecurityFilterChain} and dig to get the
 * {@link RequestMatcherDelegatingAuthorizationManager}).
 * Since the internal structure os the library classes could be subject to change between versions, it was chosen
 * to save the information here and assume {@link HttpSecurity} public methods are more stable
 * <p>
 * This section does support the check of roles or authorities, as that is assumed to be done using
 * annotations placed in http mapper methods or classes
 */
@RequiredArgsConstructor
public class AuthorizationEndpoints {

    public record Permission(ImposePermission access, String pattern, HttpMethod httpMethod) {}

    public sealed interface ImposePermission
            permits ImposePermission.Authenticated, ImposePermission.PermitAll {

        void apply(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl);

        final class PermitAll implements ImposePermission {
            @Override
            public void apply(
                    AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl) {
                authorizedUrl.permitAll();
            }
        }

        final class Authenticated implements ImposePermission {
            @Override
            public void apply(
                    AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl) {
                authorizedUrl.authenticated();
            }
        }
    }

    /**
     * At least on current version, the array order matters, as the first element is the first to be inserted
     * in {@link RequestMatcherDelegatingAuthorizationManager}, and if the pattern matches it will not continue searching
     * for other matches. This means on conflict one should place the most specific permissions first
     */
    public static final Permission[] permissions = {
        new Permission(new ImposePermission.PermitAll(), "/**", HttpMethod.GET),
        new Permission(new ImposePermission.PermitAll(), "/userAuth/**", HttpMethod.POST),
        new Permission(new ImposePermission.Authenticated(), "/users/**", HttpMethod.POST),
        new Permission(new ImposePermission.Authenticated(), "/books/**", HttpMethod.POST),
        new Permission(new ImposePermission.Authenticated(), "/**", HttpMethod.DELETE)
    };

    /**
     * set so all gets and user login creation are permitted, others need auth
     *
     * @param registry the register of endpoints authorization
     */
    public static void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
                    registry) {

        for (Permission permission : permissions) {
            permission.access.apply(
                    registry.requestMatchers(permission.httpMethod, permission.pattern));
        }
    }
}
