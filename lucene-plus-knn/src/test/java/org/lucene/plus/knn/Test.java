package org.lucene.plus.knn;

import org.apache.lucene.document.Field; 
import org.apache.lucene.document.IntPoint;
//import org.lucene.plus.knn.KNNTestCase;
import org.lucene.plus.knn.index.KNNIndexCache;
import org.lucene.plus.knn.index.KNNQuery;
import org.lucene.plus.knn.index.KNNSettings;
import org.lucene.plus.knn.index.KNNVectorFieldMapper;
import org.lucene.plus.knn.index.VectorField;
import org.lucene.plus.knn.index.codec.KNNCodecUtil;
import org.lucene.plus.knn.index.codec.KNN80Codec.KNN80Codec;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.CodecUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ChecksumIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.mockito.Mockito;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.NumericDocValuesField;

import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

public class Test extends LuceneTestCase {
	public static String indexPath = "D:\\data\\index\\knncodec";
	private static Path file = Paths.get(indexPath);

	public static void main(String[] args) {
		try {
			testIndex();
			System.out.print("end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testIndex() throws Exception {
		testdelete();
//		Codec codec = new KNN80Codec();
		Codec codec = new MyKNNCodec();
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		iwc.setCodec(codec);

		float[] array = { 1.0f, 2.0f, 3.0f };
		VectorField vectorField = new VectorField("test_vector", array, KNNVectorFieldMapper.Defaults.FIELD_TYPE);
//		VectorField vectorField = new VectorField("test_vector", array);
		
		Field age_field  = new IntPoint("age", Integer.parseInt("111"));
		Field name_field = new StringField("name", "asdasd", Store.YES);// StringField
		Field sort_field = new NumericDocValuesField("ndocvalue", Long.parseLong("5"));
		
		Field binary_field =new BinaryDocValuesField("bndocvalue", new BytesRef("111"));
		
		IndexWriter writer = new IndexWriter(dir, iwc);
		Document doc = new Document();
		doc.add(vectorField);
		doc.add(age_field);
		doc.add(sort_field);
		doc.add(name_field);
		doc.add(binary_field);
		writer.addDocument(doc);
		writer.commit();
		writer.close();
		dir.close();
	}

	 

	public static void testSearch(Codec codec) throws Exception {
		Directory dir = FSDirectory.open(file);

		IndexReader reader = DirectoryReader.open(dir);

		LeafReaderContext lrc = reader.getContext().leaves().iterator().next(); // leaf reader context
		SegmentReader segmentReader = (SegmentReader) FilterLeafReader.unwrap(lrc.reader());
		String hnswFileExtension = segmentReader.getSegmentInfo().info.getUseCompoundFile()
				? KNNCodecUtil.HNSW_COMPOUND_EXTENSION
				: KNNCodecUtil.HNSW_EXTENSION;
		String hnswSuffix = "test_vector" + hnswFileExtension;
		List<String> hnswFiles = segmentReader.getSegmentInfo().files().stream()
				.filter(fileName -> fileName.endsWith(hnswSuffix)).collect(Collectors.toList());
		assertTrue(!hnswFiles.isEmpty());
		ChecksumIndexInput indexInput = dir.openChecksumInput(hnswFiles.get(0), IOContext.DEFAULT);
		indexInput.seek(indexInput.length() - CodecUtil.footerLength());
		CodecUtil.checkFooter(indexInput); // If footer is not valid, it would throw exception and test fails
		indexInput.close();

		IndexSearcher searcher = new IndexSearcher(reader);
		assertEquals(1, searcher.count(new KNNQuery("test_vector", new float[] { 1.0f, 2.5f }, 1, "myindex")));

		reader.close();
		dir.close();
	}
	
	public static void testdelete() throws Exception {
//		Codec codec = new KNN80Codec();
		Codec codec = new MyKNNCodec();
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		iwc.setCodec(codec);
  
		IndexWriter writer = new IndexWriter(dir, iwc); 
		writer.deleteAll();
		writer.commit();
		writer.close();
		dir.close();
	}

	
}
