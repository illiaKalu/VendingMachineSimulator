package com.dev.illiaka.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by sonicmaster on 13.09.16.
 * class read JSON from Google App Engine
 */
public class HttpRequestReadJSON {

    public static String getJSON(){

        final String USER_AGENT = "Mozilla/5.0";

        String url = "http://vendingmachinestorage-142810.appspot.com/demo";

        String readedJsonString = "";

        try {

            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            // optional default is GET
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                readedJsonString = response.toString();

                in.close();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readedJsonString;

    }
}
