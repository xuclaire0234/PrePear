/**
 * Class Name: IngredientStorageCustomList
 * Version Information: Version 1.0
 * Create Date: Oct 25th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 * */


package com.example.prepear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prepear.IngredientInStorage;
import com.example.prepear.R;

import java.util.ArrayList;

/* CustomList class extends from ArrayAdapter class in order to customize the ArrayAdapter based on our application's needs
 * Substitution of IngredientStorageController Class
 * */
public class IngredientStorageCustomList extends ArrayAdapter<IngredientInStorage> {

    private ArrayList<IngredientInStorage> ingredientsInStorage; // holds for ingredients in storage entries
    private Context context; // holds for ViewIngredientStorage activity's context

    /* constructor comment */
    public IngredientStorageCustomList(Context contextParameter,
                                       ArrayList<IngredientInStorage> ingredientsInStorageParameter) {
        super(contextParameter, 0, ingredientsInStorageParameter);
        this.context = contextParameter;
        this.ingredientsInStorage = ingredientsInStorageParameter;
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
        TextView ingredientUnit = view.findViewById(R.id.unitNumberText);
        TextView ingredientAmount = view.findViewById(R.id.amountText);
        ImageView ingredientIconView = view.findViewById(R.id.ingredient_icon_view);

        int code = ingredientEntry.getIconCode();
        ingredientIconView.setImageResource(code);

        ingredientDescription.setText(ingredientEntry.getBriefDescription());
        ingredientBestBeforeDate.setText(ingredientEntry.getBestBeforeDate());
        ingredientUnit.setText(ingredientEntry.getUnit());
        String ingredientAmountString = String.valueOf(ingredientEntry.getAmountValue());
        ingredientAmount.setText(ingredientAmountString);


        return view; // return this view
    }
}