package com.smd21.smdinsole.app.datasource;

import com.smd21.smdinsole.app.datasource.annotation.ConditionalOnPropertyForList;
import com.smd21.smdinsole.app.datasource.properties.DataSourceListProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.NoSuchElementException;

/**
 * MyBatis Mapper Auto Configuration 설정
 *
 **/
@Configuration
@ConditionalOnPropertyForList(prefix = MapperConfig.PROPERTY_DATASOURCE_LIST_PREFIX)
public class MapperConfig {

	static final String PROPERTY_DATASOURCE_LIST_PREFIX = "datasources";

	@Bean
	public BeanDefinitionRegistryPostProcessor postProcessor(Environment environment) {
		try {

			return new MapperBeanPostProcessor(
					DataSourceListProperties.init(environment, MapperConfig.PROPERTY_DATASOURCE_LIST_PREFIX)
					);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return null;
	}

}