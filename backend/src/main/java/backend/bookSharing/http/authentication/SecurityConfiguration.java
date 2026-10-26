package backend.bookSharing.http.authentication;

import backend.bookSharing.http.AuthorizationEndpoints;
import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final BearerTokenAuthenticationFilter authenticationFilter;

    private final BearerTokenAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // all frontend origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                List.of("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true); // Crucial for passing cookies across origins

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
