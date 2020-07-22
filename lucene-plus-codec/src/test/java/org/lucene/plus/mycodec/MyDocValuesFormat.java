package org.lucene.plus.mycodec;

import java.io.IOException;

import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesFormat;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.SegmentReadState;
import com.lucene.index.SegmentWriteState;

public class MyDocValuesFormat extends DocValuesFormat {

	public static final String MY_EXT = "mydv";

	protected MyDocValuesFormat(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected MyDocValuesFormat() {
		super("MyCodec");
	}

	@Override
	public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
		return new MyDocValuesConsumer(state, MY_EXT);
	}

	@Override
	public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
		return new MyDocValuesProducer(state, MY_EXT);
	}

}
