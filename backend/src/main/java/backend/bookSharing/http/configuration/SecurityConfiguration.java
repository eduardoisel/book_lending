package backend.bookSharing.http.configuration;

import backend.bookSharing.http.authentication.BearerTokenAuthenticationEntryPoint;
import backend.bookSharing.http.authentication.BearerTokenAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final BearerTokenAuthenticationFilter authenticationFilter;

    private final BearerTokenAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) //removing csrf line will break the configuration, test it by using a post http method if you do not believe it, especially logins
                .authorizeHttpRequests(requests -> { // set so all gets and user login creation are permitted, others need auth
                    requests
                            .requestMatchers(HttpMethod.GET).permitAll()
                            .requestMatchers(HttpMethod.POST, "/userAuth/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/users/**").authenticated()
                            .requestMatchers(HttpMethod.POST, "/books/**").authenticated()
                            .requestMatchers(HttpMethod.DELETE).authenticated();

                })
                .exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//                .logout(logout -> logout
//                        .logoutSuccessUrl("userAuth/logout")
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.setStatus(HttpServletResponse.SC_OK);
//
//                        }));

//        http.formLogin(httpSecurityFormLoginConfigurer ->
//                httpSecurityFormLoginConfigurer.usernameParameter("email").loginPage("/userAuthentication/login")
//        );

        //Avoid filterOrderException
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
