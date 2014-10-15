package org.elasticsearch.PeliasPlugin;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Tools {
    public static JSONObject getJSONFromURL(String urlString) throws IOException {
        URL url = null;
        String responseBody = "";
        InputStream inputStream = null;
        try {
            url = new URL(urlString);
            inputStream = url.openStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            String line;
            while((line = dataInputStream.readLine()) != null){
                responseBody += line;
            }
        }
        catch (MalformedURLException e) { // For now this works for both IO and MalformedURL exceptions
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return new JSONObject(responseBody);

    }

    public static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static void cloneRepo() {
        try {
            Process p = Runtime.getRuntime().exec("git clone https://github.com/pelias/abbreviations-gatherer.git");
            p.waitFor();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getSynonymsPath(){
        String url = "http://localhost:9200/_nodes";
        try {
            JSONObject object = getJSONFromURL(url);
            JSONObject nodes = (JSONObject) object.getJSONObject("nodes");
            JSONObject nodeData = (JSONObject) nodes.get((String) nodes.keySet().toArray()[0]);
            return nodeData.getJSONObject("settings").getJSONObject("path").getString("home");
        } catch (IOException e) {
            System.out.println("Connection refused to ES. Have you initialized it? \n");
        }
        return null;
    }
}
