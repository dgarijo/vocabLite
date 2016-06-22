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
import java.util.ArrayList;
import java.util.HashMap;



import oeg.licensius.GetLicense;

/**
 * Class that defines the attributes of a vocabulary
 * @author dgarijo, mpoveda
 */
public class Vocabulary {
    private String title;
    private String name;
    private String uri;
    private String lovURI;
    private ArrayList<String> supportedSerializations;
    private String license;
    private String licenseTitle;
    private String description;
    private String firstPartDesc;
    private String secondPartDesc;
    private ArrayList<String> languages;
    private ArrayList<String> domains;
    private String prefix;
    private String creationDate;
    private String lastModifiedDate;
    private String vocabPath;//path of the vocabulary in the repo.

    public Vocabulary(){
        
    }
    public Vocabulary(String uri) {
        this.uri = uri;
        this.supportedSerializations = VocabUtils.getSerializationsOfVocab(uri);
    }

    public Vocabulary(String title, String uri, String lovURI, ArrayList<String> supportedSerializations, String license, String description, ArrayList<String> languages, ArrayList<String> domains, String prefix, String creationDate, String lastModifiedDate, String name) {
        this.title = title;
        this.uri = uri;
        this.lovURI = lovURI;
        this.supportedSerializations = supportedSerializations;
        this.license = license;
        this.description = description;
        
        String desc = this.description;
    	if (desc.length() > 360){
    		int nextSpace = desc.indexOf(" ", 360);
    		this.firstPartDesc = this.description.substring(0, nextSpace);
    		this.secondPartDesc = this.description.substring(nextSpace);
    	}
    	
        this.languages = languages;
        this.domains = domains;
        this.prefix = prefix;
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.name = name;
    }

    
    /*
     * Getters 
     */

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getDomains() {
        return domains;
    }

    public ArrayList<String> getLanguage() {
        return languages;
    }

    public String getLicense() {
        return license;
    }    
    
    public String getLicenseTitle() {
        return licenseTitle;
    }

    public ArrayList<String> getSupportedSerializations() {
        return supportedSerializations;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public String getLovURI() {
        return lovURI;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public String getShortDescrition (){
    	return firstPartDesc;
    }
    
    
    /**
     * Setters
     */ 

    public void setDescription(String description) {
        this.description = description;
        this.firstPartDesc = description;
        
        String desc = this.description;
    	if (desc.length() > TextConstants.shortDescLenght){
    		
    		int nextSpace = desc.indexOf(" ", TextConstants.shortDescLenght);
    		if (nextSpace != -1){
    			this.firstPartDesc = this.description.substring(0, nextSpace);
    			this.secondPartDesc = this.description.substring(nextSpace);
    		}
    		
    	}
    	
    }

    public void setDomains(ArrayList<String> domains) {
        this.domains = domains;
    }

    public void setLicense(String license) {
      this.license = license;
    }
    
    public void setLicenseTitle(String licenseT) {
      this.licenseTitle = licenseT;
    }
    
    public void setLicenseWithService(String uri) {        
        license = GetLicense.getFirstLicenseFound(uri);
        if (!license.isEmpty()&&license!=null){
            licenseTitle = GetLicense.getTitle(license);
        }else{
            license = "unknown";
            licenseTitle = "unknown";
        }
        if(license.equals("unknown")){
            Report.getInstance().addWarningForVocab(uri, TextConstants.Warning.LICENCE_NOT_FOUND);
        }
    }
    
    
    public void setSupportedSerializations(ArrayList<String> supportedSerializations) {
        this.supportedSerializations = supportedSerializations;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLovURI(String lovURI) {
        this.lovURI = lovURI;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public String getVocabPath() {
        return vocabPath;
    }

    public void setVocabPath(String vocabPath) {
        this.vocabPath = vocabPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    
    
    
//    public void setShortDescription(String description) {
//    	String desc = this.description;
//    	if (desc.length() > 360){
//    		int nextSpace = desc.indexOf(" ", 360);
//    		this.shortDescription = this.description.substring(0, nextSpace) + "...";
//    	}
//    }
    
    /**
     * Method that returns an html serialization of the vocabulary.
     * Assuming that there is a table 
     * @param id id for the vocab in the table
     * @return 
     */
    public String getHTMLSerializationAsRow(String id, File out){
        String html = ("<tr id=\"tr"+id+"\">\n");
        //URI and title
        String ontURI = this.uri;
        String ontTitle = this.getTitle();
        String localURL = ontURI.replace("https://","").replace("http://","").replace("/", "").replace("#", "").trim();
        html +=("<td><a href = \""+ ontURI + "\" target=\"_blank\">" + ontTitle + "</a> </td>");
//code for including oops evaluations (deactivated at the moment)
//<a href = \"ontologies/" + localURL + ".html\" target=\"_blank\"><span class=\"glyphicon glyphicon-info-sign\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"More information about this vocabulary\"/></a></td>\n");

        //Serializations
        html+="<td>\n";
        if(supportedSerializations!=null){
            //here we will just have 1
            VocabUtils.copyExternalResource(this.vocabPath, new File(out.getAbsolutePath()+File.separator+"ontologies"+File.separator+name));
            String s = supportedSerializations.get(0);//only the current serialization
            //for(String s:this.supportedSerializations){
            html+="<a href=\"ontologies/"+this.name+"\"><span class=\"label label-primary\">"+s.replace("application/", "").replace("text/", "")+"</span></a>\n";
            //}
        }
        html+="</td>\n";
        //License
        html+=("<td>");
        
        if (this.licenseTitle == null || this.licenseTitle.toLowerCase().equals("unknown")){      
            html+= "<span class=\"label label-default\">Undefined</span>";        
        }
        else{
//            html+= "<a href=\"" + license + "\" target=\"_blank\"><span class=\"label label-success\">" + licenseLabel + "</span></a>";        
            String licenseTitleReduced = licenseTitle.replace("Creative Commons ", "");
        	html+= "<a href=\"" + this.license + "\" target=\"_blank\"> <span class=\"label label-success\">" + licenseTitleReduced + "</span> </a>";        
        }

        html+=("</td>\n");

        //Natural Language
        html+=("<td>");
        if(languages!=null){
            for(String lang: languages){
            	HashMap <String, String> mapLang = new Languages().getMapLang();
            	String currLang = lang.substring(0, 2);
            	if (mapLang.containsKey(currLang)){
                    String langURI = mapLang.get(currLang);
                    html+="<a href=\"" + langURI + "\" target=\"_blank\"> <span class=\"label label-primary\">" + lang + "</span></a> ";
                }
                else{
                    //language not recognized -> add to log
                    Report.getInstance().addWarningForVocab(uri, TextConstants.Warning.LANG_NOT_FOUND);
                }
            }
        }else{
            html+="<span class=\"label label-default\">Undefined</span>";
        }
        html+="</td>\n";    
        
        //Domains
//        html+="<td>";
//        String domainText=title+"--";
//        if(domains!=null){
//            for(String d:domains){
//                html+="<span class=\"label label-primary\">" + d + "</span>\n";
//                domainText = domainText +d+ "--";
//            }
//        }else{
//            html+="<span class=\"label label-default\">Undefined</span>";
//        }
//        html+=("</td>\n");
        
        //description
        html+="<td>\n";    
        if(description!=null){
            if (firstPartDesc.length() < description.length()){        	
                html+= "<p id=\"collapse"+id+"\" data-toggle=\"collapse\" >\n";
                html+= firstPartDesc + " ... ";
                html+= "<a class=\"more"+id+"\">See more</a>\n";
                html+= "</p>\n";
                html+= "<script>\n";
                html+= "$('#collapse"+id+"').click(function () {\n";
                html+= "if($('a').hasClass('more"+id+"'))\n";
                html+= "{\n";
                html+= "$('#collapse"+id+"').html('"+ description.replace("\n", "<br>") +"  <a class=\"less"+id+"\">See less<a>'); \n";
                        html+= "}\n";
                        html+= "else\n";
                        html+= "{      \n";
                        html+= "$('#collapse"+id+"').html('"+ firstPartDesc.replace("\n", "<br>") + " ... "+" <a  class=\"more"+id+"\">See more</a>'); \n";
                        html+= "}\n";
                        html+= "}); \n";
                html+= "</script>\n";
            }
            else{
                    html+=description.replace("\n", "<br>");
            }
        }

        html+= "</td>\n";

//        html+="<input type=\"hidden\" name=\"inp"+id+"\" id=\"inp"+id+"\" value=\""+domainText+"\"/>";
        
        //finish row
        html+=("</tr>");        
        return html;
    }
    
}
