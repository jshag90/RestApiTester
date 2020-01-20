package com.secuve.tester.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("deprecation")
public class RestClient {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private String server = null;
  private RestTemplate rest;
  private HttpHeaders headers;
  private HttpStatus status;
  
  public RestClient(){}

  public void enableSSL() throws Exception {
	  
	  TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

	  	SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
  }
  
  public RestClient(String server) {
	  
	  if("https".equals(server.substring(0, 5))) {
		  try {
			  enableSSL();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
  	  }
	  init(server);
  }
  
  @SuppressWarnings("static-access")
  public RestClient(String server, boolean isSelfSignedCert) {
	  if(isSelfSignedCert){
		  ClientHttpRequestFactory requestFactory;
		try{
			SSLContext sslContext = new SSLContexts().custom().loadTrustMaterial(null,  new TrustSelfSignedStrategy()).useTLS().build();
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));
			HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().setSSLSocketFactory(sslConnectionSocketFactory).setDefaultCredentialsProvider(credentialsProvider).build();
			requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		}catch(Exception e){
			e.printStackTrace();
			requestFactory = new HttpComponentsClientHttpRequestFactory();
		}
		this.rest = new RestTemplate(requestFactory);
	  }else{
		init(server);
	  }
  }
  
  private void init(String server){
    this.rest = new RestTemplate();
	this.headers = new HttpHeaders();
	this.server = server;
	headers.add("Content-Type", "application/json; charset=UTF-8");
	headers.add("Accept", "*/*");
  }
  
  public String get(String uri) {
    HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
    HttpStatus status = responseEntity.getStatusCode();
    String body = responseEntity.getBody();
    
    logger.debug("get=================================================================");
    logger.debug("url : " + server + uri);
    logger.debug("status : " + status.toString());
    logger.debug("body : " + body);
    logger.debug("get=================================================================");
    
    this.setStatus(status);
    return body;
  }

  public String post(String uri, String json) {   
	  
	  
    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
    HttpStatus status = responseEntity.getStatusCode();
    String body = responseEntity.getBody();
    
    logger.debug("post=================================================================");
    logger.debug("url : " + server + uri);
    logger.debug("param : " + json);
    logger.debug("status : " + status.toString());
    logger.debug("body : " + body);
    logger.debug("post=================================================================");
    
    this.setStatus(status);
    return body;
  }

  @SuppressWarnings("rawtypes")
  public void put(String uri, String json) {
    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
    ResponseEntity responseEntity = rest.exchange(server + uri, HttpMethod.PUT, requestEntity, ResponseEntity.class);
    this.setStatus(responseEntity.getStatusCode());   
  }

  @SuppressWarnings("rawtypes")
  public void delete(String uri) {
    HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
    ResponseEntity responseEntity = rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, ResponseEntity.class);
    this.setStatus(responseEntity.getStatusCode());
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
  
  public RestTemplate getRestTemplate(){
    return this.rest;
  }
}