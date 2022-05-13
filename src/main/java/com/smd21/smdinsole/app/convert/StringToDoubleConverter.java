package com.smd21.smdinsole.app.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToDoubleConverter implements Converter<String, Double> {

	@Override
	public Double convert(String source) {
		if(source == null)
			return null;
		Double d = NumberUtils.toDouble(StringUtils.replace(source, ",", ""));
		return d;
	}
}
