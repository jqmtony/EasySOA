/**
 * 
 */
package org.openwide.easysoa.test.messaging;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.easysoa.EasySOAConstants;
import org.easysoa.records.ExchangeRecord;
import org.easysoa.records.persistence.filesystem.ExchangeRecordFileStore;
import org.easysoa.template.TemplateBuilder;
import org.easysoa.template.TemplateFieldSuggester;
import org.easysoa.template.TemplateProcessorRendererItf;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openwide.easysoa.test.mock.meteomock.client.MeteoMock;
import org.openwide.easysoa.test.mock.meteomock.client.MeteoMockPortType;
import org.openwide.easysoa.test.monitoring.apidetector.UrlMock;
import org.openwide.easysoa.test.util.AbstractProxyTestStarter;
import org.ow2.frascati.util.FrascatiException;

import com.openwide.easysoa.util.ContentReader;

/**
 * To test replay templates
 * 
 * @author jguillemotte
 *
 */
public class TemplateTest extends AbstractProxyTestStarter {

	// Logger
	private static Logger logger = Logger.getLogger(TemplateTest.class.getName());

	@BeforeClass
	public static void setUp() throws FrascatiException{
		// Start fraSCAti
		startFraSCAti();
		// Start Http Discovery proxy
		try {
			startHttpDiscoveryProxy("httpDiscoveryProxy.composite", new URL[] { new URL("file://target") });
		} catch (MalformedURLException e) {
			throw new FrascatiException("TemplateTest init error", e);
		}
		// Start mock services
		startMockServices(false);
	}
	
	/**
	 * ReplayTemplateWithDefaultValue : Use the replayTemplate.html velocity HTML template to build a form.
	 * Only with REST Exchanges
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void replayTemplateWithDefaultValue() throws ClientProtocolException, IOException{
		// TODO : Complete this test with 4 kinds of parameters : formParams, pathParams, QueryParams, WSDLParams
		
		// Read template file with a GET HTTP request
		// returns a request for the replay system
		DefaultHttpClient httpClient = new DefaultHttpClient();		
		HttpGet getRequest = new HttpGet("http://localhost:8090/runManager/exchangeRecordStore/replayTemplate.html");
		String response = httpClient.execute(getRequest, new BasicResponseHandler());
		logger.debug("GetTemplate response = " + response);
		assertTrue(response.contains("comment (String) : <input type=\"text\" name=\"comment\" value=\"test\" />"));
		// Specify the exchangeRecord to use => run/exchangeRecord to get the request 
		
		HttpPost postRequest = new HttpPost("http://localhost:" + EasySOAConstants.EXCHANGE_RECORD_REPLAY_SERVICE_PORT + "/templates/replayWithTemplate/Test_Run/1/testTemplate");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("user", "FR3Bourgogne.xml")); // tests HTTP Form-like params (others being HTTP path, query and SOAP)
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		postRequest.setEntity(entity);
		//HttpParams httpParams = new BasicHttpParams();
		//httpParams.setParameter("", "multipart/form-data");
		//postRequest.setParams(httpParams);
		response = httpClient.execute(postRequest, new BasicResponseHandler());
		logger.debug("replayWithTemplate response = " + response);
		
		// Set the custom values :
		
		// For WSDL : re-use the the existing code working in the scaffolder proxy
		// For Rest : 3 different cases :
		// Form params : get the inMessage content and change the values
		// Query params : get the complete url and change the values
		// Path Params : This case is harder, need to know the position of each param in the url ... must be specified in the template OR use the discovery mechanism from HTTP discovery proxy 
		
		// replay the request with specified values (can be default values)  
	}

	/**
	 * TemplateFieldSuggester test with REST Exchanges 
	 * @throws Exception
	 */
	@Test
	public void templateFieldSuggesterRestTest() throws Exception {

		String runName = "TweeterRestTestRun";
		// Start run
		startNewRun(runName);
		
		// Send tweeter mock requests
		// Get the twitter mock set and send requests to the mock through the HTTP proxy
		DefaultHttpClient httpProxyClient = new DefaultHttpClient();

		// Set client to use the HTTP Discovery Proxy
		HttpHost proxy = new HttpHost("localhost", EasySOAConstants.HTTP_DISCOVERY_PROXY_PORT);
		httpProxyClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		
		
		// Send first request - GET request with only path parameters
		HttpResponse response;
		HttpGet lastTweetRequest = new HttpGet("http://localhost:" + EasySOAConstants.TWITTER_MOCK_PORT + "/1/tweets/lastTweet/toto");
		response = httpProxyClient.execute(lastTweetRequest);
		// Need to read the response body entirely to be able to send another request
		ContentReader.read(response.getEntity().getContent());			
		
		// Send second request - GET request with path parameters and query parameters
		HttpGet severalTweetRequest = new HttpGet("http://localhost:" + EasySOAConstants.TWITTER_MOCK_PORT + "/1/tweets/severalTweets/toto?tweetNumber=5");
		response = httpProxyClient.execute(severalTweetRequest);
		ContentReader.read(response.getEntity().getContent());		
		
		// Send third request - POST request with form parameters
		HttpPost postTweetRequest = new HttpPost("http://localhost:" + EasySOAConstants.TWITTER_MOCK_PORT + "/1/tweets/postNewTweet");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("user", "toto"));
		formparams.add(new BasicNameValuePair("tweet", "This is a tweet test"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		postTweetRequest.setEntity(entity);
		response = httpProxyClient.execute(postTweetRequest);
		ContentReader.read(response.getEntity().getContent());
		
		// Stop, save and delete run
		stopAndSaveRun();
		deleteRun();
		
		TemplateFieldSuggester suggester = new TemplateFieldSuggester();
		TemplateBuilder builder = new TemplateBuilder();
		ExchangeRecordFileStore fileStore= new ExchangeRecordFileStore();
	
		// Get the exchange record list for the run
		List<ExchangeRecord> recordList = fileStore.getExchangeRecordlist(runName);

		// Get the template renderer
		TemplateProcessorRendererItf processor = frascati.getService(componentList.get(0), "processor", org.easysoa.template.TemplateProcessorRendererItf.class);

		// Build an HashMap to simulate user provided values
		HashMap<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("user", "toto");
		
		// For each custom record in the list
		for(ExchangeRecord record : recordList){
			// Build the templates with suggested fields
			Map<String, String> templateFileMap = builder.buildTemplate(suggester.suggest(record), record, runName);
			// Render the templates and replay the request
			if(templateFileMap != null){
				logger.debug("returned message form replayed template : " + processor.renderReq(templateFileMap.get("reqTemplate"), record, runName, fieldMap));
				// TODO : call the renderRes method for server mock test
			}
		}
	}
	
	/**
	 * TemplateFieldSuggester test with SOAP Exchanges 
	 * @throws Exception
	 */
	@Test
	public void templateFieldSuggesterSOAPTest() throws Exception {	
		
		String runName = "MeteoSoapTestRun";
		// Start run
		startNewRun(runName);		

		// Set the discovery proxy
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8082");
		
		// Send the test requests
		UrlMock urlMock = new UrlMock();
		for(String cityName : urlMock.getMeteoMockCities()){
			MeteoMock meteoService = new MeteoMock();
			MeteoMockPortType port = meteoService.getMeteoMockPort();
			String response = port.getTomorrowForecast(cityName);
			logger.info("Response for " + cityName + " : " + response);
		}
		
		// Remove the discovery proxy
		System.setProperty("http.proxyHost", "");
		System.setProperty("http.proxyPort", "");
		
		// Stop, save and delete run
		stopAndSaveRun();
		deleteRun();

		TemplateFieldSuggester suggester = new TemplateFieldSuggester();
		TemplateBuilder builder = new TemplateBuilder();
		ExchangeRecordFileStore fileStore= new ExchangeRecordFileStore();
	
		List<ExchangeRecord> recordList = fileStore.getExchangeRecordlist(runName);

		// Get the template renderer
		TemplateProcessorRendererItf processor = frascati.getService(componentList.get(0), "processor", org.easysoa.template.TemplateProcessorRendererItf.class);

		// Build an HashMap to simulate user provided values
		HashMap<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("user", "toto");
		
		// For each custom record in the list
		for(ExchangeRecord record : recordList){
			// Build the templates with suggested fields
			Map<String, String> templateFileMap = builder.buildTemplate(suggester.suggest(record), record, runName);
			// Render the templates and replay the request
			if(templateFileMap != null){
				logger.debug("returned message form replayed template : " + processor.renderReq(templateFileMap.get("reqTemplate"), record, runName, fieldMap));
				// TODO : call the renderRes method for server mock test
			}
		}		
	}
		
	/**
	 * This test do nothing, just wait for a user action to stop the proxy. 
	 * @throws ClientException
	 * @throws SOAPException
	 * @throws IOException
	 */
	@Test
	//@Ignore
	public final void testWaitUntilRead() throws Exception {
		logger.info("TemplateTest started, wait for user action to stop !");
		// Just push a key in the console window to stop the test
		System.in.read();
		logger.info("TemplateTest stopped !");
	}
}
