package com.kokasin.insole.app.config;


import com.kokasin.insole.app.convert.StringToDoubleConverter;
import com.kokasin.insole.app.convert.StringToIntegerConverter;
import com.kokasin.insole.app.convert.StringToLongConverter;
import com.kokasin.insole.app.view.JsonViewResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Value("${project.name}")
	private String PROJECT_NAME;


	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//		registry.addResourceHandler("/imgs/**").addResourceLocations("classpath:/static/imgs/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/").setCachePeriod(0);

		/* swagger */
		registry.addResourceHandler("/" + PROJECT_NAME + "/**")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.TEXT_HTML);
	}

	@Override
	@Profile("local")
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addRedirectViewController("/" + PROJECT_NAME + "/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true);
		registry.addRedirectViewController("/" + PROJECT_NAME + "/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
		registry.addRedirectViewController("/" + PROJECT_NAME + "/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
		registry.addRedirectViewController("/" + PROJECT_NAME + "/swagger-resources", "/swagger-resources");
	}


//	@Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setOrder(1);
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".jsp");
//        return resolver;
//    }

    @Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
	    ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
	    resolver.setOrder(2);
	    resolver.setContentNegotiationManager(manager);

	    List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
	    resolvers.add(viewResolver());
	    resolvers.add(fileViewResolver());
	    resolvers.add(jsonViewResolver());
////	    resolvers.add(xmlViewResolver());
//	    resolvers.add(excelViewResolver());
	    resolver.setViewResolvers(resolvers);
	    return resolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				//		 .allowedOrigins("https://m-dev.kkokasin.com:8080")
				.allowedMethods("GET", "POST", "HEAD")
				.allowCredentials(true)
				.maxAge(3600);

	}

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
	 * ?????????????????? view
	 */
	@Bean
	public ViewResolver fileViewResolver() {
		BeanNameViewResolver resolver = new BeanNameViewResolver();
		resolver.setOrder(0);
		return resolver;
	}

	/**
	 * Jackson??? ????????? JSON view
	 */
    @Bean
    public ViewResolver jsonViewResolver() {
        return new JsonViewResolver();
    }

	/**
	 * Converter ??????
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new StringToLongConverter());
        registry.addConverter(new StringToDoubleConverter());
    }

}