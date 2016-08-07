/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import oeg.vocablite.TextConstants.Warning;
import oeg.vocablite.TextConstants.Error;

/**
 * This class will log whther a vocabulary has failed or not.
 * Similar to a logger, but simplified.
 * @author Daniel Garijo
 */
public class Report {
    private static Report r;
    private final OntModel reportModel;
    private final String date;
    private final ArrayList<String> vocabsOk;
    private final ArrayList<String> vocabsWithErrors;
    private final ArrayList<String> vocabsWithWarnings;
    private final HashMap<String, ArrayList<String>> vocabProblems;
    
    protected Report(){
        vocabsWithErrors = new ArrayList<>();
        vocabsWithWarnings = new ArrayList<>();
        vocabsOk = new ArrayList<>();
        reportModel = ModelFactory.createOntologyModel(); 
        vocabProblems = new HashMap<>();
        //basic metadata
        Date d = new Date(); 
        date = ""+d.getTime();
        VocabUtils.addIndividual(reportModel, "Report"+date, TextConstants.report, "Report created on "+d.toString());
        VocabUtils.addDataProperty(reportModel, "Report"+date, d.toString(), TextConstants.reportDate, XSDDatatype.XSDdateTime);
    }
    
        
    public static Report getInstance(){
        if(r == null){
            r = new Report();
        }
        return r;        
    } 

    public int getNumberOfVocabError() {
        return vocabsWithErrors.size();
    }

    public int getNumberOfVocabWarning() {
        return vocabsWithWarnings.size();
    }

    public int getNumberOfVocabsOk() {
        return vocabsOk.size();
    }
    
    public void addSuccessfulEntry(String vocab){
        vocabsOk.add(vocab);
        String entryID = "Entry"+date+vocab.replace("/", "").replace(".", "").replace(":", "");
        addEntryToReport(entryID, vocab);
        VocabUtils.addProperty(reportModel, entryID, TextConstants.successStatus, TextConstants.vocabularyStatus);
    }
    
    public void addWarningForVocab(String vocab, Warning w){
        if(!vocabsWithWarnings.contains(vocab)){
            vocabsWithWarnings.add(vocab);
            vocabProblems.put(vocab, new ArrayList<String>());
        }
        String entryID = "Entry"+date+vocab.replace("/", "").replace(".", "").replace(":", "");
        addEntryToReport(entryID, vocab);
        String warning = "";
        String warningLabel = "";
        switch (w){
            case LANG_NOT_FOUND:
                warning = "LanguageNotFound";
                warningLabel = "Warning: A language was not recognized in vocabulary";
                break;
            case LICENCE_NOT_FOUND: 
                warning = "LicenseNotFound";
                warningLabel = "Warning: A license was not found for vocabulary";
                break;
            case MISSING_TITLE_OR_DESC_FOR_VOCAB: 
                warning = "MissingTitleOrDesc";
                warningLabel = "Warning: title or description missing from vocabulary";
                break;
            case NO_SERIALIZATIONS_FOR_VOCAB: 
                warning = "NoSerializationsAvailable";
                warningLabel = "Warning: no serializations available for vocab";
                break;
            case NO_DOMAINS_FOUND_FOR_VOCAB: 
                warning = "NoDomainsFoundForVocab";
                warningLabel = "Warning: No domains have been defined for this vocab";
                break;   
        }
        if(!warning.equals("")){
            VocabUtils.addIndividual(reportModel, warning, TextConstants.warning, warningLabel);
            VocabUtils.addProperty(reportModel, entryID, warning, TextConstants.detectedProblem);
            VocabUtils.addProperty(reportModel, entryID, TextConstants.warningStatus, TextConstants.vocabularyStatus);
            vocabProblems.get(vocab).add(warningLabel);
        }    
    }
    
    public void addErrorForVocab(String vocab, Error err){
        if(!vocabsWithErrors.contains(vocab)){
            vocabsWithErrors.add(vocab);
            vocabProblems.put(vocab, new ArrayList<String>());
        }
        String entryID = "Entry"+date+vocab.replace("/", "").replace(".", "").replace(":", "");
        //an entry, part of the report, about a vocabulary, has an error.
        addEntryToReport(entryID, vocab);
        String error = "";
        String errorLabel = "";
        switch (err){
            case EXCEPTION_ERROR:
                error = "GenericError";
                errorLabel = "Error: the vocabulary could not be loaded or processed";
                break;
//            case MISSING_TITLE_OR_DESC_FOR_VOCAB: 
//                error = "MissingTitleOrDesc";
//                errorLabel = "Error: title or description missing from vocabulary";
//                break;
//            case NO_SERIALIZATIONS_FOR_VOCAB: 
//                error = "NoSerializationsAvailable";
//                errorLabel = "Error: no serializations available for vocab";
//                break;
            case PARSING_ERR: 
                error = "ParsingError";
                errorLabel = "Error while parsing the vocabulary";
                break;
        }
        if(!error.equals("")){
            VocabUtils.addIndividual(reportModel, error, TextConstants.error, errorLabel);
            VocabUtils.addProperty(reportModel, entryID, error, TextConstants.detectedProblem);
            VocabUtils.addProperty(reportModel, entryID, TextConstants.errorStatus, TextConstants.vocabularyStatus);
            vocabProblems.get(vocab).add(errorLabel);
        }    
    }
    
    private void addEntryToReport(String entryID, String vocab){
        VocabUtils.addIndividual(reportModel, entryID, TextConstants.entry, "Entry for vocab "+vocab);
        VocabUtils.addProperty(reportModel, entryID, "Report"+date, TextConstants.isEntryOfReport);
        VocabUtils.addProperty(reportModel, "Report"+date, entryID, TextConstants.hasEntry);
        VocabUtils.addProperty(reportModel, entryID, vocab, TextConstants.entrySubject);
    }
    
    /**
     * Method that returns the report in html
     * @return 
     */
    public String htmlSerialization(){
        String html = TextConstants.getheader("Vocabulary report")+TextConstants.navBarReport + TextConstants.tableHeadReport;
        //first the vocabs that failed
        for(String err:vocabsWithErrors){
            html+= addEntryRowHTML(err, "error", vocabProblems.get(err));
        }
        //then vocabs with warnings
        for(String warning:vocabsWithWarnings){
            if(!vocabsWithErrors.contains(warning)){
                html+=addEntryRowHTML(warning, "warning", vocabProblems.get(warning));
            }
        }
        //then all vocabs ok
        for(String ok:vocabsOk){
            if(!vocabsWithWarnings.contains(ok)){
                html+=addEntryRowHTML(ok, "success", null);
            }
        }
        html+=TextConstants.tableEnd+ TextConstants.end+ "  </body>\n" +
                "</html>\n";
        return html;
    }
    
    private String addEntryRowHTML(String v, String status, ArrayList<String> errors){
        String vocab = v;
        if(!vocab.startsWith("http")){
            try{
                vocab = vocab.substring(vocab.lastIndexOf(File.separator)+1, vocab.length());
            }catch(Exception e){}
        }
        String rowHtml = "  <tr>\n" +
        "<td><a href = \""+v+"\" target=\"_blank\">"+vocab+"</a></td>\n" +
        "<td>\n";
        if(errors == null || errors.isEmpty()){
            rowHtml+="<span class=\"label label-success\">Sucess</span>\n</td>\n" +
            "<td>No errors</td>\n" +
            "</tr>\n"; 
            return rowHtml;
        }else if(status.equals("warning")){
            rowHtml+="<span class=\"label label-warning\">Warning</span>\n";
        }else{//error
            rowHtml+= "<span class=\"label label-danger\">Error</span>\n";
        }       
        rowHtml+= "</td>\n" +
        "<td>";
        for(String error:errors){
            rowHtml+=error+"; ";
        }
        rowHtml+= "</td>\n" +
        "</tr>\n"; 
        return rowHtml;
    }
   
    public void saveReport(String path){
        //add the last metadata: number of errors, warnings, etc.
        VocabUtils.addDataProperty(reportModel, "Report"+date, ""+this.getNumberOfVocabsOk(), TextConstants.numberVocabsOK);
        VocabUtils.addDataProperty(reportModel, "Report"+date, ""+this.getNumberOfVocabError(), TextConstants.numberVocabsError);
        VocabUtils.addDataProperty(reportModel, "Report"+date, ""+this.getNumberOfVocabWarning(), TextConstants.numberVocabsWarning);
        VocabUtils.saveDocument(path+".html", this.htmlSerialization());
        VocabUtils.exportRDFFile(path+".ttl", reportModel);
    }

}
