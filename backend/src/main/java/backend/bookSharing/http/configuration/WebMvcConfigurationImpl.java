package backend.bookSharing.http.configuration;

import backend.bookSharing.http.UserResolver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.multipart.MultipartResolver;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebMvcConfigurationImpl implements WebMvcConfigurer {

    private final UserResolver userResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userResolver);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES);
//        cacheControl.cachePublic();
//
//        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
//        webContentInterceptor.setCacheControl(cacheControl);
//
//        registry.addInterceptor(webContentInterceptor);
//    }

}
