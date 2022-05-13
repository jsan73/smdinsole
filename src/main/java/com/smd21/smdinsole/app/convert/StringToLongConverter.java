package com.smd21.smdinsole.app.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToLongConverter implements Converter<String, Long> {

	@Override
	public Long convert(String source) {
		if(source == null)
			return null;
		Long l = NumberUtils.toLong(StringUtils.replace(source, ",", ""));
		return l;
	}
}
