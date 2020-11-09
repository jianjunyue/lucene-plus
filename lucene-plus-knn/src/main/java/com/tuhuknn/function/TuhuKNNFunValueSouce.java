package com.tuhuknn.function;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.BytesRef;

import com.tuhuknn.cache.DocVectorCache;
import com.tuhuknn.index.util.BinaryBytesUtils; 

/**
 * https://blog.csdn.net/sc736031305/article/details/84711554
 * 商品有效期是小时级别的时间段，当前时间无效的排序要置底
 */
public class TuhuKNNFunValueSouce extends ValueSource {
	private static long now;
	private String field;

	public TuhuKNNFunValueSouce(String field) {
		this.field = field;
		now = LocalTime.now().getHour();
	}

	@Override
	public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
		// 此处读取内存缓成
//		final BinaryDocValues docValues = DocValues.getBinary(readerContext.reader(), field);
//       	
		final BinaryDocValues  docValues;
		 BinaryDocValues docValues_temp = DocVectorCache.getBinaryDocValues(field);
		if (docValues_temp == null) {
			docValues = DocValues.getBinary(readerContext.reader(), field);
			DocVectorCache.setLoader(field, docValues);
		}else {
			docValues=docValues_temp;
		} 
		FunctionValues fun = new FunctionValues() {
			int lastDocID;

			/**
			 * FunctionQuery的score()方法里面调用floatVal，返回值为排序评分 FunctionRangeQuery的
			 * scorer(LeafReaderContext context)，返回值起范围过滤作用
			 */
			@Override
			public float floatVal(int doc) {
				if (doc < lastDocID) {
					throw new IllegalArgumentException(
							"docs were sent out-of-order: lastDocID=" + lastDocID + " vs docID=" + doc);
				}
				try {
					lastDocID = doc;
					int curDocID = docValues.docID();
					if (doc > curDocID) {
						curDocID = docValues.advance(doc);
					}
					if (doc == curDocID) {
						float[] floatVectors = BinaryBytesUtils.bytesToFloats(docValues.binaryValue());
						System.out.println("doc:" + doc + " , f:" + floatVectors[0]);
						return floatVectors[0];
					} else {
						return 0;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}

			@Override
			public int intVal(int doc) {
				float f = 0f;
				try {
//					f=docValues.longValue();
					System.out.println("intVal");
					if (docValues.advanceExact(doc) == false)
						return 0;

					BytesRef ref = docValues.binaryValue();
					float[] floatVectors = BinaryBytesUtils.bytesToFloats(ref);
					f = floatVectors[0];
					return f < 10 ? 10 : 20;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
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
	public void createWeight(Map context, IndexSearcher searcher) throws IOException {
		System.out.println("test function");
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
