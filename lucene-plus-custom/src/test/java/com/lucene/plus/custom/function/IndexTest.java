package com.lucene.plus.custom.function;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
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

	public static String indexPath = "D:\\data\\index\\plusfun";
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
//		Field time_BytesRef_field = new SortedDocValuesField("opentime", new BytesRef(open_time));// 分组统计

		Field info_field = new StringField("stropentime", open_time, Store.YES); 
		
		
		doc.add(id_field);
		doc.add(name_field); 
		doc.add(time_BytesRef_field);
		doc.add(info_field); 

		writer.addDocument(doc);
	}
 
	private static void updateDocument() throws IOException {
		 
		writer.updateNumericDocValue(new Term("id","2"), "sortname", 115); 
	}
	
}