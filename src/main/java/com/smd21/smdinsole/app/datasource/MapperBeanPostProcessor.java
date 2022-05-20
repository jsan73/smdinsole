package com.smd21.smdinsole.app.datasource;

import com.smd21.smdinsole.app.datasource.annotation.MapperBeanNameGenerator;
import com.smd21.smdinsole.app.datasource.properties.DataSourceListProperties;
import com.smd21.smdinsole.common.CommonUtil;
import com.zaxxer.hikari.HikariDataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * 다중 Data Source를 지원하기 위한 MyBatis Mapper Post Processor
 */
public class MapperBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

	private final DataSourceListProperties dsListProperties;

	static final String CONFIG_LOCALTION = "classpath:mybatis-config.xml";
	static final String MAPPER_LOCALTION = "/mapper/**/*.xml";
	private int dataSourceCount = 0;

	@Autowired
	private ApplicationContext context;

	public MapperBeanPostProcessor(DataSourceListProperties dsListProperties) {
		this.dsListProperties = dsListProperties;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {

		Collections.reverse(dsListProperties);

		dsListProperties.forEach((v) -> {

			// DataSource 등록
			registerDataSource(beanFactory, v);

			// SqlSessionFactory
			registerSqlSessionFactory(beanFactory, v);

			// SqlSessionTemplate
			registerSqlSessionTemplate(beanFactory, v);

			// TransactionManager
			registerTransactionManager(beanFactory, v);

			if(v.getDataSource().get("jdbcUrl").contains("replication")) {
				AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
				pointcut.setExpression("execution(* " + v.getAopExecution() +")");
				registerTransactionAdvice(beanFactory, v, pointcut);
			}
			// MapperScannerConfigurer
			registerMapperScanner(beanFactory, v);

			dataSourceCount++;
		});
		
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

		return;
	}

	/**
	 * Data Source Bean 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link DataSourceListProperties.DataSourceConfig}
	 */
	private void registerDataSource(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config
	) {

		Map<String, String> dataSource = config.getDataSource();
//		dataSource.put("autoCommit", "false");
//		dataSource.put("url", dataSource.get("jdbcUrl"));


		// DataSource 등록
		GenericBeanDefinition dataSourceBeanDefinition = new GenericBeanDefinition();
		dataSourceBeanDefinition.setBeanClass(HikariDataSource.class);
		dataSourceBeanDefinition.setPropertyValues(
				new MutablePropertyValues(dataSource)
		);

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.DATASOURCE),
				dataSourceBeanDefinition
		);
	}

	/**
	 * MyBatis SessionFactory 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link DataSourceListProperties.DataSourceConfig}
	 */
	private void registerSqlSessionFactory(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config
	) {

		String baseMapper = config.getBaseMapper();
		baseMapper = CommonUtil.empty(baseMapper) ? MAPPER_LOCALTION : baseMapper;

		AbstractBeanDefinition sqlSessionFactory = BeanDefinitionBuilder
				.genericBeanDefinition(SqlSessionFactoryBean.class)
				.addPropertyReference("dataSource", config.getName(DataSourceListProperties.BEAN_TYPE.DATASOURCE))
				.addPropertyValue("configLocation", CONFIG_LOCALTION)
				.addPropertyValue("mapperLocations", "classpath:" + baseMapper)
				.getBeanDefinition();

//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource((HikariDataSource)registry.getBeanDefinition("dataSource"));
//        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/master/**/*.xml"));
//        return factoryBean.getObject();

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.SESSION_FACTORY), sqlSessionFactory
		);
	}

	/**
	 * MyBatis SessionTemplate 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link DataSourceListProperties.DataSourceConfig}
	 */
	private void registerSqlSessionTemplate(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config
	) {
		AbstractBeanDefinition sqlSessionTemplate = BeanDefinitionBuilder
				.genericBeanDefinition(SqlSessionTemplate.class)
				.addConstructorArgReference(config.getName(DataSourceListProperties.BEAN_TYPE.SESSION_FACTORY))
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.SESSION_TEMPLATE), sqlSessionTemplate
		);
	}

	/**
	 * MyBatis TransactionManager 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link DataSourceListProperties.DataSourceConfig}
	 */
	private void registerTransactionManager(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config
	) {
		AbstractBeanDefinition transactionManager = BeanDefinitionBuilder
				.genericBeanDefinition(DataSourceTransactionManager.class)
				.addPropertyReference("dataSource", config.getName(DataSourceListProperties.BEAN_TYPE.DATASOURCE))
				.addPropertyValue("nestedTransactionAllowed", true)
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.TRANSACTION_MANAGER), transactionManager
		);
	}

	public void registerTransactionAdvice(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config, AspectJExpressionPointcut pointcut
	) {
/*
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		// pointcut.setExpression("execution(* kr.richnco.rion.**.*Impl.*(..))");
		pointcut.setExpression("execution(* kr.richnco.rion..service.*.*(..)");

		List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
		DefaultTransactionAttribute attribute = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		String transactionAttributesDefinition = attribute.toString();

		Properties txAttributes = new Properties();
		txAttributes.setProperty("insert*", transactionAttributesDefinition);
		txAttributes.setProperty("add*", transactionAttributesDefinition);
		txAttributes.setProperty("create*", transactionAttributesDefinition);
		txAttributes.setProperty("modify*", transactionAttributesDefinition);
		txAttributes.setProperty("update*", transactionAttributesDefinition);
		txAttributes.setProperty("delete*", transactionAttributesDefinition);
		txAttributes.setProperty("remove*", transactionAttributesDefinition);
		txAttributes.setProperty("useTransaction*", transactionAttributesDefinition);

		TransactionInterceptor txAdvice = new TransactionInterceptor();
		txAdvice.setTransactionAttributes(txAttributes);
		txAdvice.setTransactionManagerBeanName(config.getName(DataSourceListProperties.BEAN_TYPE.TRANSACTION_MANAGER));
*/
				
		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyAttribute.setReadOnly(true);

		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
		
		Properties txAttributes = new Properties();
		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("sel*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("down*", readOnlyTransactionAttributesDefinition);

		TransactionInterceptor txAdvice = new TransactionInterceptor();
		txAdvice.setTransactionAttributes(txAttributes);
		//txAdvice.setTransactionManagerBeanName(config.getName(DataSourceListProperties.BEAN_TYPE.TRANSACTION_MANAGER));
		PlatformTransactionManager txManager = (PlatformTransactionManager) context.getBean(config.getName(DataSourceListProperties.BEAN_TYPE.TRANSACTION_MANAGER));
		txAdvice.setTransactionManager(txManager);
		
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//
//		pointcut.setExpression("execution(* kr.richnco.rion..service.Test*.*(..))");

		
		AbstractBeanDefinition transactionAdvice = BeanDefinitionBuilder
				.genericBeanDefinition(DefaultPointcutAdvisor.class)
				.addPropertyValue("pointcut", pointcut)
				.addPropertyValue("advice", txAdvice)
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.TRANSACTION_ADVICE), transactionAdvice
		);
	}



	/**
	 * MyBatis MapperScanner 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link DataSourceListProperties.DataSourceConfig}
	 */
	private void registerMapperScanner(
			BeanDefinitionRegistry registry, DataSourceListProperties.DataSourceConfig config
	) {

		AbstractBeanDefinition mapperScanner = BeanDefinitionBuilder
				.genericBeanDefinition(MapperScannerConfigurer.class)
				.addPropertyValue("sqlSessionFactoryBeanName", config.getName(DataSourceListProperties.BEAN_TYPE.SESSION_FACTORY))
				.addPropertyValue("basePackage", config.getBasePackage())
				.addPropertyValue("nameGenerator", new MapperBeanNameGenerator())
				.getBeanDefinition();
//				.addPropertyValue("nameGenerator", new MapperBeanNameGenerator())
//		if(!isLastDataSource) {
//			beanDefinitionBuilder
//			.addPropertyValue("annotationClass", DataSourceMapper.class)


//		} else {
//			beanDefinitionBuilder
//			.addPropertyValue("nameGenerator", new MapperBeanNameGenerator())
//			.addPropertyValue("annotationClass", DataSourceMapper.class)
//		}

		registry.registerBeanDefinition(
				config.getName(DataSourceListProperties.BEAN_TYPE.MAPPER_SCANNER), mapperScanner
		);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
		
	}
}