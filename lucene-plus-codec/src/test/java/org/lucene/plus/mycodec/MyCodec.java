package org.lucene.plus.mycodec;

import com.lucene.codecs.DocValuesFormat;
import com.lucene.codecs.FilterCodec;
import com.lucene.codecs.lucene84.Lucene84Codec;

public class MyCodec extends FilterCodec {

	private final MyDocValuesFormat myDocValuesFormat = new MyDocValuesFormat();

	public MyCodec() {
		super("MyCodec", new Lucene84Codec());
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocValuesFormat docValuesFormat() {
		return myDocValuesFormat;
	}

}
