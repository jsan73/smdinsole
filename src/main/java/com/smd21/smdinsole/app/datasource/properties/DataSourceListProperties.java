package com.smd21.smdinsole.app.datasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Map;

public class DataSourceListProperties extends ArrayList<DataSourceListProperties.DataSourceConfig> {

	public static DataSourceListProperties init(Environment environment, String prefix) {
		return Binder.get(environment).bind(prefix, DataSourceListProperties.class).get();
	}

	/**
	   * MyBatis Mapper 설정을 위한 Bean Instance Class Type 정의
	   */
	public enum BEAN_TYPE {
		DATASOURCE("DataSource"),
		SESSION_FACTORY("SessionFactory"),
		SESSION_TEMPLATE("SessionTemplate"),
		TRANSACTION_MANAGER(""),
		TRANSACTION_ADVICE("TxAdvice"),
		MAPPER_SCANNER("MapperScanner");

		private final String suffix;

		BEAN_TYPE(String suffix) {
			this.suffix = suffix;
		}

		public String getSuffix() {
			return this.suffix;
		}
	}

	@Data
	public static class DataSourceConfig {

	    private String name = "";
	    private String basePackage = "";
	    private String baseMapper = "";
	    private String aopExecution = "";
	    private Map<String, String> dataSource;

	    public String getName(BEAN_TYPE beanType) {
	    	return this.name.concat(beanType.getSuffix());
	    }

	}
}
