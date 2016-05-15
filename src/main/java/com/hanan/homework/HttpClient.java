package com.hanan.homework;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.Header;
import sun.misc.BASE64Encoder;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.Callable;


class HttpClient implements Callable<String> {


    public String url;
    public int connectionTimeout=5000;
    public int socketTimeout =5000;

    public String call( )throws Exception{
        try {

            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            DefaultHttpClient client = new DefaultHttpClient(httpParams);
            HttpResponse response = client.execute(new HttpGet(url));
            Header[] headers = response.getAllHeaders();

            return url + "*" + getSHA(Arrays.toString(headers));
        }
        catch(Exception e){
            return url + "#" + e.getMessage();

        }

    }

    public  String getSHA(String str) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(str.getBytes());
        byte[] result = digest.digest();
        String hash1 = (new BASE64Encoder()).encode(result);
        return hash1;

    }

    }
