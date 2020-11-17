package com.lucene.plus.custom.codec.knn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path; 
  
import com.lucene.index.BinaryDocValues;
import com.lucene.index.DirectoryReader;
import com.lucene.index.LeafReader;
import com.lucene.index.LeafReaderContext;
import com.lucene.plus.custom.codec.knn.util.KNNCodecUtil.Pair;
import com.lucene.plus.custom.codec.knn.vectors.cache.FieldDocValuesCache;
import com.lucene.store.Directory;
import com.lucene.store.FSDirectory; 

public class DocValuesUtils { 

	public static void getFloats(String indexPath, String field) {
		Directory dir = getDirectory(indexPath);
		try {
			DirectoryReader r = DirectoryReader.open(dir); 
			for (LeafReaderContext context : r.leaves()) {
				LeafReader reader = context.reader();
				BinaryDocValues dv = reader.getBinaryDocValues(field);
				Pair pair=KNNCodecUtil.getFloats(dv);
				FieldDocValuesCache.add(field, pair); 
			}
			r.close();
			dir.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
 

	private static Directory getDirectory(String indexPath) {
		File file = new File(indexPath);
		if (!file.exists()) {
			file.mkdir();
		}
		Path path = file.toPath();
		Directory dir = null;
		try {
			dir = FSDirectory.open(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dir;
	}
}
