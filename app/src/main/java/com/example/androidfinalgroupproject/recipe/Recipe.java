package com.example.androidfinalgroupproject.recipe;

/**
 * @Author: Jingshan Guan
 * @Date: 2020/12/03
 * @Version: 1.0
 */


public class Recipe {

    /**
     * Constructor of recipe to initialize title, ingredients and url
     *
     * @param dataBaseId the database id
     * @param recipe the title
     * @param ingredient the ingredient
     * @param url the web address for recipe
     */

    long dataBaseId;
    String title;
    String ingredient;
    String url;

    public Recipe() {
    }

    public Recipe(String title, String ingredient, String url) {
        this.title = title;
        this.ingredient = ingredient;
        this.url = url;
    }

    /**
     * *4-argument Constructor of recipe to initialize title, ingredient and url
     *
     * @param id         the id
     * @param recipe     the title
     * @param ingredient the ingredient
     * @param url        the web address for recipe
     */
    public Recipe(Long id, String recipe, String ingredient, String url) {
        this.dataBaseId = id;
        this.title = recipe;
        this.ingredient = ingredient;
        this.url = url;
    }

    // setters and getters
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public long getDataBaseId() {
        return dataBaseId;
    }

}

