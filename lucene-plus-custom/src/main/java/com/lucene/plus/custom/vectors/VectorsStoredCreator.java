package com.lucene.plus.custom.vectors;

import java.util.Arrays;

import com.lucene.plus.custom.vectors.Field.VectorsStoredField;
import com.lucene.util.BytesRef;
import com.lucene.util.BytesRefBuilder;

public class VectorsStoredCreator {

	public static VectorsStoredField createVectorsFiled(String filedName, Vectors vectors) {
		BytesRefBuilder builder = new BytesRefBuilder();
		for (float vector : vectors.getValue()) {
			VectorsStoredField.floatToBytes(vector, builder);
		}
//		return new VectorsStoredField(filedName, builder.bytes()); 
		return new VectorsStoredField(filedName, builder.toBytesRef());
	}

	public static float[] geVectorsFromVectorsStoredField(BytesRef ref) {
		int lenVec = ref.length / 4;
		float[] floatVectors = new float[lenVec];
		for (int i = 0; i < lenVec; i++) {
			byte[] lonBytes = Arrays.copyOfRange(ref.bytes, i * 4,(i+1)*4);
			int bits = VectorsStoredField.bytesToInt(lonBytes, 0);
			floatVectors[i] = Float.intBitsToFloat(bits);
		}
		return floatVectors;
	}
}
