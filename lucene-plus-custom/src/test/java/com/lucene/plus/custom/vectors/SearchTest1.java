package com.lucene.plus.custom.vectors;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;

import com.lucene.document.Document;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.Term;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.Sort;
import com.lucene.search.SortField;
import com.lucene.search.TermQuery;
import com.lucene.search.TopDocs;
import com.lucene.store.FSDirectory; 

import com.lucene.util.BytesRef; 


public class SearchTest1 {

	public static void main(String[] args) {
		vec_search();
		System.out.println("end");
	}


	private static String indexPath = "D:\\data\\index\\vectest";

	/**
	 * 向量内积搜索
	 * */
	private static void vec_search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file)); 
			IndexSearcher searcher =  newFixedThreadSearcher(reader,50); 
			Query query = new TermQuery(new Term("id", "1")); 
			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs; 
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);  
				getVertexs(doc.getBinaryValue("vec"));
				System.out.println(doc.get("id")+" , "+doc.get("name") +" , "+doc.get("vec")+" , "+doc.getBinaryValue("vec"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file)); 
			IndexSearcher searcher =  newFixedThreadSearcher(reader,50); 
			Query query = new TermQuery(new Term("name", "北京")); 
			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs; 
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);  
				getVertexs(doc.getBinaryValue("vec"));
				System.out.println(doc.get("id")+" , "+doc.get("name") +" , "+doc.get("vec")+" , "+doc.getBinaryValue("vec"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getVertexs(BytesRef ref) {
		float[] vertices = VectorsStoredCreator.geVectorsFromVectorsStoredField(ref);
		for(int i=0;i<vertices.length;i++) {
		  System.out.println(vertices[i]);
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
