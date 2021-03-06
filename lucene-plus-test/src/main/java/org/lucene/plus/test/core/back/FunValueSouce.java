package org.lucene.plus.test.core.back;

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
import org.lucene.plus.test.temp.BinaryBytesUtils;

/**
 * https://blog.csdn.net/sc736031305/article/details/84711554
 * 商品有效期是小时级别的时间段，当前时间无效的排序要置底
 */
public class FunValueSouce extends ValueSource {
	private static long now;
	private String field;

	public FunValueSouce(String field) {
		this.field = field;
		now = LocalTime.now().getHour();
	}

	@Override
	public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
//		final NumericDocValues docValues = DocValues.getNumeric(readerContext.reader(), field);
		final BinaryDocValues  docValues=DocValues.getBinary(readerContext.reader(), field);
		//此处读取内存缓成
		
		FunctionValues fun = new FunctionValues() {

			/**
			 * FunctionQuery的score()方法里面调用floatVal，返回值为排序评分
			 * FunctionRangeQuery的 scorer(LeafReaderContext context)，返回值起范围过滤作用
			 * */
			@Override
			public float floatVal(int doc) {
				float f=0f;
				try {
					if (docValues.advanceExact(doc) == false)
						return 0;
//					f=docValues.longValue();
					BytesRef ref=docValues.binaryValue();
					float[] floatVectors =BinaryBytesUtils.bytesToFloats(ref);
					f=floatVectors[0];
					System.out.println("doc:"+ doc+" , f:"+f);
					return f;

//					return docValues.longValue() < 10 ? -docValues.longValue() : docValues.longValue();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}

			@Override
			public int intVal(int doc) {
				float f=0f;
				try {
//					f=docValues.longValue();
					System.out.println("intVal");
					if (docValues.advanceExact(doc) == false)
						return 0;
					 
					BytesRef ref=docValues.binaryValue();
					float[] floatVectors =BinaryBytesUtils.bytesToFloats(ref);
					f=floatVectors[0];
					return f  < 10 ? 10 : 20;
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
