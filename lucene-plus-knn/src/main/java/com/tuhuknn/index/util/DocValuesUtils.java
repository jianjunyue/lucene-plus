package com.tuhuknn.index.util;

import java.util.ArrayList;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

public class DocValuesUtils {

	public static void getFloats(BinaryDocValues values)  {
		ArrayList<float[]> vectorList = new ArrayList<>();
		ArrayList<Integer> docIdList = new ArrayList<>();
		try {
			for (int doc = values.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS; doc = values.nextDoc()) {
				BytesRef bytesref = values.binaryValue();
				float[] vector = BinaryBytesUtils.bytesToFloats(bytesref);
				
				vectorList.add(vector);
				docIdList.add(doc);
			}
		} catch (Exception e) {
			 System.out.println(e.getMessage());
		}
	}

}
