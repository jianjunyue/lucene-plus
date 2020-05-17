//package com.lucene.plus.custom.query.distance;
//
//import java.io.IOException;
//
//import com.lucene.geo.GeoEncodingUtils;
//import com.lucene.geo.GeoUtils;
//import com.lucene.geo.Rectangle;
//import com.lucene.index.Term;
//import com.lucene.search.IndexSearcher;
//import com.lucene.search.Query;
//import com.lucene.search.Weight;
//import com.lucene.util.NumericUtils;
//import java.io.IOException; 
//import com.lucene.index.LeafReader;
//import com.lucene.index.LeafReaderContext;
//import com.lucene.index.PointValues; 
//import com.lucene.search.ConstantScoreScorer;
//import com.lucene.search.ConstantScoreWeight; 
//import com.lucene.search.Query;
//import com.lucene.search.ScoreMode;
//import com.lucene.search.Scorer; 
//import com.lucene.util.DocIdSetBuilder; 
//
//import com.lucene.index.PrefixCodedTerms;
//import com.lucene.geo.GeoEncodingUtils;
//import com.lucene.index.PointValues;
//import com.lucene.index.PointValues.IntersectVisitor;
//import com.lucene.index.PointValues.Relation;
//import com.lucene.index.PrefixCodedTerms.TermIterator;
//import com.lucene.search.DocIdSetIterator;
//import com.lucene.search.Query;
//import com.lucene.util.BytesRef;
//import com.lucene.util.DocIdSetBuilder;
//import com.lucene.util.SloppyMath;
//import com.lucene.util.StringHelper;
//
//
//public class TestQuery extends Query {
//	 final String field;
//	  
//	  final double latitude;
//	  
//	  final double longitude;
//	  
//	  final double radiusMeters;
//	  
//	  public TestQuery(String field, double latitude, double longitude, double radiusMeters) {
//	    if (field == null)
//	      throw new IllegalArgumentException("field must not be null"); 
//	    if (!Double.isFinite(radiusMeters) || radiusMeters < 0.0D)
//	      throw new IllegalArgumentException("radiusMeters: '" + radiusMeters + "' is invalid"); 
//	    GeoUtils.checkLatitude(latitude);
//	    GeoUtils.checkLongitude(longitude);
//	    this.field = field;
//	    this.latitude = latitude;
//	    this.longitude = longitude;
//	    this.radiusMeters = radiusMeters;
//	  }
//	  
//	  public Weight createWeight(IndexSearcher searcher, boolean needsScores) throws IOException {
//	    Rectangle box = Rectangle.fromPointDistance(this.latitude, this.longitude, this.radiusMeters);
//	    final byte[] minLat = new byte[4];
//	    final byte[] maxLat = new byte[4];
//	    final byte[] minLon = new byte[4];
//	    final byte[] maxLon = new byte[4];
//	    final byte[] minLon2 = new byte[4];
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitude(box.minLat), minLat, 0);
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitude(box.maxLat), maxLat, 0);
//	    if (box.crossesDateline()) {
//	      NumericUtils.intToSortableBytes(-2147483648, minLon, 0);
//	      NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.maxLon), maxLon, 0);
//	      NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.minLon), minLon2, 0);
//	    } else {
//	      NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.minLon), minLon, 0);
//	      NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(box.maxLon), maxLon, 0);
//	      NumericUtils.intToSortableBytes(2147483647, minLon2, 0);
//	    } 
//	    final double sortKey = GeoUtils.distanceQuerySortKey(this.radiusMeters);
//	    final double axisLat = Rectangle.axisLat(this.latitude, this.radiusMeters);
//	    return (Weight)new ConstantScoreWeight(this) {
//	        public Scorer scorer(LeafReaderContext context) throws IOException {
//	          LeafReader reader = context.reader();
//	          PointValues values = reader.getPointValues();
//	          if (values == null)
//	            return null; 
//	          FieldInfo fieldInfo = reader.getFieldInfos().fieldInfo(DistanceQuery.this.field);
//	          if (fieldInfo == null)
//	            return null; 
//	          final DocIdSetBuilder result = new DocIdSetBuilder(reader.maxDoc(), values, DistanceQuery.this.field);
//	          values.intersect(DistanceQuery.this.field, new PointValues.IntersectVisitor() {
//	                DocIdSetBuilder.BulkAdder adder;
//	                
//	                public void grow(int count) {
//	                  this.adder = result.grow(count);
//	                }
//	                
//	                public void visit(int docID) {
//	                  this.adder.add(docID);
//	                }
//	                
//	                public void visit(int docID, byte[] packedValue) {
//	                  if (StringHelper.compare(4, packedValue, 0, maxLat, 0) > 0 || 
//	                    StringHelper.compare(4, packedValue, 0, minLat, 0) < 0)
//	                    return; 
//	                  if ((StringHelper.compare(4, packedValue, 4, maxLon, 0) > 0 || 
//	                    StringHelper.compare(4, packedValue, 4, minLon, 0) < 0) && 
//	                    StringHelper.compare(4, packedValue, 4, minLon2, 0) < 0)
//	                    return; 
//	                  double docLatitude = GeoEncodingUtils.decodeLatitude(packedValue, 0);
//	                  double docLongitude = GeoEncodingUtils.decodeLongitude(packedValue, 4);
//	                  if (SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, docLatitude, docLongitude) <= sortKey)
//	                    this.adder.add(docID); 
//	                }
//	                
//	                public PointValues.Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
//	                  if (StringHelper.compare(4, minPackedValue, 0, maxLat, 0) > 0 || 
//	                    StringHelper.compare(4, maxPackedValue, 0, minLat, 0) < 0)
//	                    return PointValues.Relation.CELL_OUTSIDE_QUERY; 
//	                  if ((StringHelper.compare(4, minPackedValue, 4, maxLon, 0) > 0 || 
//	                    StringHelper.compare(4, maxPackedValue, 4, minLon, 0) < 0) && 
//	                    StringHelper.compare(4, maxPackedValue, 4, minLon2, 0) < 0)
//	                    return PointValues.Relation.CELL_OUTSIDE_QUERY; 
//	                  double latMin = GeoEncodingUtils.decodeLatitude(minPackedValue, 0);
//	                  double lonMin = GeoEncodingUtils.decodeLongitude(minPackedValue, 4);
//	                  double latMax = GeoEncodingUtils.decodeLatitude(maxPackedValue, 0);
//	                  double lonMax = GeoEncodingUtils.decodeLongitude(maxPackedValue, 4);
//	                  if ((DistanceQuery.this.longitude < lonMin || DistanceQuery.this.longitude > lonMax) && (axisLat + 8.993203677616636E-7D < latMin || axisLat - 8.993203677616636E-7D > latMax))
//	                    if (SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMin, lonMin) > sortKey && 
//	                      SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMin, lonMax) > sortKey && 
//	                      SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMax, lonMin) > sortKey && 
//	                      SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMax, lonMax) > sortKey)
//	                      return PointValues.Relation.CELL_OUTSIDE_QUERY;  
//	                  if (lonMax - DistanceQuery.this.longitude < 90.0D && DistanceQuery.this.longitude - lonMin < 90.0D && 
//	                    SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMin, lonMin) <= sortKey && 
//	                    SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMin, lonMax) <= sortKey && 
//	                    SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMax, lonMin) <= sortKey && 
//	                    SloppyMath.haversinSortKey(DistanceQuery.this.latitude, DistanceQuery.this.longitude, latMax, lonMax) <= sortKey)
//	                    return PointValues.Relation.CELL_INSIDE_QUERY; 
//	                  return PointValues.Relation.CELL_CROSSES_QUERY;
//	                }
//	              });
//	          return (Scorer)new ConstantScoreScorer((Weight)this, score(), result.build().iterator());
//	        }
//	      };
//	  }
//	  
//	  public String getField() {
//	    return this.field;
//	  }
//	  
//	  public double getLatitude() {
//	    return this.latitude;
//	  }
//	  
//	  public double getLongitude() {
//	    return this.longitude;
//	  }
//	  
//	  public double getRadiusMeters() {
//	    return this.radiusMeters;
//	  }
//	  
//	  public int hashCode() {
//	    int prime = 31;
//	    int result = classHash();
//	    result = 31 * result + this.field.hashCode();
//	    long temp = Double.doubleToLongBits(this.latitude);
//	    result = 31 * result + (int)(temp ^ temp >>> 32L);
//	    temp = Double.doubleToLongBits(this.longitude);
//	    result = 31 * result + (int)(temp ^ temp >>> 32L);
//	    temp = Double.doubleToLongBits(this.radiusMeters);
//	    result = 31 * result + (int)(temp ^ temp >>> 32L);
//	    return result;
//	  }
//	  
//	  public boolean equals(Object other) {
//	    return (sameClassAs(other) && 
//	      equalsTo((DistanceQuery)getClass().cast(other)));
//	  }
//	  
//	  private boolean equalsTo(DistanceQuery other) {
//	    return (this.field.equals(other.field) && 
//	      Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(other.latitude) && 
//	      Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(other.longitude) && 
//	      Double.doubleToLongBits(this.radiusMeters) == Double.doubleToLongBits(other.radiusMeters));
//	  }
//	  
//	  public String toString(String field) {
//	    StringBuilder sb = new StringBuilder();
//	    if (!this.field.equals(field)) {
//	      sb.append(this.field);
//	      sb.append(':');
//	    } 
//	    return sb.append(this.latitude).append(',').append(this.longitude).append(" +/- ").append(this.radiusMeters).append(" meters").toString();
//	  }
//	
//}
