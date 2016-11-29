package com.oppo.sfamanagement.database;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class API {

	private static final String TAG = "ASM_API";
    // For debugs all logs...
	public static int LOGLEVEL = 1; // set -1 to stop debugging in the app
	public static boolean DEBUG = LOGLEVEL > 0; // set LOGLEVEL = 1
    public static String HOST = "http://test.asm.securet.in/";
    private static String REST = "rest/v1/";

	private static String VALIDATE_USER = HOST+REST+"validateUser"; // done
	private static String GET_USER_SITES = HOST+REST+"getSitesForUser"; // done
	private static String INSERT_COMPLAINT = HOST+REST+"ticket/create"; // done
	private static String UPDATE_COMPLAINT = HOST+REST+"ticket/update"; //
	private static String GET_USER_SITES_LIST = HOST+REST+"ticket/forUser"; // done
    // Make a rest, add any parameters REST
//    public static String makeRequest(String URI, Map<String,Object> params) {
//        String response = null;
//        RestClient client = new RestClient(URI);
//        for(Map.Entry<String,Object> param: params.entrySet()){
//            if(param.getValue() instanceof String){
//               client.AddParam(param.getKey(), String.valueOf(param.getValue()));
//            }else if(param.getValue() instanceof List){
//                for(Object paramValue : (List)param.getValue()){
//                    client.AddParam(param.getKey(), String.valueOf(paramValue));
//                }
//            }
//        }
//        try {
//            client.Execute(RequestMethod.GET);
//        } catch (Exception e) {
//            Log.e(TAG,"Could not load request:"+URI,e);
//            response = "";
//        }
//        if(client.getResponseCode()== 200){
//            response = client.getResponse();
//        }else{
//            response = "";
//        }
//        if (DEBUG) {
//            Log.d(TAG, "Response: " + response);
//        }
//        return response;
//    }
    // Validate User REST
	public static String LoginRest(String userNameValue, String passwordValue) {
		String response = null;
		String paramString ="";
		try {
			paramString  = VALIDATE_USER+"?j_username=" + URLEncoder.encode(userNameValue,"UTF-8")+"&j_password="+ URLEncoder.encode(passwordValue,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "");
		}
		response = new RestHelper().makeRestCallAndGetResponse(paramString,"GET","");
		if (DEBUG)
			Log.d(TAG, "Response: " + response);
		return response;
	}

	public static String GetSitesRest(String userNameValue, String passwordValue) {
		String response = null;
		String paramString ="";
		try {
			paramString  = GET_USER_SITES+"?j_username=" + URLEncoder.encode(userNameValue,"UTF-8")+"&j_password="+ URLEncoder.encode(passwordValue,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "");
		}
		response = new RestHelper().makeRestCallAndGetResponse(paramString,"GET","");
		if (DEBUG)
			Log.d(TAG, "Response: " + response);
		return response;
	}
	public static String GetSitesListRest(String userNameValue, String passwordValue) {
		String response = null;
		String paramString ="";
		try {
			paramString  = GET_USER_SITES_LIST+"?j_username=" + URLEncoder.encode(userNameValue,"UTF-8")+"&j_password="+ URLEncoder.encode(passwordValue,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "");
		}
		response = new RestHelper().makeRestCallAndGetResponse(paramString,"GET","");
		if (DEBUG)
			Log.d(TAG, "Response: " + response);
		return response;
	}
	public static String GetLoginRest(String userNameValue, String passwordValue) {
		String response = null;
		String paramString ="";
		try {
			paramString  = INSERT_COMPLAINT+"?j_username=" + URLEncoder.encode(userNameValue,"UTF-8")+"&j_password="+ URLEncoder.encode(passwordValue,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "");
		}
		if (DEBUG)
			Log.d(TAG, "Response: " + response);
		return paramString;
	}
	public static String GetLogOutRest(String userNameValue, String passwordValue) {
		String response = null;
		String paramString ="";
		try {
			paramString  = UPDATE_COMPLAINT+"?j_username=" + URLEncoder.encode(userNameValue,"UTF-8")+"&j_password="+ URLEncoder.encode(passwordValue,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "");
		}
		if (DEBUG)
			Log.d(TAG, "Response: " + response);
		return paramString;
	}

//	public static String InsertComplaintsRest(
//			String siteIDValue,
//			String serviceTypeIDValue,
//			String descriptionValue,
//			String enumerationIDValue,
//			String FilenameValue,
//			String LongValue,
//			String LatValue,
//			String issueTypeIDValue,
//			String userNameValue,
//			String passwordValue) {
//		String responseStr = "";
//
//
//		/*try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            //bm.compress(CompressFormat.JPEG, 75, bos);
//            byte[] data = bos.toByteArray();
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost postRequest = new HttpPost(
//                    "http://10.0.2.2/cfc/iphoneWebservice.cfc?returnformat=json&amp;method=testUpload");
//            ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
//            // File file= new File("/mnt/sdcard/forest.png");
//            // FileBody bin = new FileBody(file);
//
//
//            MultipartEntity reqEntity = new MultipartEntity(
//                    HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("uploaded", bab);
//           // reqEntity.addPart("photoCaption", new StringEntity("sfsdfsdf"));
//            postRequest.setEntity(reqEntity);
//            HttpResponse response = httpClient.execute(postRequest);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    response.getEntity().getContent(), "UTF-8"));
//            String sResponse;
//            StringBuilder s = new StringBuilder();
//
//            while ((sResponse = reader.readLine()) != null) {
//                s = s.append(sResponse);
//            }
//            System.out.println("Response: " + s);
//        } catch (Exception e) {
//            // handle exception here
//            Log.e(e.getClass().getName(), e.getMessage());
//        }*/
//
//
//		//CloseableHttpClient httpclient = HttpClients.createDefault();
//
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			HttpPost httppost = new HttpPost(INSERT_COMPLAINT+"?j_username="+userNameValue+"&j_password="+passwordValue);
//
//
//			HttpEntity reqEntity;
//			//StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
//			if(FilenameValue.toString().equals("")||FilenameValue.toString().equals(null)){
//
//				reqEntity = MultipartEntityBuilder.create()
//						//.addPart("ticketAttachments", bin)
//						.addPart("site.siteId", new StringBody(siteIDValue))
//						.addPart("serviceType.serviceTypeId", new StringBody(serviceTypeIDValue))
//						.addPart("issueType.issueTypeId", new StringBody(issueTypeIDValue))
//						.addPart("severity.enumerationId", new StringBody(enumerationIDValue))
//						.addPart("description", new StringBody(descriptionValue))
//						.addPart("latitude", new StringBody(LatValue))
//						.addPart("longitude", new StringBody(LongValue))
//						.addPart("source", new StringBody("MOBILE_APP"))
//				/*.addPart("j_username", new StringBody(userNameValue))
//				.addPart("j_password", new StringBody(passwordValue))*/
//						.build();
//			}else{
//				FileBody bin = new FileBody(new File(FilenameValue));
//				reqEntity = MultipartEntityBuilder.create()
//						.addPart("ticketAttachments", bin)
//						.addPart("site.siteId", new StringBody(siteIDValue))
//						.addPart("serviceType.serviceTypeId", new StringBody(serviceTypeIDValue))
//						.addPart("issueType.issueTypeId", new StringBody(issueTypeIDValue))
//						.addPart("severity.enumerationId", new StringBody(enumerationIDValue))
//						.addPart("description", new StringBody(descriptionValue))
//						.addPart("latitude", new StringBody(LatValue))
//						.addPart("longitude", new StringBody(LongValue))
//						.addPart("source", new StringBody("MOBILE_APP"))
//				/*.addPart("j_username", new StringBody(userNameValue))
//				.addPart("j_password", new StringBody(passwordValue))*/
//						.build();
//			}
//			httppost.setEntity(reqEntity);
//
//			System.out.println("executing request " + httppost.getRequestLine());
//			HttpResponse httpResponse = httpclient.execute(httppost);
//			try {
//				//System.out.println("----------------------------------------");
//				//System.out.println(response.getStatusLine());
//				System.out.println("========================================== Result Code: " + httpResponse.getStatusLine().getStatusCode());
//				HttpEntity resEntity = httpResponse.getEntity();
//				//System.out.println("Response content: " + resEntity.getContent().toString());
//
//
//
//				if(httpResponse.getStatusLine().getStatusCode()==200){
//
//					InputStream instream = resEntity.getContent();
//					//System.out.println("Response content: " + convertStreamToString(instream));
//					responseStr = convertStreamToString(instream);
//					// Closing the input stream will trigger connection release
//					//instream.close();*/
//					instream.close();
//
//				}else{
//					responseStr="";
//				}
//
//				//EntityUtils.consume(resEntity);
//			}catch(Exception e){
//				responseStr  = "";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			responseStr  = "";
//		}
//
//		if (DEBUG)
//			Log.d(TAG, "Response: " + responseStr);
//
//		if (DEBUG)
//			Log.d(TAG, "Response: " + responseStr);
//		return responseStr;
//	}
//
//	// get User sites REST
//	public static String GetSitesRest(String userNameValue, String passwordValue) {
//		String response = null;
//
//		RestClient client = new RestClient(GET_USER_SITES);
//
//		client.AddParam("j_username", userNameValue);
//		client.AddParam("j_password", passwordValue);
//
//		// client.AddParam("output", "json");
//		try {
//			client.Execute(RequestMethod.GET);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response = "";
//			if (DEBUG)
//				Log.d(TAG, "");
//		}
//
//
//		if(client.getResponseCode()== 200){
//			response = client.getResponse();
//		}else{
//			response = "";
//		}
//
//		if (DEBUG)
//			Log.d(TAG, "Response: " + response);
//		return response;
//	}

//
//	// Insert COmplain Rest
//	@SuppressWarnings("deprecation")
//	public static String InsertComplaintsRest(
//				String siteIDValue,
//				String serviceTypeIDValue,
//				String descriptionValue,
//				String enumerationIDValue,
//				String FilenameValue,
//				String LongValue,
//				String LatValue,
//				String issueTypeIDValue,
//				String userNameValue,
//				String passwordValue) {
//		String responseStr = "";
//
//		//CloseableHttpClient httpclient = HttpClients.createDefault();
//
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			HttpPost httppost = new HttpPost(INSERT_COMPLAINT+"?j_username="+userNameValue+"&j_password="+passwordValue);
//
//			//httppost.addHeader("Content-Type","multipart/form-data");
//			//httppost.addHeader("Content-Disposition", "form-data; name = args");
//
//			HttpEntity reqEntity;
//			//StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
//			if(FilenameValue.toString().equals("")||FilenameValue.toString().equals(null)){
//
//				reqEntity = MultipartEntityBuilder.create()
//				//.addPart("ticketAttachments", bin)
//				.addPart("site.siteId", new StringBody(siteIDValue))
//				.addPart("serviceType.serviceTypeId", new StringBody(serviceTypeIDValue))
//				.addPart("issueType.issueTypeId", new StringBody(issueTypeIDValue))
//				.addPart("severity.enumerationId", new StringBody(enumerationIDValue))
//				.addPart("description", new StringBody(descriptionValue))
//				.addPart("latitude", new StringBody(LatValue))
//				.addPart("longitude", new StringBody(LongValue))
//				.addPart("source", new StringBody("MOBILE_APP"))
//				/*.addPart("j_username", new StringBody(userNameValue))
//				.addPart("j_password", new StringBody(passwordValue))*/
//				.build();
//			}else{
//				FileBody bin = new FileBody(new File(FilenameValue));
//				reqEntity = MultipartEntityBuilder.create()
//				.addPart("ticketAttachments", bin)
//				.addPart("site.siteId", new StringBody(siteIDValue))
//				.addPart("serviceType.serviceTypeId", new StringBody(serviceTypeIDValue))
//				.addPart("issueType.issueTypeId", new StringBody(issueTypeIDValue))
//				.addPart("severity.enumerationId", new StringBody(enumerationIDValue))
//				.addPart("description", new StringBody(descriptionValue))
//				.addPart("latitude", new StringBody(LatValue))
//				.addPart("longitude", new StringBody(LongValue))
//				.addPart("source", new StringBody("MOBILE_APP"))
//				/*.addPart("j_username", new StringBody(userNameValue))
//				.addPart("j_password", new StringBody(passwordValue))*/
//				.build();
//			}
//
//			httppost.setEntity(reqEntity);
//
//			System.out.println("executing request " + httppost.getRequestLine());
//			HttpResponse httpResponse = httpclient.execute(httppost);
//			try {
//				//System.out.println("----------------------------------------");
//				//System.out.println(response.getStatusLine());
//				System.out.println("========================================== Result Code: " + httpResponse.getStatusLine().getStatusCode());
//				HttpEntity resEntity = httpResponse.getEntity();
//				//System.out.println("Response content: " + resEntity.getContent().toString());
//
//
//
//				if(httpResponse.getStatusLine().getStatusCode()==200){
//
//					InputStream instream = resEntity.getContent();
//					//System.out.println("Response content: " + convertStreamToString(instream));
//					responseStr = convertStreamToString(instream);
//	                // Closing the input stream will trigger connection release
//	                //instream.close();*/
//	                instream.close();
//
//				}else{
//                    responseStr="";
//				}
//
//				//EntityUtils.consume(resEntity);
//			}catch(Exception e){
//                responseStr  = "";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			responseStr  = "";
//		}
//
//		if (DEBUG)
//			Log.d(TAG, "Response: " + responseStr);
//
//		if (DEBUG)
//			Log.d(TAG, "Response: " + responseStr);
//		return responseStr;
//	}


	private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
