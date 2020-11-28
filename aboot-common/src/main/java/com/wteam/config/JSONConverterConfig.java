package com.wteam.config;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JSON JPA 映射
 * @author mission
 * @since 2020/11/19 11:46
 */

public class JSONConverterConfig {


	@Converter(autoApply=true)
	public static class HuToolJSONConverter implements AttributeConverter<cn.hutool.json.JSON,String> {
		public HuToolJSONConverter() {
		}

		@Override
		public String convertToDatabaseColumn(cn.hutool.json.JSON json) {
			return json.toString();
		}

		@Override
		public cn.hutool.json.JSON convertToEntityAttribute(String s) {
			return cn.hutool.json.JSONUtil.parse(s);
		}
	}
}
