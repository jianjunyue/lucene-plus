package com.amazon.opendistroforelasticsearch.knn.index.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.BinaryDocValues;

public class KnnCache {
	
	private static Map<String,BinaryDocValues> cache=new HashMap<>();
	
    public static void setLoader(String field,BinaryDocValues docValues) {
//   	 cacheBinaryDocValues.config().setLoader(this::cacheBinaryDocValues);
//   	 cacheBinaryDocValues.config().setLoader(cacheBinaryDocValues());
//   	cacheBinaryDocValues.put(field, docValues);
//   	binaryDocValues=docValues;
    	cache.put(field, docValues);
   }
   
   public static BinaryDocValues getBinaryDocValues(String field) {
   	return cache.get(field);  
   }
}
