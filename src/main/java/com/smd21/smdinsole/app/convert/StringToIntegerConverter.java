package com.smd21.smdinsole.app.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String source) {
		if(source == null)
			return null;
		Integer i = NumberUtils.toInt(StringUtils.replace(source, ",", ""));
		return i;
	}
}
