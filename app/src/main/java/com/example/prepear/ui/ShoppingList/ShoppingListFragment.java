package com.example.prepear.ui.ShoppingList;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
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

import com.example.prepear.ComputeShoppingList;
import com.example.prepear.CustomShoppingList;
import com.example.prepear.IngredientInRecipe;
import com.example.prepear.IngredientInStorage;
import com.example.prepear.R;
import com.example.prepear.databinding.FragmentRecipeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private FragmentRecipeBinding binding;
    private String startDate, endDate, newDate;
    final String[] sortItemSpinnerContent = {"  ----select----  ", "Description", "Category"};
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
    private ArrayList<IngredientInStorage> allIngredientInStorage = new ArrayList<>();
    private ArrayList<String> checkList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Double scale;
    private IngredientInRecipe recipe;

    private CollectionReference collectionReference = db.collection("Ingredient Storage");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater popup_example = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(popup_example.inflate(R.layout.shopping_list_popup_window, null, false), 800, 300, true);
        pw.showAtLocation(this.getView(), Gravity.CENTER, 0, -200);

        fromDateText = view.findViewById(R.id.fromDate_textView);
        toDateText = view.findViewById(R.id.toDate_textView);
        sortItemSpinner = view.findViewById(R.id.sort_spinner);
        sortOrderButton = view.findViewById(R.id.sort_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        shoppingListView = view.findViewById(R.id.ingredient_listview);


        // set adapter
        try {
            ingredientShoppingListAdapter = new CustomShoppingList(this.getContext(), ingredientShoppingList);
            shoppingListView.setAdapter(ingredientShoppingListAdapter);
        } catch (NullPointerException e) {
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
        ingredientShoppingListAdapter = new CustomShoppingList(getContext(), ingredientShoppingList);
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
                    ComputeShoppingList computeShoppingList = new ComputeShoppingList("2022-11-14","2022-11-15");
                    //allIngredientInStorage = computeShoppingList.loadIngredientsInStorage();  // load all the ingredients in storage to arrayList allIngredientInStorage
//                    Log.d("ingredientStorage", String.valueOf(allIngredientInStorage.size()));  // 0

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                ingredientShoppingList = computeShoppingList.calculateShoppingList(allIngredientInStorage);
//                                ingredientShoppingListAdapter.notifyDataSetChanged();
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },9000);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            allIngredientInStorage = computeShoppingList.getAllIngredientsInStorage();
//                            Log.d("all ingredient", allIngredientInStorage.get(0).getBriefDescription());

                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date start = formatter.parse(startDate);
                                Date end = formatter.parse(endDate);

                                Calendar startDay = Calendar.getInstance();
                                startDay.setTime(start);
                                Calendar endDay = Calendar.getInstance();
                                endDay.setTime(end);
                                while (!startDay.after(endDay)) {
                                    Date targetDay = startDay.getTime(); // current date
                                    calculateShoppingList(targetDay);
                                    startDay.add(Calendar.DATE, 1);
                                    //ingredientShoppingListAdapter.notifyDataSetChanged();
                                    Log.d("size",String.valueOf(ingredientShoppingList.size()));
                                }
                            }catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                        }
                    },2000);


//                    gainAllIngredientAtDate("2022-11-14");

//                    MealPlanDailyIngredientCount count = new MealPlanDailyIngredientCount("2022-11-14");
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            for (IngredientInRecipe inRecipe: count.getIngredients()) {
//                                ingredientShoppingList.add(inRecipe);
//                            }
//                            ingredientShoppingListAdapter.notifyDataSetChanged();
//                        }
//                    }, 9000);
//                    ingredientShoppingList.add(new IngredientInRecipe("clear", "2", "kg", "new"));
//                    ingredientShoppingListAdapter.notifyDataSetChanged();


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

        if (newFromDate == "" || newToDate == "") {
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

    public void calculateShoppingList(Date targetDay) throws ParseException {
        /* initializing the arraylist for ingredients in shopping list */
        //ArrayList<IngredientInRecipe> ingredientInShoppingList = new ArrayList<>();
        //ArrayList<IngredientInStorage> allIngredientsInStorage = loadIngredientsInStorage(); // get all ingredients in storage

        /* convert String date to Date type for us to iterate and compare with ingredients in storage */
        // ingredientShoppingList.clear();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date start = formatter.parse(startDate);
//        Date end = formatter.parse(endDate);
//
//        Calendar startDay = Calendar.getInstance();
//        startDay.setTime(start);
//        Calendar endDay = Calendar.getInstance();
//        endDay.setTime(end);

        // !startDay.after(endDay) -> represents iterate inclusive the end date

            String currentDate = formatter.format(targetDay); // convert date type to String
            /* since get the current date, we should get all needed ingredients for that date */
            MealPlanDailyIngredientCount dailyIngredientCount = new MealPlanDailyIngredientCount(currentDate);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<IngredientInRecipe> dailyIngredients = dailyIngredientCount.getIngredients();

                    for (int i = 0; i < dailyIngredients.size(); i++) { // iterate ingredients for one date
                        IngredientInRecipe targetIngredient = dailyIngredients.get(i);
                        String targetDescription = targetIngredient.getBriefDescription();
                        Log.d("target description", targetDescription);
                        /* using targetIngredient's briefDescription to find if ingredientInStorage has a same ingredient*/
                        boolean dailyKey = true;
                        while(dailyKey){
                        for (int j = 0; j < allIngredientInStorage.size(); j++) {
                            IngredientInStorage targetIngredientStorage = allIngredientInStorage.get(j);
                            if (targetDescription.equals(targetIngredientStorage.getBriefDescription())) {

                                /* since we can find the same ingredient in storage, now we need to compare date*/
                                Date bestBeforeDate = null;
                                try {
                                    bestBeforeDate = formatter.parse(targetIngredientStorage.getBestBeforeDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bestBeforeDate.compareTo(targetDay) > 0 || bestBeforeDate.compareTo(targetDay) == 0) {
                                    // bestBeforeDate occurs after targetDate
                                    // now, need to compare the amount to check if we need to buy extra
                                    // we check the each ingredient unit first and transfer them to be the same
                                    double targetAmount = unitConvert(targetIngredient.getUnit(), targetIngredient.getAmountValue());
                                    double storageAmount = unitConvert(targetIngredientStorage.getUnit(), targetIngredientStorage.getAmountValue());
                                    if (storageAmount > targetAmount) {
                                        // update storage amount since it is enough to use ingredient in storage
                                        // do not need to prepare this ingredient
                                        double difference = storageAmount - targetAmount;
                                        targetIngredientStorage.setAmountValue(difference);
                                        Log.d("difference", String.valueOf(difference));

                                    } else if (storageAmount == targetAmount) {
                                        // remove this ingredient in storage
                                        // ingredient in storage can totally cover the ingredient user needs
                                        allIngredientInStorage.remove(targetIngredientStorage);
                                    } else if (storageAmount < targetAmount) {
                                        // remove this ingredient in storage
                                        // update rest needed amount of ingredient to ingredientInShoppingList
                                        // check first if there is an existing one in ingredientInShoppingList
                                        // otherwise, just update amount for that ingredient
                                        allIngredientInStorage.remove(targetIngredientStorage);
                                        double differenceCart = targetAmount - storageAmount;
                                        Log.d("differenceCar", String.valueOf(differenceCart));
                                        String targetCategory = targetIngredient.getIngredientCategory();
                                        //String targetUnit = targetIngredient.getUnit();
                                        String targetUnit = chooseUnit(targetIngredient.getUnit(), targetIngredientStorage.getUnit());
//                                        Log.d("choose unit ", targetUnit);
                                        boolean key = true;

                                        while (key) {
                                            for (int k = 0; k < ingredientShoppingList.size(); k++) {
                                                IngredientInRecipe ingredientForShopping = ingredientShoppingList.get(k);
                                                if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                                    // there is an existing ingredient in ingredientInShoppingList
                                                    double exitingAmount = ingredientForShopping.getAmountValue();
                                                    double updateAmount = exitingAmount + differenceCart;
                                                    ingredientForShopping.setAmountValue(updateAmount);  // only update amount
                                                    key = false;
                                                }
                                            }
                                            if (key) {
                                                /* after iterating ingredientInShoppingList, if there is no existing one */
                                                ingredientShoppingList.add(new IngredientInRecipe(targetDescription, String.valueOf(differenceCart), targetUnit, targetCategory));
//                                                ingredientShoppingListAdapter.notifyDataSetChanged();
                                                key = false;
                                            }
                                        }
                                    }

                                } else if (bestBeforeDate.compareTo(targetDay) < 0) {
                                    // bestBeforeDate occurs before targetDate
                                    // now, need to buy exact amount of ingredient since suer cannot use ingredients in storage
                                    // add ingredient directly into ingredient ingredientInShoppingList after check if there is existing one
                                    // otherwise, update amount for that ingredient
                                    boolean anotherKey = true;
                                    while (anotherKey) {
                                        for (int p = 0; p < ingredientShoppingList.size(); p++) {
                                            IngredientInRecipe ingredientForShopping = ingredientShoppingList.get(p);
                                            if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                                double exitingAmount = ingredientForShopping.getAmountValue();
                                                double updateAmount = exitingAmount + targetIngredient.getAmountValue(); // existing amount add needed amount
                                                ingredientForShopping.setAmountValue(updateAmount);
                                                anotherKey = false;
                                            }
                                        }
                                        if (anotherKey) {
                                            /* after iterating ingredientInShoppingList, if there is no existing one */
                                            ingredientShoppingList.add(targetIngredient);
                                            anotherKey = false;
//                                          ingredientShoppingListAdapter.notifyDataSetChanged();
                                        }
                                    }

                                }
                                dailyKey = false;
                            }

                            ingredientShoppingListAdapter.notifyDataSetChanged();
                        }
                        if (dailyKey) {  // check if dailyKey has been changed
                            // if there is no same ingredients in the storage, just directly add into shoppingList
                            boolean updateKey = true;
                            while (updateKey) {  // this while loop helps to check if shopping has the existing ingredient
                                for (int a = 0; a < ingredientShoppingList.size(); a++) {
                                    IngredientInRecipe ingredientForShopping = ingredientShoppingList.get(a);
                                    if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                        double exitingAmount = ingredientForShopping.getAmountValue();
                                        double updateAmount = exitingAmount + unitConvert(targetIngredient.getUnit(),targetIngredient.getAmountValue()); // existing amount add needed amount
                                        ingredientForShopping.setAmountValue(updateAmount);
                                        updateKey = false;
                                        dailyKey = false;
                                    }
                                }
                                if (updateKey) {
                                    /* after iterating ingredientInShoppingList, if there is no existing one */
                                    ingredientShoppingList.add(targetIngredient);
                                    updateKey = false;
                                    ingredientShoppingListAdapter.notifyDataSetChanged();
                                    dailyKey = false;
//
                                }
                            }
                        }
                    }
                    }
                    //Log.d("size shopping", String.valueOf(ingredientShoppingList.size()));
                    ingredientShoppingListAdapter.notifyDataSetChanged();
                }
            }, 2000);
//            startDay.add(Calendar.DATE, 1);
        }

//    public void calculateShoppingList(String startDate, String endDate) throws ParseException {
//        /* initializing the arraylist for ingredients in shopping list */
//        //ArrayList<IngredientInRecipe> ingredientInShoppingList = new ArrayList<>();
//        //ArrayList<IngredientInStorage> allIngredientsInStorage = loadIngredientsInStorage(); // get all ingredients in storage
//
//        /* convert String date to Date type for us to iterate and compare with ingredients in storage */
//        // ingredientShoppingList.clear();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date start = formatter.parse(startDate);
//        Date end = formatter.parse(endDate);
//
//        Calendar startDay = Calendar.getInstance();
//        startDay.setTime(start);
//        Calendar endDay = Calendar.getInstance();
//        endDay.setTime(end);
//
//        // !startDay.after(endDay) -> represents iterate inclusive the end date
//        while( !startDay.after(endDay)) {
//            Date targetDay = startDay.getTime(); // current date
//            String currentDate = formatter.format(targetDay); // convert date type to String
//            /* since get the current date, we should get all needed ingredients for that date */
//            MealPlanDailyIngredientCount dailyIngredientCount = new MealPlanDailyIngredientCount(currentDate);
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<IngredientInRecipe> dailyIngredients = dailyIngredientCount.getIngredients();
//
//                    for (int i = 0; i < dailyIngredients.size(); i++) { // iterate ingredients for one date
//                        IngredientInRecipe targetIngredient = dailyIngredients.get(i);
//                        String targetDescription = targetIngredient.getBriefDescription();
//                        Log.d("target description", targetDescription);
//                        /* using targetIngredient's briefDescription to find if ingredientInStorage has a same ingredient*/
//                        boolean dailyKey = true;
//                        while (dailyKey) {
//                            for (int j = 0; j < allIngredientInStorage.size(); j++) {
//                                IngredientInStorage targetIngredientStorage = allIngredientInStorage.get(j);
//                                if (targetDescription.equals(targetIngredientStorage.getBriefDescription())) {
//
//                                    /* since we can find the same ingredient in storage, now we need to compare date*/
//                                    Date bestBeforeDate = null;
//                                    try {
//                                        bestBeforeDate = formatter.parse(targetIngredientStorage.getBestBeforeDate());
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    if (bestBeforeDate.compareTo(targetDay) > 0 || bestBeforeDate.compareTo(targetDay) == 0) {
//                                        // bestBeforeDate occurs after targetDate
//                                        // now, need to compare the amount to check if we need to buy extra
//                                        // we check the each ingredient unit first and transfer them to be the same
//                                        double targetAmount = unitConvert(targetIngredient.getUnit(), targetIngredient.getAmountValue());
//                                        double storageAmount = unitConvert(targetIngredientStorage.getUnit(), targetIngredientStorage.getAmountValue());
//                                        if (storageAmount > targetAmount) {
//                                            // update storage amount since it is enough to use ingredient in storage
//                                            // do not need to prepare this ingredient
//                                            double difference = storageAmount - targetAmount;
//                                            targetIngredientStorage.setAmountValue(difference);
//                                            Log.d("difference", String.valueOf(difference));
//
//                                        } else if (storageAmount == targetAmount) {
//                                            // remove this ingredient in storage
//                                            // ingredient in storage can totally cover the ingredient user needs
//                                            allIngredientInStorage.remove(targetIngredientStorage);
//                                        } else if (storageAmount < targetAmount) {
//                                            // remove this ingredient in storage
//                                            // update rest needed amount of ingredient to ingredientInShoppingList
//                                            // check first if there is an existing one in ingredientInShoppingList
//                                            // otherwise, just update amount for that ingredient
//                                            allIngredientInStorage.remove(targetIngredientStorage);
//                                            double differenceCart = targetAmount - storageAmount;
//                                            Log.d("differenceCar", String.valueOf(differenceCart));
//                                            String targetCategory = targetIngredient.getIngredientCategory();
//                                            //String targetUnit = targetIngredient.getUnit();
//                                            String targetUnit = chooseUnit(targetIngredient.getUnit(), targetIngredientStorage.getUnit());
//                                            //                                        Log.d("choose unit ", targetUnit);
//                                            boolean key = true;
//
//                                            while (key) {
//                                                for (int k = 0; k < ingredientShoppingListTemp.size(); k++) {
//                                                    IngredientInRecipe ingredientForShopping = ingredientShoppingListTemp.get(k);
//                                                    if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
//                                                        // there is an existing ingredient in ingredientInShoppingList
//                                                        double exitingAmount = ingredientForShopping.getAmountValue();
//                                                        double updateAmount = exitingAmount + differenceCart;
//                                                        ingredientForShopping.setAmountValue(updateAmount);  // only update amount
//                                                        key = false;
//                                                    }
//                                                }
//                                                if (key) {
//                                                    /* after iterating ingredientInShoppingList, if there is no existing one */
//                                                    ingredientShoppingListTemp.add(new IngredientInRecipe(targetDescription, String.valueOf(differenceCart), targetUnit, targetCategory));
//                                                    //                                                ingredientShoppingListAdapter.notifyDataSetChanged();
//                                                    key = false;
//                                                }
//                                            }
//                                        }
//
//                                    } else if (bestBeforeDate.compareTo(targetDay) < 0) {
//                                        // bestBeforeDate occurs before targetDate
//                                        // now, need to buy exact amount of ingredient since suer cannot use ingredients in storage
//                                        // add ingredient directly into ingredient ingredientInShoppingList after check if there is existing one
//                                        // otherwise, update amount for that ingredient
//                                        boolean anotherKey = true;
//                                        while (anotherKey) {
//                                            for (int p = 0; p < ingredientShoppingListTemp.size(); p++) {
//                                                IngredientInRecipe ingredientForShopping = ingredientShoppingListTemp.get(p);
//                                                if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
//                                                    double exitingAmount = ingredientForShopping.getAmountValue();
//                                                    double updateAmount = exitingAmount + targetIngredient.getAmountValue(); // existing amount add needed amount
//                                                    ingredientForShopping.setAmountValue(updateAmount);
//                                                    anotherKey = false;
//                                                }
//                                            }
//                                            if (anotherKey) {
//                                                /* after iterating ingredientInShoppingList, if there is no existing one */
//                                                ingredientShoppingListTemp.add(targetIngredient);
//                                                anotherKey = false;
//                                                //                                          ingredientShoppingListAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//
//                                    }
//                                    dailyKey = false;
//                                }
//
//                                ingredientShoppingListAdapter.notifyDataSetChanged();
//                            }
//                            if (dailyKey) {  // check if dailyKey has been changed
//                                ingredientShoppingListTemp.add(targetIngredient);
//                                ingredientShoppingListAdapter.notifyDataSetChanged();
//                                dailyKey = false;
//                            }
//                            // if there is no same ingredients in the storage, just directly add into shoppingList
//                        }
//                        //                        Log.d("add shopping", ingredientShoppingList.get(i).getBriefDescription());
//                    }
//                    Log.d("size shopping", String.valueOf(ingredientShoppingListTemp.size()));
//                }
//            }, 2000);
//            startDay.add(Calendar.DATE, 1);
//        }
//    }

    private Double unitConvert(String unit, Double amount) {
        Double scale = 1.0;
        if (unit.equals("kg")) {  // to g
            scale = 1000.0;
        }else if (unit.equals("oz")) {  // to g
            scale = 28.3495;
        }else if (unit.equals("lb")) {  // to g
            scale = 453.592;
        }else if (unit.equals("l")) {  // to ml
            scale = 1000.0;
        }
        return amount * scale;
    }

    private String chooseUnit(String targetUnit, String storageUnit){
        String unit = "null";

        if(targetUnit.equals("kg") || targetUnit.equals("oz")||targetUnit.equals("lb") ){
            if (storageUnit.equals("kg")|| storageUnit.equals("oz" )|| storageUnit.equals("lb")){
                unit = "g";
            }
        }else if(targetUnit.equals("l" )|| storageUnit.equals("l")){
            unit = "ml";
        }
        return unit;
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

