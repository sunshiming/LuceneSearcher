package com.sun.index.test;

import org.junit.Test;

import com.sun.index.IndexUtil;

public class IndexUtilTest {

	@Test
	public void testIndex(){
		IndexUtil index = new IndexUtil();
		index.index();
	}
	
	@Test
	public void testQuery(){
		IndexUtil index = new IndexUtil();
		index.query();
	}
	
	@Test
	public void testSearch(){
		IndexUtil index = new IndexUtil();
		index.search();
	}
	
}
