package com.example.farre.foodle;

/**
 * Created by farre on 04/12/2016.
 */

public class User {
    public String Name;
    public String Vegan;
    public String Coeliac;
    public String Vegetarian;
    public String Protein;
    public String Carbs;

    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize users

    }
    public User(String carbs, String coeliac, String protein, String vegetarian, String name, String vegan) {
        this.Name = name;
        this.Vegan = vegan;
        this.Coeliac = coeliac;
        this.Vegetarian = vegetarian;
        this.Carbs = carbs;
        this.Protein = protein;
    }
}
