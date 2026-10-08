package backend.bookSharing;

import backend.bookSharing.services.user.services.TokenValidation;
import java.time.Duration;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
@EnableJpaRepositories()
public class Main {

    @Bean
    public TokenValidation.TokenValidTime tokenValidationBean() {
        return new TokenValidation.TokenValidTime(Duration.ofHours(10), Duration.ofMinutes(30));
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

    /*
    From https://www.baeldung.com/etags-for-rest-with-spring
    TODO find a better place to put the bean
     */
    @Bean
    public FilterRegistrationBean<?> shallowEtagHeaderFilter() {

        FilterRegistrationBean<?> filterRegistrationBean =
                new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/*"); // limited to http get by filter
        return filterRegistrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
