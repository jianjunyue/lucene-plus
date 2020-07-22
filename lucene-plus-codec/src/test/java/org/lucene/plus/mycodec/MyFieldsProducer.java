package org.lucene.plus.mycodec;

import java.io.IOException;
import java.util.Iterator;

import com.lucene.codecs.FieldsProducer;
import com.lucene.index.Terms;
 

public class MyFieldsProducer extends FieldsProducer {

	@Override
	public long ramBytesUsed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkIntegrity() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Terms terms(String field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
 

}
