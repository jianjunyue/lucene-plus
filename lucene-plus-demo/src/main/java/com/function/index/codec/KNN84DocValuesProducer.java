package com.function.index.codec;

import java.util.ArrayList;
import java.util.List;

import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.BinaryDocValues;
import com.lucene.index.DocIDMerger;
import com.lucene.index.DocValuesType;
import com.lucene.index.EmptyDocValuesProducer;
import com.lucene.index.FieldInfo;
import com.lucene.index.MergeState;
 
/**
 * Reader for KNNDocValues from the segments
 */
public class KNN84DocValuesProducer extends EmptyDocValuesProducer {

    private MergeState mergeState;

    KNN84DocValuesProducer(MergeState mergeState) {
        this.mergeState = mergeState;
    }

    @Override
    public BinaryDocValues getBinary(FieldInfo field) {
        try {
            List<BinaryDocValuesSub> subs = new ArrayList<>(this.mergeState.docValuesProducers.length);
            for (int i = 0; i < this.mergeState.docValuesProducers.length; i++) {
                BinaryDocValues values = null;
                DocValuesProducer docValuesProducer = mergeState.docValuesProducers[i];
                if (docValuesProducer != null) {
                    FieldInfo readerFieldInfo = mergeState.fieldInfos[i].fieldInfo(field.name);
                    if (readerFieldInfo != null && readerFieldInfo.getDocValuesType() == DocValuesType.BINARY) {
                        values = docValuesProducer.getBinary(readerFieldInfo);
                    }
                    if (values != null) {
                        subs.add(new BinaryDocValuesSub(mergeState.docMaps[i], values));
                    }
                }
            }
            return new KNN84BinaryDocValues(DocIDMerger.of(subs, mergeState.needsIndexSort));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}