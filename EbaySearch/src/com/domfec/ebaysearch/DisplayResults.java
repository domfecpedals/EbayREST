package com.domfec.ebaysearch;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;

public class DisplayResults extends ActionBarActivity {
	JSONObject obj;
	TextView banner;
	TextView title;
	TextView price;
	TextView titleHandler;
	ImageView gallery;
	ImageView imgHandler;
	Bitmap bitmap;
    ProgressDialog pDialog;
    String priceInfo;
    String shippingCost;
    ImageLoadTask loadImage;
    
    
    JSONObject item1;
	JSONObject item2;
	JSONObject item3;
	JSONObject item4;
	JSONObject item5;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_main);
		
		
		
		titleHandler = (TextView) findViewById(R.id.title0);
		titleHandler.setOnClickListener(new title0ClickListener());
		titleHandler = (TextView) findViewById(R.id.title1);
		titleHandler.setOnClickListener(new title1ClickListener());
		titleHandler = (TextView) findViewById(R.id.title2);
		titleHandler.setOnClickListener(new title2ClickListener());
		titleHandler = (TextView) findViewById(R.id.title3);
		titleHandler.setOnClickListener(new title3ClickListener());
		titleHandler = (TextView) findViewById(R.id.title4);
		titleHandler.setOnClickListener(new title4ClickListener());
		imgHandler=(ImageView) findViewById(R.id.imageView0);
		imgHandler.setOnClickListener(new image0ClickListener());
		imgHandler=(ImageView) findViewById(R.id.superImage);
		imgHandler.setOnClickListener(new image1ClickListener());
		imgHandler=(ImageView) findViewById(R.id.oneDayImg);
		imgHandler.setOnClickListener(new image2ClickListener());
		imgHandler=(ImageView) findViewById(R.id.returnImg);
		imgHandler.setOnClickListener(new image3ClickListener());
		imgHandler=(ImageView) findViewById(R.id.imageView4);
		imgHandler.setOnClickListener(new image4ClickListener());
		
		
		
		// Get all info from search results
		String fullresult = getIntent().getExtras().getString("output");
		String itemInfo;
		String keyword=getIntent().getExtras().getString("keyword");
		banner=(TextView) findViewById(R.id.resultsBanner);
		banner.setText("Results for '"+keyword+"'");
		
		try{
			obj = new JSONObject(fullresult);
			
			// For item1
			itemInfo =obj.getString("item0");
			item1=new JSONObject(itemInfo);
			// Add title
			title=(TextView) findViewById(R.id.title0);
			title.setText(item1.getJSONObject("basicInfo").getString("title"));
			// Load gallery image
			gallery=(ImageView)findViewById(R.id.imageView0);
			loadImage = new  ImageLoadTask(item1.getJSONObject("basicInfo").getString("galleryURL"),gallery);
			loadImage.execute();	
			// Add price and shipping
			priceInfo="Price: $"+item1.getJSONObject("basicInfo").getString("convertedCurrentPrice");

			shippingCost=item1.getJSONObject("basicInfo").getString("shippingServiceCost");
			if(shippingCost.equals("")|shippingCost.equals("0.0")){
				shippingCost=" (FREE shipping)";
			}else{
				shippingCost=" (+$"+shippingCost+" shipping)";
			}
			
			price=(TextView) findViewById(R.id.price0);
			price.setText(priceInfo+shippingCost);
			
			
			
			// For item2
			itemInfo =obj.getString("item1");
			item2=new JSONObject(itemInfo);
			// Add title
			title=(TextView) findViewById(R.id.title1);
			title.setText(item2.getJSONObject("basicInfo").getString("title"));
			// Load gallery image
			gallery=(ImageView)findViewById(R.id.superImage);
			loadImage = new  ImageLoadTask(item2.getJSONObject("basicInfo").getString("galleryURL"),gallery);
			loadImage.execute();	
			// Add price and shipping
			priceInfo="Price: $"+item2.getJSONObject("basicInfo").getString("convertedCurrentPrice");

			shippingCost=item2.getJSONObject("basicInfo").getString("shippingServiceCost");
			if(shippingCost.equals("")|shippingCost.equals("0.0")){
				shippingCost=" (FREE shipping)";
			}else{
				shippingCost=" (+$"+shippingCost+" shipping)";
			}
			price=(TextView) findViewById(R.id.price1);
			price.setText(priceInfo+shippingCost);
			
			// For item3
			itemInfo =obj.getString("item2");
			item3=new JSONObject(itemInfo);
			// Add title
			title=(TextView) findViewById(R.id.title2);
			title.setText(item3.getJSONObject("basicInfo").getString("title"));
			// Load gallery image
			gallery=(ImageView)findViewById(R.id.oneDayImg);
			loadImage = new  ImageLoadTask(item3.getJSONObject("basicInfo").getString("galleryURL"),gallery);
			loadImage.execute();	
			// Add price and shipping
			priceInfo="Price: $"+item3.getJSONObject("basicInfo").getString("convertedCurrentPrice");
			shippingCost=item3.getJSONObject("basicInfo").getString("shippingServiceCost");
			if(shippingCost.equals("")|shippingCost.equals("0.0")){
				shippingCost=" (FREE shipping)";
			}else{
				shippingCost=" (+$"+shippingCost+" shipping)";
			}
			price=(TextView) findViewById(R.id.price2);
			price.setText(priceInfo+shippingCost);
			
			
			// For item4
			itemInfo =obj.getString("item3");
			item4=new JSONObject(itemInfo);
			// Add title
			title=(TextView) findViewById(R.id.title3);
			title.setText(item4.getJSONObject("basicInfo").getString("title"));
			// Load gallery image
			gallery=(ImageView)findViewById(R.id.returnImg);
			loadImage = new  ImageLoadTask(item4.getJSONObject("basicInfo").getString("galleryURL"),gallery);
			loadImage.execute();	
			// Add price and shipping
			priceInfo="Price: $"+item4.getJSONObject("basicInfo").getString("convertedCurrentPrice");
			shippingCost=item4.getJSONObject("basicInfo").getString("shippingServiceCost");
			if(shippingCost.equals("")|shippingCost.equals("0.0")){
				shippingCost=" (FREE shipping)";
			}else{
				shippingCost=" (+$"+shippingCost+" shipping)";
			}
			price=(TextView) findViewById(R.id.price3);
			price.setText(priceInfo+shippingCost);
			
			
			
			// For item5
			itemInfo =obj.getString("item4");
			item5=new JSONObject(itemInfo);
			// Add title
			title=(TextView) findViewById(R.id.title4);
			title.setText(item5.getJSONObject("basicInfo").getString("title"));
			// Load gallery image
			gallery=(ImageView)findViewById(R.id.imageView4);
			loadImage = new  ImageLoadTask(item5.getJSONObject("basicInfo").getString("galleryURL"),gallery);
			loadImage.execute();	
			// Add price and shipping
			priceInfo="Price: $"+item5.getJSONObject("basicInfo").getString("convertedCurrentPrice");
			shippingCost=item5.getJSONObject("basicInfo").getString("shippingServiceCost");
			if(shippingCost.equals("")|shippingCost.equals("0.0")){
				shippingCost=" (FREE shipping)";
			}else{
				shippingCost=" (+$"+shippingCost+" shipping)";
			}
			price=(TextView) findViewById(R.id.price4);
			price.setText(priceInfo+shippingCost);
			
			
			
			

			
//			Log.v("Debug", item1.getJSONObject("basicInfo").getString("galleryURL"));
		}catch (Throwable t) {
		    Log.e("JSON bug", "failed");
		}
		
	}
	
	// AsyncTask to fetch JPEG from the URL
		public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		    private String url;
		    private ImageView imageView;

		    public ImageLoadTask(String url, ImageView imageView) {
		        this.url = url;
		        this.imageView = imageView;
		    }

		    @Override
		    protected Bitmap doInBackground(Void... params) {
		        try {
		            URL urlConnection = new URL(url);
		            Log.e("Async jpeg", urlConnection.toString());
		            HttpURLConnection connection = (HttpURLConnection) urlConnection
		                    .openConnection();
		            connection.setDoInput(true);
		            connection.connect();
		            InputStream input = connection.getInputStream();
		            Bitmap myBitmap = BitmapFactory.decodeStream(input);
		            return myBitmap;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return null;
		    }

		    @Override
		    protected void onPostExecute(Bitmap result) {
		        super.onPostExecute(result);
		        imageView.setImageBitmap(result);
		    }

		}
	
	public class title0ClickListener implements OnClickListener{
		public void onClick(View v){
			//Open details page
			Intent intent = new Intent(DisplayResults.this, DetailsActivity.class);
			intent.putExtra("itemDetails", item1.toString());
			startActivity(intent);
		}
	}
	public class title1ClickListener implements OnClickListener{
		public void onClick(View v){
			//Open details page
			Intent intent = new Intent(DisplayResults.this, DetailsActivity.class);
			intent.putExtra("itemDetails", item2.toString());
			startActivity(intent);
		}
	}
	public class title2ClickListener implements OnClickListener{
		public void onClick(View v){
			//Open details page
			Intent intent = new Intent(DisplayResults.this, DetailsActivity.class);
			intent.putExtra("itemDetails", item3.toString());
			startActivity(intent);
		}
	}
	public class title3ClickListener implements OnClickListener{
		public void onClick(View v){
			//Open details page
			Intent intent = new Intent(DisplayResults.this, DetailsActivity.class);
			intent.putExtra("itemDetails", item4.toString());
			startActivity(intent);
		}
	}
	public class title4ClickListener implements OnClickListener{
		public void onClick(View v){
			//Open details page
			Intent intent = new Intent(DisplayResults.this, DetailsActivity.class);
			intent.putExtra("itemDetails", item5.toString());
			startActivity(intent);
		}
	}
	
	
	public class image0ClickListener implements OnClickListener{
		public void onClick(View v){
			String path="";
			try {
				path=item1.getJSONObject("basicInfo").getString("viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(path)));
		}
	}
	public class image1ClickListener implements OnClickListener{
		public void onClick(View v){
			String path="";
			try {
				path=item2.getJSONObject("basicInfo").getString("viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(path)));
		}
	}
	public class image2ClickListener implements OnClickListener{
		public void onClick(View v){
			String path="";
			try {
				path=item3.getJSONObject("basicInfo").getString("viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(path)));
		}
	}
	public class image3ClickListener implements OnClickListener{
		public void onClick(View v){
			String path="";
			try {
				path=item4.getJSONObject("basicInfo").getString("viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(path)));
		}
	}
	public class image4ClickListener implements OnClickListener{
		public void onClick(View v){
			String path="";
			try {
				path=item5.getJSONObject("basicInfo").getString("viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(path)));
		}
	}
	

	
		
}
