package org.lucene.plus.knn;

import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.FilterCodec;
import org.apache.lucene.codecs.lucene84.Lucene84Codec;
import org.lucene.plus.knn.index.codec.KNN80Codec.KNN80DocValuesFormat;

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
