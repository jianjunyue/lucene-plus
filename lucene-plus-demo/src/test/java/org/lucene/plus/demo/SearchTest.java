package org.lucene.plus.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.Field.Store;
import com.lucene.document.NumericDocValuesField;
import com.lucene.document.SortedDocValuesField;
import com.lucene.document.StringField;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.IndexWriter;
import com.lucene.index.IndexWriterConfig;
import com.lucene.index.Term;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.Sort;
import com.lucene.search.SortField;
import com.lucene.search.TermQuery;
import com.lucene.search.TopDocs;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory;
import com.lucene.util.BytesRef;

public class SearchTest {

	public static void main(String[] args) {
		search();
		System.out.println("end");
	}


	private static String indexPath = "/Users/apple/data/test";

	private static void search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file));
//			IndexSearcher searcher = new IndexSearcher(reader);
			IndexSearcher searcher =  newFixedThreadSearcher(reader,50);
		    Sort sort = new Sort(new SortField("sortname",SortField.Type.LONG,false));
//		    Sort sort = new Sort(new SortField[]{new SortField("sortname", SortField.Type.INT, true)});
//		    sort = new Sort(new SortField("groupname",SortField.Type.STRING,true));

		    //group 
//		      sort = Sort.RELEVANCE;
//		    new TermAllGroupsCollector(groupField);
		    
//			Query query = new TermQuery(new Term("name", "tian"));
			Query query = new TermQuery(new Term("name", "上海"));
//			Query query = new TermQuery(new Term("name", "beijing"));

//			TopDocs results = searcher.search(query, 5000,sort);
			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs;
			System.out.println(hits.length);
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
//				System.out.println(doc.get("sortname")+" , "+doc.get("groupname"));
				 
				System.out.println(doc.get("id")+" , "+doc.get("name") +" , "+doc.get("sortvalue")+" , "+doc.get("groupvalue"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static IndexSearcher newFixedThreadSearcher(IndexReader r, int nThreads) {
        return new IndexSearcher(r.getContext(), Executors.newFixedThreadPool(nThreads));
//        return new IndexSearcher(r.getContext());
    }

	/**
	 * 排序。 sort=id,INT,false
	 */
	public Sort getSort(String strSort) {
		try {
			if (strSort == null || strSort.trim().length() == 0) {
				return null;
			}
			String[] s = strSort.trim().split(",");
			if (s.length == 2 || s.length == 3) {
				boolean reverse = false;// 默认是小到大排序
				if (s.length == 3) {
					reverse = Boolean.valueOf(s[2].toLowerCase());
				}
				SortField.Type type = SortField.Type.INT;
				Sort sort = new Sort(new SortField(s[0].toLowerCase(), type, reverse));
				return sort;
			} else { 
			}
		} catch (Exception e) { 
		}
		return null;
	}
}