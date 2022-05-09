package com.smd21.smdinsole.app.datasource.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class MapperBeanNameGenerator implements BeanNameGenerator {

//    private final AnnotationBeanNameGenerator defaultGenerator = new AnnotationBeanNameGenerator();

    @Override
    public String generateBeanName(final BeanDefinition definition, final BeanDefinitionRegistry registry) {
//        final String result;

//        if (isAnnotationMapper(definition)) {
//        	result = this.determineBeanNameFromAnnotation((AnnotatedBeanDefinition)definition);
//        } else {
//            result = this.defaultGenerator.generateBeanName(definition, registry);
//        }
//
//        return result;

        return generateFullBeanName((AnnotatedBeanDefinition)definition);
    }

    private String generateFullBeanName(final AnnotatedBeanDefinition definition) {
        // 패키지를 포함한 전체 이름을 반환

    	return definition.getMetadata().getClassName();
    }

    /**
	 * Derive a bean name from one of the annotations on the class.
	 * @param annotatedDef the annotation-aware bean definition
	 * @return the bean name, or {@code null} if none is found
	 */
//	@Nullable
//	protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
//		AnnotationMetadata amd = annotatedDef.getMetadata();
//		Set<String> types = amd.getAnnotationTypes();
//		String beanName = null;
//		for (String type : types) {
//			Map<String, Object> attributes = amd.getAnnotationAttributes(type);
//			if(attributes.containsKey("value")) {
//				Object value = attributes.get("value");
//				if (value instanceof String) {
//					String strVal = (String) value;
//					if (StringUtils.hasLength(strVal)) {
//						if (beanName != null && !strVal.equals(beanName)) {
//							throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
//									"component names: '" + beanName + "' versus '" + strVal + "'");
//						}
//						beanName = strVal;
//					}
//				}
//			}
//		}
//		return beanName;
//	}

    /*
     * DependsMapper인지 판별하는 메서드
     */
//    private boolean isAnnotationMapper(final BeanDefinition definition) {
//        if (definition instanceof AnnotatedBeanDefinition) {
//            final Set<String> annotationTypes = ((AnnotatedBeanDefinition) definition).getMetadata()
//                    .getAnnotationTypes();
//
//            return annotationTypes.stream()
//                    .filter(type -> type.equals(DataSourceMapper.class.getName()))
//                    .findAny()
//                    .isPresent();
//        }
//        return false;
//    }
}
