package com.lucene.plus.custom.codec.knn.realtime.index;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Executors;
 
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.StringField;
import com.lucene.document.Field.Store;
import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.Term;
import com.lucene.plus.custom.codec.knn.realtime.RealIndexSearchManager;
import com.lucene.plus.custom.codec.knn.realtime.RealTimeIndexSearcherInstance;
import com.lucene.plus.custom.codec.knn.util.DocValuesUtils;
import com.lucene.plus.custom.codec.knn.vectors.cache.FieldDocValuesCache;
import com.lucene.plus.custom.codec.knn.vectors.function.VectorsValueSouce;
import com.lucene.queries.function.FunctionQuery;
import com.lucene.queries.function.ValueSource;
import com.lucene.search.BooleanQuery;
import com.lucene.search.IndexSearcher;
import com.lucene.search.Query;
import com.lucene.search.ScoreDoc;
import com.lucene.search.TermQuery;
import com.lucene.search.TopDocs;
import com.lucene.search.BooleanClause.Occur; 

public class RealTimeIndexTest {

	public static void main(String[] args) throws InterruptedException {
		String indexPath = RealTimeIndexSearcherInstance.getInstance().getIndexPath();
		String field = "vectorfiled";
		DocValuesUtils.getFloats(indexPath, field);

		Map<Integer, float[]>  map=FieldDocValuesCache.getValues(field);
		
		realIndex();

		for (int i = 0; i < 1000; i++) {
			search();
			Thread.sleep(3000);
		}
	}

	private static void realIndex() {
		Thread thread = new Thread() {
			public void run() {
				RealIndexSearchManager.getInstance().runIndex();
			}
		};
		thread.start();
	}

	/**
	 * 向量内积搜索
	 */
	private static void search() {
		try {
			IndexSearcher searcher = RealTimeIndexSearcherInstance.getInstance().getIndexSearcher();

			float[] queryVertices = { 22.1f, 112.1f, 14.23f, 4.523f, 74.23f };
			ValueSource func = new VectorsValueSouce("vectorfiled", queryVertices);
			FunctionQuery functionQuery = new FunctionQuery(func);

			Query query = new TermQuery(new Term("type", "1"));
			BooleanQuery.Builder blquery = new BooleanQuery.Builder();
			blquery.add(query, Occur.MUST);
			blquery.add(functionQuery, Occur.MUST);
			query = blquery.build();

			TopDocs results = searcher.search(query, 5);
			ScoreDoc[] hits = results.scoreDocs;
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.println("docId：" + hit.doc + " , " + doc.get("id") + " , " + doc.get("name") + " , "
						+ doc.get("vectorfiled") + " , " + hit.score);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
