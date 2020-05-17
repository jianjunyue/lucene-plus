package com.lucene.plus.custom.temp;


import java.util.ArrayList;
import java.util.List;

import com.lucene.codecs.lucene60.Lucene60PointsReader;
import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.geo.GeoEncodingUtils;
import com.lucene.geo.GeoUtils;
import com.lucene.geo.Polygon;
import com.lucene.index.DocValuesType;
import com.lucene.index.FieldInfo;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.PointValues;
import com.lucene.plus.custom.util.BinaryBytesUtils;
import com.lucene.plus.custom.vectors.Vectors;
import com.lucene.search.BooleanQuery;
import com.lucene.search.IndexSearcher;
import com.lucene.search.MatchNoDocsQuery;
import com.lucene.search.PointRangeQuery;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.TopFieldDocs;
import com.lucene.util.Bits;
import com.lucene.util.BytesRef;
import com.lucene.util.BytesRefBuilder;
import com.lucene.util.NumericUtils;
import com.lucene.util.bkd.BKDReader;


public class LatLonPointField  extends Field {
	  public static final FieldType TYPE = new FieldType();
	  
	  static {
	    TYPE.setDimensions(2, 4);
	    TYPE.freeze();
	  }
	  
	  public void setLocationValue(double latitude, double longitude) {
	    byte[] bytes;
	    if (this.fieldsData == null) {
	      bytes = new byte[8];
	      this.fieldsData = new BytesRef(bytes);
	    } else {
	      bytes = ((BytesRef)this.fieldsData).bytes;
	    } 
	    int latitudeEncoded = GeoEncodingUtils.encodeLatitude(latitude);
	    int longitudeEncoded = GeoEncodingUtils.encodeLongitude(longitude);
	    NumericUtils.intToSortableBytes(latitudeEncoded, bytes, 0);
	    NumericUtils.intToSortableBytes(longitudeEncoded, bytes, 4);
	  }
	  
	  public LatLonPointField(String name, double latitude, double longitude) {
	    super(name, TYPE);
	    setLocationValue(latitude, longitude);
	  }
	  
	  public String toString() {
	    StringBuilder result = new StringBuilder();
//	    result.append(getClass().getSimpleName());
//	    result.append(" <");
//	    result.append(this.name);
//	    result.append(':');
//	    byte[] bytes = ((BytesRef)this.fieldsData).bytes;
//	    result.append(GeoEncodingUtils.decodeLatitude(bytes, 0));
//	    result.append(',');
//	    result.append(GeoEncodingUtils.decodeLongitude(bytes, 4));
//	    result.append('>');
	    return result.toString();
	  }
	  
//	  private static byte[] encode(double latitude, double longitude) {
//	    byte[] bytes = new byte[8];
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitude(latitude), bytes, 0);
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitude(longitude), bytes, 4);
//	    return bytes;
//	  }
//	  
//	  private static byte[] encodeCeil(double latitude, double longitude) {
//	    byte[] bytes = new byte[8];
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLatitudeCeil(latitude), bytes, 0);
//	    NumericUtils.intToSortableBytes(GeoEncodingUtils.encodeLongitudeCeil(longitude), bytes, 4);
//	    return bytes;
//	  }
	  
	  public static void checkCompatible(FieldInfo fieldInfo) {
//	    if (fieldInfo.getPointDataDimensionCount() != 0 && fieldInfo.getPointDataDimensionCount() != TYPE.pointDataDimensionCount())
//	      throw new IllegalArgumentException("field=\"" + fieldInfo.name + "\" was indexed with numDims=" + fieldInfo.getPointDataDimensionCount() + " but this point type has numDims=" + TYPE
//	          .pointDataDimensionCount() + ", is the field really a LatLonPoint?"); 
//	    if (fieldInfo.getPointNumBytes() != 0 && fieldInfo.getPointNumBytes() != TYPE.pointNumBytes())
//	      throw new IllegalArgumentException("field=\"" + fieldInfo.name + "\" was indexed with bytesPerDim=" + fieldInfo.getPointNumBytes() + " but this point type has bytesPerDim=" + TYPE
//	          .pointNumBytes() + ", is the field really a LatLonPoint?"); 
	  }
	  
//	  public static Query newBoxQuery(String field, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
//	    if (minLatitude == 90.0D)
//	      return (Query)new MatchNoDocsQuery("LatLonPoint.newBoxQuery with minLatitude=90.0"); 
//	    if (minLongitude == 180.0D) {
//	      if (maxLongitude == 180.0D)
//	        return (Query)new MatchNoDocsQuery("LatLonPoint.newBoxQuery with minLongitude=maxLongitude=180.0"); 
//	      if (maxLongitude < minLongitude)
//	        minLongitude = -180.0D; 
//	    } 
//	    byte[] lower = encodeCeil(minLatitude, minLongitude);
//	    byte[] upper = encode(maxLatitude, maxLongitude);
//	    if (maxLongitude < minLongitude) {
//	      BooleanQuery.Builder q = new BooleanQuery.Builder();
//	      q.setDisableCoord(true);
//	      byte[] leftOpen = (byte[])lower.clone();
//	      NumericUtils.intToSortableBytes(-2147483648, leftOpen, 4);
//	      Query left = newBoxInternal(field, leftOpen, upper);
//	      q.add(new BooleanClause(left, BooleanClause.Occur.SHOULD));
//	      byte[] rightOpen = (byte[])upper.clone();
//	      NumericUtils.intToSortableBytes(2147483647, rightOpen, 4);
//	      Query right = newBoxInternal(field, lower, rightOpen);
//	      q.add(new BooleanClause(right, BooleanClause.Occur.SHOULD));
//	      return (Query)new ConstantScoreQuery((Query)q.build());
//	    } 
//	    return newBoxInternal(field, lower, upper);
//	  }
	  
//	  private static Query newBoxInternal(String field, byte[] min, byte[] max) {
//	    return (Query)new PointRangeQuery(field, min, max, 2) {
//	        protected String toString(int dimension, byte[] value) {
//	          if (dimension == 0)
//	            return Double.toString(GeoEncodingUtils.decodeLatitude(value, 0)); 
//	          if (dimension == 1)
//	            return Double.toString(GeoEncodingUtils.decodeLongitude(value, 0)); 
//	          throw new AssertionError();
//	        }
//	      };
//	  }
//	  
//	  public static Query newDistanceQuery(String field, double latitude, double longitude, double radiusMeters) {
//	    return (Query)new LatLonPointDistanceQuery(field, latitude, longitude, radiusMeters);
//	  }
	  
//	  public static Query newPolygonQuery(String field, Polygon... polygons) {
//	    return (Query)new LatLonPointInPolygonQuery(field, polygons);
//	  }
	  
//	  public static TopFieldDocs nearest(IndexSearcher searcher, String field, double latitude, double longitude, int n) throws IOException {
//	    GeoUtils.checkLatitude(latitude);
//	    GeoUtils.checkLongitude(longitude);
//	    if (n < 1)
//	      throw new IllegalArgumentException("n must be at least 1; got " + n); 
//	    if (field == null)
//	      throw new IllegalArgumentException("field must not be null"); 
//	    if (searcher == null)
//	      throw new IllegalArgumentException("searcher must not be null"); 
//	    List<BKDReader> readers = new ArrayList<>();
//	    List<Integer> docBases = new ArrayList<>();
//	    List<Bits> liveDocs = new ArrayList<>();
//	    int totalHits = 0;
//	    for (LeafReaderContext leaf : searcher.getIndexReader().leaves()) {
//	      PointValues points = leaf.reader().getPointValues(field);
//	      if (points != null) {
//	        if (!(points instanceof Lucene60PointsReader))
//	          throw new IllegalArgumentException("can only run on Lucene60PointsReader points implementation, but got " + points); 
//	        totalHits += points.getDocCount(searcher.getIndexReader(),field);
//	        BKDReader reader = ((Lucene60PointsReader)points).getBKDReader(field);
//	        if (reader != null) {
//	          readers.add(reader);
//	          docBases.add(Integer.valueOf(leaf.docBase));
//	          liveDocs.add(leaf.reader().getLiveDocs());
//	        } 
//	      } 
//	    } 
//	    NearestNeighbor.NearestHit[] hits = NearestNeighbor.nearest(latitude, longitude, readers, liveDocs, docBases, n);
//	    ScoreDoc[] scoreDocs = new ScoreDoc[hits.length];
//	    for (int i = 0; i < hits.length; i++) {
//	      NearestNeighbor.NearestHit hit = hits[i];
//	      scoreDocs[i] = (ScoreDoc)new FieldDoc(hit.docID, 0.0F, new Object[] { Double.valueOf(hit.distanceMeters) });
//	    } 
//	    return new TopFieldDocs(totalHits, scoreDocs, null, 0.0F);
//	  }
	}
