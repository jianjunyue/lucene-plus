package com.function.vectors.cache;

public class KNNPair {

	private int uniqueId;
	private float[] vector;
	
	public KNNPair(	float[] vectors) {
		if(vectors.length>1) {
			uniqueId=(int)vectors[0];
			vector=vectors;
		}
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public float[] getVector() {
		return vector;
	}

	public void setVector(float[] vector) {
		this.vector = vector;
	}

}
