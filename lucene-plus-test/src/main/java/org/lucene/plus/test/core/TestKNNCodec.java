package org.lucene.plus.test.core;

import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.FilterCodec;
import org.apache.lucene.codecs.lucene84.Lucene84Codec;
 
public class TestKNNCodec extends FilterCodec {

	private final TestKNN80DocValuesFormat myDocValuesFormat = new TestKNN80DocValuesFormat();

	public TestKNNCodec() {
		super("TestKNNCodec", new Lucene84Codec());
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocValuesFormat docValuesFormat() {
		return myDocValuesFormat;
	}

}
