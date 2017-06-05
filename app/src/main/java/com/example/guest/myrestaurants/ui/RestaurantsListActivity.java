package com.example.guest.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.guest.myrestaurants.Constants;
import com.example.guest.myrestaurants.R;
import com.example.guest.myrestaurants.adapters.RestaurantListAdapter;
import com.example.guest.myrestaurants.models.Restaurant;
import com.example.guest.myrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsListActivity extends AppCompatActivity {
    public static final String TAG = RestaurantsListActivity.class.getSimpleName();

//    private SharedPreferences mSharedPreferences;
//    private String mRecentAddress;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
//        if(mRecentAddress != null) {
//            getRestaurants(mRecentAddress);
//        }

        getRestaurants(location);
    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();

        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                RestaurantsListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantsListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }
}