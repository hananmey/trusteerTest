package com.hanan.homework;

import org.apache.log4j.Logger;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/*
Main class of application.
Input : 1-configFile 2-Email"

The program iterates Urls id config file ,
decode the response headers to sha-1
if decoded header value changed it send main to the address in args(1)

 */
public class UrlFetcher {

    public static void main(String[] args) throws Exception {


        InputFileHandler handlr = new InputFileHandler();
        HashMap<String, String> UrlAndSHA = new HashMap<String, String>();
        Logger logger = Logger.getLogger(UrlFetcher.class);
        //BasicConfigurator.configure();
        String log4jConfPath = "./Log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        if(args.length!=2){
          //  System.out.println("please provide 2 params 1- conf file loaction ,2- mail to send notification");
            logger.error("please provide 2 params 1- conf file loaction ,2- mail to send notification !");
            System.exit(1);
        }

        String mailTo = args[1];
        String configFile=args[0];

        while (true) {
            Date startDate =new Date();
            HashSet<String> map = handlr.readFile(configFile);
            ExecutorService executor = Executors.newFixedThreadPool(map.size());
            ArrayList<Future<String>> results = new ArrayList<Future<String>>();

            for (String line : map) {

                HttpClient counter = new HttpClient();
                counter.url = line.trim();

                logger.info("send request to url:+  " + counter.url);
              //  System.out.println("send request to url:+  "+counter.url);

                Future<String> submit = executor.submit(counter);
                results.add(submit);

            }

            for (Future<String> result : results) {
                try {
                    String res = result.get();

                    if (res.contains("*")) {
                        String url = res.split("\\*")[0];
                        String hash = res.split("\\*")[1];
                        logger.info("recieved response to url:+  "+url);
                        System.out.println("recieved response to url:+  "+url);

                        if (UrlAndSHA.containsKey(url)) {
                            if (!UrlAndSHA.get(url).equals(hash)) {
                                System.out.println("send main");
                                MailSender.sendMail(mailTo, "header SHA-1 changed from url: " + url);
                                logger.info("header changed for URL: " + url);
                            //    System.out.println("header changed for URL: " + url);
                            }
                        } else {
                            UrlAndSHA.put(url, hash);
                        }
                    } else if (res.contains("#")) {
                        System.out.println("log exception");
                        logger.info("recieved exception : " + res);
                    //    System.out.println("recieved exception : " + res);

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
         //           System.out.println("recieved exception : " + e);


                }
            }

            Date endDate  = new Date();
            long duration  = endDate.getTime() - startDate.getTime();
            long diffInSeconds = TimeUnit.MILLISECONDS.toMillis(duration);

            if(TimeUnit.MILLISECONDS.toMillis(duration)<600000){
                Thread.sleep(600000-TimeUnit.MILLISECONDS.toMillis(duration));

            }

        }
    }
}
