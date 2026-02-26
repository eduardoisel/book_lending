package backend.bookSharing.http.configuration;

import backend.bookSharing.http.authentication.BearerTokenAuthenticationEntryPoint;
import backend.bookSharing.http.authentication.BearerTokenAuthenticationFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    //private BasicAuthenticationFilter;

    //private DefaultHandlerExceptionResolver a;

    //private MissingRequestHeaderException a;

    private BearerTokenAuthenticationFilter authenticationFilter;

    private BearerTokenAuthenticationEntryPoint authenticationEntryPoint;

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
                .sessionManagement(management -> {
                    management.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

//                .logout(logout -> logout
//                        .logoutSuccessUrl("userAuth/logout")
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.setStatus(HttpServletResponse.SC_OK);
//
//                        }));
//
//        http.formLogin(httpSecurityFormLoginConfigurer ->
//                httpSecurityFormLoginConfigurer.usernameParameter("email").loginPage("/userAuth/login")
//        );

        http.addFilterBefore(authenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    @Bean
//    public BearerTokenAuthenticationFilter authenticationFilter(){
//        return new BearerTokenAuthenticationFilter(authenticationManager);
//    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BearerTokenAuthenticationEntryPoint();
    }

    /*
    private static class EmptySecurityFilterChain implements SecurityFilterChain {

        @Override
        public boolean matches(HttpServletRequest request) {
            return false;
        }

        @Override
        public List<Filter> getFilters() {
            return List.of();
        }
    }



//    https://docs.spring.io/spring-boot/reference/web/spring-security.html
//    Not placing this will require authentication by html form
//    Substitutes the default by one that does not have any security.
//    Todo review the link when working fully on http side
//
    @Bean
    public SecurityFilterChain securityFilterChainBean() {
        return new EmptySecurityFilterChain();

    }
    */


}
