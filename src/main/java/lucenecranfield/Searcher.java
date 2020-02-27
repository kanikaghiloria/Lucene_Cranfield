package lucenecranfield;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * This class is used to search the indexes created by the Indexer to search the requested content
 */
public class Searcher
{
    IndexSearcher indexSearcher;
    Directory indexDirectory;
    QueryParser queryParser;
    IndexReader reader;
    Query query;
    HashMap<Integer, String> queries;
    /**
     *
     * @param indexDirectoryPath
     * @throws IOException
     */
    public Searcher(String indexDirectoryPath, Analyzer analyzer, Similarity similarity) throws IOException {
        indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(similarity);

        System.out.println("similarity: " + indexSearcher.getSimilarity().toString());
//        queryParser = new QueryParser(LuceneConstants.CONTENTS, analyzer);
        queryParser = new MultiFieldQueryParser(new String[]{LuceneConstants.TITLE, LuceneConstants.AUTHOR, LuceneConstants.BIBLIOGRAPHY, LuceneConstants.CONTENTS}, analyzer);
//        queryParser = new MultiFieldQueryParser(new String[]{LuceneConstants.CONTENTS}, analyzer);
    }
    /**
     *
     * @param searchQuery
     * @param indexorder
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public TopDocs search(String searchQuery, Sort indexorder) throws ParseException, IOException {
        searchQuery = searchQuery.trim();
        searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9\\s.-]","");
        query = queryParser.parse(searchQuery);
//        System.out.println("Searching for: " + searchQuery);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }
    /**
     *
     * @param scoreDoc
     * @return
     * @throws CorruptIndexException
     * @throws IOException
     */
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void highlightResults(int docID, Analyzer analyzer, String fieldName, String text) throws IOException, InvalidTokenOffsetsException {
        Formatter formatter = new SimpleHTMLFormatter();
        QueryScorer scorer = new QueryScorer(this.query);
        Highlighter highlighter = new Highlighter(formatter, scorer);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
        highlighter.setTextFragmenter(fragmenter);
//        TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(text));
        TokenStream tokenStream = TokenSources.getAnyTokenStream(this.reader, docID, fieldName, analyzer);
        String[] frags = highlighter.getBestFragments(tokenStream, text, 100);
//        String[] frags = highlighter.getBestFragments(analyzer, fieldName, text, 100000);
//        String str = highlighter.getBestFragment(tokenStream, text);
//        System.out.println(str);
        for (String frag : frags)
        {
            System.out.println("=======================");
            System.out.println("In " + fieldName +" >>>> "+ frag);
        }
    }

    public HashMap<Integer, String> returnQueries(String path) throws IOException {
        File queryFile = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(queryFile));
        queries = new HashMap<Integer, String>();
        String line = br.readLine();
        int i=0;
        while(line != null)
        {
            String[] str = null;
            String query = "";
            if (Pattern.matches("[.][I][ ][0-9]+", line)) {
                str = line.split("[ ]");
                line = br.readLine();
            }
            if (Pattern.matches("[.][W]", line))
            {
                line = br.readLine();
                while (line != null && !Pattern.matches("[.][I][ ][0-9]+", line))
                {
                    query = query + " " + line;
//                   System.out.println(".T: " + line);
                    line = br.readLine();
                }
            }
            queries.put(Integer.parseInt(str[1]), query);

//            i++;
//            if(i == 4)
//                break;
        }
                                                                                        //        for (Integer ctr : queries.keySet()) {
                                                                                        //            System.out.println("key: " + ctr + " value: " + queries.get(ctr));
                                                                                        //        }
        return queries;
    }

}
