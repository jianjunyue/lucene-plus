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

package com.tuhuknn.index.Field;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.lucene.util.BytesRef;

import com.tuhuknn.index.util.BinaryBytesUtils;
import com.tuhuknn.index.util.FieldUtils;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class TuhuKNNField extends Field {

	public static final FieldType TYPE = new FieldType();
	public static final String KNN_FIELD = FieldUtils.KNN_FIELD;

	static {
//		TYPE.setStored(true);
//		TYPE.setDocValuesType(DocValuesType.SORTED);
//		TYPE.putAttribute(KNN_FIELD, "true"); //This attribute helps to determine knn field type
//		TYPE.freeze();

		TYPE.setTokenized(false);
		TYPE.setIndexOptions(IndexOptions.NONE);
		TYPE.setDocValuesType(DocValuesType.BINARY);
//		TYPE.setDocValuesType(DocValuesType.SORTED);
		TYPE.putAttribute(KNN_FIELD, "true"); // This attribute helps to determine knn field type
		TYPE.freeze();
	}
  
	public TuhuKNNField(String name, float[] value) {
		super(name, new BytesRef(), TYPE);
		try {
			BytesRef ref = BinaryBytesUtils.floatToBytes(value);
			this.setBytesValue(ref.bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
