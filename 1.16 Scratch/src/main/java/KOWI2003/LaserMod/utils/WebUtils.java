package KOWI2003.LaserMod.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebUtils {

	public static String getFileContent(String url) {
		URL url1;String file="";
        try {
            url1 = new URL(url);
            URLConnection uc;
            uc = url1.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                file=file+line+"\n";
//            System.out.println(file);
            	return file;

        } catch (IOException e) {
//            System.out.println("Wrong username and password");
            return null;
        }
	}
	
	public static JsonObject getJsonObj(String url) {
		URL url1;String file = "";
        try {
            url1 = new URL(url);
            URLConnection uc;
            uc = url1.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                file=file+line+"\n";
            JsonObject jObj = new JsonParser().parse(file).getAsJsonObject();
            return jObj;

        } catch (Exception e) {
            return null;
        }
	}
	
	public static void splitToKeys() {
		File file = new File("");
		JsonObject jObj = new JsonObject();
	}
	
}
