package com.oppo.sfamanagement.database;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Visvendra on 1/7/16.
 */
public class RestHelper
{

    public String makeRestCallAndGetResponse(String strUrl, String method, String parameters) {
        return makeRestCallAndGetResponse(strUrl, method, parameters, 0);
    }
    public String makeRestCallAndGetResponse(String strUrl, String method, String parameters, int counter) {
        String response ="";
        TrustManager[] trustAllCerts = new TrustManager[]
                {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        }
                };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response ="";
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

            if("POST".equals(method))
            {
                connection.setRequestMethod(method);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("User-Agent", "mozilla");//
            }else if("POST_MULTI".equals(method))
            {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data");
                connection.setRequestProperty("User-Agent", "mozilla");//
            }
            else
            {
                connection.setRequestMethod(method);
                connection.setRequestProperty("User-Agent", "mozilla");
            }
            if(!TextUtils.isEmpty(parameters))
            {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(parameters);
                writer.flush();
                writer.close();
                os.close();
            }
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine())!=null)
            {
                buffer.append(line);
            }
            response = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    public String makeRestCallAndGetResponseImageUpload(String strUrl, String strFile)
    {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response ="";
        HttpURLConnection connection = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(strFile);
        if (!sourceFile.isFile())
        {
            return "{JSESSIONID :data:{errorMsg:Source File Does not exist,imageUrl:,isSuccess:false},responseStatus:success,status:200}";
        }
        URL url = null;
        BufferedReader reader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true); // Allow Inputs
            connection.setDoOutput(true); // Allow Outputs
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("User-Agent", "mozilla");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", strFile);
            dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ strFile + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder bufferout = new StringBuilder();
            String line = "";
            while ((line = reader.readLine())!=null){
                bufferout.append(line);
            }
            response = bufferout.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("File response",response);
            return response;

        }
    }
}