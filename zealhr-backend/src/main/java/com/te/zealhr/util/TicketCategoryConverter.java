package com.te.zealhr.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.AttributeConverter;

public class TicketCategoryConverter implements AttributeConverter<Map<String, Map<String, String>>, String> {

	@Override
	public String convertToDatabaseColumn(Map<String, Map<String, String>> attribute) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("{");

		for (Map.Entry<String, Map<String, String>> outerEntry : attribute.entrySet()) {
			String outerKey = outerEntry.getKey();
			Map<String, String> innerMap = outerEntry.getValue();

			stringBuilder.append("\"").append(outerKey).append("\": {");

			for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
				String innerKey = innerEntry.getKey();
				String innerValue = innerEntry.getValue();

				stringBuilder.append("\"").append(innerKey).append("\": \"").append(innerValue).append("\", ");
			}

			// Remove the trailing comma and space
			if (!innerMap.isEmpty()) {
				stringBuilder.setLength(stringBuilder.length() - 2);
			}

			stringBuilder.append("}, ");
		}

		// Remove the trailing comma and space
		if (!attribute.isEmpty()) {
			stringBuilder.setLength(stringBuilder.length() - 2);
		}

		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	@Override
	public Map<String, Map<String, String>> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new LinkedHashMap<>();
		}
		Map<String, Map<String, String>> resultMap = new LinkedHashMap<>();

		Pattern outerPattern = Pattern.compile("\"(.*?)\":\\s*\\{(.*?)\\}");
		Matcher outerMatcher = outerPattern.matcher(dbData);

		while (outerMatcher.find()) {
			String outerKey = outerMatcher.group(1);
			String innerMapString = outerMatcher.group(2);

			Map<String, String> innerMap = new LinkedHashMap<>();

			Pattern innerPattern = Pattern.compile("\"(.*?)\":\\s*\"(.*?)\"");
			Matcher innerMatcher = innerPattern.matcher(innerMapString);

			while (innerMatcher.find()) {
				String innerKey = innerMatcher.group(1);
				String innerValue = innerMatcher.group(2);
				innerMap.put(innerKey, innerValue);
			}

			resultMap.put(outerKey, innerMap);
		}

		return resultMap;
	}

}
