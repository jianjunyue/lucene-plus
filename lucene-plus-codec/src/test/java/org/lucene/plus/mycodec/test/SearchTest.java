package org.lucene.plus.mycodec.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import org.lucene.plus.codec.utils.BinaryBytesUtils;

import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.Field.Store;
import com.lucene.document.IntPoint;
import com.lucene.document.LongPoint;
import com.lucene.document.NumericDocValuesField;
import com.lucene.document.SortedDocValuesField;
import com.lucene.document.StringField;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.IndexWriter;
import com.lucene.index.IndexWriterConfig;
import com.lucene.index.Term;
import com.lucene.search.IndexSearcher;
import com.lucene.search.PointRangeQuery;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.Sort;
import com.lucene.search.SortField;
import com.lucene.search.TermQuery;
import com.lucene.search.TermRangeQuery;
import com.lucene.search.TopDocs;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory;
import com.lucene.util.BytesRef;

public class SearchTest {

	public static void main(String[] args) {
		search();
		System.out.println("end");
	}


	private static String indexPath =IndexTest.indexPath;

	private static void search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file));
//			IndexSearcher searcher = new IndexSearcher(reader);
			IndexSearcher searcher =  newFixedThreadSearcher(reader,50);
//		    Sort sort = new Sort(new SortField("sortname",SortField.Type.LONG,false));
//		    Sort sort = new Sort(new SortField[]{new SortField("sortname", SortField.Type.INT, true)});
//		    sort = new Sort(new SortField("groupname",SortField.Type.STRING,true));

		    //group 
//		      sort = Sort.RELEVANCE;
//		    new TermAllGroupsCollector(groupField);
			
			
			Query query = IntPoint.newExactQuery("age", 225);
//			Query query = new TermRangeQuery("sortvalue");
//			Query query = new TermQuery(new Term("name", "上海"));
//			Query query =IntPoint.newRangeQuery("age",  100,225); 
//			TopDocs results = searcher.search(query, 5000,sort);
			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs; 
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				Object obj= doc.get("test_vector");
				float[] floatVectors = BinaryBytesUtils.bytesToFloats((byte[])obj);
				System.out.println(floatVectors[0]);
				System.out.println(hit.doc+" , "+doc.get("id")+" , "+doc.get("test_vector")+" , "+doc.get("name") +" , "+doc.get("sortvalue")+" , "+doc.get("groupvalue"));
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