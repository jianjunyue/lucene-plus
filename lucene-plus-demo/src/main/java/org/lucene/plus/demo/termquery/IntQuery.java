package org.lucene.plus.demo.termquery;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.lucene.index.IndexReaderContext;
import com.lucene.index.LeafReader;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.PointValues;
import com.lucene.index.PostingsEnum;
import com.lucene.index.ReaderUtil;
import com.lucene.index.Term;
import com.lucene.index.TermState;
import com.lucene.index.TermStates;
import com.lucene.index.TermsEnum;
import com.lucene.index.PointValues.IntersectVisitor;
import com.lucene.index.PointValues.Relation;
import com.lucene.search.ConstantScoreScorer;
import com.lucene.search.ConstantScoreWeight;
import com.lucene.search.DocIdSetIterator;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreMode;
import com.lucene.search.Scorer;
import com.lucene.search.ScorerSupplier;
import com.lucene.search.Weight;
import com.lucene.search.similarities.Similarity;
import com.lucene.util.BitSetIterator;
import com.lucene.util.DocIdSetBuilder;
import com.lucene.util.FixedBitSet;
import com.lucene.util.FutureArrays;
import com.lucene.util.NumericUtils;

public class IntQuery extends Query {
	private String field;
	private byte[] byteValue = new byte[4]; 

	public IntQuery(String field, int value) {
		this.field = field; 
		encodeDimension(value, byteValue, 0); 
	}

	@Override
	public final Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {

		return new ConstantScoreWeight(this, boost) {

			private boolean matches(byte[] packedValue) {
				int offset=0; 
					if (FutureArrays.compareUnsigned(packedValue, offset, offset , byteValue, offset,
							offset ) < 0) {
						// Doc's value is too low, in this dimension
						return false;
					}
					if (FutureArrays.compareUnsigned(packedValue, offset, offset , byteValue, offset,
							offset ) > 0) {
						// Doc's value is too high, in this dimension
						return false;
					} 
				return true;
			}

			private Relation relate(byte[] minPackedValue, byte[] maxPackedValue) {

				boolean crosses = false;
 
					int offset = 0;

					if (FutureArrays.compareUnsigned(minPackedValue, offset, offset , byteValue, offset,
							offset ) > 0
							|| FutureArrays.compareUnsigned(maxPackedValue, offset, offset , byteValue,
									offset, offset) < 0) {
						return Relation.CELL_OUTSIDE_QUERY;
					}

					crosses |= FutureArrays.compareUnsigned(minPackedValue, offset, offset , byteValue,
							offset, offset ) < 0
							|| FutureArrays.compareUnsigned(maxPackedValue, offset, offset , byteValue,
									offset, offset ) > 0;
				 

				if (crosses) {
					return Relation.CELL_CROSSES_QUERY;
				} else {
					return Relation.CELL_INSIDE_QUERY;
				}
			}

			private IntersectVisitor getIntersectVisitor(DocIdSetBuilder result) {
				return new IntersectVisitor() {

					DocIdSetBuilder.BulkAdder adder;

					@Override
					public void grow(int count) {
						adder = result.grow(count);
					}

					@Override
					public void visit(int docID) {
						adder.add(docID);
					}

					@Override
					public void visit(int docID, byte[] packedValue) {
						if (matches(packedValue)) {
							visit(docID);
						}
					}

					@Override
					public void visit(DocIdSetIterator iterator, byte[] packedValue) throws IOException {
						if (matches(packedValue)) {
							int docID;
							while ((docID = iterator.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
								visit(docID);
							}
						}
					}

					@Override
					public Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
						return relate(minPackedValue, maxPackedValue);
					}
				};
			}

			/**
			 * Create a visitor that clears documents that do NOT match the range.
			 */
			private IntersectVisitor getInverseIntersectVisitor(FixedBitSet result, int[] cost) {
				return new IntersectVisitor() {

					@Override
					public void visit(int docID) {
						result.clear(docID);
						cost[0]--;
					}

					@Override
					public void visit(int docID, byte[] packedValue) {
						if (matches(packedValue) == false) {
							visit(docID);
						}
					}

					@Override
					public void visit(DocIdSetIterator iterator, byte[] packedValue) throws IOException {
						if (matches(packedValue) == false) {
							int docID;
							while ((docID = iterator.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
								visit(docID);
							}
						}
					}

					@Override
					public Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
						Relation relation = relate(minPackedValue, maxPackedValue);
						switch (relation) {
						case CELL_INSIDE_QUERY:
							// all points match, skip this subtree
							return Relation.CELL_OUTSIDE_QUERY;
						case CELL_OUTSIDE_QUERY:
							// none of the points match, clear all documents
							return Relation.CELL_INSIDE_QUERY;
						default:
							return relation;
						}
					}
				};
			}

			@Override
			public ScorerSupplier scorerSupplier(LeafReaderContext context) throws IOException {
				LeafReader reader = context.reader();

				PointValues values = reader.getPointValues(field);
				if (values == null) {
					// No docs in this segment/field indexed any points
					return null;
				}
 
				boolean allDocsMatch;
				if (values.getDocCount() == reader.maxDoc()) {
					final byte[] fieldPackedLower = values.getMinPackedValue();
					final byte[] fieldPackedUpper = values.getMaxPackedValue();
					allDocsMatch = true; 
						int offset = 0;
						if (FutureArrays.compareUnsigned(byteValue, offset, offset , fieldPackedLower,
								offset, offset ) > 0
								|| FutureArrays.compareUnsigned(byteValue, offset, offset ,
										fieldPackedUpper, offset, offset ) < 0) {
							allDocsMatch = false; 
					}
				} else {
					allDocsMatch = false;
				}

				final Weight weight = this;
				if (allDocsMatch) {
					// all docs have a value and all points are within bounds, so everything matches
					return new ScorerSupplier() {
						@Override
						public Scorer get(long leadCost) {
							return new ConstantScoreScorer(weight, score(), scoreMode,
									DocIdSetIterator.all(reader.maxDoc()));
						}

						@Override
						public long cost() {
							return reader.maxDoc();
						}
					};
				} else {
					return new ScorerSupplier() {

						final DocIdSetBuilder result = new DocIdSetBuilder(reader.maxDoc(), values, field);
						final IntersectVisitor visitor = getIntersectVisitor(result);
						long cost = -1;

						@Override
						public Scorer get(long leadCost) throws IOException {
							if (values.getDocCount() == reader.maxDoc() && values.getDocCount() == values.size()
									&& cost() > reader.maxDoc() / 2) {
								// If all docs have exactly one value and the cost is greater
								// than half the leaf size then maybe we can make things faster
								// by computing the set of documents that do NOT match the range
								final FixedBitSet result = new FixedBitSet(reader.maxDoc());
								result.set(0, reader.maxDoc());
								int[] cost = new int[] { reader.maxDoc() };
								values.intersect(getInverseIntersectVisitor(result, cost));
								final DocIdSetIterator iterator = new BitSetIterator(result, cost[0]);
								return new ConstantScoreScorer(weight, score(), scoreMode, iterator);
							}

							values.intersect(visitor);
							DocIdSetIterator iterator = result.build().iterator();
							return new ConstantScoreScorer(weight, score(), scoreMode, iterator);
						}

						@Override
						public long cost() {
							if (cost == -1) {
								// Computing the cost may be expensive, so only do it if necessary
								cost = values.estimateDocCount(visitor);
								assert cost >= 0;
							}
							return cost;
						}
					};
				}
			}

			@Override
			public Scorer scorer(LeafReaderContext context) throws IOException {
				ScorerSupplier scorerSupplier = scorerSupplier(context);
				if (scorerSupplier == null) {
					return null;
				}
				return scorerSupplier.get(Long.MAX_VALUE);
			}

			@Override
			public boolean isCacheable(LeafReaderContext ctx) {
				return true;
			}

		};

	}

	public void encodeDimension(int value, byte dest[], int offset) {
		NumericUtils.intToSortableBytes(value, dest, offset);
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