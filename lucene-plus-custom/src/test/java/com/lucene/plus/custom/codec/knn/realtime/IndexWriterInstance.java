package com.lucene.plus.custom.codec.knn.realtime;

import java.io.File;
import java.nio.file.Path;

import com.lucene.plus.custom.codec.knn.KnnBinaryCodec;
import com.lucene.analysis.Analyzer;
import com.lucene.analysis.standard.StandardAnalyzer;
import com.lucene.codecs.Codec;
import com.lucene.document.Document;
import com.lucene.index.IndexWriter;
import com.lucene.index.IndexWriterConfig;
import com.lucene.index.Term;
import com.lucene.index.IndexWriterConfig.OpenMode;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory;

/**
 * @Description: 索引更新
 * 
 *               全量更新： 1.String getIndexPath()； 2.boolean
 *               changeIndexSearcher(String path)； 3.IndexSearcher
 *               getPreIndexSearcher()； 4.void realChangeSearcher(String
 *               pathIndexing) 增量更新： 1.boolean updateIndexSearcher()；
 *               2.IndexSearcher getPreIndexSearcher()； 3.void
 *               realChangeSearcher(String pathIndexing)
 * 
 *
 * @date: 2015年2月5日 下午7:43:15
 * 
 * @author yuejianjun
 *
 */
public class IndexWriterInstance {
	private static IndexWriterInstance instance = null;
	private static IndexWriter indexWriter = null;
	private static boolean isIndexClose = true;

	public static IndexWriterInstance getInstance() {
		if (instance == null) {
			instance = new IndexWriterInstance();
			instance.close();
		}
		return instance;
	}

	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	/**
	 * 先删除索引，在增加索引
	 */
	public void addDataIndexDoc(Document document, String id, Boolean status, String indexPath) {
		try {
			if (isIndexClose || indexWriter == null) {
				createIndexWriter(indexPath);
			}
			indexWriter.addDocument(document);
		} catch (Exception e) {
			System.out.println("IndexWriterInstance addDataIndexDoc is error" + e.getMessage());
		}
	}

	/**
	 * 追加新版美食索引，先删除该餐厅索引 2016-11-18
	 */
	public void deleteDocuments(String restaurant_id, String indexPath) {
		try {
			if (isIndexClose || indexWriter == null) {
				createIndexWriter(indexPath);
			}
			Term term = new Term("restaurant_id", restaurant_id);
			indexWriter.deleteDocuments(term);
		} catch (Exception e) {
			System.out.println("IndexWriterInstance deleteDocuments is error" + e.getMessage());
		}
	}

	/**
	 * 增加索引
	 */
	public void addDataIndexDoc(Document document, String id, String indexPath) {
		try {
			if (isIndexClose || indexWriter == null) {
				createIndexWriter(indexPath);
			}
			Term term = new Term("id", id);
			indexWriter.deleteDocuments(term);
//			indexWriter.addDocument(document);
		} catch (Exception e) {
			System.out.println("IndexWriterInstance addDataIndexDoc is error" + e.getMessage());
		}
	}

	public void commit() {
		try {
			indexWriter.commit();
			indexWriter.forceMerge(2);
		} catch (Exception e) {
			indexWriter = null;
			System.out.println("IndexWriterInstance commit is error" + e.getMessage());
		}
	}

	public void close() {
		try {
			isIndexClose = true;
			if (indexWriter != null) {
				indexWriter.commit();
				indexWriter.forceMerge(2);
				indexWriter.close();
				indexWriter = null;
			}
		} catch (Exception e) {
			indexWriter = null;
			System.out.println("IndexWriterInstance close is error" + e.getMessage());
		}
	}

	/**
	 * 设置索引关闭，重新加载索引器
	 */
	public void setIndexClose() {
		isIndexClose = true;
	}

	private synchronized void createIndexWriter(String indexPath) {
		try {
			File file = new File(indexPath);
			if (!file.exists()) {
				file.mkdir();
			}
			Path path = file.toPath();
			Directory dir = FSDirectory.open(path);

			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setRAMBufferSizeMB(256.0);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			Codec codec = new KnnBinaryCodec();
			iwc.setCodec(codec);
			indexWriter = new IndexWriter(dir, iwc);
			isIndexClose = false;
		} catch (Exception e) {
			indexWriter = null;
			System.out.println("IndexWriterInstance createIndexWriter is error" + e.getMessage());
		}
	}

}