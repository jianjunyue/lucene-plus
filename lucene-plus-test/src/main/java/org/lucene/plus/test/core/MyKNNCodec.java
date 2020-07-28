package org.lucene.plus.test.core;

import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.FilterCodec;
import org.apache.lucene.codecs.lucene84.Lucene84Codec;
 
public class MyKNNCodec extends FilterCodec {

	private final KNN80DocValuesFormat myDocValuesFormat = new KNN80DocValuesFormat();

	public MyKNNCodec() {
		super("MyKNNCodec", new Lucene84Codec());
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocValuesFormat docValuesFormat() {
		return myDocValuesFormat;
	}

}
