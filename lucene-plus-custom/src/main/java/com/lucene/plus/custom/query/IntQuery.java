package com.lucene.plus.custom.query;

import java.io.IOException;  
 
import com.lucene.index.LeafReader;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.PointValues; 
import com.lucene.index.PointValues.Relation;
import com.lucene.search.ConstantScoreScorer;
import com.lucene.search.ConstantScoreWeight; 
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreMode;
import com.lucene.search.Scorer; 
import com.lucene.search.Weight; 
import com.lucene.util.DocIdSetBuilder; 
import com.lucene.util.FutureArrays;
import com.lucene.util.NumericUtils;

/**
 * 进行int值精确比较重写Query
 * 对应的Field为IntPoint，但也可以是多个数值二进制byte[]
 * */
public class IntQuery extends Query {
	private String field;
	private byte[] queryPackedValue = new byte[4];
	private int value;

	public IntQuery(String field, int value) {
		this.field = field;
		this.value = value;
		NumericUtils.intToSortableBytes(value, queryPackedValue, 0);
	}

	@Override
	public Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {

		return new ConstantScoreWeight(this, boost) {

			@Override
			public Scorer scorer(LeafReaderContext context) throws IOException {
				LeafReader reader = context.reader();
				PointValues values = reader.getPointValues(field);
				DocIdSetBuilder result = new DocIdSetBuilder(reader.maxDoc(), values, field);
				values.intersect(new PointValues.IntersectVisitor() {
					DocIdSetBuilder.BulkAdder adder;

					@Override
					public void grow(int count) {
						adder = result.grow(count);
					}

					/**
					 * Relation.CELL_INSIDE_QUERY 时执行，已经是范围内数据，无需再做精确判断
					 */
					@Override
					public void visit(int docID) throws IOException {
						adder.add(docID);
					}

					/**
					 * Relation.CELL_CROSSES_QUERY 时执行，需要做精确判断
					 */
					@Override
					public void visit(int docID, byte[] leaf) throws IOException {
						int leafValue = NumericUtils.sortableBytesToInt(leaf, 0);
						if (value == leafValue) {
							visit(docID);
						} 
					}

					/** 粗过滤 */
					@Override
					public Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {

						int bytesPerDim = 4;
						int offset = 0;

						boolean match = FutureArrays.compareUnsigned(queryPackedValue, offset, offset + bytesPerDim,
								minPackedValue, offset, offset + bytesPerDim) >= 0
								&& FutureArrays.compareUnsigned(queryPackedValue, offset, offset + bytesPerDim,
										maxPackedValue, offset, offset + bytesPerDim) <= 0;

						return match == true ? Relation.CELL_CROSSES_QUERY : Relation.CELL_OUTSIDE_QUERY;
					}
				});
				return (Scorer) new ConstantScoreScorer((Weight) this, score(), scoreMode, result.build().iterator());
			}

			@Override
			public boolean isCacheable(LeafReaderContext ctx) {
				return false;
			}
		};

	}

	@Override
	public String toString(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}