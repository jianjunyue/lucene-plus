package com.function.index.codec;
    
import com.lucene.codecs.DocValuesFormat;

import com.lucene.codecs.FilterCodec;
import com.lucene.codecs.lucene84.Lucene84Codec; 

/**
 * knn binary codec
 * */
public class KnnBinaryCodec extends FilterCodec {

	private final KNN84DocValuesFormat knnDocValuesFormat = new KNN84DocValuesFormat();

	
	public KnnBinaryCodec() { 
		super("KnnBinaryCodec", new Lucene84Codec());  
	}
	
	@Override
	public DocValuesFormat docValuesFormat() {
		return knnDocValuesFormat;
	}
 

}
