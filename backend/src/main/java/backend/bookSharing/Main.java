package backend.bookSharing;

import backend.bookSharing.services.user.services.TokenValidation;
import java.time.Duration;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
@EnableAutoConfiguration
public class Main {

    @Bean
    public TokenValidation.TokenValidTime tokenValidationBean() {
        return new TokenValidation.TokenValidTime(Duration.ofHours(10), Duration.ofMinutes(30));
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Bean
    AntPathMatcher antPathMatcher() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        pathMatcher.setCaseSensitive(false);
        return pathMatcher;
    }

    /*
    From https://www.baeldung.com/etags-for-rest-with-spring
    TODO find a better place to put the bean
     */
    @Bean
    @FilterRegistration(urlPatterns = "/*")
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
