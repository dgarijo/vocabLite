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

/**
 * Queries to be used by vocab
 * @author mpoveda
 * Integrated by dgarijo
 */
public class Queries {
	
	public static final String LOVEndpoint = "http://lov.okfn.org/dataset/lov/sparql";
	
//	public static final String LOVAggEndpoint = "http://lov.okfn.org/endpoint/lov_aggregator";
	
	public static final String prefixes = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
			"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>" +
			"PREFIX dcterms:<http://purl.org/dc/terms/>" +
			"PREFIX dcelements:<http://purl.org/dc/elements/1.1/>" +
			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
			"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
			"PREFIX skos:<http://www.w3.org/TR/skos-reference/#>" +
			"PREFIX foaf:<http://xmlns.com/foaf/0.1/>" +
			"PREFIX void:<http://rdfs.org/ns/void#>" +
			"PREFIX vann:<http://purl.org/vocab/vann/>" ;
			
	
        public static String isVocabInLOV(String uri){
            String vocabInLOV = "SELECT ?vocabURI ?vocabPrefix { " +
			"GRAPH <http://lov.okfn.org/dataset/lov>{ " +
			"?vocabURI a <http://purl.org/vocommons/voaf#Vocabulary>." +
			"?vocabURI <http://purl.org/vocab/vann/preferredNamespacePrefix> ?vocabPrefix." +
			"FILTER regex(str(?vocabURI), \""+uri+"\")}}";
            return vocabInLOV;
        }
        
        public static final String languagesUsed = "SELECT distinct (lang(?lang) as ?langUsed) WHERE { ?thing ?property ?lang}";


//	public static String vocabInLOV = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
//			"PREFIX voaf:<http://purl.org/vocommons/voaf#>" +
//			"PREFIX vann:<http://purl.org/vocab/vann/>" +
//			"SELECT ?vocabURI ?vocabPrefix " +
//			"WHERE{" +
//			"?vocabURI a voaf:Vocabulary." +
//			"?vocabURI vann:preferredNamespacePrefix ?vocabPrefix." +
//			"FILTER regex(str(?vocabURI), \"";

}
