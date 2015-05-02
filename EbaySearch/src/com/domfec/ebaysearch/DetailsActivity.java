package com.domfec.ebaysearch;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.facebook.*;
import com.facebook.share.*;
import com.facebook.share.model.*;
import com.facebook.share.widget.ShareDialog;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends ActionBarActivity {

	CallbackManager callbackManager;
	ShareDialog shareDialog;

	JSONObject itemObj;
	ImageButton basicB;
	ImageButton sellerB;
	ImageButton shippingB;
	ImageButton buyB;
	ImageView fbBtn;
	View basicV;
	View sellerV;
	View shippingV;
	ImageView superImage;
	String superURL;
	ImageLoadTask loadImage;
	


	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onSuccess(Sharer.Result result) {
			if(result.getPostId()==null){
				Toast.makeText(getApplicationContext(), "Post not posted!",
						Toast.LENGTH_SHORT).show();
			}else{
//			Log.e("POST ID", result.getPostId());
			Toast.makeText(getApplicationContext(), "Post shared!",
					Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Post not posted!",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(FacebookException e) {
			Log.e("JSON bug", e.toString());
			e.printStackTrace();
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_main);

		// Facebook initialization
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		// Button binding
		basicB = (ImageButton) findViewById(R.id.basicBtn);
		basicB.setOnClickListener(new basicBtnClickListener());
		sellerB = (ImageButton) findViewById(R.id.sellerBtn);
		sellerB.setOnClickListener(new sellerBtnClickListener());
		shippingB = (ImageButton) findViewById(R.id.shippingBtn);
		shippingB.setOnClickListener(new shippingBtnClickListener());

		buyB = (ImageButton) findViewById(R.id.buyBtn);
		buyB.setOnClickListener(new buyBtnClickListener());
		fbBtn = (ImageView) findViewById(R.id.fbBtn);
		fbBtn.setOnClickListener(new fbBtnClickListener());

		// bind tab view
		basicV = (View) findViewById(R.id.basicView);
		sellerV = (View) findViewById(R.id.sellerView);
		shippingV = (View) findViewById(R.id.shippingView);
		// initial set basic info visible
		basicV.setVisibility(View.VISIBLE);
		sellerV.setVisibility(View.GONE);
		shippingV.setVisibility(View.GONE);
		basicB.setImageResource(R.drawable.basicb);

		
		
		// Get information from previous activity
		String itemDetails = getIntent().getExtras().getString("itemDetails");

		try {
			itemObj = new JSONObject(itemDetails);

			// superImage at center of details page
			superURL = itemObj.getJSONObject("basicInfo").getString(
					"pictureURLSuperSize");
			superImage = (ImageView) findViewById(R.id.superImage);
			loadImage = new ImageLoadTask(superURL, superImage);
			loadImage.execute();

			// Title, price and location
			String title = itemObj.getJSONObject("basicInfo")
					.getString("title");
			TextView detailTitle = (TextView) findViewById(R.id.detailsTitle);
			detailTitle.setText(title);

			String priceInfo = "Price: $"
					+ itemObj.getJSONObject("basicInfo").getString(
							"convertedCurrentPrice");
			String shippingCost = itemObj.getJSONObject("basicInfo").getString(
					"shippingServiceCost");
			if (shippingCost.equals("") | shippingCost.equals("0.0")) {
				shippingCost = " (FREE shipping)";
			} else {
				shippingCost = " (+$" + shippingCost + " shipping)";
			}
			TextView detailsPrice = (TextView) findViewById(R.id.detailsPrice);
			detailsPrice.setText(priceInfo + shippingCost);

			String location = itemObj.getJSONObject("basicInfo").getString(
					"location");
			TextView detailsLocation = (TextView) findViewById(R.id.locationText);
			detailsLocation.setText(location);

			String topRate = itemObj.getJSONObject("basicInfo").getString(
					"topRatedListing");
			ImageView topRateBadge = (ImageView) findViewById(R.id.topRate);
			if (topRate.equals("true")) {
				topRateBadge.setVisibility(View.VISIBLE);
			} else {
				topRateBadge.setVisibility(View.INVISIBLE);
			}

		} catch (Throwable t) {
			Log.e("JSON bug", "failed");
		}

		loadBasic();
		loadSeller();
		loadShipping();
	}

	public void loadBasic() {
		try {
			JSONObject basicInfo = itemObj.getJSONObject("basicInfo");
			// Category
			String category = basicInfo.getString("categoryName");
			if(category.equals("")){
				category="N/A";
			}
			TextView categoryText = (TextView) findViewById(R.id.category);
			categoryText.setText(category);

			String condition = basicInfo.getString("conditionDisplayName");
			if(condition.equals("")){
				condition="N/A";
			}
			TextView conditionText = (TextView) findViewById(R.id.condition);
			conditionText.setText(condition);

			String format = basicInfo.getString("listingType");
			TextView formatText = (TextView) findViewById(R.id.format);
			formatText.setText(format);

		} catch (Throwable t) {
			Log.e("JSON bug", "failed");
		}
	}

	public void loadSeller() {
		try {
			JSONObject sellerInfo = itemObj.getJSONObject("sellerInfo");

			String userName = sellerInfo.getString("sellerUserName");
			TextView userText = (TextView) findViewById(R.id.userText);
			userText.setText(userName);

			String score = sellerInfo.getString("feedbackScore");
			TextView scoreText = (TextView) findViewById(R.id.scoreText);
			scoreText.setText(score);

			String positiveRate = sellerInfo
					.getString("positiveFeedbackPercent");
			TextView positiveText = (TextView) findViewById(R.id.positiveText);
			positiveText.setText(positiveRate + "%");

			String feedbackRating = sellerInfo.getString("feedbackRatingStar");
			TextView feedbackRatingText = (TextView) findViewById(R.id.ratingText);
			feedbackRatingText.setText(feedbackRating);

			String topSeller = sellerInfo.getString("topRatedSeller");
			ImageView topBadge = (ImageView) findViewById(R.id.topRateImg);
			if (topSeller.equals("true")) {
				topBadge.setImageResource(R.drawable.check);
			} else {
				topBadge.setImageResource(R.drawable.cross);
			}

			String store = sellerInfo.getString("sellerStoreName");
			if (store.equals("")) {
				store = "N/A";
			}
			TextView storeText = (TextView) findViewById(R.id.storeText);
			storeText.setText(store);

		} catch (Throwable t) {
			Log.e("JSON bug", "failed");
		}
	}

	public void loadShipping() {
		try {
			JSONObject shippingInfo = itemObj.getJSONObject("shippingInfo");

			String shippingType = shippingInfo.getString("shippingType");
			TextView shippingTypeText = (TextView) findViewById(R.id.shippingText);
			shippingTypeText.setText(splitCapital(shippingType));

			String shipToLocations = shippingInfo.getString("shipToLocations");
			TextView shipLocText = (TextView) findViewById(R.id.shipLocationText);
			shipLocText.setText(shipToLocations);

			String handling = shippingInfo.getString("handlingTime");
			TextView handleText = (TextView) findViewById(R.id.handlingText);
			if(handling.equals("1")){
				handleText.setText(handling + "day");
			}else{
				handleText.setText(handling + "days");
			}

			String expeditedShipping = shippingInfo
					.getString("expeditedShipping");
			ImageView expeditedImg = (ImageView) findViewById(R.id.expeditedImg);
			if (expeditedShipping.equals("true")) {
				expeditedImg.setImageResource(R.drawable.check);
			} else {
				expeditedImg.setImageResource(R.drawable.cross);
			}

			String oneDay = shippingInfo.getString("oneDayShippingAvailable");
			ImageView oneDayImg = (ImageView) findViewById(R.id.oneDayImg);
			if (oneDay.equals("true")) {
				oneDayImg.setImageResource(R.drawable.check);
			} else {
				oneDayImg.setImageResource(R.drawable.cross);
			}

			String returnsAccepted = shippingInfo.getString("returnsAccepted");
			ImageView returnImg = (ImageView) findViewById(R.id.returnImg);
			if (returnsAccepted.equals("true")) {
				returnImg.setImageResource(R.drawable.check);
			} else {
				returnImg.setImageResource(R.drawable.cross);
			}

		} catch (Throwable t) {
			Log.e("JSON bug", "failed");
		}
	}

	public class basicBtnClickListener implements OnClickListener {
		public void onClick(View v) {
			basicV.setVisibility(View.VISIBLE);
			sellerV.setVisibility(View.GONE);
			shippingV.setVisibility(View.GONE);
			basicB.setImageResource(R.drawable.basicb);
			sellerB.setImageResource(R.drawable.sellerw);
			shippingB.setImageResource(R.drawable.shipw);

		}
	}

	public class sellerBtnClickListener implements OnClickListener {
		public void onClick(View v) {
			basicV.setVisibility(View.GONE);
			sellerV.setVisibility(View.VISIBLE);
			shippingV.setVisibility(View.GONE);
			basicB.setImageResource(R.drawable.basicw);
			sellerB.setImageResource(R.drawable.sellerb);
			shippingB.setImageResource(R.drawable.shipw);

		}
	}

	public class shippingBtnClickListener implements OnClickListener {
		public void onClick(View v) {
			basicV.setVisibility(View.GONE);
			sellerV.setVisibility(View.GONE);
			shippingV.setVisibility(View.VISIBLE);
			basicB.setImageResource(R.drawable.basicw);
			sellerB.setImageResource(R.drawable.sellerw);
			shippingB.setImageResource(R.drawable.shipb);

		}
	}

	public class buyBtnClickListener implements OnClickListener {
		public void onClick(View v) {
			String path = "";
			try {
				path = itemObj.getJSONObject("basicInfo").getString(
						"viewItemURL");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
					.parse(path)));
		}
	}

	public class fbBtnClickListener implements OnClickListener {
		public void onClick(View v) {
			// Title, price and location
			try {
				String contentTitle = itemObj.getJSONObject("basicInfo")
						.getString("title");
				String contentDescription = "Price: $"
						+ itemObj.getJSONObject("basicInfo").getString(
								"convertedCurrentPrice");
				String shippingCost = itemObj.getJSONObject("basicInfo")
						.getString("shippingServiceCost");
				if (shippingCost.equals("") | shippingCost.equals("0.0")) {
					shippingCost = " (FREE shipping)";
				} else {
					shippingCost = " (+$" + shippingCost + " shipping), ";
				}
				contentDescription=contentDescription + shippingCost;
				contentDescription = contentDescription
						+ " Location: "
						+ itemObj.getJSONObject("basicInfo").getString(
								"location");
				String contentURL = itemObj.getJSONObject("basicInfo")
						.getString("viewItemURL");
				String imageURL = itemObj.getJSONObject("basicInfo").getString(
						"galleryURL");

				if (ShareDialog.canShow(ShareLinkContent.class)) {
					ShareLinkContent linkContent = new ShareLinkContent.Builder()
							.setContentTitle(contentTitle)
							.setContentDescription(contentDescription)
							.setContentUrl(Uri.parse(contentURL))
							.setImageUrl(Uri.parse(imageURL)).build();
					shareDialog.show(linkContent,ShareDialog.Mode.FEED);
				}

			} catch (Throwable t) {
				Log.e("JSON bug", "failed");
			}

		}
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);

	}
	
	public String splitCapital(String oldStr){
	    String split_string = "";
	    for (int i=0; i < oldStr.length(); i++){
	        char c = oldStr.charAt(i);
	        if(Character.isUpperCase(c)){               
	        	split_string = split_string + " " + c;
	        }
	        else {
	        	split_string = split_string + c;
	        }
	    }
	    split_string=split_string.substring(1,split_string.length());
	    return split_string;
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
	
	
	

}
