package org.lucene.plus.test;

import org.apache.lucene.document.Field; 
import org.apache.lucene.document.IntPoint;
//import org.lucene.plus.knn.KNNTestCase; 
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.CodecUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext; 
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ChecksumIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext; 
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
 
import org.apache.lucene.util.BytesRef;
import org.lucene.plus.test.core.MyKNNCodec;
import org.lucene.plus.test.core.VectorField; 

public class Test   {
	public static String indexPath = "D:\\data\\index\\testknncodec";
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
		Codec codec = new MyKNNCodec();
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		iwc.setCodec(codec);

		float[] array = { 1.0f, 2.0f, 3.0f };
//		VectorField vectorField = new VectorField("test_vector", array, KNNVectorFieldMapper.Defaults.FIELD_TYPE);
		VectorField vectorField = new VectorField("test_vector", array);
		
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

		IndexSearcher searcher = new IndexSearcher(reader);
//		assertEquals(1, searcher.count(new KNNQuery("test_vector", new float[] { 1.0f, 2.5f }, 1, "myindex")));

		reader.close();
		dir.close();
	}
	
	public static void testdelete() throws Exception { 
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
