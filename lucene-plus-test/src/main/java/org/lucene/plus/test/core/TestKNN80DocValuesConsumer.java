 
package org.lucene.plus.test.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.MergeState;
import org.apache.lucene.index.SegmentWriteState;
import org.lucene.plus.test.cache.DocVectorCache;

import java.io.Closeable;
import java.io.IOException;

/**
 * Lucene80DocValuesConsumer
 * 重写 org.apache.lucene.codecs.lucene80.Lucene80DocValuesConsumer
 * This class writes the KNN docvalues to the segments
 */
class TestKNN80DocValuesConsumer extends DocValuesConsumer implements Closeable {

	private final Logger logger = LogManager.getLogger(TestKNN80DocValuesConsumer.class);

	private final String TEMP_SUFFIX = "tmp";
	private DocValuesConsumer delegatee;
	private SegmentWriteState state;

	TestKNN80DocValuesConsumer(DocValuesConsumer delegatee, SegmentWriteState state) throws IOException {
		this.delegatee = delegatee;
		this.state = state;
	}

	@Override
	public void addBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		System.out.println("addBinaryField：" + field.name);
		if (!field.attributes().containsKey(VectorField.KNN_FIELD)) {
			delegatee.addBinaryField(field, valuesProducer); // 执行 Lucene80DocValuesConsumer的addBinaryField方法
		} else {
			addKNNBinaryField(field, valuesProducer);
		}
	}

	public void addKNNBinaryField(FieldInfo field, DocValuesProducer valuesProducer) throws IOException {
		delegatee.addBinaryField(field, valuesProducer);
		System.out.println("addKNNBinaryField：" + field.name);
	}

	/**
	 * Merges in the fields from the readers in mergeState
	 *
	 * @param mergeState Holds common state used during segment merging
	 */
	@Override
	public void merge(MergeState mergeState) {
		try {
			System.out.println("mergeState："  );
			delegatee.merge(mergeState);
//			assert mergeState != null;
//			assert mergeState.mergeFieldInfos != null;
//			for (FieldInfo fieldInfo : mergeState.mergeFieldInfos) {
//				DocValuesType type = fieldInfo.getDocValuesType();
//				if (type == DocValuesType.BINARY) {
//					addKNNBinaryField(fieldInfo, new KNN80DocValuesReader(mergeState));
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
