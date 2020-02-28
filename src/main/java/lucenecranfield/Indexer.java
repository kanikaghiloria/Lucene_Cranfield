package lucenecranfield;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * This class is used to index the raw data so that we can make it searchable using the Lucene library
 */
public class Indexer
{
    private IndexWriter writer;
    private Directory indexDirectory;
    File fin, fout;

    public Indexer(String indexDirectoryPath, Analyzer analyzer, Similarity similarity) throws IOException
    {
        //this directory will contain indexes
        indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
//        String [] list = indexDirectory.listAll();
//        for(int i=0; i<list.length; i++)
//        {
//            indexDirectory.deleteFile(list[i]);
//        }

        //create the indexer
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        cfg.setSimilarity(similarity);
        writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer));
    }

    public void close() throws IOException { writer.close(); }

    private Document getDocument(File file) throws IOException
    {
        Document document = new Document();
        Field fileIdField, titleField, authorField, bibliographyField, filePathField;
        String[] id = null;
        StringBuilder title = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder bibliography = new StringBuilder();
        StringBuilder text = new StringBuilder();
        File wordFile;
        FileOutputStream fosW;
        BufferedWriter bwW;

        //Index file contents

//        wordFile = File.createTempFile("wordFile", ".txt", new File("D:\\Lectures\\sem 2\\Info Retreival and Web search"));
        wordFile = new File("DocumentsCollection/contentFile");
        fosW = new FileOutputStream(wordFile);
        bwW = new BufferedWriter(new OutputStreamWriter(fosW));
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while(line != null)
        {
            if(Pattern.matches("[.][I][ ][0-9]+", line))
            {
                id = line.split("[ ]");
                line = br.readLine();
            }
            if (Pattern.matches("[.][T]", line))
            {
                line = br.readLine();
                while (!Pattern.matches("[.][A]", line)) {
                    title.append(" ").append(line);
//                   System.out.println(".T: " + line);
                   line = br.readLine();
               }
            }
            if (Pattern.matches("[.][A]", line))
            {
                line = br.readLine();
                while (!Pattern.matches("[.][B]", line)) {
                    author.append(" ").append(line);
//                    System.out.println(".A: " + line);
                    line = br.readLine();
                }
            }
            if (Pattern.matches("[.][B]", line))
            {
                line = br.readLine();
                while (!Pattern.matches("[.][W]", line)) {
                    bibliography.append(" ").append(line);
//                    System.out.println(".B: " + line);
                    line = br.readLine();
                }
            }
            if (Pattern.matches("[.][W]", line))
            {
                line = br.readLine();
                while (line != null) {
                    text.append(line).append("\n");
                    bwW.write(line);
                    bwW.newLine();
//                    System.out.println(".W: " + line);
                    line = br.readLine();
                }
                bwW.close();
            }
        }
        br.close();

        filePathField = new StringField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES);
        fileIdField = new StringField(LuceneConstants.FILE_ID, Objects.requireNonNull(id)[1], Field.Store.YES); //        System.out.println("fileIdField: " + fileIdField.stringValue());
        titleField = new TextField(LuceneConstants.TITLE, title.toString(), Field.Store.YES);//        System.out.println("titleField: " + titleField.stringValue());
        authorField = new TextField(LuceneConstants.AUTHOR, author.toString(), Field.Store.YES);//        System.out.println("authorField: " + authorField.stringValue());
        bibliographyField = new TextField(LuceneConstants.BIBLIOGRAPHY, bibliography.toString(), Field.Store.YES);//        System.out.println("bibliographyField: " + bibliographyField.stringValue());
//        Field contentField = new TextField(LuceneConstants.CONTENTS, new FileReader(wordFile));//        System.out.println("contentField: " + contentField.stringValue());
        Field contentField = new TextField(LuceneConstants.CONTENTS, text.toString(), Field.Store.YES);//        System.out.println("contentField: " + contentField.stringValue());

        //index file path
        document.add(filePathField);
        document.add(fileIdField);
        document.add(titleField);
        document.add(authorField);
        document.add(bibliographyField);
        document.add(contentField);

        wordFile.deleteOnExit();

        return document;
    }

    private void indexFiles(File file) throws IOException {
//        System.out.println("Indexing "+file.getCanonicalPath());
        Document document = getDocument(file);
                                                                                    //        List<IndexableField> fields = document.getFields();
                                                                                    //        for(int i = 0; i < fields.size(); i++)
                                                                                    //        {
                                                                                    //            IndexableField f = fields.get(i);
                                                                                    //            System.out.println(f.name() + " : stringValue: " + f.stringValue());
                                                                                    //            if (f.name() == "contents") {
                                                                                    //                try {
                                                                                    //                    Reader reader = f.readerValue();
                                                                                    //                    int data = reader.read();
                                                                                    //                    while (data != -1) {
                                                                                    //                        System.out.print((char) data);
                                                                                    //                        data = reader.read();
                                                                                    //                    }
                                                                                    ////                    reader.close();
                                                                                    //                } catch (Exception e){
                                                                                    //                    System.out.println(e.getMessage());
                                                                                    //                }
                                                                                    //            }

                                                                                    //        }
//                                                                                            System.out.println("document: " + document);
//        writer.addDocument(document);
        writer.updateDocument(new Term("filePath", file.getCanonicalPath()), document);
    }

    public IndexWriter.DocStats createIndex(String dataDirPath, String docCollectionPath) {
//    public void createIndexNew(String dataDirPath, FileFilter filter) throws IOException {
        try {
            fin = new File(dataDirPath);
            BufferedReader br = new BufferedReader(new FileReader(fin));
            int i = 1;
            String line = br.readLine();
            while(line != null)
            {
                fout = new File(docCollectionPath + "/doc" + i);
//                if (fout.createNewFile()) {
//                                                                                            //                    System.out.println("File created: " + fout.getName());
//                } else {
//                                                                                            //                    System.out.println("File already exists");
//                }
                FileOutputStream fos = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                 do {
                     bw.write(line);
                     bw.newLine();
                     line = br.readLine();
                }while (line!= null && (!Pattern.matches("[.][I][ ][0-9]+", line)));
                bw.close();
                i++;
                indexFiles(fout);
//                                                                                                                    if (i ==3)
//                                                                                                                        break;
            }
        }catch (IOException e)
        {System.out.println("An error occurred.");
            e.printStackTrace();}
        return writer.getDocStats();
    }
}
