/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lucene.document;

import java.io.IOException;
import java.util.Arrays;

import com.lucene.geo.Component2D;
import com.lucene.geo.GeoEncodingUtils;
import com.lucene.geo.Polygon;
import com.lucene.geo.Polygon2D;
import com.lucene.index.DocValues;
import com.lucene.index.LeafReaderContext;
import com.lucene.index.SortedNumericDocValues;
import com.lucene.search.ConstantScoreScorer;
import com.lucene.search.ConstantScoreWeight;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.QueryVisitor;
import com.lucene.search.ScoreMode;
import com.lucene.search.Scorer;
import com.lucene.search.TwoPhaseIterator;
import com.lucene.search.Weight;

/** Polygon query for {@link LatLonDocValuesField}. */
public class LatLonDocValuesPointInPolygonQuery extends Query {

  private final String field;
  private final Polygon[] polygons;


  LatLonDocValuesPointInPolygonQuery(String field, Polygon... polygons) {
    if (field == null) {
      throw new IllegalArgumentException("field must not be null");
    }
    if (polygons == null) {
      throw new IllegalArgumentException("polygons must not be null");
    }
    if (polygons.length == 0) {
      throw new IllegalArgumentException("polygons must not be empty");
    }
    for (int i = 0; i < polygons.length; i++) {
      if (polygons[i] == null) {
        throw new IllegalArgumentException("polygon[" + i + "] must not be null");
      }
    }
    this.field = field;
    this.polygons = polygons;
  }

  @Override
  public String toString(String field) {
    StringBuilder sb = new StringBuilder();
    if (!this.field.equals(field)) {
      sb.append(this.field);
      sb.append(':');
    }
    sb.append("polygons(").append(Arrays.toString(polygons));
    return sb.append(")").toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (sameClassAs(obj) == false) {
      return false;
    }
    LatLonDocValuesPointInPolygonQuery other = (LatLonDocValuesPointInPolygonQuery) obj;
    return field.equals(other.field) &&
           Arrays.equals(polygons, other.polygons);
  }

  @Override
  public int hashCode() {
    int h = classHash();
    h = 31 * h + field.hashCode();
    h = 31 * h + Arrays.hashCode(polygons);
    return h;
  }

  @Override
  public void visit(QueryVisitor visitor) {
    if (visitor.acceptField(field)) {
      visitor.visitLeaf(this);
    }
  }

  @Override
  public Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {

    return new ConstantScoreWeight(this, boost) {

      final Component2D tree = Polygon2D.create(polygons);
      final GeoEncodingUtils.PolygonPredicate polygonPredicate = GeoEncodingUtils.createComponentPredicate(tree);

      @Override
      public Scorer scorer(LeafReaderContext context) throws IOException {
        final SortedNumericDocValues values = context.reader().getSortedNumericDocValues(field);
        if (values == null) {
          return null;
        }

        final TwoPhaseIterator iterator = new TwoPhaseIterator(values) {

          @Override
          public boolean matches() throws IOException {
            for (int i = 0, count = values.docValueCount(); i < count; ++i) {
              final long value = values.nextValue();
              final int lat = (int) (value >>> 32);
              final int lon = (int) (value & 0xFFFFFFFF);
              if (polygonPredicate.test(lat, lon)) {
                return true;
              }
            }
            return false;
          }

          @Override
          public float matchCost() {
            return 1000f; // TODO: what should it be?
          }
        };
        return new ConstantScoreScorer(this, boost, scoreMode, iterator);
      }

      @Override
      public boolean isCacheable(LeafReaderContext ctx) {
        return DocValues.isCacheable(ctx, field);
      }

    };
  }
}
