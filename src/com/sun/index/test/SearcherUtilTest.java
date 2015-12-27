package com.sun.index.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
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
	
	@Test
	public void searchByQueryParser() throws ParseException{
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", 
				new StandardAnalyzer(Version.LUCENE_35));
		//将字符串首字符模糊匹配打开
		parser.setAllowLeadingWildcard(true);
		
		Query query = parser.parse("like");
		
		//区间匹配 中间TO必须为大写，[]为闭区间，{}为开区间
		query = parser.parse("id:[1 TO 3]");
		query = parser.parse("id:{1 TO 3}");
		
		//模糊匹配
		query = parser.parse("email:*@itat.org");
		
		query = parser.parse("name:make~");
		
		query = parser.parse("\"I football\"~2");
		
		query = parser.parse("-name:mike +football");
		query = parser.parse("NOT name:mike AND football");
		
		su.searchByQueryParser(query, 10);
	}
}
