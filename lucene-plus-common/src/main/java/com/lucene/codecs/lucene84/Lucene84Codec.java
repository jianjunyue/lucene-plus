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
package com.lucene.codecs.lucene84;

import java.util.Objects;

import com.lucene.codecs.Codec;
import com.lucene.codecs.CompoundFormat;
import com.lucene.codecs.DocValuesFormat;
import com.lucene.codecs.FieldInfosFormat;
import com.lucene.codecs.FilterCodec;
import com.lucene.codecs.LiveDocsFormat;
import com.lucene.codecs.NormsFormat;
import com.lucene.codecs.PointsFormat;
import com.lucene.codecs.PostingsFormat;
import com.lucene.codecs.SegmentInfoFormat;
import com.lucene.codecs.StoredFieldsFormat;
import com.lucene.codecs.TermVectorsFormat;
import com.lucene.codecs.lucene50.Lucene50CompoundFormat;
import com.lucene.codecs.lucene50.Lucene50LiveDocsFormat;
import com.lucene.codecs.lucene50.Lucene50StoredFieldsFormat;
import com.lucene.codecs.lucene50.Lucene50StoredFieldsFormat.Mode;
import com.lucene.codecs.lucene50.Lucene50TermVectorsFormat;
import com.lucene.codecs.lucene60.Lucene60FieldInfosFormat;
import com.lucene.codecs.lucene60.Lucene60PointsFormat;
import com.lucene.codecs.lucene70.Lucene70SegmentInfoFormat;
import com.lucene.codecs.lucene80.Lucene80NormsFormat;
import com.lucene.codecs.perfield.PerFieldDocValuesFormat;
import com.lucene.codecs.perfield.PerFieldPostingsFormat;

/**
 * Implements the Lucene 8.4 index format, with configurable per-field postings
 * and docvalues formats.
 * <p>
 * If you want to reuse functionality of this codec in another codec, extend
 * {@link FilterCodec}.
 *
 * @see com.lucene.codecs.lucene84 package documentation for file format details.
 *
 * @lucene.experimental
 */
public class Lucene84Codec extends Codec {
  private final TermVectorsFormat vectorsFormat = new Lucene50TermVectorsFormat();
  private final FieldInfosFormat fieldInfosFormat = new Lucene60FieldInfosFormat();
  private final SegmentInfoFormat segmentInfosFormat = new Lucene70SegmentInfoFormat();
  private final LiveDocsFormat liveDocsFormat = new Lucene50LiveDocsFormat();
  private final CompoundFormat compoundFormat = new Lucene50CompoundFormat();
  private final PostingsFormat defaultFormat;
  
  private final PostingsFormat postingsFormat = new PerFieldPostingsFormat() {
    @Override
    public PostingsFormat getPostingsFormatForField(String field) {
      return Lucene84Codec.this.getPostingsFormatForField(field);
    }
  };
  
  private final DocValuesFormat docValuesFormat = new PerFieldDocValuesFormat() {
    @Override
    public DocValuesFormat getDocValuesFormatForField(String field) {
      return Lucene84Codec.this.getDocValuesFormatForField(field);
    }
  };
  
  private final StoredFieldsFormat storedFieldsFormat;

  /** 
   * Instantiates a new codec.
   */
  public Lucene84Codec() {
    this(Mode.BEST_SPEED);
  }
  
  /** 
   * Instantiates a new codec, specifying the stored fields compression
   * mode to use.
   * @param mode stored fields compression mode to use for newly
   *             flushed/merged segments.
   */
  public Lucene84Codec(Mode mode) {
    super("Lucene84");
    this.storedFieldsFormat = new Lucene50StoredFieldsFormat(Objects.requireNonNull(mode));
    this.defaultFormat = new Lucene84PostingsFormat();
  }
  
  @Override
  public final StoredFieldsFormat storedFieldsFormat() {
    return storedFieldsFormat;
  }
  
  @Override
  public final TermVectorsFormat termVectorsFormat() {
    return vectorsFormat;
  }

  @Override
  public final PostingsFormat postingsFormat() {
    return postingsFormat;
  }
  
  @Override
  public final FieldInfosFormat fieldInfosFormat() {
    return fieldInfosFormat;
  }
  
  @Override
  public final SegmentInfoFormat segmentInfoFormat() {
    return segmentInfosFormat;
  }
  
  @Override
  public final LiveDocsFormat liveDocsFormat() {
    return liveDocsFormat;
  }

  @Override
  public final CompoundFormat compoundFormat() {
    return compoundFormat;
  }

  @Override
  public final PointsFormat pointsFormat() {
    return new Lucene60PointsFormat();
  }

  /** Returns the postings format that should be used for writing 
   *  new segments of <code>field</code>.
   *  
   *  The default implementation always returns "Lucene84".
   *  <p>
   *  <b>WARNING:</b> if you subclass, you are responsible for index 
   *  backwards compatibility: future version of Lucene are only 
   *  guaranteed to be able to read the default implementation. 
   */
  public PostingsFormat getPostingsFormatForField(String field) {
    return defaultFormat;
  }
  
  /** Returns the docvalues format that should be used for writing 
   *  new segments of <code>field</code>.
   *  
   *  The default implementation always returns "Lucene80".
   *  <p>
   *  <b>WARNING:</b> if you subclass, you are responsible for index 
   *  backwards compatibility: future version of Lucene are only 
   *  guaranteed to be able to read the default implementation. 
   */
  public DocValuesFormat getDocValuesFormatForField(String field) {
    return defaultDVFormat;
  }
  
  @Override
  public final DocValuesFormat docValuesFormat() {
    return docValuesFormat;
  }

  private final DocValuesFormat defaultDVFormat = DocValuesFormat.forName("Lucene80");

  private final NormsFormat normsFormat = new Lucene80NormsFormat();

  @Override
  public final NormsFormat normsFormat() {
    return normsFormat;
  }
}
