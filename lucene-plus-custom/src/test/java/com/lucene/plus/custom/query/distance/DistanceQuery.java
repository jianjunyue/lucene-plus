package com.lucene.plus.custom.query.distance;

import java.io.IOException;

import com.lucene.geo.GeoEncodingUtils;
import com.lucene.geo.GeoUtils;
import com.lucene.geo.Rectangle;
import com.lucene.index.Term;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.Weight;
import com.lucene.util.NumericUtils;

public class DistanceQuery extends Query {
	String field = "";
	double latitude = 0;
	double longitude = 0;
	double radiusMeters = 0;

	public DistanceQuery(String field, double latitude, double longitude, double radiusMeters) {
		this.field = field;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radiusMeters = radiusMeters;
	} 

	public Weight createWeight(IndexSearcher searcher, boolean needsScores) throws IOException {
		return new DistanceWeight(this, getDistanceInfo(), 0f);
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

	private DistanceInfo getDistanceInfo() {
		Rectangle box = Rectangle.fromPointDistance(this.latitude, this.longitude, this.radiusMeters);
		final byte[] minLat = new byte[4];
		final byte[] maxLat = new byte[4];
		final byte[] minLon = new byte[4];
		final byte[] maxLon = new byte[4];
		final byte[] minLon2 = new byte[4];
		NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitude(box.minLat), minLat, 0);
		NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitude(box.maxLat), maxLat, 0);
		if (box.crossesDateline()) {
			NumericUtils.intToSortableBytes(-2147483648, minLon, 0);
			NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.maxLon), maxLon, 0);
			NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.minLon), minLon2, 0);
		} else {
			NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.minLon), minLon, 0);
			NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.maxLon), maxLon, 0);
			NumericUtils.intToSortableBytes(2147483647, minLon2, 0);
		}
		final double sortKey = GeoUtils.distanceQuerySortKey(this.radiusMeters);
		final double axisLat = Rectangle.axisLat(this.latitude, this.radiusMeters);
		return new DistanceInfo(field, latitude, longitude, radiusMeters, sortKey, axisLat, minLat, maxLat, minLon,
				maxLon, minLon2);
	}

}
