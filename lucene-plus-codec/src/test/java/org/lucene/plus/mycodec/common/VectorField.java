package org.lucene.plus.mycodec.common;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.index.DocValuesType;
import com.lucene.util.BytesRef;

public class VectorField extends Field {

	public static final String KNN_FIELD = "knn_field";
	public static final FieldType TYPE = new FieldType();

	static {
		TYPE.setStored(true);
		TYPE.setDocValuesType(DocValuesType.SORTED);

//		TYPE.setDocValuesType(DocValuesType.BINARY);
		TYPE.putAttribute(KNN_FIELD, "true"); // This attribute helps to determine knn field type

		TYPE.freeze();
	}

	public VectorField(String name, byte[] value) {
		super(name, value, TYPE);
	}

	public VectorField(String name, BytesRef value) {
		super(name, value, TYPE);
	}

	public VectorField(String name, float[] vectors) {
		super(name, TYPE);
		byte[] bytes = floatToByte(vectors);
		this.fieldsData = new BytesRef(bytes);
	}

	public byte[] floatToByte(float[] floats) {
		byte[] bytes;
		try {
			try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);) {
				objectStream.writeObject(floats);
				bytes = byteStream.toByteArray();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bytes;
	}

}