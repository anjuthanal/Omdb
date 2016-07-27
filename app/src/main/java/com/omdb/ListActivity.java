package com.omdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements GetDataAsync.SetOnGetEpisodes {

    private RecyclerView recyclerView;
    private ArrayList<GameofThrones.Episodes> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Game of Thrones");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView backImageView = (ImageView) findViewById(R.id.backImage);
        ImageView homeImageView = (ImageView) findViewById(R.id.homeImage);
        ImageView msgImageView = (ImageView) findViewById(R.id.msgImage);

        if (backImageView != null) {
            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ListActivity.this, "Back Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (homeImageView != null) {
            homeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ListActivity.this, "Home Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (msgImageView != null) {
            msgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ListActivity.this, "Message Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        getData();
    }

    public void getData() {
        new GetDataAsync(this, GetDataAsync.GET_EPISODES, this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onGetEpisodes(GameofThrones gameofThrones) {
        if (gameofThrones != null) {
            if (gameofThrones.Episodes != null && gameofThrones.Episodes.size() > 0) {
                this.episodes = gameofThrones.Episodes;
                recyclerView.setAdapter(new RecyclerViewAdapter());
            }
        }
    }

    // adapter to load audio content into recycler view
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {


        @Override
        public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item, null);
            return new RecyclerViewHolders(layoutView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolders holder, int position) {
            holder.episodeName.setText(episodes.get(position).Title);
        }

        @Override
        public int getItemCount() {
            return episodes.size();
        }
    }

    /**
     * View holder of recyclerview
     */
    class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView episodeName;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            episodeName = (TextView) itemView.findViewById(R.id.tv_episode_title);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            intent.putExtra("EPISODE", episodes.get(getAdapterPosition()));
            startActivity(intent);
        }
    }
}
