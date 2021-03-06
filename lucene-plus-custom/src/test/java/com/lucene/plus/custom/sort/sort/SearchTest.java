package com.lucene.plus.custom.sort.sort;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import com.lucene.document.Document;
import com.lucene.document.IntPoint;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.Term;
import com.lucene.queries.function.FunctionQuery;
import com.lucene.search.BooleanClause.Occur;
import com.lucene.search.BooleanQuery;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.Sort;
import com.lucene.search.SortField;
import com.lucene.search.TermQuery;
import com.lucene.search.TopDocs;
import com.lucene.store.FSDirectory; 

public class SearchTest {

	public static void main(String[] args) {
		search();
		System.out.println("end");
	}

	private static String indexPath = IndexTest.indexPath;

	private static void search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file)); 
			IndexSearcher searcher = newFixedThreadSearcher(reader, 50);
//			FunctionQuery functionQuery = new FunctionQuery(new TimeValueSouce("opentime"));
			Sort sort=getSort("opentime,INT,true");
//			Query query = new TermQuery(new Term("name", "北京")); 
			Query query0 = IntPoint.newSetQuery("opentime", 5); 
			Query query1 = IntPoint.newSetQuery("opentime", 6); 
			Query query2 = IntPoint.newSetQuery("opentime", 13);
			
			BooleanQuery.Builder blquery = new BooleanQuery.Builder();

			blquery.add(query0, Occur.SHOULD);
			blquery.add(query1, Occur.SHOULD);
			blquery.add(query2, Occur.SHOULD); 
			Query query = blquery.build();

			TopDocs results = searcher.search(query, 5000,sort);
			ScoreDoc[] hits = results.scoreDocs;
			System.out.println(hits.length);
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.println(doc.get("id") + " , " + doc.get("name")  + " , "
						+ doc.get("opentime")+" , "+hit.score);
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
	public static Sort getSort(String strSort) {
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
