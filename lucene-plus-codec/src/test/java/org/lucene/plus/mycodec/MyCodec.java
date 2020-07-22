package org.lucene.plus.mycodec;
 
import com.lucene.codecs.FilterCodec;
import com.lucene.codecs.lucene84.Lucene84Codec; 

public class MyCodec extends FilterCodec {
	
	static {
		System.out.println("Hello from MyCodec!");
	}

	public MyCodec() {
		super("MyCodec", new Lucene84Codec());
		// TODO Auto-generated constructor stub
	}


}
