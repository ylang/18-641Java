package com.ece.smartGallery.activity.fb;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ece.smartGallery.R;
import com.ece.smartGallery.entity.Photo;
import com.ece.smartGallery.util.Utility;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class FBFragment extends Fragment {

	// fragment activity context
	private FragmentActivity activity;

	// login button ui
	private UiLifecycleHelper uiHelper;
	private Button postButton;

	// permissions
	private static final List<String> PERMISSIONS = Arrays.asList(
			"publish_stream", "publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	// the photo from the display activity
	private Uri photoUri;

	// debugging log tag
	private static final String TAG = "SmartGallery.LoginFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		Intent intent = this.activity.getIntent();
		photoUri = intent.getParcelableExtra(Photo.IMAGE);
		uiHelper = new UiLifecycleHelper(this.activity,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChange(session, state, exception);
					}
				});
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_login, container, false);
		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setPublishPermissions(PERMISSIONS);
		authButton.setFragment(this);

		postButton = (Button) view.findViewById(R.id.post);
		postButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postPhoto();
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
		super.onActivityResult(requestCode, resultCode, data);
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

	/**
	 * Scale photo and post to facebook.
	 */
	private void postPhoto() {
		Session session = Session.getActiveSession();
		if (session == null) {
			Log.i(TAG, "session is null");
			return;
		}

		List<String> permissions = session.getPermissions();
		if (!isSubsetOf(PERMISSIONS, permissions)) {
			Log.i(TAG, "don't have permission");
			pendingPublishReauthorization = true;
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					this, PERMISSIONS);
			session.requestNewPublishPermissions(newPermissionsRequest);
			return;
		}

		Bitmap bm = null;
		try {
			bm = Utility.scaleImage(this.activity, photoUri);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final FragmentActivity fa = this.activity;
		Request request = Request.newUploadPhotoRequest(
				Session.getActiveSession(), bm, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						Toast toast = Toast
								.makeText(fa, "Successfully posted photo!",
										Toast.LENGTH_LONG);
						toast.show();
					}
				});
		Bundle params = request.getParameters();
		params.putString("message", "Smart Gallery App photo upload");
		request.executeAsync();
	}

	/**
	 * @param subset
	 * @param superset
	 * @return if the first set is a subset of the second set.
	 */
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				Log.i(TAG, "dont have" + string);
				return false;
			}
		}
		return true;
	}
}