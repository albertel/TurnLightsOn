package com.albertelli.android.turnonlights.turnlightson;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.albertelli.android.turnonlights.turnlightson.turnlightson.R;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.
import org.w3c.dom.Text;

import java.util.Date;

public class LightsFragment extends Fragment {
    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        @Override
        protected void onLocationReceived(Context context, Location location) {
            mLastLocation = location;
            if (isVisible()) {
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            String toastText = enabled ? "GPS Enabled" : "GPS Disabled";
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
        }
    };

    private LightsManager mLightsManager;

    private Button mB1, mB2;
    private TextView mLatitudeTextView, mLongitudeTextView, mDistanceTextView, mMessagesTextView;
    private Lights mLights;
    private Location mLastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLightsManager = LightsManager.get(getActivity());
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lights, container, false);

        mLatitudeTextView = (TextView)view.findViewById(R.id.lights_latitudeTextView);
        mLongitudeTextView = (TextView)view.findViewById(R.id.lights_longitudeTextView);
        mDistanceTextView = (TextView)view.findViewById(R.id.lights_distance);
        mMessagesTextView = (TextView)view.findViewById(R.id.lights_messages);

        mB1 = (Button)view.findViewById(R.id.lights_b1);
        mB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightsManager.stopLocationUpdates();
                updateUI();
            }
        });
        mB2 = (Button)view.findViewById(R.id.lights_b2);
        mB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightsManager.startLocationUpdates();
                updateUI();
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(LightsManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private void updateUI() {
        if (mLastLocation != null) {
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mMessagesTextView.append("updated " + System.currentTimeMillis() +"\n");
        }

        boolean started = mLightsManager.isTrackingLocation();
        mB2.setEnabled(!started);
        mB1.setEnabled(started);
    }
}
