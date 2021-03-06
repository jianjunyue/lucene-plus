package com.lucene.plus.custom.codec.knn.vectors.function;

import java.io.IOException; 
import java.util.Map;

import com.lucene.plus.custom.codec.knn.util.FieldUtils;
import com.lucene.plus.custom.codec.knn.vectors.cache.FieldDocValuesCache;
import com.lucene.index.DocValues; 
import com.lucene.index.LeafReaderContext;
import com.lucene.index.NumericDocValues; 
import com.lucene.queries.function.FunctionValues;
import com.lucene.queries.function.ValueSource;
 

/**
 * https://blog.csdn.net/sc736031305/article/details/84711554 向量内积分数排序
 */
public class VectorsValueSouce extends ValueSource {
	private float[] queryVector; 
	private String field;

	public VectorsValueSouce(String field, float[] queryVertices) {
		this.field = field;
		queryVector = queryVertices;
	}

	@Override
	public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
 		final NumericDocValues docValues=DocValues.getNumeric(readerContext.reader(), FieldUtils.KEYID_FIELD);
		 
		FunctionValues fun = new FunctionValues() {

			@Override
			public float floatVal(int doc) {
				float val = 0;
				try {
					if (docValues.advanceExact(doc) == false)
						return val; 
					long keyIdValue=docValues.longValue();
					float[] vertices = FieldDocValuesCache.getBinaryValue(field, keyIdValue) ;
				
					if (queryVector.length == vertices.length) {
						for (int i = 0; i < queryVector.length; i++) {
							val += queryVector[i] * vertices[i];
						}
					} 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return val;
			}

 

			@Override
			public String toString(int doc) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}

		};
		return fun;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
