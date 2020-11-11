package com.tuhuknn.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.index.BinaryDocValues;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheLoader;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;

public class DocVectorCache {
	
	private static Map<String,BinaryDocValues> cache=new HashMap<>();
	
    public static void setLoader(String field,BinaryDocValues docValues) {
  	cache.put(field, docValues);
   }
   
   public static BinaryDocValues getBinaryDocValues(String field) {
   	return cache.get(field);  
   }
}
