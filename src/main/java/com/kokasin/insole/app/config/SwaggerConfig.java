package com.kokasin.insole.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API 문서 자동화
 * 접속주소 : {domain}/swagger-ui.html
 *
 */

@Profile({"local","dev", "prod"})
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

	@Value("${project.name}")
	private String PROJECT_NAME;

    private ApiInfo apiInfo() {

    	return new ApiInfoBuilder()
    			.title(PROJECT_NAME)
    			.description("API swagger")
    			.build();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public Docket api() {
        List global = new ArrayList();
        global.add(
                new ParameterBuilder()
                        .name("Content-Type")
                        .defaultValue("application/json")
                        .parameterType("header")
                        .required(false)
                        .hidden(true)
                        .modelRef(new ModelRef("string")).build()
        );

        global.add(
                new ParameterBuilder()
                        .name("Accept")
                        .defaultValue("application/json")
                        .parameterType("header")
                        .required(false)
                        .hidden(true)
                        .modelRef(new ModelRef("string")).build()
        );
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .globalOperationParameters(global)
                .select()
                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
                .paths(PathSelectors.ant("/**/api/**"))
//                .paths(PathSelectors.regex("/error.*"))
                .build()
                .useDefaultResponseMessages(false)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "X-Auth-Token", "header");
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

}
