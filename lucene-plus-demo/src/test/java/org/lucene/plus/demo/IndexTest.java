package org.lucene.plus.demo;

import java.io.IOException; 
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lucene.codecs.Codec;

import com.function.index.codec.KnnBinaryCodec;
import com.function.vectors.Vectors;
import com.function.vectors.Field.VectorsStoredField;
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
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory; 

public class IndexTest {

	public static void main(String[] args) {
		try { 
			IndexFiles();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Hello World!");
	}

	public static String indexPath = "D:\\data\\index\\vectest"; 
 
	public static void IndexFiles() throws IOException { 
		Path file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
		Codec codec = new KnnBinaryCodec();
		iwc.setCodec(codec);
		IndexWriter writer = new IndexWriter(dir, iwc);
		writer.deleteAll();
		writer.commit();  
		

		float[] valuevectors1={1f,12.1f,174.23f,4.23f,174.23f}; 
		Document doc1 = indexDoc("1", "上海1",valuevectors1); 
		
		float[] valuevectors2={2f,112.1f,14.23f,4.523f,74.23f}; 
		Document doc2 =	indexDoc("2", "北京",valuevectors2); 
		
		float[] valuevectors3={3f, 2.1f,1.23f,34.523f,7.23f}; 
		Document doc3 =indexDoc("3", "上海3",valuevectors3); 
		
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
		
//		updateDocument();
		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static Document indexDoc(String id, String name,  float[] valuevectors) throws IOException {
		Document doc = new Document(); 
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField
//		Field vec_field = VectorsStoredCreator.createVectorsFiled("vectorfiled",new Vectors(valuevectors) ) ;
		Field vec_field = new VectorsStoredField("vectorfiled",new Vectors(valuevectors) ) ;

		Field type_field = new StringField("type", name.startsWith("上海")?"1":"0", Store.YES);
//		Field time_BytesRef_field = new SortedDocValuesField("opentime", new BytesRef(open_time));// 分组统计

		doc.add(id_field);
		doc.add(name_field); 
		doc.add(vec_field); 
		doc.add(type_field); 

	return doc;
	}
 
//	private static void updateDocument() throws IOException {
//		 
//		writer.updateNumericDocValue(new Term("id","2"), "sortname", 115); 
//	}
	
	 public static void update() throws IOException {
	    	Path file = Paths.get(indexPath);
			Directory dir = FSDirectory.open(file);
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
			Codec codec = new KnnBinaryCodec();
			iwc.setCodec(codec);
			IndexWriter	writer = new IndexWriter(dir, iwc);  

//			float[] valuevectors1={112.1f,12.1f,174.23f,4.23f,174.23f}; 
//			indexDoc("4", "上海4",valuevectors1); 
			
			float[] valuevectors1={4f,162.1f,174.23f,4.23f,174.23f}; 
			Document doc1 =indexDoc("4", "上海4",valuevectors1); 
			
			float[] valuevectors2={5f,112.1f,14.23f,4.523f,74.23f}; 
			Document doc2 =indexDoc("5", "北京5",valuevectors2); 
			
			writer.addDocument(doc1);
			writer.addDocument(doc2);
			
			writer.commit();
			writer.forceMerge(5);
			writer.close();
	 }
	
}
