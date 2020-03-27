package com.lucene.plus.custom;

import java.util.Iterator;
import java.util.Map;

import com.lucene.document.Document;
import com.lucene.document.Field;
import com.lucene.document.FieldType;
import com.lucene.document.NumericDocValuesField;
import com.lucene.document.SortedDocValuesField;
import com.lucene.document.StoredField;
import com.lucene.document.StringField;
import com.lucene.document.TextField;
import com.lucene.util.BytesRef; 
 
public class FieldDocument {
	 
	 public static Document getDocument(Map<String, String> map, Map<String, IndexField> mapProfile) {
	        Document doc = new Document();
	        Field field = null;
	        try {
	            Iterator<String> keys = mapProfile.keySet().iterator();
	            while (keys.hasNext()) {
	                String key = (String) keys.next();
	                if (map.containsKey(key)) {
						String value = map.get(key);
						value = value == null ? " " : value;
	                    IndexField indexField = mapProfile.get(key);
	                    Field.Store store = Field.Store.YES;
	                    boolean isIndex = true;
	                    boolean isAnalyzed = true;
	                    int weight = 0;
	                    // 是否索引
	                    if (indexField.getIndex() == IndexType.Yes) {
	                        if (indexField.getAnalyzed() != IndexType.Yes) {
	                            isAnalyzed = false;// 分词
	                        }
	                    } else {
	                        isIndex = false;
	                    }
	                    // 是否存储
	                    if (indexField.getStore() != IndexType.Yes) {
	                        store = Field.Store.NO;
	                    }
	                    field = getField(key.toLowerCase(), value, indexField.getFieldType(), store, isIndex, isAnalyzed);
	                   
	                   
	                    doc.add(field);
	                    if (indexField.getFieldType() == FieldType) {
							doc.add(new StoredField(key.toLowerCase(), value));
						}
	                }
	            }
	        } catch (Exception e) { 
	            doc = null;
	        }
	        return doc;
	    }
	    /**
	     * 得到Field
	     */
	    private static Field getField(String key, String value, FieldType dataType, Field.Store store, boolean isIndex, boolean isAnalyzed) {
	        Field field = new TextField(key, value, store);
	        String strDataType = dataType.toString();
	        if ("stringfield".equals(strDataType)) {
	            if (isIndex) {
	                if (isAnalyzed) {
	                	// value标准化
							field = new TextField(key, value, store);// TextField
	                    // 索引分词(存储或不存储)
	                } else {
	                    field = new StringField(key, value, store);// StringField
	                    // 索引不分词(存储或不存储)
	                }
	            } else {
	                field = new StoredField(key, value);// StoredField 不索引(存储或不存储)
	            }
	            // } else if ("intfield".equals(strDataType)) {
	            // field = new IntField(key, Integer.parseInt(value), store);//
	            // 索引但不分词
	            // } else if ("floatfield".equals(strDataType)) {
	            // field = new FloatField(key, Float.parseFloat(value), store);//
	            // 索引但不分词
	            // } else if ("doublefield".equals(strDataType)) {
	            // field = new DoubleField(key, Double.parseDouble(value), store);//
	            // 索引但不分词
	            // } else if ("longfield".equals(strDataType)) {
	            // field = new LongField(key, Long.parseLong(value), store);//
	            // 索引但不分词
	        } else if ("groupfield".equals(strDataType)) {
	            field = new SortedDocValuesField(key, new BytesRef(value));// 分组统计
	        } else if ("sortlongfield".equals(strDataType)) {
	            // field =new LongField(key ,MathUtil.longParse(value),
	            // LongFieldType.fieldType());// long sort
	            field = new NumericDocValuesField(key,  Long.parseLong(value));
	        } else {
	            field = new TextField(key, value, Field.Store.NO);
	        }
	        return field;
	    }

//	public static Document getDocument(Map<String, String> map, Map<String, IndexField> mapProfile) {
//		Document doc = new Document();
//		Field field = null;
//		try {
//			Iterator<String> keys = mapProfile.keySet().iterator();
//			while (keys.hasNext()) {
//				String key = (String) keys.next();
//				IndexField indexField = mapProfile.get(key);
//				String value = map.get(key);
//				value = value == null ? "" : value;
//				Store store = Field.Store.YES;
//				boolean isIndex = true;
//				boolean isAnalyzed = true;
//				int weight = 0;
//				// 是否索引
//				if (indexField.getIndex() == IndexType.Yes) {
//					if (indexField.getAnalyzed() != IndexType.Yes) {
//						isAnalyzed = false;// 分词
//					}
//				} else {
//					isIndex = false;
//				}
//
//				// 是否存储
//				if (indexField.getStore() != IndexType.Yes) {
//					store = Field.Store.NO;
//				}
//
//				field = getField(key.toLowerCase(), value, indexField.getFieldType(), store, isIndex, isAnalyzed);
//				weight = indexField.getWeight();
//				if (weight > 0) {
//					field.setBoost(weight);
//				} 
//				doc.add(field); 
//			}
//		} catch (Exception e) {
//			logger.error("FieldDocument getDocument is error", e);
//			doc = null;
//		}
//		return doc;
//	} 

	/**
	 * 得到Field
	 */
//	private static Field getField(String key, String value, FieldType dataType, Store store, boolean isIndex, boolean isAnalyzed) {
//		Field field = new TextField(key, value, store);
//		String strDataType = dataType.toString();
//		if ("stringfield".equals(strDataType)) {
//			if (isIndex) {
//				if (isAnalyzed) {
//					field = new TextField(key, value, store);// TextField
//					// 索引分词(存储或不存储)
//				} else {
//					field = new StringField(key, value, store);// StringField
//					// 索引不分词(存储或不存储)
//				}
//			} else {
//				field = new StoredField(key, value);// StoredField 不索引(存储或不存储)
//			}
//			// } else if ("intfield".equals(strDataType)) {
//			// field = new IntField(key, Integer.parseInt(value), store);//
//			// 索引但不分词
//			// } else if ("floatfield".equals(strDataType)) {
//			// field = new FloatField(key, Float.parseFloat(value), store);//
//			// 索引但不分词
//			// } else if ("doublefield".equals(strDataType)) {
//			// field = new DoubleField(key, Double.parseDouble(value), store);//
//			// 索引但不分词
//			// } else if ("longfield".equals(strDataType)) {
//			// field = new LongField(key, Long.parseLong(value), store);//
//			// 索引但不分词
////		} else if ("groupfield".equals(strDataType)) {
////			field = new SortedDocValuesField(key, new BytesRef(value));// 分组统计
//		} else if ("sortlongfield".equals(strDataType)) {
//			// field =new LongField(key ,MathUtil.longParse(value),
//			// LongFieldType.fieldType());// long sort
//			field = new NumericDocValuesField(key, MathUtil.longParse(value));
//		} else {
//			field = new StringField(key, value, store);
//		}
//		return field;
//	}
}
