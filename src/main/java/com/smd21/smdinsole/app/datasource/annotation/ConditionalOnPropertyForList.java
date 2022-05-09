package com.smd21.smdinsole.app.datasource.annotation;

import com.smd21.smdinsole.app.datasource.properties.DataSourceListProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.*;
import java.util.Objects;

/**
 * YML에 정의된 Property 유무를 판단하는 Annotation
 * - 정의되지 않을 경우 Annotation이 선언된 Class 또는 Method는 실행되지 않음
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({ConditionalOnPropertyForList.OnPropertyCondition.class})
public @interface ConditionalOnPropertyForList {
	String prefix() default "";

	class OnPropertyCondition implements Condition {

		@Override
		public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
			String prefix = (String) Objects.requireNonNull(metadata.getAnnotationAttributes(
					ConditionalOnPropertyForList.class.getName())).get("prefix");

			DataSourceListProperties repoConfig = Binder.get(conditionContext.getEnvironment())
					.bind(prefix, DataSourceListProperties.class)
					.orElse(new DataSourceListProperties());

			return !repoConfig.isEmpty();
		}
	}
}
