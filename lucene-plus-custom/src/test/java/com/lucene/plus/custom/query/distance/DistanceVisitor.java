package com.lucene.plus.custom.query.distance;

import java.io.IOException;

import com.lucene.index.PrefixCodedTerms;
import com.lucene.geo.GeoEncodingUtils;
import com.lucene.index.PointValues;
import com.lucene.index.PointValues.IntersectVisitor;
import com.lucene.index.PointValues.Relation;
import com.lucene.index.PrefixCodedTerms.TermIterator;
import com.lucene.search.DocIdSetIterator;
import com.lucene.search.Query;
import com.lucene.util.BytesRef;
import com.lucene.util.DocIdSetBuilder;
import com.lucene.util.SloppyMath;
import com.lucene.util.StringHelper;

public class DistanceVisitor implements IntersectVisitor {

	private DocIdSetBuilder.BulkAdder adder;
	private DocIdSetBuilder result;
	private Query query;
	private String field;
	private double latitude;
	private double longitude;
	private double radiusMeters;
	private double sortKey;
	private double axisLat;

	private byte[] minLat = new byte[4];
	private byte[] maxLat = new byte[4];
	private byte[] minLon = new byte[4];
	private byte[] maxLon = new byte[4];
	private byte[] minLon2 = new byte[4];

	public DistanceVisitor(Query query, DistanceInfo distanceInfo, DocIdSetBuilder result) throws IOException {
		this.result = result;
		this.query = query;
		this.latitude = distanceInfo.getLatitude();
		this.longitude = distanceInfo.getLongitude();
		this.radiusMeters = distanceInfo.getRadiusMeters();
		this.sortKey = distanceInfo.getSortKey();
		this.axisLat = distanceInfo.getAxisLat();

		this.minLat = distanceInfo.getMinLat();
		this.maxLat = distanceInfo.getMaxLat();
		this.minLon = distanceInfo.getMinLon();
		this.maxLon = distanceInfo.getMaxLon();
		this.minLon2 = distanceInfo.getMinLon2();
	}

	public void grow(int count) {
		this.adder = result.grow(count);
	}

	public void visit(int docID) {
		this.adder.add(docID);
	}

	public void visit(int docID, byte[] packedValue) {
		if (StringHelper.compare(4, packedValue, 0, maxLat, 0) > 0
				|| StringHelper.compare(4, packedValue, 0, minLat, 0) < 0)
			return;
		if ((StringHelper.compare(4, packedValue, 4, maxLon, 0) > 0
				|| StringHelper.compare(4, packedValue, 4, minLon, 0) < 0)
				&& StringHelper.compare(4, packedValue, 4, minLon2, 0) < 0)
			return;
		double docLatitude = GeoEncodingUtils.decodeLatitude(packedValue, 0);
		double docLongitude = GeoEncodingUtils.decodeLongitude(packedValue, 4);
		if (SloppyMath.haversinSortKey(latitude, longitude, docLatitude, docLongitude) <= sortKey)
			this.adder.add(docID);
	}

	public PointValues.Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
		if (StringHelper.compare(4, minPackedValue, 0, maxLat, 0) > 0
				|| StringHelper.compare(4, maxPackedValue, 0, minLat, 0) < 0)
			return PointValues.Relation.CELL_OUTSIDE_QUERY;
		if ((StringHelper.compare(4, minPackedValue, 4, maxLon, 0) > 0
				|| StringHelper.compare(4, maxPackedValue, 4, minLon, 0) < 0)
				&& StringHelper.compare(4, maxPackedValue, 4, minLon2, 0) < 0)
			return PointValues.Relation.CELL_OUTSIDE_QUERY;
		double latMin = GeoEncodingUtils.decodeLatitude(minPackedValue, 0);
		double lonMin = GeoEncodingUtils.decodeLongitude(minPackedValue, 4);
		double latMax = GeoEncodingUtils.decodeLatitude(maxPackedValue, 0);
		double lonMax = GeoEncodingUtils.decodeLongitude(maxPackedValue, 4);
		if ((longitude < lonMin || longitude > lonMax)
				&& (axisLat + 8.993203677616636E-7D < latMin || axisLat - 8.993203677616636E-7D > latMax))
			if (SloppyMath.haversinSortKey(latitude, longitude, latMin, lonMin) > sortKey
					&& SloppyMath.haversinSortKey(latitude, longitude, latMin, lonMax) > sortKey
					&& SloppyMath.haversinSortKey(latitude, longitude, latMax, lonMin) > sortKey
					&& SloppyMath.haversinSortKey(latitude, longitude, latMax, lonMax) > sortKey)
				return PointValues.Relation.CELL_OUTSIDE_QUERY;
		if (lonMax - longitude < 90.0D && longitude - lonMin < 90.0D
				&& SloppyMath.haversinSortKey(latitude, longitude, latMin, lonMin) <= sortKey
				&& SloppyMath.haversinSortKey(latitude, longitude, latMin, lonMax) <= sortKey
				&& SloppyMath.haversinSortKey(latitude, longitude, latMax, lonMin) <= sortKey
				&& SloppyMath.haversinSortKey(latitude, longitude, latMax, lonMax) <= sortKey)
			return PointValues.Relation.CELL_INSIDE_QUERY;
		return PointValues.Relation.CELL_CROSSES_QUERY;
	}

}
