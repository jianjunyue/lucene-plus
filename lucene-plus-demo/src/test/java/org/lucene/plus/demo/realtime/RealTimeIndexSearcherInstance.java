package org.lucene.plus.demo.realtime;

import java.io.File;

import com.lucene.index.DirectoryReader;
import com.lucene.index.IndexReader;
import com.lucene.index.IndexWriter;
import com.lucene.search.IndexSearcher;
import com.lucene.store.FSDirectory;

public class RealTimeIndexSearcherInstance {
    private static RealTimeIndexSearcherInstance instance = new RealTimeIndexSearcherInstance();
	private static String indexPath = "D:\\data\\index\\realindex";// 当前搜索索引路径
    private static boolean blMainSearcher = true;

	private static IndexReader indexMainReader = null;
	private static IndexReader indexReader = null;

	private static IndexSearcher indexMainSearcher = null;
	private static IndexSearcher indexSearcher = null;
	public static RealTimeIndexSearcherInstance getInstance() {
	 
 
		return instance;
	}
	
	
	public IndexSearcher getIndexSearcher() {
		if (blMainSearcher) {
			if (indexMainSearcher == null) {
//				indexMainSearcher = new IndexSearcher(indexMainReader);
				startCreateIndexSearcher(indexPath);
			}
			return indexMainSearcher;
		} else {
			if (indexSearcher == null) {
//				indexSearcher = new IndexSearcher(indexReader); 
				startCreateIndexSearcher(indexPath);
			}
			return indexSearcher;
		}
	}
	
	public void realChangeSearcher() {
		if (blMainSearcher) {
			blMainSearcher = false;
		} else {
			blMainSearcher = true;
		}
	}
	
	/**
	 * 增量索引更新
	 * */
	public boolean changeMemoryIndexSearcher() {
		IndexReader newReader = null;
		try {
			IndexWriter indexWriter = IndexWriterInstance.getInstance().getIndexWriter();
 
			if (indexWriter != null) {
				if (blMainSearcher) {
					try {
						if(indexMainReader==null)
						{
							startIndexMainReader();
						}
						newReader = DirectoryReader.openIfChanged((DirectoryReader) indexMainReader, indexWriter, true);
					} catch (Exception e) {
//						logger.error("RealTimeIndexSearcherInstance changeMemoryIndexSearcher is error - 1", e);
					}
					if (newReader != null) {
						if(indexReader!=null)
						{
						indexReader.close();
						}
						indexReader = newReader;
						indexSearcher = new IndexSearcher(indexReader);
						return true;
					}
				} else {
					try {
						if(indexReader==null)
						{
							startIndexReader();
						}
						newReader = DirectoryReader.openIfChanged((DirectoryReader) indexReader, indexWriter, true);
					} catch (Exception e) {
//						logger.error("RealTimeIndexSearcherInstance changeMemoryIndexSearcher is error - 2", e);
					}
					if (newReader != null) {
						if(indexMainReader!=null)
						{
						indexMainReader.close();
						}
						indexMainReader = newReader;
						indexMainSearcher = new IndexSearcher(indexMainReader);
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("RealTimeIndexSearcherInstance changeMemoryIndexSearcher is error"+ e.getMessage());
		}
		return false;
	}
	
	private void startIndexMainReader()
	{
		try { 
			System.out.println("RealTimeIndexSearcherInstance startIndexMainReader is start..");
			File file = new File(indexPath);
			indexMainReader = DirectoryReader.open(FSDirectory.open(file.toPath()));
		} catch (Exception e) {
			System.out.println("RealTimeIndexSearcherInstance startIndexMainReader is error"+ e.getMessage());
		}
	}
	
	private void startIndexReader()
	{
		try {
			System.out.println("RealTimeIndexSearcherInstance startIndexReader is start..");
			File file = new File(indexPath);
			indexReader = DirectoryReader.open(FSDirectory.open(file.toPath()));
		} catch (Exception e) {
			System.out.println("RealTimeIndexSearcherInstance startIndexReader is error"+e.getMessage());
		}
	}
	public String getIndexPath() {
		return indexPath;
	}
	
	private void startCreateIndexSearcher(String path) {
		try {
//			logger.error("RealTimeIndexSearcherInstance startCreateIndexSearcher is start.. blMainSearcher:" + blMainSearcher);
			File file = new File(path);
			if (blMainSearcher) {
				indexMainReader = DirectoryReader.open(FSDirectory.open(file.toPath()));
				indexMainSearcher = new IndexSearcher(indexMainReader);
//				indexSearcher = new IndexSearcher(indexReader);
			} else {
				indexReader = DirectoryReader.open(FSDirectory.open(file.toPath()));
//				indexMainSearcher = new IndexSearcher(indexMainReader);
				indexSearcher = new IndexSearcher(indexReader);
			}
		} catch (Exception e) {
			System.out.println("RealTimeIndexSearcherInstance startCreateIndexSearcher is error"+ e.getMessage());
		}
	}
}
