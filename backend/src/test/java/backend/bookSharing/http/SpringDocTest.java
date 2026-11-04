package backend.bookSharing.http;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.utils.Constants;
import org.springdoc.ui.AbstractSwaggerWelcome;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springdoc.webmvc.ui.SwaggerWebMvcConfigurer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest()
@Import({
    backend.bookSharing.http.springdoc.SpringDocConfiguration.class,
    // above for further testing, checking finer details of project configuration
    SpringDocConfiguration.class,
    SpringDocWebMvcConfiguration.class,
    SpringDocConfigProperties.class,
    // for json/yaml above, below swagger
    SwaggerConfig.class,
    SwaggerWebMvcConfigurer.class,
    SwaggerUiConfigProperties.class,
    // SwaggerUiConfigParameters.class, //
    // WebMvcAutoConfiguration.class, //
    // SwaggerIndexPageTransformer.class,
    SwaggerUiOAuthProperties.class,
    // SwaggerWelcomeWebMvc.class
})
public class SpringDocTest extends ControllerMockTestBase {

    @Autowired private MockMvc mockMvc;

    // this should require redirect? (changes url on request)

    /**
     * @implNote  Should be called on {@link SwaggerWelcomeWebMvc#redirectToUi(HttpServletRequest)},
     * created by bean {@link SwaggerConfig#swaggerWelcome(SwaggerUiConfigProperties, SpringDocConfigProperties, ObjectProvider)}.
     * Does not seem to register
     * @implNote Possibly just to remove, does not seem necessary
     */
    @Test
    void isAccessibleWithoutAuthenticationSwaggerRedirect() throws Exception {
        mockMvc.perform(get("/swagger-ui.html")).andExpect(status().isOk());
    }

    /**
     * @implNote {@link SwaggerWelcomeCommon#redirectToUi(HttpServletRequest)} calls {@link Constants#SWAGGER_UI_URL}
     * stored in {@link AbstractSwaggerWelcome#getSwaggerUiUrl()}. This is the conclusion that one would take by seeing
     * where the {@link Constants#SWAGGER_UI_URL} is used, but the method above is overridden by {@link SwaggerWelcomeWebMvc#redirectToUi(HttpServletRequest)},
     * the one used in test {@link SpringDocTest#isAccessibleWithoutAuthenticationSwaggerRedirect()}. This method ALSO
     * isn't called. {@link SwaggerWelcomeWebMvc#buildUrlWithContextPath(SwaggerUiConfigParameters, String)} is called, seeing from there
     */
    @Test
    void isAccessibleWithoutAuthenticationSwaggerDirect() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

    @Test
    void isYamlAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs.yaml")).andExpect(status().isOk());
    }

    @Test
    void isJsonAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
