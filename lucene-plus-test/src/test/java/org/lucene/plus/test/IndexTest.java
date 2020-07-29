package org.lucene.plus.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.lucene.plus.test.core.MyKNNCodec;
import org.lucene.plus.test.core.VectorField;
import org.lucene.plus.test.temp.BinaryBytesUtils;

public class IndexTest {

	public static void main(String[] args) {
		try {
			init();
			IndexFiles();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Hello World!");
	}

	public static String indexPath = "D:\\data\\index\\testcodecplusfun";
	private static IndexWriter writer;
	private static Path file;
	
	private static void init() throws IOException {
		file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		Codec codec = new MyKNNCodec();
		iwc.setCodec(codec);
		writer = new IndexWriter(dir, iwc);
		writer.deleteAll();
		writer.commit();
	}
	 
	private static void IndexFiles() throws IOException { 

		indexDoc("1", "上海", "2", "shanghai1");
		indexDoc("2", "上海", "1", "shanghai2");
		indexDoc("3", "上海", "13", "shanghai3");
		indexDoc("4", "上海", "14", "shanghai4");
		indexDoc("5", "北京", "24", "beijing1");
		indexDoc("6", "北京", "6", "beijing2");
		indexDoc("7", "北京", "7", "beijing3");
//		indexDoc("8", "北京", "2,18", "beijing4");
		  
		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static void indexDoc(String id, String name,  String open_time, String info) throws IOException {
		Document doc = new Document(); 
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField
		Field time_BytesRef_field = new NumericDocValuesField("opentime", Long.parseLong(open_time));
		float[] array = { 1.0f, 2.0f, 3.0f };
		VectorField vectorField = new VectorField("test_vector",array);
		
		Field info_field = new StringField("stropentime", open_time, Store.YES); 
		float[] value = { 1111.0f, 1112.0f, 22223.0f };
		BytesRef ref = BinaryBytesUtils.floatToBytes(value);
		BinaryDocValuesField bdvf=new BinaryDocValuesField("bdvf",ref);
		doc.add(id_field);
		doc.add(name_field); 
		doc.add(time_BytesRef_field);
		doc.add(info_field); 
		doc.add(vectorField);
		doc.add(bdvf); 

		writer.addDocument(doc);
	}
 
	private static void updateDocument() throws IOException {
		 
		writer.updateNumericDocValue(new Term("id","2"), "sortname", 115); 
	}
	
}