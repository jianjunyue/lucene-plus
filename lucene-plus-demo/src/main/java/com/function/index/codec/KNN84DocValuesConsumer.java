package com.function.index.codec;

import java.io.Closeable;
import java.io.IOException;

import com.lucene.index.DocValuesType;

import com.function.util.KNNCodecUtil;
import com.function.util.FieldUtils;
import com.function.vectors.cache.FieldDocValuesCache;
import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.BinaryDocValues;
import com.lucene.index.FieldInfo;
import com.lucene.index.MergeState;
import com.lucene.index.SegmentWriteState;
import com.lucene.plus.custom.util.BinaryBytesUtils;

public class KNN84DocValuesConsumer extends DocValuesConsumer implements Closeable {

	private final String TEMP_SUFFIX = "tmp";
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
	public void merge(MergeState mergeState) {
		try {
			delegatee.merge(mergeState);
//			assert mergeState != null;
//			assert mergeState.mergeFieldInfos != null;
//			for (FieldInfo fieldInfo : mergeState.mergeFieldInfos) {
//				DocValuesType type = fieldInfo.getDocValuesType();
//				if (type == DocValuesType.BINARY) {
//					addKNNBinaryField(fieldInfo, new KNN84DocValuesProducer(mergeState));
//				}
//			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addSortedSetField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedSetField(field, valuesProducer);
		System.out.println("addSortedSetField：" + field.name);
	}

	@Override
	public void addSortedNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedNumericField(field, valuesProducer);
		System.out.println("addSortedNumericField：" + field.name);
	}

	@Override
	public void addSortedField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addSortedField(field, valuesProducer);
		System.out.println("addSortedField：" + field.name);
	}

	@Override
	public void addNumericField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addNumericField(field, valuesProducer);
		System.out.println("addNumericField：" + field.name);
	}

	@Override
	public void close() throws IOException {
		delegatee.close();
	}

}
