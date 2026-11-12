package backend.bookSharing.http.springdoc;

import backend.bookSharing.Main;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.ServletContext;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * Class exists due to {@link ConditionalOnBean} not guaranteeing any order of bean creation, leading to
 * necessity of {@link AutoConfigureAfter}
 *
 * <p>
 * This customizer add shallow filter as param and not modified as response for get requests
 *
 * @implNote assumes it has {@link FilterRegistration} annotation with path encompassing all. Alternative may be
 * possible with ServletContext instance,but base class of filters {@link ServletContext#getFilterRegistrations() getFilterRegistrations}
 * base class contains too little info for documentation.
 */
@Component
@AutoConfigureAfter(Main.class) // contains shallow etag filter
@ConditionalOnClass(ShallowEtagHeaderFilter.class)
public class ShallowEtagFilterCustomizer implements OpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        openApi.getPaths()
                .forEach(
                        (_, pathItem) -> {
                            if (pathItem.getGet() != null) {
                                Operation getOperation = pathItem.getGet();

                                ApiResponse notModifiedResponse =
                                        new ApiResponse().description("Content not modified");

                                getOperation
                                        .getResponses()
                                        .addApiResponse(
                                                String.valueOf(HttpStatus.NOT_MODIFIED.value()),
                                                notModifiedResponse);

                                getOperation.addParametersItem(
                                        new Parameter()
                                                .in("header")
                                                .name(HttpHeaders.ETAG)
                                                .schema(new StringSchema())
                                                .required(false)
                                                .description("Shallow tag"));
                            }
                        });
    }
}
