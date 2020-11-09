package com.tuhuknn.cache;

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
	
	private static BinaryDocValues binaryDocValues;
	
    @CreateCache(name = "cacheBinaryDocValues", cacheType = CacheType.LOCAL, expire = 60, timeUnit = TimeUnit.MINUTES, localLimit = 1000)
    @CachePenetrationProtect
    private static Cache<String, BinaryDocValues> cacheBinaryDocValues ;
     
	public void init() {
        RefreshPolicy policy = RefreshPolicy.newPolicy(50, TimeUnit.MINUTES).stopRefreshAfterLastAccess(6, TimeUnit.HOURS);
        cacheBinaryDocValues.config().setRefreshPolicy(policy); 
    }
    
    public static void setLoader(String field,BinaryDocValues docValues) {
//    	 cacheBinaryDocValues.config().setLoader(this::cacheBinaryDocValues);
//    	 cacheBinaryDocValues.config().setLoader(cacheBinaryDocValues());
//    	cacheBinaryDocValues.put(field, docValues);
    	binaryDocValues=docValues;
    }
    
    public static BinaryDocValues getBinaryDocValues(String field) {
//    	return cacheBinaryDocValues.get(field); 
    	return binaryDocValues;
    }
    
//    DocValuesUtil.getFloats(docValues);
    
    
//    private  static CacheLoader<String, BinaryDocValues> cacheBinaryDocValues(String field,BinaryDocValues docValues) {
//    	cacheBinaryDocValues.put(field, docValues);
//    	return loader;
//    }
//    
//    private static BinaryDocValues cacheBinaryDocValues(String key) {
//       return null;
//    }
}
