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

/** 
 * The logical representation of a {@link com.lucene.document.Document} for indexing and searching.
 * <p>The document package provides the user level logical representation of content to be indexed and searched.  The
 * package also provides utilities for working with {@link com.lucene.document.Document}s and {@link com.lucene.index.IndexableField}s.</p>
 * <h2>Document and IndexableField</h2>
 * <p>A {@link com.lucene.document.Document} is a collection of {@link com.lucene.index.IndexableField}s.  A
 *   {@link com.lucene.index.IndexableField} is a logical representation of a user's content that needs to be indexed or stored.
 *   {@link com.lucene.index.IndexableField}s have a number of properties that tell Lucene how to treat the content (like indexed, tokenized,
 *   stored, etc.)  See the {@link com.lucene.document.Field} implementation of {@link com.lucene.index.IndexableField}
 *   for specifics on these properties.
 * </p>
 * <p>Note: it is common to refer to {@link com.lucene.document.Document}s having {@link com.lucene.document.Field}s, even though technically they have
 * {@link com.lucene.index.IndexableField}s.</p>
 * <h2>Working with Documents</h2>
 * <p>First and foremost, a {@link com.lucene.document.Document} is something created by the user application.  It is your job
 *   to create Documents based on the content of the files you are working with in your application (Word, txt, PDF, Excel or any other format.)
 *   How this is done is completely up to you.  That being said, there are many tools available in other projects that can make
 *   the process of taking a file and converting it into a Lucene {@link com.lucene.document.Document}.
 * </p>
 * <p>The {@link com.lucene.document.DateTools} is a utility class to make dates and times searchable. {@link
 * com.lucene.document.IntPoint}, {@link com.lucene.document.LongPoint},
 * {@link com.lucene.document.FloatPoint} and {@link com.lucene.document.DoublePoint} enable indexing
 * of numeric values (and also dates) for fast range queries using {@link com.lucene.search.PointRangeQuery}</p>
 */
package com.lucene.document;
