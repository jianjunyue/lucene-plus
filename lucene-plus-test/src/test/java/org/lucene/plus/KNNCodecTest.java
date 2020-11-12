/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.lucene.plus;

import com.amazon.opendistroforelasticsearch.knn.KNNTestCase;
import com.amazon.opendistroforelasticsearch.knn.index.KNNIndexCache;
import com.amazon.opendistroforelasticsearch.knn.index.KNNQuery;
import com.amazon.opendistroforelasticsearch.knn.index.KNNSettings;
import com.amazon.opendistroforelasticsearch.knn.index.KNNVectorFieldMapper;
import com.amazon.opendistroforelasticsearch.knn.index.VectorField;
import com.amazon.opendistroforelasticsearch.knn.index.codec.KNNCodecUtil;
import com.amazon.opendistroforelasticsearch.knn.index.codec.KNN86Codec.KNN86Codec;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.CodecUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ChecksumIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.watcher.ResourceWatcherService;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test used for testing Codecs
 */
public class  KNNCodecTest   {
	public static String indexPath = "D:\\data\\index\\amazon";
	private static IndexWriter writer;
	private static Path file = Paths.get(indexPath);;

	public static void main(String[] args)  {
		try {
		testFooter(new KNN86Codec());
		testMultiFieldsKnnIndex(new KNN86Codec());
		}catch(Exception e) { 
			e.printStackTrace();
		}
		System.out.println("end");
	}

	public static void testFooter(Codec codec) throws Exception {
		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		iwc.setCodec(codec);
		writer = new IndexWriter(dir, iwc);
		writer.deleteAll();
		writer.commit();

		float[] array = { 1.0f, 2.0f, 3.0f };
		VectorField vectorField = new VectorField("test_vector", array, KNNVectorFieldMapper.Defaults.FIELD_TYPE);
//        RandomIndexWriter writer = new RandomIndexWriter(random(), dir, iwc);
		Document doc = new Document();
		doc.add(vectorField);
		writer.addDocument(doc);
		writer.commit();

//        KNNIndexCache.setResourceWatcherService(createDisabledResourceWatcherService());
		IndexReader reader = getReader();
		LeafReaderContext lrc = reader.getContext().leaves().iterator().next(); // leaf reader context
		SegmentReader segmentReader = (SegmentReader) FilterLeafReader.unwrap(lrc.reader());
		String hnswFileExtension = segmentReader.getSegmentInfo().info.getUseCompoundFile()
				? KNNCodecUtil.HNSW_COMPOUND_EXTENSION
				: KNNCodecUtil.HNSW_EXTENSION;
		String hnswSuffix = "test_vector" + hnswFileExtension;
		Collection<String> temp= segmentReader.getSegmentInfo().files();
		List<String> hnswFiles = segmentReader.getSegmentInfo().files().stream()
				.filter(fileName -> fileName.endsWith(hnswSuffix)).collect(Collectors.toList());
//		Assert.assertTrue(!hnswFiles.isEmpty());
//		ChecksumIndexInput indexInput = dir.openChecksumInput(hnswFiles.get(0), IOContext.DEFAULT);
//		indexInput.seek(indexInput.length() - CodecUtil.footerLength());
//		CodecUtil.checkFooter(indexInput); // If footer is not valid, it would throw exception and test fails
//		indexInput.close();

		IndexSearcher searcher = new IndexSearcher(reader);
		int count=searcher.count(new KNNQuery("test_vector", new float[] { 1.0f, 2.5f }, 1, "myindex"));
//		Assert.assertEquals(1, searcher.count(new KNNQuery("test_vector", new float[] { 1.0f, 2.5f }, 1, "myindex")));

		reader.close();
		writer.close();
		dir.close();
	}

	public static void testMultiFieldsKnnIndex(Codec codec) throws Exception {

		Directory dir = FSDirectory.open(file);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setMergeScheduler(new SerialMergeScheduler());
		iwc.setCodec(codec);

		/**
		 * Add doc with field "test_vector"
		 */
		float[] array = { 5.0f, 3.0f, 4.0f };
		VectorField vectorField = new VectorField("test_vector", array, KNNVectorFieldMapper.Defaults.FIELD_TYPE);

		writer = new IndexWriter(dir, iwc);
		writer.deleteAll();
		writer.commit();
		Document doc = new Document();
		doc.add(vectorField);
		writer.addDocument(doc);
		writer.close();

		/**
		 * Add doc with field "my_vector"
		 */
		IndexWriterConfig iwc1 = new IndexWriterConfig(analyzer);
		iwc1.setMergeScheduler(new SerialMergeScheduler());
		iwc1.setCodec(new KNN86Codec());
		writer = new IndexWriter(dir, iwc1);
		float[] array1 = { 6.0f, 14.0f };
		VectorField vectorField1 = new VectorField("my_vector", array1, KNNVectorFieldMapper.Defaults.FIELD_TYPE);
		Document doc1 = new Document();
		doc1.add(vectorField1);
		writer.addDocument(doc1);
		IndexReader reader = getReader();
		writer.close();
//        KNNIndexCache.setResourceWatcherService(createDisabledResourceWatcherService());
		List<String> hnswfiles = Arrays.stream(dir.listAll()).filter(x -> x.contains("hnsw"))
				.collect(Collectors.toList());

		// there should be 2 hnsw index files created. one for test_vector and one for
		// my_vector
		Assert.assertEquals(hnswfiles.size(), 2);
		Assert.assertEquals(
				hnswfiles.stream().filter(x -> x.contains("test_vector")).collect(Collectors.toList()).size(), 1);
		Assert.assertEquals(hnswfiles.stream().filter(x -> x.contains("my_vector")).collect(Collectors.toList()).size(),
				1);

		// query to verify distance for each of the field
		IndexSearcher searcher = new IndexSearcher(reader);
		float score = searcher.search(new KNNQuery("test_vector", new float[] { 1.0f, 0.0f, 0.0f }, 1, "dummy"),
				10).scoreDocs[0].score;
		float score1 = searcher.search(new KNNQuery("my_vector", new float[] { 1.0f, 2.0f }, 1, "dummy"),
				10).scoreDocs[0].score;
		Assert.assertEquals(score, 0.1667f, 0.01f);
		Assert.assertEquals(score1, 0.0714f, 0.01f);

		// query to determine the hits
		Assert.assertEquals(1,
				searcher.count(new KNNQuery("test_vector", new float[] { 1.0f, 0.0f, 0.0f }, 1, "dummy")));
		Assert.assertEquals(1, searcher.count(new KNNQuery("my_vector", new float[] { 1.0f, 1.0f }, 1, "dummy")));

		reader.close();
		dir.close();
	}

	private static IndexReader getReader() {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(file));
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return reader;
	}
}

