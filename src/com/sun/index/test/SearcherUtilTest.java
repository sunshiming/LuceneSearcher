package com.sun.index.test;

import org.junit.Before;
import org.junit.Test;

import com.sun.index.SearcherUtil;

public class SearcherUtilTest {

	private SearcherUtil su;
	
	@Before
	public void init(){
		su = new SearcherUtil();
	}
	
	@Test
	public void searchTest(){
		su.searchByTerm("name", "mike", 3);
	}
	
	@Test
	public void searchByTermTest(){
		su.searchByTermRange("id", "1", "3", 3);
	}
	
	@Test
	public void searchByPrefixTest(){
		su.searchByPrefix("name", "j", 3);
	}
	
	@Test
	public void searchByWildcard(){
		su.searchByWildcard("name", "j???", 3);
	}
	
	@Test
	public void searchByBoolean(){
		su.searchByBoolean(3);
	}
	
	@Test
	public void searchByPhrase(){
		su.searchByPhrase(3);
	}
	
	@Test
	public void searchByFuzzy(){
		su.searchByFuzzy(3);
	}
}
