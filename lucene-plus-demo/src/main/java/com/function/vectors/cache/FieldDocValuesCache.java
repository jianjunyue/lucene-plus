package com.function.vectors.cache;

import java.util.HashMap;
import java.util.Map;

public class FieldDocValuesCache {

	private static Map<String, Map<Integer, float[]>> fieldCache = new HashMap<String, Map<Integer, float[]>>();

	public static void add(String field, int docId, float[] vertices) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		temp.put(docId, vertices);
		fieldCache.put(field, temp);
	}

	public static float[] getBinaryValue(String field, int docId) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		return temp.getOrDefault(docId, new float[] {});
	}
}