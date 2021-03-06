package com.lucene.plus.custom.temp;

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

			String field = "latlon";
			double longitude = 112.321; 
			double latitude = 31.67;
			double radiusMeters = 100;
			Query query = new LatLonPointDistanceQuery(field, latitude, longitude, radiusMeters);

			BooleanQuery.Builder blquery = new BooleanQuery.Builder();

			blquery.add(query, Occur.SHOULD);
			query = blquery.build();

			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs;
			System.out.println(hits.length);
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.println(
						doc.get("id") + " , " + doc.get("name") + " , " + doc.get("opentime") + " , " + hit.score);
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
