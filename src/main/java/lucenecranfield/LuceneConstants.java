package lucenecranfield;

import org.apache.lucene.analysis.CharArraySet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to provide various constants to be used across the sample application
 */
public class LuceneConstants
{
    public static final String FILE_ID = "fileId";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String BIBLIOGRAPHY = "bibliography";
    public static final String CONTENTS = "contents";
//    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String OUTPUT_FILE_RELEVANCE = "\\output_relevance.txt";
    public static final String OUTPUT_FILE_RELEVANCE_ENGLISH_ANALYZER = "\\output_relevance_englishanalyzer.txt";
    public static final String OUTPUT_FILE_INDEXORDER = "\\output_indexorder.txt";
    public static final int MAX_SEARCH = 120;
    public static final List<String> stopWords = new ArrayList<String>(
            Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
                    "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
                    "that", "the", "their", "then", "there", "these", "they", "this", "to", "was",
                    "will", "with", "how", "what", "why", "where", "i.e.", "much", "so")
    );
    public static final CharArraySet stopWordsSet = new CharArraySet(stopWords, true);
}
