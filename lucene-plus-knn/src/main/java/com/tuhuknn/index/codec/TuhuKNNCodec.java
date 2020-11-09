package com.tuhuknn.index.codec;

import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.FilterCodec;
import org.apache.lucene.codecs.lucene84.Lucene84Codec;
 
public class TuhuKNNCodec extends FilterCodec {

	private final TuhuKNN84DocValuesFormat tuhuKNNDocValuesFormat = new TuhuKNN84DocValuesFormat();

	public TuhuKNNCodec() {
		super("TuhuKNNCodec", new Lucene84Codec()); 
	}

	@Override
	public DocValuesFormat docValuesFormat() {
		return tuhuKNNDocValuesFormat;
	}

}