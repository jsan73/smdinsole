package com.kokasin.insole.app.mybatis;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.*;

public class ObjectFactory extends DefaultObjectFactory {

	private static final long serialVersionUID = -1161306552941923827L;

	/**
	 * @MapKey("id") 사용시 순서를 보장하기 위해 Map 인터페이스의 구현을 LinkedHashMap으로 바꿈
	 *
	 * mybatis-config.xml에 다음을 추가하여야 동작함
	 * 		<objectFactory type="com.tims.app.mybatis.ObjectFactory"></objectFactory>
	 */
	@Override
	protected Class<?> resolveInterface(Class<?> type) {
		Class<?> classToCreate;
		if (type == List.class || type == Collection.class || type == Iterable.class) {
			classToCreate = ArrayList.class;
		} else if (type == Map.class) {
			classToCreate = LinkedHashMap.class;
		} else if (type == SortedSet.class) {
			classToCreate = TreeSet.class;
		} else if (type == Set.class) {
			classToCreate = HashSet.class;
		} else {
			classToCreate = type;
		}
		return classToCreate;
	}

}
