/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.lucene.plus.test.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;

import java.io.IOException;

/**
 * Encodes/Decodes per document values
 */
public class TestKNN80DocValuesFormat extends DocValuesFormat {
	private final Logger logger = LogManager.getLogger(TestKNN80DocValuesFormat.class);
	public static final String KNN_80 = "TestKNNCodec";
	public static final String LUCENE_80 = "Lucene80";
	private final DocValuesFormat delegate = DocValuesFormat.forName(LUCENE_80);

	public TestKNN80DocValuesFormat() {
        super(LUCENE_80);
//		super(KNN_80);
	}

	@Override
	public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
		return new TestKNN80DocValuesConsumer(delegate.fieldsConsumer(state), state);
	}

	@Override
	public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
		return delegate.fieldsProducer(state);
	}
}
