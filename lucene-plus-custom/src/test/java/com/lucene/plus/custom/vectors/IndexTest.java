package com.lucene.plus.custom.vectors;

import java.io.IOException; 
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field; 
import com.lucene.document.StringField;
import com.lucene.document.Field.Store;
import com.lucene.document.SortedDocValuesField;
import com.lucene.index.IndexWriter;
import com.lucene.index.IndexWriterConfig;
import com.lucene.index.Term;
import com.lucene.plus.custom.vectors.Field.VectorsStoredField;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory; 

public class IndexTest {

	public static void main(String[] args) {
		try {
			init(true);
			IndexFiles();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Hello World!");
	}

	public static String indexPath = "D:\\data\\index\\vectest1";
	private static IndexWriter writer;
	private static Path file;
	
	public static void init(Boolean del) throws IOException {
		file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
		writer = new IndexWriter(dir, iwc);
		if(del) {
		writer.deleteAll();
		}
		writer.commit();  
	}

	public static void IndexFiles() throws IOException { 
	    int i=	(int) (System.currentTimeMillis()%100);
		float[] valuevectors1={1f,i+12.1f,174.23f,4.23f,174.23f}; 
		indexDoc("1", "上海1",valuevectors1); 
		
		float[] valuevectors2={2f,i+112.1f,14.23f,4.523f,74.23f}; 
		indexDoc("2", "北京",valuevectors2); 
		
		float[] valuevectors3={3f, i+2.1f,1.23f,34.523f,7.23f}; 
		indexDoc("1", "上海2",valuevectors3); 
 
		
//		updateDocument();
		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static void indexDoc(String id, String name,  float[] valuevectors) throws IOException {
		Document doc = new Document(); 
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField
//		Field vec_field = VectorsStoredCreator.createVectorsFiled("vectorfiled",new Vectors(valuevectors) ) ;
		Field vec_field = new VectorsStoredField("vectorfiled",new Vectors(valuevectors) ) ;

//		Field time_BytesRef_field = new SortedDocValuesField("opentime", new BytesRef(open_time));// 分组统计

		doc.add(id_field);
		doc.add(name_field); 
		doc.add(vec_field); 

		writer.addDocument(doc);
	}
 
	private static void updateDocument() throws IOException {
		 
		writer.updateNumericDocValue(new Term("id","2"), "sortname", 115); 
	}
	
}
