/**
 * Class Name: ChooseIngredientIconActivity
 * Version Information: Version 1.0
 * Create Date: Nov 11th, 2022
 * Author: Jingyi Xu
 * Copyright Notice:
 * */

package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.prepear.AddEditIngredientActivity;
import com.example.prepear.R;

/**
 * This class provide icon for ingredient that allows user to choose one of the icon to represent
 */
public class ChooseIngredientIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ingredient_icon);

    }

    /**
     * This method will send image tag to AddEditIngredientActivity when user selected one icon
     * @param view
     */
    public void PassImage(View view){
        String tag = (String) view.getTag();  // get image name of selected image
        Log.d("image tag", tag);
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra("image tag", tag);
        setResult(RESULT_OK,intent);
        finish();
    }

}