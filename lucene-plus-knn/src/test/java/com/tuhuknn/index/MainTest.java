package com.tuhuknn.index;

import java.io.IOException;

public class MainTest {

	public static void main(String[] args) throws IOException { 
		IndexTest.IndexFiles();
		SearchTest.search();
		IndexTest.updateIndexFiles();
		SearchTest.search();
		
	}

}
