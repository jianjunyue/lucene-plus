package com.lucene.plus.custom.query.distance;

import java.io.IOException; 
import com.lucene.index.LeafReader;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.PointValues; 
import com.lucene.search.ConstantScoreScorer;
import com.lucene.search.ConstantScoreWeight; 
import com.lucene.search.Query;
import com.lucene.search.ScoreMode;
import com.lucene.search.Scorer; 
import com.lucene.util.DocIdSetBuilder; 

public class DistanceWeight extends ConstantScoreWeight {

	private String field;
	public Query query;
	public DistanceInfo distanceInfo;

	protected DistanceWeight(Query query, DistanceInfo distanceInfo, float score) {
		super(query, score);
		this.query = query;
		this.distanceInfo = distanceInfo;
		this.field = distanceInfo.getField();

	}

	@Override
	public boolean isCacheable(LeafReaderContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Scorer scorer(LeafReaderContext context) throws IOException {
		LeafReader reader = context.reader();

		PointValues values = reader.getPointValues(field);
		if (values == null) {
			// No docs in this segment/field indexed any points
			return null;
		}

		DocIdSetBuilder result = new DocIdSetBuilder(reader.maxDoc(), values, field);
 
		values.intersect(new DistanceVisitor(query, distanceInfo, result));

		return new ConstantScoreScorer(this, score(), ScoreMode.TOP_SCORES, result.build().iterator());

	}

}
