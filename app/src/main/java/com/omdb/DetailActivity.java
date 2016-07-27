package com.omdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements GetDataAsync.SetOnGetDetails {

    private LinearLayout detailslinearLayout;
    private GameofThrones.Episodes selectedEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detailslinearLayout = (LinearLayout) findViewById(R.id.detailsMainLayout);
        ImageView backImageView = (ImageView) findViewById(R.id.backImage);
        ImageView homeImageView = (ImageView) findViewById(R.id.homeImage);
        ImageView msgImageView = (ImageView) findViewById(R.id.msgImage);

        selectedEpisode = (GameofThrones.Episodes) getIntent().getExtras().getSerializable("EPISODE");

        getSupportActionBar().setTitle(selectedEpisode.Title);


        if (backImageView != null) {
            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (homeImageView != null) {
            homeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DetailActivity.this, "Home Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (msgImageView != null) {
            msgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DetailActivity.this, "Message Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void getData() {
        new GetDataAsync(this, GetDataAsync.GET_DETAILS, this, selectedEpisode.imdbID).execute();
    }

    public View getDetailItem(String detailKey, String detailValue) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.detail_item, null);

        TextView detailTitle = (TextView) view.findViewById(R.id.tv_detail_title);
        TextView detailValueTextView = (TextView) view.findViewById(R.id.tv_detailValue);

        detailTitle.setText(detailKey);
        detailValueTextView.setText(detailValue);

        return view;
    }

    @Override
    public void onGetDetails(HashMap<String, String> details) {
        for (String s : details.keySet()) {
            if (!TextUtils.isEmpty(details.get(s))) {
                detailslinearLayout.addView(getDetailItem(s, details.get(s)));
            }
        }
    }
}
