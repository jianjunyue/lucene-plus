package com.function.vectors.Field;

import com.lucene.index.IndexOptions;
import com.function.util.FieldUtils;
import com.function.vectors.Vectors;
import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.index.DocValuesType;
import com.lucene.plus.custom.util.BinaryBytesUtils; 
import com.lucene.util.BytesRef;
import com.lucene.util.BytesRefBuilder; 

public class VectorsStoredField extends Field {

	public static final FieldType TYPE = new FieldType();

	public static final String KNN_FIELD = FieldUtils.KNN_FIELD;
	static {
		TYPE.setTokenized(false);
		TYPE.setIndexOptions(IndexOptions.NONE);
		TYPE.setDocValuesType(DocValuesType.BINARY); 
		TYPE.putAttribute(KNN_FIELD, "true"); // This attribute helps to determine knn field type
		TYPE.freeze();
	}

	public VectorsStoredField(String name, byte[] value) {
		super(name, value, TYPE);
	}

	public VectorsStoredField(String name, BytesRef value) {
		super(name, value, TYPE);
	}

	public VectorsStoredField(String name, Vectors vectors) {
		super(name, TYPE);
		BytesRefBuilder builder = new BytesRefBuilder();
		for (float vector : vectors.getValue()) {
			BinaryBytesUtils.floatToBytes(vector, builder);
		}
		this.fieldsData = builder.toBytesRef();
	}

	

}
