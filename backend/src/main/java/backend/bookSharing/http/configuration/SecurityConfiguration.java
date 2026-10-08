package backend.bookSharing.http.configuration;

import backend.bookSharing.http.AuthorizationEndpoints;
import backend.bookSharing.http.authentication.BearerTokenAuthenticationEntryPoint;
import backend.bookSharing.http.authentication.BearerTokenAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final BearerTokenAuthenticationFilter authenticationFilter;

    private final BearerTokenAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // removing csrf line will break the configuration somehow
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(AuthorizationEndpoints::configure)
                //                .formLogin(
                //                        (login) ->
                //                                login.permitAll()
                //                                        .loginPage("/userAuth/login")
                //                                        .loginProcessingUrl("/userAuth/login")
                //                                        .usernameParameter("email"))
                .exceptionHandling(
                        handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(
                        management ->
                                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //                .logout(logout -> logout
        //                        .logoutSuccessUrl("userAuth/logout")
        //                        .logoutSuccessHandler((request, response, authentication) -> {
        //                            response.setStatus(HttpServletResponse.SC_OK);
        //
        //                        }));

        // Avoid filterOrderException
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
