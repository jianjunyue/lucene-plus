package org.lucene.plus.mycodec;

import java.io.IOException;

import com.lucene.codecs.FieldsConsumer;
import com.lucene.codecs.FieldsProducer;
import com.lucene.codecs.PostingsFormat;
import com.lucene.index.SegmentReadState;
import com.lucene.index.SegmentWriteState;
 

public class MyPostingsFormat extends PostingsFormat {

	protected MyPostingsFormat(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FieldsConsumer fieldsConsumer(SegmentWriteState arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldsProducer fieldsProducer(SegmentReadState arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
