package com.example.prepear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;
import java.util.ArrayList;

public class ChooseIngredientIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ingredient_icon);

    }

    public void PassImage(View view){
        String tag = (String) view.getTag();  // get image name of selected image
        Log.d("image tag", tag);
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Receive code","1");
        bundle.putString("image tag",tag);
        intent.putExtras(bundle);
//        intent.putExtra("image tag", tag);
        startActivity(intent);
    }

}