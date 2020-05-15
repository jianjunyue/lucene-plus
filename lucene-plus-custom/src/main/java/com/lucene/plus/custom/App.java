package com.lucene.plus.custom;

import com.lucene.document.IntPoint;
import com.lucene.search.Sort;
import com.lucene.search.SortField;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    { 
//    	IntPoint.newRangeQuery(field, lowerValue, upperValue)
        System.out.println( "Hello World!" );
    }
    
    /**
	 * 排序。 sort=id,INT,false
	 */
//	public Sort getSort(String strSort) {
//		try {
//			if (strSort == null || strSort.trim().length() == 0) {
//				return null;
//			}
//			String[] s = strSort.trim().split(",");
//			if (s.length == 2 || s.length == 3) {
//				boolean reverse = false;// 默认是小到大排序
//				if (s.length == 3) {
//					reverse = Boolean.valueOf(s[2].toLowerCase());
//				}
//				SortField.Type type = SortField.Type.INT;
//				Sort sort = new Sort(new SortField(s[0].toLowerCase(), type, reverse));
//				return sort;
//			} else { 
//			}
//		} catch (Exception e) { 
//		}
//		return null;
//	}
}
