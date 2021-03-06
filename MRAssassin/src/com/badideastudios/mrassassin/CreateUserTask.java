package com.badideastudios.mrassassin;

import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.AsyncTask;

/*
 * CreateUserTask is used to create an asynchronous task 
 * for performing XML POST commands and relaying our client information
 * back to the server
 */
public class CreateUserTask extends AsyncTask<Void, Void, Boolean>{
	public WeakReference<Activity> parentActivity;
	XMLDelegate delegate;
	DefaultHandler handler;
	public String outputString = "I vant to kill your code! Ah, ah ah!";
	private String name, targetURL, mac;
	private int score;
	private double lat, lon;
	private boolean getsResponse = false;

	
	private String contentType, content, response;
	
	public CreateUserTask(Activity activity)
	{
		parentActivity = new WeakReference<Activity>(activity);
	}
	
	public CreateUserTask(Activity activity, XMLDelegate delegate, DefaultHandler handler)
	{
		parentActivity = new WeakReference<Activity>(activity);
		this.delegate = delegate;
		this.handler = handler;
	}
/*	
	public CreateUserTask(Activity activity, XMLDelegate delegate, DefaultHandler handler)
	{
		parentActivity = new WeakReference<Activity>(activity);
		this.delegate = delegate;
		this.handler = handler;
		
	}
*/
	@Override
	protected void onPreExecute()
	{
		
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {

		try
		{
			// Create the XML string we want to send
			String xmlString = new String();
			// And create the address we want to use (for now, we'll just use one address)
			String content = this.content;
			String contentType = this.contentType;
			String postURL = targetURL;
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(postURL);
		
			
			// Create a string entity of our XML string
			StringEntity se = new StringEntity(content, HTTP.UTF_8);
			// Set ContentType to "application/xml" so the server will accept it
			se.setContentType(contentType);//"application/xml");

			httppost.setEntity(se);
			HttpResponse httpresponse = httpclient.execute(httppost);
			String strResponse = EntityUtils.toString(httpresponse.getEntity());
			if((strResponse != null) && getsResponse)
			{
				outputString = strResponse;
				System.out.println(strResponse);
			}
	//		HttpEntity he = httpresponse.getEntity();
			System.out.println("HEAR YE HEAR YE, " + httpresponse.getStatusLine().getStatusCode());
			
			return (httpresponse.getStatusLine().getStatusCode() == 200);
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
		

	@Override
	protected void onPostExecute(Boolean result)
	{
		if(result)
		{
			System.out.println("Successful delivery");
		}
		else System.out.println("Shamefrul dispray");
		if(getsResponse)
		{
			XMLParser xmlp = new XMLParser(delegate, handler);
			xmlp.setInput(outputString);
			xmlp.execute();
		}
	}


	public void SetInformation(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public void SetAddress(String address)
	{
		targetURL = address;
	}
	
	public void SetMAC(String mac)
	{
		this.mac = mac;
	}
	
	public void SetName(String tag)
	{
		this.name = tag;
	}
	
	public void SetLat(Double lat)
	{
		this.lat = lat;
	}
	
	public void SetLon(Double lon)
	{
		this.lon = lon;
	}
	
	public void SetContent(String content)
	{
		this.content = content;
	}
	
	public void SetContentType(String contentType)
	{
		this.contentType = contentType;
	}
	
	public void setResponse(boolean response)
	{
		this.getsResponse = response;
	}
	public String GetOutput()
	{
		return outputString;
	}
	

}
