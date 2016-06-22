/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Polit�cnica de Madrid, Spain
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
package oeg.licensius;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import oeg.vocablite.Report;
import oeg.vocablite.TextConstants;

/**
 *
 * @author Victor Rodriguez Doncel.
 * Integrated by Maria Poveda and Daniel Garijo
 */
public class GetLicense {
    
    
   public static String getTitle(String licenseURI) {
    String output="unknown";
    try {
        String uri=TextConstants.licensiusURIServiceLicenseInfo;
        String encodedData = URLEncoder.encode(licenseURI);
        uri+=encodedData;
//        System.out.println(uri);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
        if (conn.getResponseCode() != 200) {
         throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
        }

                String r="";
                String linea="";
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while ((linea = br.readLine()) != null) {
                    r+=linea+"\n";
        }

                JSONParser parser=new JSONParser();
                Object obj=parser.parse(r);
                JSONObject array=(JSONObject)obj;
                output = (String) array.get("label");
        conn.disconnect();
    } catch (Exception e) {
        //Report.getInstance().addWarningForVocab(licenseURI, TextConstants.Warning.LICENCE_NOT_FOUND);
    }
    return output;
}


public static String getFirstLicenseFound(String uriToScan) {
    String output="unknown";
    try {
        String uri=TextConstants.licensiusURIServiceLicense;
        String encodedData = URLEncoder.encode(uriToScan);
        uri+=encodedData;
//        System.out.println(uri);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
        }
        String r="";
        String linea="";
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        
        while ((linea = br.readLine()) != null) {
            r+=linea+"\n";
        }

        JSONParser parser=new JSONParser();
        Object obj=parser.parse(r);
        JSONArray array=(JSONArray)obj;
        for (Object o : array) {
            String ll=(String)((HashMap)o).get("license");
            if (!ll.isEmpty())
            {
                return ll;
            }
        }
        conn.disconnect();
    } catch (Exception e) {
        Report.getInstance().addWarningForVocab(uriToScan, TextConstants.Warning.LICENCE_NOT_FOUND);
    }
    return output;
}

//old invokation to the service
 /**
     * Invokes the getLicense method of the Licensius service, so that
     * the license in a RDF resource is found.
     * @param uriToScan URI to scan, for example http://purl.org/goodrelations/v1.owl
     * @return 
     */
//    public static HashMap getLicense(String uriToScan) {
//        String output=null;
//        HashMap license = null;
//
//        try {
//            String encodedData = URLEncoder.encode(uriToScan);
//            String uri=TextConstants.licensiusURIServiceLicense;            
//            uri+=encodedData;
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
//            conn.setRequestProperty("Accept", "application/json");
//            if (conn.getResponseCode() != 200) {
//                throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//            String linea;
//            while ((linea = br.readLine()) != null) {
//                output+=linea;
//            }
//            JSONParser  parser = new JSONParser();
//            Object obj = parser.parse(output);
//            license = (HashMap)(obj);
////            System.out.println("License used: "+license.get("title")+ " with URI: "+ license.get("uri"));
//            conn.disconnect();
//        } catch (MalformedURLException e) {
//            Report.getInstance().addToReport("-->Could not load license for vocab");
//        } catch (IOException e) {
//            Report.getInstance().addToReport("-->Could not load license for vocab");
//        } catch(ParseException e){
//            Report.getInstance().addToReport("-->Could not load license for vocab (parse exception)");
//        }
//        return license;
//    }
    
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) throws ParseException {
////        HashMap license=getLicense("http://purl.org/goodrelations/v1.owl");
////        HashMap license=getLicense("http://purl.org/net/p-plan");
//        String title = "Not found";
//        String uri = "http://purl.org/net/p-plan";
//
//        //WE OBTAIN THE MOST PROBABLE LICENSE FOUND IN THE RDF DOCUMENT POINTED BY A URI
//        String license=getFirstLicenseFound(uri);
//            if (!license.isEmpty())
//            {
//                //IF WE HAPPEN TO HAVE FOUND ANYTHING, WE WANT TO KNOW THE LABEL
//                title = getTitle(license);
//            }
//        System.out.println(title);
//        
//    }
}
    
