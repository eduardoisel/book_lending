package backend.bookSharing.http.authentication;

import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

//        private final UserDetailsService userDetailsService;
//        private final TokenService tokenService;

    private final UserService service;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");

        /*
          Necessary. At least on current configurations it will always check, even if the endpoint of the api
          does not require authentication
         */
        if (requestTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!requestTokenHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token must start with 'Bearer '");
            return;
        }

        String token = requestTokenHeader.substring(7);

        User user = service.checkAuthentication(token);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not in use on the database");
            return;
        }

        LinkedList<GrantedAuthority> defaultRole = new LinkedList<>();

        defaultRole.add((GrantedAuthority) () -> "ROLE_AUTHENTICATED");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, token, defaultRole);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


}
