package com.lucene.plus.custom.temp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.IntPoint;
import com.lucene.document.Field.Store;
import com.lucene.document.NumericDocValuesField;
import com.lucene.document.SortedDocValuesField;
import com.lucene.document.StringField;
import com.lucene.index.IndexWriter;
import com.lucene.index.IndexWriterConfig;
import com.lucene.index.IndexWriterConfig.OpenMode;
import com.lucene.index.Term;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory;
import com.lucene.util.BytesRef;

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

	public static String indexPath = "D:\\data\\index\\plussquery";
	private static IndexWriter writer;
	private static Path file;
	
	private static void init() throws IOException {
		file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(dir, iwc);
		writer.deleteAll();
		writer.commit();
	}
	 
	private static void IndexFiles() throws IOException { 

		indexDoc("1", "上海", "32.21", "130.11");
		indexDoc("2", "上海", "31.67", "112.32"); 
		  
		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static void indexDoc(String id, String name,  String open_time, String info) throws IOException {
		Document doc = new Document(); 
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);
//		Field latlon_field = new StringField("latlon", info, Store.YES);// StringField 

		Field latlon_field = new LatLonPointField("latlon",Float.parseFloat(open_time),Float.parseFloat(info));
		
		doc.add(id_field);
		doc.add(name_field); 
		doc.add(latlon_field); 

		writer.addDocument(doc);
	}
  
}