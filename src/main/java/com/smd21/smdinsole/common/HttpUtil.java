package com.smd21.smdinsole.common;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

public class HttpUtil {
	private HttpResponse response;
	private String headerName;
	private String headerValue;

	public void get(String requestURL) throws Exception  {

		HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
		HttpGet getRequest = new HttpGet(requestURL); 			//GET 메소드 URL 생성
		if(headerName != null && !headerName.equals("")) {
			getRequest.addHeader(headerName, headerValue); //KEY 입력
		}
		response = client.execute(getRequest);

		//Response 출력
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("response is error : " + response.getStatusLine().getStatusCode());
		}

	}

	public void post(String requestURL, Map<String, String> parameters) throws Exception {
		HttpEntity entity = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(parameters != null) {

			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			entity = new UrlEncodedFormEntity(params);
		}


		this.post(requestURL, entity, "form");
	}

	public void jpost(String requestURL, JSONObject json) throws Exception {
		HttpEntity entity = null;

		if(json != null) {
			entity = new StringEntity(json.toJSONString(), "utf-8");
		}

		this.post(requestURL, entity, "json");

	}

	private void post(String requestURL, HttpEntity entity, String contentType) throws Exception {
		HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성

		HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성

		postRequest.setHeader("Accept", "application/json");

		if(contentType.equals("json")) {
			postRequest.addHeader("content-type", "application/json; charset=utf8");
		} else {
			postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
		}


		if(entity != null) {
			postRequest.setEntity(entity);
		}

		response = client.execute(postRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("response is error : " + response.getStatusLine().getStatusCode());
		}

	}

	public String getResponseBody() throws ClientProtocolException, IOException  {
		ResponseHandler<String> handler = new BasicResponseHandler();
		String body = handler.handleResponse(response);
		return body;
	}

	public String getResponseHeader(String headerKey) {
		Header[] headers = response.getHeaders(headerKey);
		response.getHeaders(headerKey);
		if(headers.length == 1) return headers[0].getValue();
		return null;
	}

	public void setHeader(String key, String value) {
		this.headerName = key;
		this.headerValue = value;
	}


}
