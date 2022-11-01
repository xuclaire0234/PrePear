/*
 * Class Name: IngredientStorageCustomList
 * Version Information: Version 1.0
 * Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */
package com.example.prepear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* CustomList class extends from ArrayAdapter class in order to customize the ArrayAdapter based on our application's needs
 * Substitution of IngredientStorageController Class
 * */
public class IngredientStorageCustomList extends ArrayAdapter<IngredientInStorage> {

    private ArrayList<IngredientInStorage> ingredientsInStorage; // holds for ingredients in storage entries
    private Context context; // holds for ViewIngredientStorage activity's context
    private int sortItemIngredient = 0;

    /* constructor comment */
    public IngredientStorageCustomList(Context contextParameter,
                                       ArrayList<IngredientInStorage> ingredientsInStorageParameter) {
        super(contextParameter, 0, ingredientsInStorageParameter);
        this.context = contextParameter;
        this.ingredientsInStorage = ingredientsInStorageParameter;
    }

    public int getSortItemIngredient() {
        return this.sortItemIngredient;
    }

    public void setSortItemIngredient(int index) {
        this.sortItemIngredient = index;
    }

    public void sortIngredient(int index) {
        this.sortItemIngredient = index;
        if (index == 0) {
            Collections.sort(this.ingredientsInStorage, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBriefDescription().compareTo(ingredient2.getBriefDescription());
                }
            });
        }else if (index == 1) {
            Collections.sort(ingredientsInStorage, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBriefDescription().compareTo(ingredient1.getBriefDescription());
                }
            });
        }else if (index == 2) {
            Collections.sort(ingredientsInStorage, new Comparator<IngredientInStorage>(){
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient1.getBestBeforeDate().compareTo(ingredient2.getBestBeforeDate());
                }
            });
        }else if (index == 3) {
            Collections.sort(ingredientsInStorage, new Comparator<IngredientInStorage>() {
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getBestBeforeDate().compareTo(ingredient1.getBestBeforeDate());
                }
            });
        }else if (index == 4){
            Collections.sort(ingredientsInStorage, new Comparator<IngredientInStorage>(){
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getLocation().compareTo(ingredient1.getLocation());
                }
            });
        }else if (index == 5){
            Collections.sort(ingredientsInStorage, new Comparator<IngredientInStorage>(){
                @Override
                public int compare(IngredientInStorage ingredient1, IngredientInStorage ingredient2) {
                    return ingredient2.getIngredientCategory().compareTo(ingredient1.getIngredientCategory());
                }
            });
        }
    }



    @NonNull
    @Override
    /*method comment*/
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView; // get the reference to the current convertView object

        if (view == null) {
            // if the convertView holds nothing, then inflate the ingredient_info.xml
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_info, parent, false);
        }
        // on below line get the current in-storage ingredient entry,
        // and extract its information from the in-storage ingredients list
        IngredientInStorage ingredientEntry = ingredientsInStorage.get(position);
        // and set this in-storage ingredient's description,
        // best before date and unit count to the corresponding Textview object for displaying on the activity
        TextView ingredientDescription = view.findViewById(R.id.descriptionText);
        TextView ingredientBestBeforeDate = view.findViewById(R.id.bestBeforeDateText);
        TextView ingredientUnit = view.findViewById(R.id.unitText);
//        TextView ingredientCategory = view.findViewById(R.id.categoryText);
//        TextView ingredientLocation = view.findViewById(R.id.locationText);

        ingredientDescription.setText(ingredientEntry.getBriefDescription());
        ingredientBestBeforeDate.setText(ingredientEntry.getBestBeforeDate());
        ingredientUnit.setText(ingredientEntry.getUnit());
//        ingredientLocation.setText(ingredientEntry.getLocation());
//        ingredientCategory.setText(ingredientEntry.getIngredientCategory());

        return view; // return this view
    }
}