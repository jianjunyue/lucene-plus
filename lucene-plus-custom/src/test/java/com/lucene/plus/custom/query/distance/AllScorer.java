package com.lucene.plus.custom.query.distance;

import java.io.IOException;

import com.lucene.index.IndexReader;
import com.lucene.index.LeafReaderContext;
import com.lucene.queries.function.FunctionValues; 
import com.lucene.search.DocIdSetIterator;
import com.lucene.search.Explanation;
import com.lucene.search.Scorer;
import com.lucene.search.Weight;

public class AllScorer extends Scorer {

	protected AllScorer(LeafReaderContext context,Weight weight) {
		super(weight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocIdSetIterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMaxScore(int upTo) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float score() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int docID() {
		// TODO Auto-generated method stub
		return 0;
	}
  

}
