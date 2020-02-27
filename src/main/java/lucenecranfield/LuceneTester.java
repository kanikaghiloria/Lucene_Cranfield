package lucenecranfield;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queries.function.valuesource.IDFValueSource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
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
public class LuceneTester {
    String indexDir = "D:\\Lectures\\sem 2\\Info Retreival and Web search\\LuceneData\\index";
    String documentsCollectionPath = "D:\\Lectures\\sem 2\\Info Retreival and Web search\\LuceneData\\CranfieldDocumentsCollection";
    String dataDir = "D:\\Lectures\\sem 2\\Info Retreival and Web search\\LuceneData\\cran.all.1400";
    String queriesDir = "D:\\Lectures\\sem 2\\Info Retreival and Web search\\LuceneData\\cran.qry";
    String outputDir = "D:\\Lectures\\sem 2\\Info Retreival and Web search\\LuceneCranfield18315430";
    Indexer indexer;
    Searcher searcher;
    HashMap<Integer, String> queries;
    File fout;

    /**
     *
     * @param args
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        LuceneTester tester;

        tester = new LuceneTester();
//        tester.createIndex();
//        tester.search("Mohan");
//        tester.search("aeronautical engineering");
//        tester.search("brenckman");
//        tester.search("experimental investigation of the aerodynamics of a wing in a slipstream");
//        tester.search("propeller slipstream");
//        tester.search("what chemical kinetic system is applicable to hypersonic aerodynamic problems");

//        tester.createIndex(new BM25Similarity());
//        tester.search(new BM25Similarity());

//        tester.createIndex(new ClassicSimilarity());
//        tester.search(new ClassicSimilarity());
//
        tester.createIndex(new LMDirichletSimilarity());
        tester.search(new LMDirichletSimilarity());

//        tester.search(new MultiSimilarity());
//        tester.search(new IBSimilarity());
//        tester.search(new DFRSimilarity());

////        tester.search(new LMJelinekMercerSimilarity());

    }
    public Analyzer getAnalyzer()
    {
//        return new StandardAnalyzer(LuceneConstants.stopWordsSet);
        return new EnglishAnalyzer(LuceneConstants.stopWordsSet);
//        return new StopAnalyzer(LuceneConstants.stopWordsSet);
//        return new StopAnalyzer(LuceneConstants.stopWordsSet);
//        return new SimpleAnalyzer();
    }

    /**
     *
     * @throws IOException
     */
    private void createIndex(Similarity similarity) throws IOException {
        indexer = new Indexer(indexDir, getAnalyzer(), similarity);
        IndexWriter.DocStats numIndexed;
        long startTime = System.currentTimeMillis();
//        indexer.createIndex(dataDir, documentsCollectionPath,new TextFileFilter());
        numIndexed = indexer.createIndex(dataDir, documentsCollectionPath);
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }

    /**
     *
     *
     * @throws IOException
     * @throws ParseException
     */
//    private void search(String searchQuery) throws IOException, ParseException, InvalidTokenOffsetsException {
    private void search(Similarity similarity) throws IOException, ParseException, InvalidTokenOffsetsException {

        int num =0;
//        fout = new File(outputDir + LuceneConstants.OUTPUT_FILE_INDEXORDER);
        fout = new File(outputDir + LuceneConstants.OUTPUT_FILE_RELEVANCE);
        fout = new File(outputDir + LuceneConstants.OUTPUT_FILE_RELEVANCE_ENGLISH_ANALYZER);
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
//                if(queryCount < 4)
//                {
//                    System.out.println(queryCount + " : " + queries.get(i));
//                    System.out.println("File id: " + doc.getField(LuceneConstants.FILE_ID).stringValue());
//                    System.out.println("File Path: " + doc.get(LuceneConstants.FILE_PATH));
//    //                System.out.println("File Title: " + doc.getField(LuceneConstants.TITLE).stringValue());
//    //                System.out.println("File Author: " + doc.getField(LuceneConstants.AUTHOR).stringValue());
//                    System.out.println("Score: " + scoreDoc.score);
//
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.TITLE, doc.get(LuceneConstants.TITLE));
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.AUTHOR, doc.get(LuceneConstants.AUTHOR));
//                    searcher.highlightResults(docid, getAnalyzer(), LuceneConstants.BIBLIOGRAPHY, doc.get(LuceneConstants.BIBLIOGRAPHY));
//                    searcher.highlightResults(docid, getAnalyzer(),LuceneConstants.CONTENTS, doc.get(LuceneConstants.CONTENTS));
//                    System.out.println("=======================================");
//                }

                bw.write(queryCount + " " + "Q0" + " " + doc.getField(LuceneConstants.FILE_ID).stringValue() + " " + num + " " + scoreDoc.score + " " +"STANDARD");
                bw.newLine();
                ctr++;
            }
            queryCount++;
        }
        bw.close();
    }

}
