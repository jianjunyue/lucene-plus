package org.lucene.plus.mycodec;

import java.io.IOException; 

import com.lucene.index.Terms;
import com.lucene.index.TermsEnum; 
 

public class MyTerms extends Terms {

	@Override
	public TermsEnum iterator() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long size() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSumTotalTermFreq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSumDocFreq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDocCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasFreqs() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasOffsets() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPositions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPayloads() {
		// TODO Auto-generated method stub
		return false;
	}
 

}
