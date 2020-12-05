package com.example.androidfinalgroupproject.recipe;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/03
 * @Version: 1.0
 */


public class Recipe {

/**
 * @param  title,the title of the recipe
 * @param  ingredient, the ingredient(s) of the recipe
 * @param  url, the url of the specific recipe
 */

    private String title;
    private String ingredient;
    private String url;

    public Recipe() {
    }

    public Recipe(String title, String ingredient, String url) {
        this.title = title;
        this.ingredient = ingredient;
        this.url = url;
    }

   // setters and getters
    public void  setTitle(String title){this.title = title;}
    public String getTitle(){return title; }

    public void setIngredient(String ingredient){this.ingredient = ingredient;}
    public String getIngredient(){return ingredient;}

    public void setUrl(String url){this.url= url;}
    public String getUrl(){return url;}

}

