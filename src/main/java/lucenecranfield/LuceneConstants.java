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
    public static final String OUTPUT_FILE_RELEVANCE = "/output_relevance.txt";
    public static final String OUTPUT_FILE_RELEVANCE_ENGLISH_ANALYZER = "/output_relevance_englishanalyzer.txt";
    public static final String OUTPUT_FILE_INDEXORDER = "/output_indexorder.txt";
    public static final int MAX_SEARCH = 120;
//    public static final List<String> stopWords = new ArrayList<String>(
//            Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
//                    "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
//                    "that", "the", "their", "then", "there", "these", "they", "this", "to", "was",
//                    "will", "with", "how", "what", "why", "where", "i.e.", "much", "so")
//    );
    public static final List<String> stopWords = new ArrayList<>(
        Arrays.asList("a", "an", "and", "are", "as", "at", "about", "above", "after", "again", "against", "all", "am",
                "any", "aren't", "because", "be", "but", "by", "been", "before", "being", "below", "between",
                "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during",
                "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
                "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's",
                "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's",
                "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other",
                "ought", "our", "ours", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was",
                "will", "with", "how", "what", "why", "where", "i.e.", "much", "so")
);
    public static final CharArraySet stopWordsSet = new CharArraySet(stopWords, true);
}
