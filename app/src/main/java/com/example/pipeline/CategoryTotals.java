package com.example.pipeline;

import java.io.Serializable;

public class CategoryTotals implements Serializable {

    //This class calculates totals per category, returning total by category, color and total values

    //declaring variables to be used
    private int total;
    private String category;
    private String color;

    //constructor
    public CategoryTotals(String pCategory, int pTotal, String pColor){
        //setting respective values passed to constructor, to variables
        this.total = pTotal;
        this.category = pCategory;
        this.color = pColor;
    }
    //get category method
    public String getCategory(){
        return category;
    }

    //get total method
    public int getTotal(){
        return total;
    }

    //get color method
    public String getColor(){
        return color;
    }

}
