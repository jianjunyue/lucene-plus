package com.lucene.plus.custom.vectors.Field;

import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.index.DocValuesType;
import com.lucene.plus.custom.util.BinaryBytesUtils;
import com.lucene.plus.custom.vectors.Vectors;
import com.lucene.util.BytesRef;
import com.lucene.util.BytesRefBuilder;

public class VectorsStoredField extends Field {

	public static final FieldType TYPE = new FieldType();

	static {
		TYPE.setStored(true);
		TYPE.setDocValuesType(DocValuesType.SORTED);
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
