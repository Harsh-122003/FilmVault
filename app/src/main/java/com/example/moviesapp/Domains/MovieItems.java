package com.example.moviesapp.Domains;

public class MovieItems {
    public int id;
    public String title, img;

    public MovieItems(String img, String title, int id){
        this.img = img;
        this.title = title;
        this.id = id;
    }
}