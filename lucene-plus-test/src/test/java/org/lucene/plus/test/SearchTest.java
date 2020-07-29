package org.lucene.plus.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.FunctionRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.lucene.plus.test.temp.FunValueSouce;
 

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
			IndexSearcher searcher = newFixedThreadSearcher(reader, 50);//bdvf  opentime
			FunctionRangeQuery functionQuery = new FunctionRangeQuery(new FunValueSouce("bdvf"),1,10,true,true);
//			FunctionRangeQuery functionQuery = new FunctionRangeQuery(new FunValueSouce("opentime"),1,10,true,true);
	 		
			Query query1 = new TermQuery(new Term("name", "北京"));
			BooleanQuery.Builder blquery = new BooleanQuery.Builder();
//			blquery.add(query1, Occur.MUST);
			blquery.add(functionQuery, Occur.MUST);
			Query	query = blquery.build();

			TopDocs results = searcher.search(query, 5000);
			ScoreDoc[] hits = results.scoreDocs; 
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.println(doc.get("id") + " , " + doc.get("name")  + " , "
						+ doc.get("opentime")+" , "+ doc.get("stropentime")+" , "+hit.score);
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
