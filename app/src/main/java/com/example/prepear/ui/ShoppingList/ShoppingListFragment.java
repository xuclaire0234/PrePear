package com.example.prepear.ui.ShoppingList;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepear.AddEditIngredientFragment;
import com.example.prepear.ComputeShoppingList;
import com.example.prepear.CustomRecipeList;
import com.example.prepear.CustomShoppingList;
import com.example.prepear.IngredientInRecipe;
import com.example.prepear.IngredientInStorage;
import com.example.prepear.R;
import com.example.prepear.RecipeController;
import com.example.prepear.ShoppingListClickboxFragment;
import com.example.prepear.databinding.FragmentRecipeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private FragmentRecipeBinding binding;
    private String startDate, endDate, newDate;
    final String[] sortItemSpinnerContent = {"  ----select----  ","Description", "Category"};
    private ArrayList<IngredientInRecipe> ingredientShoppingList;  // store all the ingredients needed to show in the listView
    private ArrayAdapter<IngredientInRecipe> ingredientShoppingListAdapter;
    private DatePickerDialog dialog;
    private IngredientInRecipe ingre;

    TextView fromDateText, toDateText;
    Spinner sortItemSpinner;
    ImageButton sortOrderButton;
    Button confirmButton;
    ListView shoppingListView;

    private ArrayList<IngredientInRecipe> ingredients;
    private String date;
    private String TAG = "Meal Plan";
    private ArrayList<String> recipeIdsCollection, ingredientIdsCollection;
    private ArrayList<Double> recipeScaleCollection, ingredientScaleCollection;
    private ArrayList<String> checkList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Double scale;
    private IngredientInRecipe recipe;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater popup_example = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(popup_example.inflate(R.layout.shopping_list_popup_window, null, false),800,300, true);
        pw.showAtLocation(this.getView(), Gravity.CENTER, 0, -200);

        fromDateText = view.findViewById(R.id.fromDate_textView);
        toDateText = view.findViewById(R.id.toDate_textView);
        sortItemSpinner = view.findViewById(R.id.sort_spinner);
        sortOrderButton = view.findViewById(R.id.sort_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        shoppingListView = view.findViewById(R.id.ingredient_listview);

        ingredientShoppingList = new ArrayList<>();

        // set adapter
       try {
           ingredientShoppingListAdapter = new CustomShoppingList(this.getContext(), ingredientShoppingList);
           shoppingListView.setAdapter(ingredientShoppingListAdapter);
       }catch (NullPointerException e) {
       }


        sortItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortItemSpinnerContent);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortItemSpinner.setAdapter(ad);


        ingredientShoppingList = new ArrayList<>();
        ingredientShoppingListAdapter = new CustomShoppingList(getContext(),ingredientShoppingList);
        shoppingListView.setAdapter(ingredientShoppingListAdapter);


        fromDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker(fromDateText);
            }
        });

        toDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker(toDateText);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    /* instantiation computeShoppingList class to get the ingredientShoppingList
                    * since user input valid startDate and endDate
                    */
                    ingredientShoppingList.clear();
//                    gainAllIngredientAtDate("2022-11-14");

                    MealPlanDailyIngredientCount count = new MealPlanDailyIngredientCount("2022-11-14");


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            for (IngredientInRecipe inRecipe: count.getIngredients()) {
                                ingredientShoppingList.add(inRecipe);
                            }
                            ingredientShoppingListAdapter.notifyDataSetChanged();
                        }
                    }, 5000);
                    ingredientShoppingListAdapter.notifyDataSetChanged();
                    ingredientShoppingList.add(new IngredientInRecipe("clear","2","kg","new"));
                    ingredientShoppingListAdapter.notifyDataSetChanged();
//                    ComputeShoppingList computeShoppingList = new ComputeShoppingList(startDate,endDate);
//                    try {
//                        ingredientShoppingList = computeShoppingList.;
//                        ingredientShoppingListAdapter.notifyDataSetChanged();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }catch (NullPointerException e) {
//                    }
                    //MealPlanDailyIngredientCount count = new MealPlanDailyIngredientCount("2022-11-14");

                }
            }
        });



    }

    private Boolean check() {

        Boolean check = Boolean.TRUE;
        String newFromDate = fromDateText.getText().toString();
        String newToDate = toDateText.getText().toString();

        if (newFromDate == ""|| newToDate == "") {
            check = Boolean.FALSE;

        } else {
            Integer compare = newFromDate.compareTo(newToDate);
            if (compare <= 0) {
                startDate = newFromDate;
                endDate = newToDate;
            } else {
                check = Boolean.FALSE;
            }
        }
        fromDateText.setText(startDate);
        toDateText.setText(endDate);
        return check;
    }

    private void createDatePicker(TextView textView) {
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(getContext(), R.style.activity_date_picker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        /* On below parts:
                         * set the day of month , the month of year and the year value in the edit text
                         * if-conditional statement helps to regulate the format of yyyy-mm-dd.
                         * */
                        newDate = "";
                        if (monthOfYear < 9 && dayOfMonth < 10) {
                            newDate = year + "-" + "0" + (monthOfYear + 1) +
                                    "-" + "0" + dayOfMonth;
                        } else if (dayOfMonth < 10) {
                            newDate = year + "-" + (monthOfYear + 1) +
                                    "-" + "0" + dayOfMonth;
                        } else if (monthOfYear < 9) {
                            newDate = year + "-" + "0" + (monthOfYear + 1) +
                                    "-" + dayOfMonth;
                        } else {
                            newDate = year + "-" + (monthOfYear + 1) +
                                    "-" + dayOfMonth;
                        }
                        textView.setText(newDate);
                    }
                }, currentYear, currentMonth, currentDay);
        dialog.show();
    }



//    public void gainAllIngredientAtDate(String date) {
//        ingredients = new ArrayList<>();
//        ingredientIdsCollection = new ArrayList<>();
//        ingredientScaleCollection = new ArrayList<>();
//        recipeIdsCollection = new ArrayList<>();
//        recipeScaleCollection = new ArrayList<>();
//        db
//                .collection("Daily Meal Plans")
//                .document(date)
//                .collection("Meals")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
//                        for (QueryDocumentSnapshot document : task1.getResult()) {
//                            if (task1.isSuccessful()) {
//                                String id = (String) document.getData().get("Document ID");
//                                String type = (String) document.getData().get("Meal Type");
//                                Number scaleOfItem = (Number) document.getData().get("Customized Scaling Number");
//                                if (type.equals("Recipe")) {
//                                    recipeIdsCollection.add(id);
//                                    recipeScaleCollection.add(scaleOfItem.doubleValue());
//                                } else if (type.equals("Ingredient")) {
//                                    ingredientIdsCollection.add(id);
//                                    ingredientScaleCollection.add(scaleOfItem.doubleValue());
//                                }
//                            }
//                        }
//
//                        for (int i = 0; i < ingredientIdsCollection.size(); i++) {
//                            scale = ingredientScaleCollection.get(i);
//                            db
//                                    .collection("Ingredient Storage")
//                                    .document(ingredientIdsCollection.get(i)).get()
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
//                                            DocumentSnapshot doc = task2.getResult();
//                                            if (task2.isSuccessful()) {
//                                                String briefDescription = (String) doc.getData().get("description");
//                                                String unit = (String) doc.getData().get("unit");
//                                                String ingredientCategory = (String) doc.getData().get("category");
//
//                                                // might change it later
//                                                ingredientShoppingList.add(new IngredientInRecipe(briefDescription,String.valueOf(scale),unit,ingredientCategory));
//                                                ingredientShoppingListAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//                                    });
//                        }
//
//                        for (int i = 0; i < recipeIdsCollection.size(); i++) {
//                            final Double scaleOfThisRecipe = recipeScaleCollection.get(i);
//                            final String idOfThisRecipe = recipeIdsCollection.get(i);
//                            db
//                                    .collection("Recipes")
//                                    .document(recipeIdsCollection.get(i))
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
//                                            DocumentSnapshot doc2 = task3.getResult();
//                                            Number numberOfServings = (Number) doc2.get("Number of Servings");
//                                            scale = numberOfServings.doubleValue();
//
//                                            scale = scaleOfThisRecipe/scale;
//
//                                            db
//                                                    .collection("Recipes")
//                                                    .document(idOfThisRecipe)
//                                                    .collection("Ingredient")
//                                                    .get()
//                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task4) {
//                                                            for (QueryDocumentSnapshot ingredientDoc : task4.getResult()) {
//                                                                if (task4.isSuccessful()) {
//                                                                    String briefDescription = (String) ingredientDoc.getData().get("Brief Description");
//                                                                    Number amount = (Number) ingredientDoc.getData().get("Amount");
//                                                                    String unit = (String) ingredientDoc.getData().get("Unit");
//                                                                    String ingredientCategory = (String) ingredientDoc.getData().get("Ingredient Category");
//
//                                                                    Double amountValue = amount.doubleValue();
//                                                                    amountValue = amountValue * scale;
//                                                                    ingredientShoppingList.add(new IngredientInRecipe(briefDescription,String.valueOf(amountValue),unit,ingredientCategory));
//                                                                    ingredientShoppingListAdapter.notifyDataSetChanged();
//                                                                }
//                                                            }
//                                                        }
//                                                    });
//
//
//
//
//                                        }
//                                    });
//
//                        }
//
//
//                    }
//                });
//    }

}

