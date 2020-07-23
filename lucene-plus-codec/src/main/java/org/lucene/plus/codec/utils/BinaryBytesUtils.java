package org.lucene.plus.codec.utils;

import java.util.Arrays;
import com.lucene.util.BytesRef;
import com.lucene.util.BytesRefBuilder;

public class BinaryBytesUtils {
	
	public static float[] bytesToFloats(byte[] bytes) {
		BytesRef ref=new BytesRef(bytes);
		return bytesToFloats(ref); 
	}

	public static float[] bytesToFloats(BytesRef ref) {
		int lenVec = ref.length / 4;
		float[] floatVectors = new float[lenVec];
		for (int i = 0; i < lenVec; i++) {
			byte[] lonBytes = Arrays.copyOfRange(ref.bytes, i * 4, (i + 1) * 4);
			floatVectors[i] = bytesToFloat(lonBytes);
		}
		return floatVectors;
	}

	public static float bytesToFloat(byte[] bytes) {
		int bits = bytesToInt(bytes, 0);
		float f = Float.intBitsToFloat(bits);
		return f;
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
		intToBytes(value,result);
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
		int x = (encoded[offset] & 0xFF) << 24 | (encoded[offset + 1] & 0xFF) << 16 | (encoded[offset + 2] & 0xFF) << 8
				| encoded[offset + 3] & 0xFF;
		return x ^ Integer.MIN_VALUE;
	}

}
