package org.lucene.plus.test.cache;

public class DocVectorEntry {

	private int docId;
	private float[] vector;
	
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public float[] getVector() {
		return vector;
	}
	public void setVector(float[] vector) {
		this.vector = vector;
	}

}
