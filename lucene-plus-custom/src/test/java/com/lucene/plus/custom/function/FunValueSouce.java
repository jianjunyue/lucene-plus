package com.lucene.plus.custom.function;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

import com.lucene.index.DocValues;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.NumericDocValues;
import com.lucene.queries.function.FunctionValues;
import com.lucene.queries.function.ValueSource;
import com.lucene.search.IndexSearcher;

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
		final NumericDocValues docValues = DocValues.getNumeric(readerContext.reader(), field);
		FunctionValues fun = new FunctionValues() {

			/**
			 * FunctionQuery的score()方法里面调用floatVal，返回值为排序评分
			 * FunctionRangeQuery的 scorer(LeafReaderContext context)，返回值起范围过滤作用
			 * */
			@Override
			public float floatVal(int doc) {
				try {
					if (docValues.advanceExact(doc) == false)
						return 0;
					
					return docValues.longValue();

//					return docValues.longValue() < 10 ? -docValues.longValue() : docValues.longValue();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}

			@Override
			public int intVal(int doc) {
				try {
					
					System.out.println("intVal");
					if (docValues.advanceExact(doc) == false)
						return 0;
					 

					return docValues.longValue() < 10 ? 10 : 20;
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
