package com.lucene.plus.custom.codec.knn.realtime;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lucene.plus.custom.codec.knn.util.FieldUtils;
import com.lucene.plus.custom.codec.knn.vectors.Vectors;
import com.lucene.plus.custom.codec.knn.vectors.field.VectorsStoredField;
import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.StringField;
import com.lucene.document.Field.Store;
import com.lucene.document.NumericDocValuesField;

public class RealIndexSearchManager {
	private static RealIndexSearchManager instance = new RealIndexSearchManager();

	private static int lastMemoryMinute = 0;
	private static int lastDiskHour = 0;

	public static RealIndexSearchManager getInstance() {
		return instance;
	}

	public void runIndex() {
		while (true) {
			try {
				Thread.sleep(2000);
				addIndex();
				memoryIndexChange();
				diskIndexChange();
			} catch (Exception e) {
				System.out.println("RealIndexSearchManager runIndex is error" + e.getMessage());
			}
		}
	}

	private void memoryIndexChange() {
		try {
			int second = LocalDateTime.now().getHour() * 3600 + LocalDateTime.now().getMinute() * 60
					+ LocalDateTime.now().getSecond();
			second = second / 5;
			if (lastMemoryMinute < second) {
				RealTimeIndexSearcherInstance.getInstance().changeMemoryIndexSearcher();

				RealTimeIndexSearcherInstance.getInstance().realChangeSearcher();

				lastMemoryMinute = second;
				System.out.println(LocalDateTime.now().toString() + " memoryIndex second:" + second);
			}
		} catch (Exception e) {
			System.out.println("RealIndexSearchManager memoryIndenChange is error" + e.getMessage());
		}
	}

	/**
	 * 磁盘索引处理
	 */
	private void diskIndexChange() {
		try {
			int minute = LocalDateTime.now().getHour() * 60 + LocalDateTime.now().getMinute();

			if (lastDiskHour < minute) {
				RealTimeIndexSearcherInstance.getInstance().changeMemoryIndexSearcher();
				IndexWriterInstance.getInstance().commit();

				RealTimeIndexSearcherInstance.getInstance().realChangeSearcher();
				lastDiskHour = minute;

				System.out.println(LocalDateTime.now().toString() + " lastDiskHour hour:" + minute);
			}
		} catch (Exception e) {
			System.out.println("RealIndexSearchManager diskIndenChange is error" + e.getMessage());
		}
	}

	private void addIndex() {
		try {
			String id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

			String indexPath = RealTimeIndexSearcherInstance.getInstance().getIndexPath();
			IndexWriterInstance.getInstance().addDataIndexDoc(indexDoc(id), id, indexPath);

		} catch (Exception e) {
			System.out.println("RealIndexSearchManager addIndex is error" + e.getMessage());
		}
	}

	private Document indexDoc(String id) throws IOException {
		Document doc = new Document();
		String name = "上海_" + id;
		float[] valuevectors = { Float.parseFloat(id), Float.parseFloat(id), 12.1f, 174.23f, 4.23f, 174.23f };
		NumericDocValuesField keyid_field = new NumericDocValuesField(FieldUtils.KEYID_FIELD, Integer.parseInt(id));//唯一主键id
		Field id_field = new StringField("id", id, Store.YES);
		Field name_field = new StringField("name", name, Store.YES);// StringField
		Field vec_field = new VectorsStoredField("vectorfiled", new Vectors(valuevectors));
		Field type_field = new StringField("type", name.startsWith("上海") ? "1" : "0", Store.YES);
		doc.add(id_field);
		doc.add(name_field);
		doc.add(vec_field);
		doc.add(type_field);
		doc.add(keyid_field);
		return doc;
	}
}
