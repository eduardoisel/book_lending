package backend.bookSharing.http;

import backend.bookSharing.services.book.BookService;
import backend.bookSharing.services.user.UserService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mockito.InjectMocks;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Should include all {@link Service}. This allows to Autowire them, allowing making it not necessary to create a class
 * field and use {@link InjectMocks}, but can still put them if necessary to use mockito
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MockitoBean(
        types = {
            UserService.class,
            BookService.class,
        })
public @interface ServiceMocks {}
