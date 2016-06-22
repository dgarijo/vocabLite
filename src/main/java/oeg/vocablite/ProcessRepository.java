/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.vocablite;

import java.io.File;
import java.util.ArrayList;

/**
 * Class for processing the input file with the URIs and domains of the vocabs
 * The path of the vocabularies is relative.
 * @author dgarijo
 */
public class ProcessRepository {
    
    public static ArrayList<Vocabulary> processRepositoryFolder(String path){
        ArrayList<Vocabulary> vocabs = new ArrayList<>();
        File dir = new File (path);
        if(dir.exists() && dir.isDirectory()){
            getVocabList(dir, vocabs);
        }else{
            System.err.println("The directory sent as parameter could not be read");
        }
        return vocabs;
    }
    
    private static void getVocabList(File dir, ArrayList<Vocabulary> vocabs){
        for (File f:dir.listFiles()){
            if(f.isDirectory()){
                getVocabList(f, vocabs);
            }else{
                //we only admit .ttl, .rdf, .owl, .xml, .rdfs
                String p = f.getAbsolutePath();
                if(p.endsWith(".owl")||p.endsWith(".rdf")||p.endsWith(".ttl")||p.endsWith(".xml")||p.endsWith(".rdfs")){
                    processVocab(f, vocabs);
                    //System.out.println(p);
                }
            }
        }
        
    }
    
    /**
     * Method that given a path of a vocab, it processes and adds it to the
     * arrayList send as reference.
     * @param vocab 
     * @param vocabs 
     */
    private static void processVocab(File vocab, ArrayList<Vocabulary> vocabs){
        try{
            System.out.println("\nDealing with Vocabulary "+vocab.getName());
            Vocabulary v = VocabUtils.getVocabularyMetadata(vocab);
            if(v!=null){
                 vocabs.add(v);
                if(v.getDescription()!=null && !v.getDescription().equals("") &&
                        v.getTitle()!=null && !v.getTitle().equals("")){
                    Report.getInstance().addSuccessfulEntry(v.getUri());
                }else{
                    Report.getInstance().addWarningForVocab(v.getUri(), TextConstants.Warning.MISSING_TITLE_OR_DESC_FOR_VOCAB);
                }
            }else{
                Report.getInstance().addErrorForVocab(v.getUri(), TextConstants.Error.PARSING_ERR);
            }
        }
        catch(Exception e){
            System.out.println("Error while dealing with vocab: "+vocab.getName()+" "+e.getMessage());
            Report.getInstance().addErrorForVocab(vocab.getName(), TextConstants.Error.EXCEPTION_ERROR);
            //e.printStackTrace();
        }
       }
    
//    public static void main(String[] args){
//        //processCSV(ProcessCSVFile.class.getResource("/vocab/test.csv").getPath());
//        processRepositoryFolder("repoTest");
//    }
    
}
