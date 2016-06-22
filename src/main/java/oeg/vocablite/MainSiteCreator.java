/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package oeg.vocablite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for processing vocabularies and converting them to HTML.
 * Input: csv with 2 columns: URI of vocab and domains (separated by ,)
 * Input 2: Path where you want the output
 * Output: an html file with the table summarizing all the vocabs
 * @author dgarijo and mpoveda
 */
public class MainSiteCreator {
    public static void createFolderStructure(String savePath){
        VocabUtils.unZipIt(TextConstants.vocabResources, savePath);
        //copy vocab on the Ontologies folder
        File ont = new File( savePath+File.separator+TextConstants.ontologyFolder);
        ont.mkdirs();
        VocabUtils.unZipIt(TextConstants.oopsResources, ont.getAbsolutePath());
        
//        File reportFolder = new File (savePath+File.separator+TextConstants.reportFolder);
//        reportFolder.mkdirs(); 
//        VocabUtils.unZipIt(TextConstants.vocabResources, reportFolder.getAbsolutePath());
    }
    public static void main(String[] args) throws IOException{
        String pathToRepo = "";
//        if(args.length<2){
//            System.out.println("Usage: java-jar vocab.jar -i input repository folder [-o outputDirectoryPath -n name of the repository -oops]");oops will activate/deactivate oops eval
//            return;
//        }
//        pathToRepo = args[1];
        int argNumber = 0;
        String outputFile = null;
        boolean useOOPS = false;
        String repoName = "Repository";
        try{
            while(argNumber< args.length){
                String s = args[argNumber];
                if(s.equals("-i")){
                    argNumber++;
                    pathToRepo = args[argNumber];
                }else if(s.equals("-o")){
                    argNumber++;
                    outputFile = args[argNumber];
                }else if(s.equals("-oops")){
                    useOOPS = true;
                }else if(s.equals("-n")){
                    argNumber++;
                    repoName = args[argNumber];
                }
                argNumber++;
            }
        }catch(Exception e){
            System.out.println("Wrong input usage. Usage: java-jar vocab.jar -i input repository folder [-o outputDirectoryPath -n name of the repository -oops]");
        }
//        //to do: add the repo name if known
        
//        if(args.length==4 && args[2].equals("-o")){
//            outputFile = args[3];
//        }
        //for local tests
        pathToRepo = "C:\\Users\\dgarijo\\Dropbox (OEG-UPM)\\NetBeansProjects\\vocabLite\\repoTest";
        
        //end tests        
        String urlOut = "";
        File outputFolder;
        if(outputFile==null){
            outputFolder = new File(urlOut);
            outputFolder = new File (outputFolder.getAbsolutePath()+File.separator+TextConstants.getsiteFolderName());
            outputFolder.mkdirs();
        }else{
            outputFolder = new File(outputFile);
            outputFolder.mkdirs();
        }
        MainSiteCreator.createFolderStructure(outputFolder.getAbsolutePath());
        String catalogOutPath = outputFolder.getAbsolutePath()+File.separator+TextConstants.siteName;
        String urlReportOut = outputFolder.getAbsolutePath()+File.separator+TextConstants.reportName;
        String html = TextConstants.header+TextConstants.getNavBarVocab(repoName);
        html+=TextConstants.tableHeadVocab;
        try{
            //ArrayList<Vocabulary> vocs = processCSV(ProcessCSVFile.class.getResource("/vocab/test.csv").getPath());
            ArrayList<Vocabulary> vocs = ProcessRepository.processRepositoryFolder(pathToRepo);
//            ArrayList<String> domains = new ArrayList();
            int i=1;
            for(Vocabulary v:vocs){
                html+=v.getHTMLSerializationAsRow(""+i, outputFolder);
                i++;                  
            }
            html+=TextConstants.tableEnd+TextConstants.end;//+TextConstants.getScriptForFilteringAndEndDocument(domains);
            VocabUtils.saveDocument(catalogOutPath, html);
            Report.getInstance().saveReport(urlReportOut);
        }catch(Exception e){
            System.err.println("Could not create the site: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
}
