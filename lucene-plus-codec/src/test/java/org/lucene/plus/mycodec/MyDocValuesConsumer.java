package org.lucene.plus.mycodec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.lucene.search.DocIdSetIterator;
import com.lucene.util.BytesRef;
//import org.lucene.plus.knn.index.codec.KNNCodecUtil;

import com.lucene.index.BinaryDocValues;

import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.FieldInfo;
import com.lucene.index.IndexFileNames;
import com.lucene.index.SegmentWriteState;
import com.lucene.store.IndexOutput;

/**
 * 扩展 DocValuesConsumer 实现数据写入文件（比如通过 addBinaryField() 写 BINARY 类型字段数据）
 * */
public class MyDocValuesConsumer extends DocValuesConsumer {

	IndexOutput data;

    private final String TEMP_SUFFIX = "tmp";
	public MyDocValuesConsumer(SegmentWriteState state, String ext) throws IOException {
		System.out.println("WRITE: " + IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext)
				+ " " + state.segmentInfo.maxDoc() + " docs");
		data = state.directory.createOutput(
				IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext), state.context);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		System.out.println("addBinaryField");
		BinaryDocValues values = valuesProducer.getBinary(field);
		values.binaryValue();
	}
	
	 

	@Override
	public void addSortedField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSortedNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSortedSetField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// TODO Auto-generated method stub

	}

}
