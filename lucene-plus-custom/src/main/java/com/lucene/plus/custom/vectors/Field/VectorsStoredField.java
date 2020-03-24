package com.lucene.plus.custom.vectors.Field;

import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.util.BytesRefBuilder;

public class VectorsStoredField extends Field {

	public static final FieldType TYPE = new FieldType();

	static {
		TYPE.setStored(true);
		TYPE.freeze();
	}

	public VectorsStoredField(String name, byte[] value) {
		super(name, value, TYPE);
	}

	public static void intToBytes(int value, BytesRefBuilder result) {
		value ^= Integer.MIN_VALUE;
		result.append((byte) (value >> 24));
		result.append((byte) (value >> 16));
		result.append((byte) (value >> 8));
		result.append((byte) value);
	}

	public static void floatToBytes(float floatValue, BytesRefBuilder result) {
		int value = Float.floatToIntBits(floatValue);
		value ^= Integer.MIN_VALUE;
		result.append((byte) (value >> 24));
		result.append((byte) (value >> 16));
		result.append((byte) (value >> 8));
		result.append((byte) value);
	}

	public static void doubleToBytes(double doubleValue, BytesRefBuilder result) {
		long value = Double.doubleToLongBits(doubleValue);
		value ^= Integer.MIN_VALUE;
		result.append((byte) (value >> 24));
		result.append((byte) (value >> 16));
		result.append((byte) (value >> 8));
		result.append((byte) value);
	}

	  public static int bytesToInt(byte[] encoded, int offset) {
		    int x = (encoded[offset] & 0xFF) << 24 | (encoded[offset + 1] & 0xFF) << 16 | (encoded[offset + 2] & 0xFF) << 8 | encoded[offset + 3] & 0xFF;
		    return x ^ Integer.MIN_VALUE;
		  }
}
