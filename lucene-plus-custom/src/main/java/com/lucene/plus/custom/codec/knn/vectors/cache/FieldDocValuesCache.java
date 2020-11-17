package com.lucene.plus.custom.codec.knn.vectors.cache;

import java.util.HashMap;
import java.util.Map;

import com.lucene.plus.custom.codec.knn.util.KNNCodecUtil;

public class FieldDocValuesCache {

	private static Map<String, Map<Integer, float[]>> fieldCache = new HashMap<String, Map<Integer, float[]>>();

	public static Map<Integer, float[]> getValues(String field){
		return fieldCache.get(field); 
	}

	public static void add(String field, KNNCodecUtil.Pair pair) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		for(int i=0;i<pair.docs.length;i++) { 
			temp.put(pair.docs[i], pair.vectors[i]);
		}
		fieldCache.put(field, temp);
	}
	
	public static void add(String field, int docId, float[] vertices) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		temp.put(docId, vertices);
		fieldCache.put(field, temp);
	}

	public static float[] getBinaryValue(String field, int docId) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		return temp.getOrDefault(docId, new float[] {});
	}
	

	public static float[] getBinaryValue(String field, long keyId) {
		Map<Integer, float[]> temp = fieldCache.getOrDefault(field, new HashMap<Integer, float[]>());
		return temp.getOrDefault((int)keyId, new float[] {});
	}
}
