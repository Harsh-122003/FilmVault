package com.example.moviesapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviesapp.Adapters.ActorsAdapter;
import com.example.moviesapp.Adapters.CategoryAdapter;
import com.example.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ImageView movieBackground, backBtn;
    private TextView movieRating, movieDuration, movieName, movieSummary, actorInfo;
    private RecyclerView genreRecycler, actorsRecycler;
    private ProgressBar genrePBar, actorsPBar;
    private int id;
    private ArrayList<String> genreList, actorsList;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieBackground = findViewById(R.id.movieBackground);
        backBtn = findViewById(R.id.backBtn);
        movieRating = findViewById(R.id.movieRating);
        movieDuration = findViewById(R.id.movieDuration);
        movieSummary = findViewById(R.id.movieSummary);
        actorInfo = findViewById(R.id.actorInfo);
        movieName = findViewById(R.id.movieName);
        genreRecycler = findViewById(R.id.genreRecycler);
        actorsRecycler = findViewById(R.id.actorsRecycler);
        genrePBar = findViewById(R.id.genrePBar);
        actorsPBar = findViewById(R.id.actorsPBar);

        genreRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        actorsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        id = getIntent().getIntExtra("id", 1);

        addItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addItems(){
        genreList = new ArrayList<>();
        actorsList = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://moviesapi.ir/api/v1/movies/" + id;
        genrePBar.setVisibility(View.VISIBLE);
        actorsPBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Glide.with(DetailActivity.this)
                            .load(jsonObject.getString("poster"))
                            .into(movieBackground);
                    movieName.setText(jsonObject.getString("title"));
                    movieRating.setText(jsonObject.getString("imdb_rating"));
                    movieDuration.setText(jsonObject.getString("runtime"));
                    movieSummary.setText(jsonObject.getString("plot"));
                    actorInfo.setText(jsonObject.getString("actors"));

                    JSONArray genres = jsonObject.getJSONArray("genres");
                    for(int i=0;i<genres.length();i++)
                    {
                        genreList.add((String) genres.get(i));
                    }
                    genrePBar.setVisibility(View.GONE);
                    genreRecycler.setAdapter(new CategoryAdapter(DetailActivity.this, genreList));

                    JSONArray images = jsonObject.getJSONArray("images");
                    for(int i=0;i<images.length();i++)
                    {
                        actorsList.add((String) images.get(i));
                    }
                    actorsPBar.setVisibility(View.GONE);
                    actorsRecycler.setAdapter(new ActorsAdapter(DetailActivity.this, actorsList));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DetailActivity.this, "You Are Offline", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(objectRequest);
    }
}