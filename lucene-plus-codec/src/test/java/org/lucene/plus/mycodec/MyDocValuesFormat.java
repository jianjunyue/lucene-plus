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
		System.out.println("MyDocValuesFormat name ");
		// TODO Auto-generated constructor stub
	}

	protected MyDocValuesFormat() {
		super("MyCodec");
		System.out.println("MyDocValuesFormat ");
	}

	@Override
	public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
		System.out.println("fieldsConsumer ");
		return new MyDocValuesConsumer(state, MY_EXT);
	}

	@Override
	public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
		System.out.println("fieldsProducer ");
		return new MyDocValuesProducer(state, MY_EXT);
	}

}
