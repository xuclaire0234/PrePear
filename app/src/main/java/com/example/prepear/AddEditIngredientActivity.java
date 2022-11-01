package com.example.prepear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText descriptionView;
    private EditText dateView;
    private Spinner ingredientCategory;
    private Spinner ingredientLocation;
    private EditText ingredientAmount;
    private Spinner ingredientUnit;
    private Button cancelIngredient;
    private Button addIngredient;
    private FirebaseFirestore dbForInStorageIngredients;
    private IngredientInStorage viewedIngredient;
    String idOfIngredient;
    Boolean isEditedIngredient;
    String bbd_str;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        final String TAG = "Ingredient";

        descriptionView = findViewById(R.id.brief_description);
        dateView = findViewById(R.id.dateView);
        ingredientCategory = (Spinner) findViewById(R.id.ingredient_category);
        ingredientUnit = (Spinner) findViewById(R.id.ingredient_unit);
        ingredientAmount = findViewById(R.id.ingredient_amount);
        ingredientLocation = (Spinner) findViewById(R.id.ingredient_location);

        cancelIngredient = findViewById(R.id.cancel_ingredient_button);
        addIngredient = findViewById(R.id.add_ingredient_button);

        dbForInStorageIngredients = FirebaseFirestore.getInstance();


        ArrayAdapter<CharSequence> adapterForCategory = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapterForCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ingredientCategory.setAdapter(adapterForCategory);

        ArrayAdapter adapterForLocation = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        adapterForLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ingredientLocation.setAdapter(adapterForLocation);


        ArrayAdapter adapterForUnit = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
        adapterForUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ingredientUnit.setAdapter(adapterForUnit);



        //editDeleteListSaved = new ArrayList<String>();

        if (getIntent().getStringExtra("calling activity").equals("2")) {
            viewedIngredient = (IngredientInStorage) getIntent().getSerializableExtra("viewed Ingredient");
//            Glide.with(AddEditIngredientActivity.this)
//                    .load(linkOfImage).into(imageImageView);
            descriptionView.setText(viewedIngredient.getBriefDescription());
            dateView.setText(viewedIngredient.getBestBeforeDate());
            ingredientLocation.setSelection(adapterForLocation.getPosition(viewedIngredient.getLocation()));
            ingredientCategory.setSelection(adapterForCategory.getPosition(viewedIngredient.getIngredientCategory()));
            ingredientAmount.setText((int) viewedIngredient.getAmount());
            ingredientUnit.setSelection(adapterForUnit.getPosition(viewedIngredient.getUnit()));
            idOfIngredient = viewedIngredient.getId();
            isEditedIngredient = Boolean.TRUE;
        } else {
            //ingredientInRecipeDataList = new ArrayList<>();
            idOfIngredient = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            isEditedIngredient = Boolean.FALSE;
        }


        cancelIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("calling activity").equals("1")) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                } else {
                    finish();
                }
            }
        });


        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* create a date picker for the best before date of the ingredient */
                Calendar currentDate = Calendar.getInstance();
                int mYear = currentDate.get(Calendar.YEAR);
                int mMonth = currentDate.get(Calendar.MONTH);
                int mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddEditIngredientActivity.this, R.style.activity_date_picker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                // if condition statement helps to regulate the format.
                                if (monthOfYear < 9 && dayOfMonth < 10) {
                                    bbd_str = year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                } else if (dayOfMonth < 10) {
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                } else if (monthOfYear < 9) {
                                    bbd_str = year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                } else {
                                    bbd_str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                dateView.setText(bbd_str);
                            }
                        }, mYear, mMonth, mDay);
                dialog.show();
                /* Temporarily remove keyboards before displaying the dialog*/
//                InputMethodManager inputMethodManager =
//                        (InputMethodManager) dateView.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);
//                inputMethodManager.hideSoftInputFromWindow(ingredientAmount.getWindowToken(),0);
            }
        });

        descriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionView.setFocusable(true);
                if(descriptionView.getText().toString().isEmpty()) {
                    descriptionView.setError("Cannot leave Ingredient Name Empty");
                    descriptionView.requestFocus();
                }
            }
        });




        ingredientAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientAmount.setFocusable(true);
                if(ingredientAmount.getText().toString().isEmpty()) {
                    ingredientAmount.setError("Cannot leave Ingredient amount empty");
                    ingredientAmount.requestFocus();
                }else if(ingredientAmount.getText().toString().equals("0")){
                    ingredientAmount.setError("Cannot enter zero for ingredient amount");
                    ingredientAmount.requestFocus();
                }
            }
        });
        
    }


    public void uploadIngredient (View view) {
        Map<String,Object> data = new HashMap<>();
        final String description = descriptionView.getText().toString();
        final String category = ingredientCategory.getSelectedItem().toString();
        final String date = dateView.getText().toString();
        final String location = ingredientLocation.getSelectedItem().toString();
        final String unit = ingredientUnit.getSelectedItem().toString();
        final String amount = ingredientAmount.getText().toString();

        if (description.isEmpty() || category.isEmpty() || date.isEmpty()
                || location.isEmpty() || amount.isEmpty() || unit.isEmpty()) {
            CharSequence text = "Error, Some Fields Are Empty!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

        }else if (amount.equals("0")){
            CharSequence text = "Error, Amount Can Not Be Zero!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }else{
            data.put("description",description );
            data.put("bestBeforeDate", date);
            data.put("unit",unit);
            data.put("category", category);
            data.put("location", location);
            data.put("amount", amount);
            data.put("Id", idOfIngredient);
            // two ingredients with the same descriptions (as id) should be allowed
            dbForInStorageIngredients
                    .collection("Ingredient Storage")
                    .document(idOfIngredient)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Toast.makeText(AddEditIngredientActivity.this, "Ingredient is uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Toast.makeText(AddEditIngredientActivity.this, "fails to upload ingredient", Toast.LENGTH_SHORT).show();
                        }});
            Intent returnIntent = new Intent();
            returnIntent.putExtra("new ingredient", new IngredientInStorage(description,category, date, location, amount, unit));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }





}


