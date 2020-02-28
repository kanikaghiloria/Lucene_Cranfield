package lucenecranfield;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.similarities.*;

import java.io.*;
import java.util.HashMap;

/**
 * This class is used to test the indexing and search capability of lucene library
 */
public class LuceneCranfieldMain {
    String indexDir = "index";
    String documentsCollectionPath = "DocumentsCollection";
    String dataDir = "cran.all.1400";
    String queriesDir = "cran.qry";
//    String outputDir = "src/main/java/lucenecranfield/";
    String outputDir = "outputQrel.txt";
    Indexer indexer;
    Searcher searcher;
    HashMap<Integer, String> queries;
    File fout;

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        LuceneCranfieldMain tester;

        tester = new LuceneCranfieldMain();
//        tester.createIndex();
//        tester.search("Mohan");
//        tester.search("aeronautical engineering");
//        tester.search("brenckman");
//        tester.search("propeller slipstream");
//        tester.search("what chemical kinetic system is applicable to hypersonic aerodynamic problems");

        tester.createIndex(new BM25Similarity());
        tester.search(new BM25Similarity());

//        tester.createIndex(new ClassicSimilarity());
//        tester.search(new ClassicSimilarity());
//
//        tester.createIndex(new LMDirichletSimilarity());
//        tester.search(new LMDirichletSimilarity());

    }
    public Analyzer getAnalyzer()
    {
        return new EnglishAnalyzer(LuceneConstants.stopWordsSet);
//        return new StandardAnalyzer(LuceneConstants.stopWordsSet);
//        return new StopAnalyzer(LuceneConstants.stopWordsSet);
    }

    private void createIndex(Similarity similarity) throws IOException {
        indexer = new Indexer(indexDir, getAnalyzer(), similarity);
        IndexWriter.DocStats numIndexed;
        long startTime = System.currentTimeMillis();
//        indexer.createIndex(dataDir, documentsCollectionPath,new TextFileFilter());
        numIndexed = indexer.createIndex(dataDir, documentsCollectionPath);
        long endTime = System.currentTimeMillis();
        indexer.close();
//        System.out.println(numIndexed+" File indexed, time taken: "
//                +(endTime-startTime)+" ms");
    }

    private void search(Similarity similarity) throws IOException, ParseException {

        int num =0;
//        fout = new File(outputDir + LuceneConstants.OUTPUT_FILE_INDEXORDER);
//        fout = new File(outputDir + LuceneConstants.OUTPUT_FILE_RELEVANCE);
        fout = new File(outputDir);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        searcher = new Searcher(indexDir, getAnalyzer(), similarity);
        queries = searcher.returnQueries(queriesDir);
        int queryCount = 1;
        for(Integer i : queries.keySet())
        {
//            if (queryCount == 4)
//                break;
            long startTime = System.currentTimeMillis();
//            TopDocs hits = searcher.search(queries.get(i), Sort.INDEXORDER);
            TopDocs hits = searcher.search(queries.get(i), Sort.RELEVANCE );
            long endTime = System.currentTimeMillis();
//            System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));
            int ctr = 0;
            for(ScoreDoc scoreDoc : hits.scoreDocs) {
                int docid = hits.scoreDocs[ctr].doc;

                Document doc = searcher.getDocument(scoreDoc);

//                if(queryCount < 4) {
//                    System.out.println(queryCount + " : " + queries.get(i));
//                    System.out.println("File id: " + doc.getField(LuceneConstants.FILE_ID).stringValue());
//                    System.out.println("File Path: " + doc.get(LuceneConstants.FILE_PATH));
//
//                    //                System.out.println("File Title: " + doc.getField(LuceneConstants.TITLE).stringValue());
//                    //                System.out.println("File Author: " + doc.getField(LuceneConstants.AUTHOR).stringValue());
//                    System.out.println("Score: " + scoreDoc.score);
//
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.TITLE, doc.get(LuceneConstants.TITLE));
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.AUTHOR, doc.get(LuceneConstants.AUTHOR));
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.BIBLIOGRAPHY, doc.get(LuceneConstants.BIBLIOGRAPHY));
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.CONTENTS, doc.get(LuceneConstants.CONTENTS));
//                    System.out.println("=======================================");
//                }

                bw.write(queryCount + " " + "Q0" + " " + doc.getField(LuceneConstants.FILE_ID).stringValue() + " " + num++ + " " + scoreDoc.score + " " +"STANDARD");
                bw.newLine();
                ctr++;
            }
            queryCount++;
        }
        bw.close();
    }

}
