package com.lucene.plus.custom.filter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.Field.Store;
import com.lucene.document.FloatPoint;
import com.lucene.document.IntPoint;
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
			delete();
//			IndexFiles2();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Hello World!");
	}

	public static String indexPath = "D:\\data\\index\\plusfilter";
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

		indexDoc("1", "上海", "1", "shanghai1");
		indexDoc("2", "上海", "5", "shanghai2");
		indexDoc("3", "上海", "13", "shanghai3");
		indexDoc("4", "上海", "14", "shanghai4");
		indexDoc("5", "北京", "24", "beijing1");
		indexDoc("6", "北京", "6", "beijing2");
		indexDoc("7", "北京", "7", "beijing3");

		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static void indexDoc(String id, String name, String open_time, String info) throws IOException {
		Document doc = new Document();
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField

		Field opentime_field = new IntPoint("opentime", Integer.parseInt(open_time));
//		Field opentime_field = new FloatPoint("opentime", Float.parseFloat(open_time));
		
		doc.add(id_field);
		doc.add(name_field);
		doc.add(opentime_field);

		writer.addDocument(doc);
	}
	
	private static void delete() {
		try { 
			file = Paths.get(indexPath);
			Directory dir = FSDirectory.open(file);
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			IndexWriter deleWriter = new IndexWriter(dir, iwc);
			
			deleWriter.deleteDocuments(new Term("id","13"));
			deleWriter.deleteDocuments(new Term("id","2")); 
			deleWriter.commit();
			deleWriter.forceMerge(5);
			deleWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private static void IndexFiles2() throws IOException {
		file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(dir, iwc);

		indexDoc("8", "上海", "8", "shanghai1");
		indexDoc("9", "上海", "9", "shanghai2");
		indexDoc("3", "上海", "13", "shanghai3"); 

		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}
	

	

}