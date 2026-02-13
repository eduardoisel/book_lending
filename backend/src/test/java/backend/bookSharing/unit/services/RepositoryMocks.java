package backend.bookSharing.unit.services;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.LendRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.RequestRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.services.book.api.BookApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MockitoBean(types = {
        RegionRepository.class, //
        UserRepository.class,
        OwnedRepository.class,
        BookRepository.class,
        RequestRepository.class,
        LendRepository.class,
        BookApi.class
})
public @interface RepositoryMocks {
}
