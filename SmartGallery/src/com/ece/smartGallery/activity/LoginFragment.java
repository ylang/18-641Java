package com.ece.smartGallery.activity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ece.smartGallery.R;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.FacebookError;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

	private UiLifecycleHelper uiHelper;
	private static final String TAG = "WAT";
	private Button postButton;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_login, container, false);
		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);

		postButton = (Button) view.findViewById(R.id.post);
		postButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hehe();
				Log.i("WAT", "hehe done");
				// publishStory();
			}
		});

		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}
		return view;
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			postButton.setVisibility(View.VISIBLE);
		} else if (state.isClosed()) {
			postButton.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		Log.i("WAT", "onActivityResult");
		switch (requestCode) {
		/*
		 * if this is the result for a photo picker from the gallery, upload the
		 * image after scaling it. You can use the Utility.scaleImage() function
		 * for scaling
		 */
		case 42: {
			Log.i("WAT", "onActivityResult case 42");
			if (resultCode == Activity.RESULT_OK) {
				Uri photoUri = data.getData();
				if (photoUri != null) {
					Bundle params = new Bundle();
					Log.i("WAT", photoUri.getPath());
					try {
						params.putByteArray("photo",
								Utility.scaleImage(getActivity(), photoUri));
					} catch (IOException e) {
						e.printStackTrace();
					}
					params.putString("caption",
							"FbAPIs Sample App photo upload");
					Utility.mAsyncRunner.request("me/photos", params, "POST",
							new PhotoUploadListener(), null);
				} else {
					Toast.makeText(getActivity(),
							"Error selecting image from the gallery.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), "No image selected for upload.",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		}

		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		uiHelper.onSaveInstanceState(outState);
	}

	// private void publishStory() {
	// Session session = Session.getActiveSession();
	//
	// if (session != null) {
	// // Check for publish permissions
	// List<String> permissions = session.getPermissions();
	// if (!isSubsetOf(PERMISSIONS, permissions)) {
	// pendingPublishReauthorization = true;
	// Session.NewPermissionsRequest newPermissionsRequest = new
	// Session.NewPermissionsRequest(
	// this, PERMISSIONS);
	// session.requestNewPublishPermissions(newPermissionsRequest);
	// return;
	// }
	//
	// Bundle postParams = new Bundle();
	// postParams.putString("name", "Facebook SDK for Android");
	// postParams.putString("caption",
	// "Build great social apps and get more installs.");
	// postParams
	// .putString(
	// "description",
	// "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	// postParams.putString("link",
	// "https://developers.facebook.com/android");
	// postParams
	// .putString("picture",
	// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
	//
	// Request.Callback callback = new Request.Callback() {
	// public void onCompleted(Response response) {
	// JSONObject graphResponse = response.getGraphObject()
	// .getInnerJSONObject();
	// String postId = null;
	// try {
	// postId = graphResponse.getString("id");
	// } catch (JSONException e) {
	// Log.i(TAG, "JSON error " + e.getMessage());
	// }
	// FacebookRequestError error = response.getError();
	// if (error != null) {
	// Toast.makeText(getActivity().getApplicationContext(),
	// error.getErrorMessage(), Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// Toast.makeText(getActivity().getApplicationContext(),
	// postId, Toast.LENGTH_LONG).show();
	// }
	// }
	// };
	//
	// Request request = new Request(session, "me/feed", postParams,
	// HttpMethod.POST, callback);
	//
	// RequestAsyncTask task = new RequestAsyncTask(request);
	// task.execute();
	// }
	// }
	//
	// private boolean isSubsetOf(Collection<String> subset,
	// Collection<String> superset) {
	// for (String string : subset) {
	// if (!superset.contains(string)) {
	// return false;
	// }
	// }
	// return true;
	// }

	private void hehe() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
		Log.i("what", "hehe");
		startActivityForResult(intent, 42);
	}

	public class PhotoUploadListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			// dialog.dismiss();
			// mHandler.post(new Runnable() {
			// @Override
			// public void run() {
			// new UploadPhotoResultDialog(Hackbook.this,
			// "Upload Photo executed", response).show();
			// }
			// });
		}

		public void onFacebookError(FacebookError error) {
			// dialog.dismiss();
			// Toast.makeText(getApplicationContext(),
			// "Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG)
			// .show();
		}
	}

}