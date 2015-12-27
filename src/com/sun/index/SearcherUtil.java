package com.sun.index;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SearcherUtil {
	private String[] ids = {"1", "2", "3", "4", "5", "6"};
	private String[] emails = {"aa@itat.org", "bb@itat.org", "cc@itat.org", "dd@itat.org", 
			"ee@zttc.edu", "ff@sina.com"};
	private String[] contents = {
			"welcome to vidited the space I like bike", 
			"hello boy I like run", 
			"my name is cc I like jump", 
			"I like football",
			"I like football and I like basketball too", 
			"I like movie and swim"
	};
//	private int[] attachs = {2, 3, 1, 4, 5, 5};
	private String[] names = {"zhangsan", "lisi", "john", "jetty", "mike", "jake"};
	private Directory directory = null;
	private IndexReader reader = null;
	
	public SearcherUtil(){
		directory = new RAMDirectory();
		index();
	}
	
	public void index(){
		IndexWriter writer = null;
		try{
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			writer = new IndexWriter(directory, config);
			writer.deleteAll();
			Document doc = null;
			
			for(int i=0; i < contents.length; i++){
				doc = new Document();
				doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", contents[i], Field.Store.NO, Field.Index.ANALYZED));
				doc.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
//				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				writer.addDocument(doc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 精确搜索
	 * */
	public void searchByTerm(String field, String name, int num){
		try{
			IndexSearcher search = getSearcher();
			TermQuery query = new TermQuery(new Term(field, name));
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * TermRange搜索
	 * */
	public void searchByTermRange(String field, String start, String end, int num){
		try{
			IndexSearcher search = getSearcher();
			Query query = new TermRangeQuery(field, start, end, true, true);
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 前缀搜索
	 * */
	public void searchByPrefix(String field, String value, int num){
		try{
			IndexSearcher search = getSearcher();
			Query query = new PrefixQuery(new Term(field, value));
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 通配符搜索
	 * */
	public void searchByWildcard(String field, String value, int num){
		try{
			IndexSearcher search = getSearcher();
			Query query = new WildcardQuery(new Term(field, value));
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void searchByBoolean(int num){
		try{
			IndexSearcher search = getSearcher();
			BooleanQuery query = new BooleanQuery();
			query.add(new TermQuery(new Term("name", "jake")), Occur.MUST);
			query.add(new TermQuery(new Term ("content", "game")), Occur.MUST);
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void searchByPhrase(int num){
		try{
			IndexSearcher search = getSearcher();
			PhraseQuery query = new PhraseQuery();
			query.setSlop(1);
			query.add(new Term("content", "i"));
			query.add(new Term ("content", "football"));
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void searchByFuzzy(int num){
		try{
			IndexSearcher search = getSearcher();
			FuzzyQuery query = new FuzzyQuery(new Term("name", "make"));
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void searchByQueryParser(Query query, int num){
		try{
			IndexSearcher search = getSearcher();
			TopDocs tds = search.search(query, num);
			System.out.println("一共查询出" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				outPutDocument(sd, doc);
			}
			
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public IndexSearcher getSearcher(){
		try{
			if(null == reader){
				reader = IndexReader.open(directory);
			}else{
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(null != tr){
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 输出检索内容
	 * */
	private void outPutDocument(ScoreDoc sd, Document doc){
		System.out.println(sd.doc + "\t" +doc.getBoost() + "\t" + sd.score +"\t" +
				"name:" + doc.get("name") + "\t" +
				"email:" + doc.get("email") + "----->" +doc.get("id"));
	}
}












