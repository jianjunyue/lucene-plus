package com.lucene.plus.custom.vectors;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.util.concurrent.Executors;

import com.lucene.document.Document;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.Term;
import com.lucene.plus.custom.vectors.function.VectorsValueSouce;
import com.lucene.queries.function.FunctionQuery;
import com.lucene.queries.function.ValueSource;
import com.lucene.search.BooleanQuery;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.Sort;
import com.lucene.search.SortField;
import com.lucene.search.TermQuery;
import com.lucene.search.TopDocs;
import com.lucene.search.BooleanClause.Occur;
import com.lucene.store.FSDirectory; 

import com.lucene.util.BytesRef; 


public class SearchTest {

	public static void main(String[] args) throws IOException {
		search();
		System.out.println("---------------------------------");
		IndexTest.init(false);
		IndexTest.IndexFiles();
		
		search();
		System.out.println("end");
	}

	private static String indexPath = IndexTest.indexPath;

	/**
	 * 向量内积搜索
	 * */
	private static void search() {
		Path file = Paths.get(indexPath);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file)); 
			IndexSearcher searcher = newFixedThreadSearcher(reader, 50);
			
			float[] queryVertices={22.1f,112.1f,14.23f,4.523f,74.23f};
			ValueSource func=new VectorsValueSouce("vectorfiled",queryVertices);
			FunctionQuery functionQuery = new FunctionQuery(func);

			Query query = new TermQuery(new Term("id", "1"));
			BooleanQuery.Builder blquery = new BooleanQuery.Builder();
			blquery.add(query, Occur.MUST);
			blquery.add(functionQuery, Occur.MUST);
			query = blquery.build();

			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs;
			System.out.println(hits.length);
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.println("docId:"+hit.doc + " , " +doc.get("id") + " , " + doc.get("name")  + " , "
						+ doc.get("vectorfiled")+" , "+hit.score);
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
}
