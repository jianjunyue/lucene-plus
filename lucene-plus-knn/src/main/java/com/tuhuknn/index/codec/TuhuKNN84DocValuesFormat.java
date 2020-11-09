package com.tuhuknn.index.codec;
 
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;

import com.tuhuknn.index.util.LuceneUtils; 

/**
 * Encodes/Decodes per document values
 */
public class TuhuKNN84DocValuesFormat extends DocValuesFormat {
    private final Logger logger = LogManager.getLogger(TuhuKNN84DocValuesFormat.class);
    
    private final DocValuesFormat delegate = DocValuesFormat.forName(LuceneUtils.LUCENE_84);

    public TuhuKNN84DocValuesFormat() {
        super(LuceneUtils.LUCENE_84);
    }

    @Override
    public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
        return new TuhuKNN84DocValuesConsumer(delegate.fieldsConsumer(state), state);
    }

    @Override
    public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
        return delegate.fieldsProducer(state);
    }
}