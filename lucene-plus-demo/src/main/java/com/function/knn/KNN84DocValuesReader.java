package com.function.knn;

import java.io.IOException;

import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.BinaryDocValues;
import com.lucene.index.FieldInfo;
import com.lucene.index.IndexFileNames;
import com.lucene.index.NumericDocValues;
import com.lucene.index.SegmentReadState;
import com.lucene.index.SortedDocValues;
import com.lucene.index.SortedNumericDocValues;
import com.lucene.index.SortedSetDocValues;
import com.lucene.store.IndexInput;

public class KNN84DocValuesReader extends DocValuesProducer  {

	 final IndexInput data;
	    final int maxDoc;
	    
	    public KNN84DocValuesReader(SegmentReadState state, String ext) throws IOException {
	        System.out.println("dir=" + state.directory + " seg=" + state.segmentInfo.name + " file=" + IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext));
	        data = state.directory.openInput(IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext), state.context);
	        maxDoc = state.segmentInfo.maxDoc();
	        
	    }
	    
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long ramBytesUsed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NumericDocValues getNumeric(FieldInfo field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BinaryDocValues getBinary(FieldInfo field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedDocValues getSorted(FieldInfo field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedNumericDocValues getSortedNumeric(FieldInfo field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSetDocValues getSortedSet(FieldInfo field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkIntegrity() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
