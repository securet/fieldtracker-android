package com.oppo.sfamanagement.webmethods;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;

import org.json.JSONObject;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Visvendra on 1/7/16.
 */
public class RestHelper
{

    public String makeRestCallAndGetResponse(String strUrl, String method, String parameters,Preferences preferences) {
        return makeRestCallAndGetResponse(strUrl, method, parameters,preferences, 0);
    }

    public String makeRestCallAndGetResponse(String strUrl, String method, String parameters,Preferences preferences, int counter) {
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
            Logger.e("Log",e);
        }
        response ="";
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            if(AppsConstant.POST.equals(method))
            {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("User-Agent", "mozilla");//
                connection.setRequestProperty("Authorization", "" + preferences.getString(Preferences.BASIC_AUTH,""));
            }
            else
            {
                connection.setRequestProperty("User-Agent", "mozilla");
                connection.setRequestProperty("Authorization", "" + preferences.getString(Preferences.BASIC_AUTH,""));
            }
            if(!TextUtils.isEmpty(parameters))
            {
                Logger.e(parameters);
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
            Logger.e("Log",e);
        } catch (IOException e) {
            Logger.e("Log",e);
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                Logger.e("Log",e);
            }
            return response;
        }
    }
    public String makeRestCallAndGetResponseLogin(String strUrl, String UserName, String password,Preferences preferences)
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
            Logger.e("Log",e);
        }

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        String response ="";
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "mozilla");
            String basicAuth = "Basic " + new String(Base64.encode((UserName+":"+password).getBytes(),Base64.NO_WRAP ));
            connection.setRequestProperty("Authorization", basicAuth);
            preferences.saveString(Preferences.BASIC_AUTH,basicAuth);
            preferences.commit();
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Accept","*/*");
            connection.connect();
            int status = connection.getResponseCode();
            InputStream stream;
            if(status!=200){
                stream = connection.getErrorStream();
            }else{
                stream = connection.getInputStream();
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine())!=null){
                buffer.append(line);
            }
            response = buffer.toString();
        } catch (MalformedURLException e) {
            Logger.e("Log",e);
        } catch (IOException e) {
            Logger.e("Log",e);
        } finally{
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                Logger.e("Log",e);
            }
            return response;
        }
    }
    public String makeRestCallAndGetResponseImageUpload(String strUrl, String strFile,Preferences preferences)
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
            Logger.e("Log",e);
        }
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
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
            Logger.e("uploadFile Source File Does not exist");
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
            connection.setRequestProperty("Authorization", "" + preferences.getString(Preferences.BASIC_AUTH,""));
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
            Logger.e("Log",e);
        } catch (IOException e) {
            Logger.e("Log",e);
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
                Logger.e("Log",e);
            }
            Log.e("File response",response);
            return response;

        }
    }
//    private static Picasso mInstance = null;
//    public static Picasso getInstance(Context context)
//    {
//        if (mInstance == null) {
//            OkHttpClient client = new OkHttpClient();
//            client.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String s, SSLSession sslSession) {
//                    return true;
//                }
//            });
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(
//                        java.security.cert.X509Certificate[] x509Certificates,
//                        String s) throws java.security.cert.CertificateException {
//                }
//
//                @Override
//                public void checkServerTrusted(
//                        java.security.cert.X509Certificate[] x509Certificates,
//                        String s) throws java.security.cert.CertificateException {
//                }
//
//                @Override
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return new java.security.cert.X509Certificate[]{};
//                }
//            }};
//            try {
//                SSLContext sc = SSLContext.getInstance("TLS");
//                sc.init(null, trustAllCerts, new java.security.SecureRandom());
//                client.setSslSocketFactory(sc.getSocketFactory());
//            } catch (Exception e) {
//                Logger.e("Log",e);
//                Crashlytics.log(1, "ERROR IN RESPONSE", "Error while reading response JSON parsing" + e.toString());
//            }
//
//            mInstance = new Picasso.Builder(context).loggingEnabled(true)
//                    .downloader(new OkHttpDownloader(client))
//                    .listener(new Picasso.Listener() {
//                        @Override
//                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                            Log.i("loadImage","Failed loading image: "+uri+" with exception: "+exception.toString());
//                            Crashlytics.log("Failed loading image: "+uri+" with exception: "+exception.toString());
//                            Crashlytics.logException(exception);
//                        }
//                    }).build();
//        }
//        return mInstance;
//    }
}