package com.lucene.plus.custom.codec.knn;

import java.io.Closeable;
import java.io.IOException;

import com.lucene.index.DocValuesType;

import com.lucene.plus.custom.codec.knn.util.KNNCodecUtil;
import com.lucene.plus.custom.codec.knn.util.FieldUtils;
import com.lucene.plus.custom.codec.knn.vectors.cache.FieldDocValuesCache;
import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.BinaryDocValues;
import com.lucene.index.FieldInfo;
import com.lucene.index.MergeState;
import com.lucene.index.SegmentWriteState;
import com.lucene.index.SortedDocValues;
import com.lucene.plus.custom.util.BinaryBytesUtils;

public class KNN84DocValuesConsumer extends DocValuesConsumer implements Closeable {

	private DocValuesConsumer delegatee;
	private SegmentWriteState state;

	KNN84DocValuesConsumer(DocValuesConsumer delegatee, SegmentWriteState state) throws IOException {
		this.delegatee = delegatee;
		this.state = state;
	}

	@Override
	public void addBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addBinaryField(field, valuesProducer); // 执行 Lucene80DocValuesConsumer的addBinaryField方法

		if (field.attributes().containsKey(FieldUtils.KNN_FIELD)) {
			addKNNBinaryField(field, valuesProducer);
		}
	}

	public void addKNNBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		// 增加缓存
		BinaryDocValues docValues = valuesProducer.getBinary(field);

		KNNCodecUtil.Pair pair = KNNCodecUtil.getFloats(docValues);
		FieldDocValuesCache.add(field.name, pair);
	}

	/**
	 * Merges in the fields from the readers in mergeState
	 *
	 * @param mergeState Holds common state used during segment merging
	 */
	@Override
	public void merge(MergeState mergeState) throws IOException {
		delegatee.merge(mergeState);
	}

	@Override
	public void addSortedSetField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedSetField(field, valuesProducer);
	}

	@Override
	public void addSortedNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedNumericField(field, valuesProducer);
	}

	@Override
	public void addSortedField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedField(field, valuesProducer);
	}

	@Override
	public void addNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addNumericField(field, valuesProducer);
	}

	@Override
	public void close() throws IOException {
		delegatee.close();
	}

}
