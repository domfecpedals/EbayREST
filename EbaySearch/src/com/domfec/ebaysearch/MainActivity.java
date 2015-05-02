package com.domfec.ebaysearch;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

	private Spinner sortList;
	//Used to store value to spinner
	List<String> sortOptions = new ArrayList<String>();
	Button Request;
	EditText keywordText;
	EditText priceFromText;
	EditText priceToText;
	Spinner sort;
	String searchURL="http://cs-server.usc.edu:49185/hw9.php";
	String actualURL="";
	ProgressDialog pd;
	String searchResult="";
	TextView errorMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create sort select spinner
		addSortSpinner();
		
		
		// Create listener for two buttons
		Request = (Button) findViewById(R.id.clearBtn);
		Request.setOnClickListener(new ClearClickListener());
		Request = (Button) findViewById(R.id.searchBtn);
		Request.setOnClickListener(new SearchClickListener());
		sort = (Spinner)findViewById(R.id.sortSpinner);
		
		keywordText   = (EditText)findViewById(R.id.keywordInput);
		priceFromText   = (EditText)findViewById(R.id.priceFromInput);
		priceToText   = (EditText)findViewById(R.id.priceToInput);
		
	}
	
	public boolean validateInput(){
		String key=keywordText.getText().toString();
		errorMsg=(TextView) findViewById(R.id.errorMsg);
		if(key.equals("")){
			errorMsg.setText("Please enter a keyword");
			errorMsg.setVisibility(View.VISIBLE);
			return false;
		}
		
		
		
		String from=priceFromText.getText().toString();
		if(!from.equals("")){
			
			if(from.matches("^[0-9]+(\\.[0-9]+)?$")){	
				
			}else{
				errorMsg.setText("Price must be a valid number");
				errorMsg.setVisibility(View.VISIBLE);
				return false;
			}
		}
		
		String to=priceToText.getText().toString();
		if(!to.equals("")){
			
			if(to.matches("^[0-9]+(\\.[0-9]+)?$")){	
				
			}else{
				errorMsg.setText("Price must be a valid number");
				errorMsg.setVisibility(View.VISIBLE);
				return false;
			}
		}
		
		//Validate price
		if(!from.equals("")&!to.equals("")){
			if(Double.parseDouble(to)<Double.parseDouble(from)){
				errorMsg.setText("Max price should not be less than the min price");
				errorMsg.setVisibility(View.VISIBLE);
				return false;
			}
		}
		
		
		errorMsg.setText("");
		errorMsg.setVisibility(View.INVISIBLE);
		return true;
	}
	
	
	public class SearchClickListener implements OnClickListener{
		public void onClick(View v){

			
			keywordText.getText().toString();
			String priceFrom=priceFromText.getText().toString();
			String priceTo=priceToText.getText().toString();
			String urlPara="?keyword=";
			try {
				urlPara+=URLEncoder.encode(keywordText.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			
			int sortIndex=sort.getSelectedItemPosition();
			String sortMethod=sortOptions.get(sortIndex);
			urlPara+="&sort="+sortMethod;
		
			if(priceFrom.length()!=0){
				urlPara+="&rangefrom="+priceFrom;
			}
			if(priceTo.length()!=0){
				urlPara+="&rangeto="+priceTo;
			}

			actualURL=searchURL+urlPara;
			
			if(validateInput()){
				GETcall prepare = new  GETcall();
				prepare.execute();
			}
			
		}
	}
	
	
	protected class GETcall extends AsyncTask<Context, Integer, String> {
		@Override
		protected String doInBackground(Context... params) {
			String json_string = "";
			try {
				HttpGet httppost = new HttpGet(actualURL);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse responce = httpclient.execute(httppost);
				HttpEntity httpEntity = responce.getEntity();
				json_string = EntityUtils.toString(httpEntity);
			} catch (Exception e) {
				Log.v("Debug", "Connection failed");
			}
			return json_string;
		}

		@Override
		protected void onPostExecute(String result) {
			String ack="";
			try{
				JSONObject jobj=new JSONObject(result);
				ack=jobj.getString("ack");
				
				searchResult=result;
				
			}catch (Throwable t) {
			    Log.e("JSON bug", "failed");
			}
			
			
			if(ack.equals("Success")){
				errorMsg.setText("");
				errorMsg.setVisibility(View.INVISIBLE);
				//Open results display page
				Intent intent = new Intent(MainActivity.this, DisplayResults.class);
				intent.putExtra("output", searchResult);
				intent.putExtra("keyword", keywordText.getText().toString());
				startActivity(intent);
			}else{
				errorMsg.setText("No result found");
				errorMsg.setVisibility(View.VISIBLE);
			}
			

		}
	}
	
	public class ClearClickListener implements OnClickListener{
		public void onClick(View v){
			keywordText.setText("");
			priceFromText.setText("");
			priceToText.setText("");
			sort.setSelection(0);
			actualURL="";
			TextView errorMsg=(TextView) findViewById(R.id.errorMsg);
			errorMsg.setText("");
			errorMsg.setVisibility(View.INVISIBLE);
		}
	}
	
	
	public void addSortSpinner() {
		   
	        sortList = (Spinner) findViewById(R.id.sortSpinner);
	        List<String> list = new ArrayList<String>();
	        list.add("Best Match");
	        sortOptions.add("BestMatch");
	        list.add("Price:highest first");
	        sortOptions.add("CurrentPriceHighest");
	        list.add("Price+Shipping:highest first");
	        sortOptions.add("PricePlusShippingHighest");
	        list.add("Price+Shipping:lowest first");
	        sortOptions.add("PricePlusShippingLowest");
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, list);
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        sortList.setAdapter(dataAdapter);
	        
	 }
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
