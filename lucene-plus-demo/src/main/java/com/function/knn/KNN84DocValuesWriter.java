package com.function.knn;

import java.io.IOException;

import com.function.util.FieldUtils;
import com.function.util.KNNCodecUtil;
import com.function.vectors.cache.FieldDocValuesCache;
import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.BinaryDocValues;
import com.lucene.index.FieldInfo;
import com.lucene.index.IndexFileNames;
import com.lucene.index.SegmentWriteState;
import com.lucene.store.IndexOutput;

public class KNN84DocValuesWriter extends DocValuesConsumer {

	IndexOutput data;

	public KNN84DocValuesWriter(SegmentWriteState state, String ext) throws IOException {
		System.out.println("WRITE: " + IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext)
				+ " " + state.segmentInfo.maxDoc() + " docs");
		data = state.directory.createOutput(
				IndexFileNames.segmentFileName(state.segmentInfo.name, state.segmentSuffix, ext), state.context);
		int maxDoc = state.segmentInfo.maxDoc();
		System.out.println("maxDoc：" + maxDoc);
		System.out.println("-----------------------------------------------");

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
//		delegatee.addBinaryField(field, valuesProducer); // 执行 Lucene80DocValuesConsumer的addBinaryField方法

		if (field.attributes().containsKey(FieldUtils.KNN_FIELD)) {
			addKNNBinaryField(field, valuesProducer);
		}
	}

	public void addKNNBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// 增加缓存
		BinaryDocValues docValues = valuesProducer.getBinary(field);
		KNNCodecUtil.Pair pair = KNNCodecUtil.getFloats(docValues);
		String str = "";
		for (int i = 0; i < pair.docs.length; i++) {
			str += pair.docs[i] + " , ";
		}
		System.out.println("pair.docs：" + str);
		System.out.println("-----------------------------------------------");
		FieldDocValuesCache.add(field.name, pair);
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
