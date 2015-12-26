package com.sun.index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexUtil {
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
	private Map<String, Float> score = new HashMap<String, Float>();
	
	public IndexUtil(){
		try{
			score.put("itat.org", 1.0f);
			score.put("zttc.edu", 1.5f);
			directory = FSDirectory.open(new File("d:/lucene/index02"));
		}catch(Exception e){
			e.printStackTrace();
		}
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
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				if(score.containsKey(et)){
					doc.setBoost(score.get(et));
				}
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

	public void query(){
		try {
			IndexReader reader = IndexReader.open(directory);
			System.out.println("numDocs:" + reader.numDocs());
			System.out.println("maxNum:" + reader.maxDoc());
			System.out.println("deleteDoc:" + reader.numDeletedDocs());
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void search(){
		try{
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher search = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("content", "like"));
			TopDocs tds = search.search(query, 10);
			
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = search.doc(sd.doc);
				System.out.println(sd.doc + "\t" +doc.getBoost() + "\t" + sd.score +"\t"+
						"email:" + doc.get("email") + "----->" +doc.get("id"));
			}
			
			reader.close();
			search.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void delete(){
		IndexWriter writer = null;
		try{
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, 
					new StandardAnalyzer(Version.LUCENE_35));
			writer = new IndexWriter(directory, config);
			writer.deleteDocuments(new Term("id", "1"));
			writer.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void forceDelete(){
		IndexWriter writer = null;
		try{
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, 
					new StandardAnalyzer(Version.LUCENE_35));
			writer = new IndexWriter(directory, config);
			writer.forceMergeDeletes();;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}












