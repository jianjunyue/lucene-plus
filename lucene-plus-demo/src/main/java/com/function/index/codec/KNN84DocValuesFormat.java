package com.function.index.codec;
 
import java.io.IOException;

import com.function.knn.KNN84DocValuesReader;
import com.function.knn.KNN84DocValuesWriter;
import com.lucene.codecs.DocValuesConsumer;
import com.lucene.codecs.DocValuesFormat;
import com.lucene.codecs.DocValuesProducer;
import com.lucene.index.SegmentReadState;
import com.lucene.index.SegmentWriteState;
 

/**
 * Encodes/Decodes per document values
 */
public class KNN84DocValuesFormat extends DocValuesFormat {
    public static final String LUCENE_84 = "Lucene80"; // Lucene Codec to be used

    public static final String MY_EXT = "Knndv";
    
    private final DocValuesFormat delegate = DocValuesFormat.forName(LUCENE_84);

    public KNN84DocValuesFormat() {
        super(LUCENE_84);
//        super("KnnBinaryCodec");
    }

    @Override
    public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
//    	state.segmentInfo.maxDoc();
    	return new KNN84DocValuesConsumer(delegate.fieldsConsumer(state), state);
//    	return new KNN84DocValuesWriter(state, MY_EXT);
    }

    @Override
    public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
        return delegate.fieldsProducer(state); 
//    	 return new KNN84DocValuesReader(state, MY_EXT);
    }
}