package com.example.moviesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.Adapters.CategoryAdapter;
import com.example.moviesapp.Adapters.MoviesAdapter;
import com.example.moviesapp.Adapters.SliderAdapter;
import com.example.moviesapp.Domains.MovieItems;
import com.example.moviesapp.Domains.SliderItems;
import com.example.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler sliderHandler;
    private AutoCompleteTextView searchBar;
    private RecyclerView recyclerViewBestMovies, recyclerViewUpcoming, recyclerViewCategory;
    private ProgressBar bestMoviesPBar, categoryPBar, upcomingPBar;
    private ArrayList<MovieItems> listBestMovies, listUpComing;
    private ArrayList<String> listCategory, searchList;
    private HashMap<String, Integer> movieTitleId;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
        viewPager = findViewById(R.id.viewPager);
        sliderHandler = new Handler();
        recyclerViewBestMovies = findViewById(R.id.recyclerView1);
        recyclerViewCategory = findViewById(R.id.recyclerView2);
        recyclerViewUpcoming = findViewById(R.id.recyclerView3);
        bestMoviesPBar = findViewById(R.id.bestMoviesPBar);
        categoryPBar = findViewById(R.id.categoryPBar);
        upcomingPBar = findViewById(R.id.upcomingPBar);

        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        queue = Volley.newRequestQueue(this);

        searchList = new ArrayList<>();
        movieTitleId = new HashMap<>();

        addListBestMovies();
        addListCategory();
        addListUpComing();

        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide2));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager.setAdapter(new SliderAdapter(this, sliderItems, viewPager));
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager.setPageTransformer(transformer);
        viewPager.setCurrentItem(1);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }

    private void addListBestMovies(){
        listBestMovies = new ArrayList<>();
        String url = "https://moviesapi.ir/api/v1/movies?page=1";
        bestMoviesPBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject object = data.getJSONObject(i);
                        listBestMovies.add(new MovieItems(object.getString("poster"), object.getString("title"), object.getInt("id")));
                        searchList.add(object.getString("title"));
                        movieTitleId.put(object.getString("title"), object.getInt("id"));
                    }
                    bestMoviesPBar.setVisibility(View.GONE);
                    recyclerViewBestMovies.setAdapter(new MoviesAdapter(MainActivity.this, listBestMovies));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                bestMoviesPBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "You Are Offline", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(objectRequest);
    }

    private void addListUpComing(){
        listUpComing = new ArrayList<>();
        String url = "https://moviesapi.ir/api/v1/movies?page=2";
        upcomingPBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject object = data.getJSONObject(i);
                        listUpComing.add(new MovieItems(object.getString("poster"), object.getString("title"), object.getInt("id")));
                        searchList.add(object.getString("title"));
                        movieTitleId.put(object.getString("title"), object.getInt("id"));
                    }
                    upcomingPBar.setVisibility(View.GONE);
                    recyclerViewUpcoming.setAdapter(new MoviesAdapter(MainActivity.this, listUpComing));
                    addListSearchBar();
                } catch (JSONException e) {
                    upcomingPBar.setVisibility(View.GONE);
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                upcomingPBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "You Are Offline", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(objectRequest);
    }

    private void addListCategory(){
        listCategory = new ArrayList<>();
        String url = "https://moviesapi.ir/api/v1/genres";
        categoryPBar.setVisibility(View.VISIBLE);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for(int i=0;i<jsonArray.length();i++)
                {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        listCategory.add(object.getString("name"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                categoryPBar.setVisibility(View.GONE);
                recyclerViewCategory.setAdapter(new CategoryAdapter(MainActivity.this, listCategory));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                categoryPBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "You Are Offline", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(arrayRequest);
    }

    private void addListSearchBar(){
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, searchList);
        searchBar.setAdapter(searchAdapter);
        searchBar.setThreshold(1);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieName = (String) parent.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("id", movieTitleId.get(movieName));
                startActivity(i);
                searchBar.setText("");
            }
        });
    }
}