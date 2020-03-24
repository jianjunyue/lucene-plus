package com.lucene.plus.custom.vectors;

public class Vectors {
	public float[] getValue() {
		return value;
	}

	public void setValue(float[] value) {
		this.value = value;
	}

	private float[] value;
	
	public Vectors(float[] value) {
		this.value= value;
	}

}
