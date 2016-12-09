package com.example.farre.foodle;


public class Recipe {

    public String Name;
    public String Category;
    public String Calories;
    public String Ingredients;
    public String Directions;
    public String Img;
    public String id;


    public Recipe() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
}