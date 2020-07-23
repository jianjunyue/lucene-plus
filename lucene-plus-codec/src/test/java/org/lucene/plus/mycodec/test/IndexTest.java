package org.lucene.plus.mycodec.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lucene.plus.mycodec.MyCodec;
import org.lucene.plus.mycodec.common.VectorField;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.Field.Store;
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
			IndexFiles();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Hello World!");
	}

	public static String indexPath = "D:\\data\\index\\mycodec";
	private static IndexWriter writer;
	private static Path file;
	 
	private static void IndexFiles() throws IOException {
		file = Paths.get(indexPath);
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer); 
		iwc.setOpenMode(OpenMode.CREATE);
//		iwc.setCodec(new MyCodec());
		writer = new IndexWriter(dir, iwc);
  
		indexDoc("1", "上海", "1111", "shanghai1");
		indexDoc("2", "上海", "12", "shanghai2");
		indexDoc("3", "上海", "313", "shanghai3");
		indexDoc("4", "上海", "114", "shanghai4");
		indexDoc("5", "北京", "225", "beijing1");
		indexDoc("6", "北京", "226", "beijing2");
		indexDoc("7", "北京", "227", "beijing3");
		indexDoc("8", "北京", "228", "beijing4");
		 
		
//		updateDocument();
		writer.commit();
		writer.forceMerge(5);
		writer.close();
	}

	private static void indexDoc(String id, String name,  String sortname, String groupname) throws IOException {
		Document doc = new Document(); 
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField
		Field sort_field = new NumericDocValuesField("sortname", Long.parseLong(sortname));
		Field group_field = new SortedDocValuesField("groupname", new BytesRef(groupname));// 分组统计

		Field sortvalue_field = new StringField("sortvalue", sortname, Store.YES);
		Field namevalue_field = new StringField("groupvalue", groupname, Store.YES);
		

	    Field age_field  = new IntPoint("age", Integer.parseInt(sortname));
		
	    float[] array = {1.0f, 3.0f, 4.0f};
        VectorField vectorField = new VectorField("test_vector", array );
	    

		doc.add(vectorField);
	    
		doc.add(id_field);
		doc.add(name_field); 
		doc.add(sort_field);
		doc.add(group_field);

		doc.add(sortvalue_field);
		doc.add(namevalue_field);
		
		doc.add(age_field);

		writer.addDocument(doc);
	}
 
	private static void updateDocument() throws IOException {
		 
		writer.updateNumericDocValue(new Term("id","2"), "sortname", 115); 
	}
	
}