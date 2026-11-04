package backend.bookSharing.http;

import backend.bookSharing.http.authentication.BearerTokenAuthenticationEntryPoint;
import backend.bookSharing.http.authentication.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.request.AbstractMockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.json.JsonMapper;

/*

*/
/**
 * {@link Import} necessary to load Security configuration, allows for both testing authentication for each path and
 * disabling the CSRF (although mockMvc {@link AbstractMockHttpServletRequestBuilder#with(RequestPostProcessor)} can
 * generate valid CSRF for workaround)
 */
@ServiceMocks
@Import({SecurityConfiguration.class, BearerTokenAuthenticationEntryPoint.class})
public class ControllerMockTestBase {

    /**
     * parser for testing
     */
    @Autowired protected JsonMapper jsonMapper;
}
