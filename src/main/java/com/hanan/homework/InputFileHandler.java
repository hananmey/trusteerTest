package com.hanan.homework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class InputFileHandler  {

    HashSet<String> urlMap = new HashSet<String>();
    BufferedReader br;

    public  HashSet<String> readFile(String fileName)throws Exception
    {
        try {
            br = new BufferedReader(new FileReader(fileName));

            String line = null;
            while ((line = br.readLine())  != null) {
                if(line != null && line.length()>1) {

                    String[] s = line.split("\\s+");

                    System.out.println(s[0]);
                    System.out.println(s[1]);
                    urlMap.add(replaceUrl(s[0], s[1]));
                }
            }
        }
        catch (Exception e){
            System.out.println("ggg"+e);

        }
        finally {
            br.close();
        }
        return urlMap;

    }

    private  String replaceUrl(String url,String ip) throws NullPointerException, URISyntaxException {

        try {
            if (ip.contains("*"))
                return url;
            URI uri = new URI(url);
            String au = uri.getAuthority();
            return url.replaceFirst(au, ip);
        }
        catch (Exception e){
            System.out.println("ss" + e.getMessage());

        }
        return "";
    }

}
