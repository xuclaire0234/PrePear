/**
 * Classname: ShoppingListFragment
 * Version Information: 1.0.0
 * Date: 11/19/2022
 * Author: Jingyi Xu, Yingyue Cao, Jamie Lee
 * Copyright Notice:
 */

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
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Date;

public class ShoppingListFragment extends Fragment {

    private String startDate, endDate, newDate;
    final String[] sortItemSpinnerContent = {"  ----select----  ", "Description", "Category"};
    private ShoppingListController ingredientShoppingList;  // store all the ingredients needed to show in the listView
    private ArrayAdapter<IngredientInRecipe> ingredientShoppingListAdapter;
    private DatePickerDialog dialog;

    TextView fromDateText, toDateText;
    Spinner sortItemSpinner;
    ImageButton sortOrderButton;
    Button confirmButton;
    ListView shoppingListView;

    private ArrayList<IngredientInStorage> allIngredientInStorage = new ArrayList<>();
    private Boolean reverse = Boolean.FALSE;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* This shopping list pop up window will show up when user gets into shopping fragment
         * which uses for notifying user to choose a time period to acquire the ingredients needed to buy
         */
        LayoutInflater popup_example = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(popup_example.inflate(R.layout.shopping_list_popup_window, null, false), 800, 300, true);
        pw.showAtLocation(this.getView(), Gravity.CENTER, 0, -200);

        // On below part: connect the layout with the views and buttons
        fromDateText = view.findViewById(R.id.fromDate_textView);
        toDateText = view.findViewById(R.id.toDate_textView);
        sortItemSpinner = view.findViewById(R.id.sort_spinner);
        sortOrderButton = view.findViewById(R.id.sort_button);
        sortOrderButton.setImageResource(R.drawable.ic_sort);
        confirmButton = view.findViewById(R.id.confirm_button);
        shoppingListView = view.findViewById(R.id.ingredient_listview);


        // set adapter
        try {
            ingredientShoppingList = new ShoppingListController();
            ingredientShoppingListAdapter = new CustomShoppingList(this.getContext(), ingredientShoppingList.getIngredients());
            shoppingListView.setAdapter(ingredientShoppingListAdapter);
        } catch (NullPointerException e) {
        }

        /*
         * When the sort order button were pressed, the sort order should reverse
         */
        sortOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reverse) {
                    sortOrderButton.setImageResource(R.drawable.ic_sort);
                    reverse = Boolean.FALSE;
                } else {
                    reverse = Boolean.TRUE;
                    sortOrderButton.setImageResource(R.drawable.ic_sort_reverse);
                }
                ingredientShoppingList.reverseOrder();
                ingredientShoppingListAdapter.notifyDataSetChanged();
            }
        });

        /*
         * The spinner is set and linked to the string array which contains all the
         * possible sort values
         */
        sortItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    ingredientShoppingList.sortIngredient(i-1);
                    ingredientShoppingListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // On below: Sort-by Spinner Initialization
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortItemSpinnerContent);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortItemSpinner.setAdapter(ad);

        // On below: allow the user to input startDate by using pup up calendar
        fromDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker(fromDateText);
            }
        });

        // On below: allow the user to input endDate by using pup up calendar
        toDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker(toDateText);
            }
        });

        /* set setOnClickListener for confirmButton for identifying
         * if the user-inputted startDate and endDate is valid or not
         * then, if the user-inputted dates is invalid, setText as empty
         * which prompts user to enter again
         */

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    /* instantiation computeShoppingList class to get the ingredientShoppingList
                     * since user input valid startDate and endDate
                     */
                    ingredientShoppingList.clear();  // clear previous ingredientShoppingList
                    ComputeShoppingList computeShoppingList = new ComputeShoppingList();  // load all ingredient in storage

                    /*
                     * Using Handler to ensure the all ingredients are loaded into the ArrayList allIngredientInStorage
                     * Therefore, we can continue to calculate what ingredients user has to buy by calling
                     * method calculateShoppingList(Date)
                     */

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            allIngredientInStorage = computeShoppingList.getAllIngredientsInStorage();
                            try {
                                // convert type of startDate and endDate from String to Date first
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date start = formatter.parse(startDate);
                                Date end = formatter.parse(endDate);

                                Calendar startDay = Calendar.getInstance();
                                startDay.setTime(start);
                                Calendar endDay = Calendar.getInstance();
                                endDay.setTime(end);

                                /*
                                 * !startDay.after(endDay) -> represents iterate inclusive the end date
                                 * we will find out the needed ingredients for this time period day by day
                                 */
                                while (!startDay.after(endDay)) {
                                    Date targetDay = startDay.getTime(); // current date
                                    calculateShoppingList(targetDay);
                                    startDay.add(Calendar.DATE, 1);  // date + 1 to get ingredients for the next day
                                }
                            }catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                        }
                    },2000);
                }
            }
        });


    }

    /**
     * This method will check if user input an valid time period
     * if time period is invalid, check will be false
     * and vice versa
     * @return Boolean variable check
     */
    private Boolean check() {

        Boolean check = Boolean.TRUE;  // initializing boolean variable check
        String newFromDate = fromDateText.getText().toString();
        String newToDate = toDateText.getText().toString();

        if (newFromDate.isEmpty() || newToDate.isEmpty()) {   // if both dates are empty
            check = Boolean.FALSE;

        } else {   // since both dates are not empty
            Integer compare = newFromDate.compareTo(newToDate);
            if (compare <= 0) {   // compare <= 0 means endDate is bigger than startDate, valid input
                startDate = newFromDate;
                endDate = newToDate;
            } else {   // endDate is less than startDate, invalid input
                check = Boolean.FALSE;
            }
        }

        /*
         * Since user input is valid
         * we should set the correct time period to TextViews for user to see
         */
        fromDateText.setText(startDate);
        toDateText.setText(endDate);
        return check;
    }

    /**
     * This method will provide a calendar for user to choose both startDate and endDate
     * When user clicks the TextView, the datePicker will pop up
     * @param textView Show time as startDate or endDate
     */
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


    /**
     * This method will calculate all the ingredients needed to buy and shown on the listView for one targetDate
     * @param targetDay one Date in the user selected time period
     * @throws ParseException Signals that an error has been reached unexpectedly while parsing (SimpleDateFormat)
     */
    public void calculateShoppingList(Date targetDay) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(targetDay); // convert date type to String
        /* Since get the Target date,
         * we should get all needed ingredients for that date
         * By comparing with ingredientInStorage
         */

        // instantiation MealPlanDailyIngredientCount Class to get all needed ingredient for the meals in targetDate
        MealPlanDailyIngredientCount dailyIngredientCount = new MealPlanDailyIngredientCount(currentDate);

        // Using Handler to ensure dailyIngredients is loaded completely
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // acquire the dailyIngredients by calling method getIngredients() in MealPlanDailyIngredientCount Class
                ArrayList<IngredientInRecipe> dailyIngredients = dailyIngredientCount.getIngredients();

                for (int i = 0; i < dailyIngredients.size(); i++) {  // iterate ingredients for the targetDate
                    Log.d("daily size", String.valueOf(dailyIngredients.size()));
                    /* Comparing the ingredient in dailyIngredients one by one
                     * Using the briefDescription of targetIngredient to find
                     * if ingredientInStorage has a same ingredient
                     */

                    IngredientInRecipe targetIngredient = dailyIngredients.get(i);
                    String targetDescription = targetIngredient.getBriefDescription();
                    Log.d("Target",targetDescription);
                    boolean dailyKey = true;
                    while(dailyKey){

                        /* This for loop will deal with the situation, which there is an existing ingredient
                         * We should consider the best before date for that existing ingredient in storage
                         */

                        for (int j = 0; j < allIngredientInStorage.size(); j++) {  // iterate ingredients in ingredientInStorage
                            IngredientInStorage targetIngredientStorage = allIngredientInStorage.get(j);

                            if (targetDescription.equals(targetIngredientStorage.getBriefDescription())) {
                                // since we can find the same ingredient in storage, now we need to compare date
                                Date bestBeforeDate = null;
                                try {  // convert type of bestBeforeDate in storage from String to Date
                                    bestBeforeDate = formatter.parse(targetIngredientStorage.getBestBeforeDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (bestBeforeDate.compareTo(targetDay) > 0 || bestBeforeDate.compareTo(targetDay) == 0) {

                                    /* bestBeforeDate occurs after targetDate
                                     * now, need to compare the amount to check if we need to buy extra ingredient
                                     * we check the each ingredient unit first and transfer them to be the same
                                     * and then, calculate the difference
                                     */

                                    double targetAmount = unitConvert(targetIngredient.getUnit(), targetIngredient.getAmountValue());
                                    double storageAmount = unitConvert(targetIngredientStorage.getUnit(), targetIngredientStorage.getAmountValue());
                                    if (storageAmount > targetAmount) {  // update storage amount since it is enough to use ingredient in storage
                                        // do not need to prepare this ingredient
                                        double difference = storageAmount - targetAmount;
                                        String updatedUnit = chooseUnit(targetIngredient.getUnit(),targetIngredientStorage.getUnit());
                                        targetIngredientStorage.setUnit(updatedUnit);
                                        targetIngredientStorage.setAmountValue(difference);

                                    } else if (storageAmount == targetAmount) {

                                        /* remove this ingredient in storage
                                         * ingredient in storage can totally cover the ingredient user needs
                                         */
                                        allIngredientInStorage.remove(targetIngredientStorage);

                                    } else if (storageAmount < targetAmount) {
                                        /* remove this ingredient in allIngredientInStorage
                                         * update rest needed amount of ingredient to ingredientInShoppingList
                                         * check first if we need to add new one in ingredientInShoppingList
                                         * otherwise, just update amount for that ingredient
                                         */

                                        allIngredientInStorage.remove(targetIngredientStorage);
                                        double differenceCart = targetAmount - storageAmount;
                                        double roundedDifferenceCart = Math.round(differenceCart*100d)/100d;
                                        String targetCategory = targetIngredient.getIngredientCategory();
                                        String targetUnit = chooseUnit(targetIngredient.getUnit(), targetIngredientStorage.getUnit());
//
                                        boolean key = true;
                                        while (key) {
                                            /* This for loop will deal with the situation, which there is an existing ingredient in ingredientInShoppingList
                                             * We should consider to update amount for that existing ingredient
                                             */

                                            for (int k = 0; k < ingredientShoppingList.countIngredients(); k++) {
                                                IngredientInRecipe ingredientForShopping = ingredientShoppingList.getIngredientAt(k);

                                                if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                                    double exitingAmount = ingredientForShopping.getAmountValue();
                                                    double updateAmount = exitingAmount + differenceCart;
                                                    double roundedUpdate = Math.round(updateAmount*100d)/100d;
                                                    ingredientForShopping.setAmountValue(roundedUpdate);  // only update amount
                                                    key = false;
                                                }
                                            }
                                            /* After iterating ingredientInShoppingList
                                             * Since there is no exiting ingredient in ingredientInShoppingList
                                             * we should add new one into it
                                             */
                                            if (key) {
                                                ingredientShoppingList.add(new IngredientInRecipe(targetDescription, String.valueOf(roundedDifferenceCart), targetUnit, targetCategory));
                                                key = false;
                                            }
                                        }
                                    }

                                } else if (bestBeforeDate.compareTo(targetDay) < 0) {
                                    /* bestBeforeDate occurs before targetDate
                                     * now, need to buy exact amount of ingredient since uer cannot use ingredients in storage
                                     * add ingredient directly into ingredient ingredientInShoppingList after check if there is existing one
                                     * otherwise, update amount for that ingredient
                                     */

                                    boolean anotherKey = true;
                                    while (anotherKey) {
                                        /* This for loop will deal with the situation, which there is an existing ingredient in ingredientInShoppingList
                                         * We should consider to update amount for that existing ingredient
                                         */

                                        for (int p = 0; p < ingredientShoppingList.countIngredients(); p++) {
                                            IngredientInRecipe ingredientForShopping = ingredientShoppingList.getIngredientAt(p);
                                            if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                                double exitingAmount = ingredientForShopping.getAmountValue();
                                                double updateAmount = exitingAmount + targetIngredient.getAmountValue();
                                                double roundUpdateAmount = Math.round(updateAmount*100d)/100d;
                                                ingredientForShopping.setAmountValue(roundUpdateAmount);
                                                anotherKey = false;
                                            }
                                        }

                                        /* After iterating ingredientInShoppingList
                                         * Since there is no exiting ingredient in ingredientInShoppingList
                                         * we should add new one into it
                                         */
                                        if (anotherKey) {
                                            ingredientShoppingList.add(targetIngredient);
                                            anotherKey = false;
                                        }
                                    }

                                }
                                dailyKey = false;
                            }

                            ingredientShoppingListAdapter.notifyDataSetChanged();
                        }
                        /* After iterating allIngredientInStorage
                         * Since there is no exiting ingredient in allIngredientInStorage
                         * we should add new one into ingredientInShoppingList
                         */
                        if (dailyKey) {
                            boolean updateKey = true;
                            while (updateKey) {   // this while loop helps to check if ingredientInShoppingList has the existing ingredient
                                /* This for loop will deal with the situation, which there is an existing ingredient in ingredientInShoppingList
                                 * We should consider to update amount for that existing ingredient
                                 */

                                for (int a = 0; a < ingredientShoppingList.countIngredients(); a++) {
                                    IngredientInRecipe ingredientForShopping = ingredientShoppingList.getIngredientAt(a);
                                    if (targetDescription.equals(ingredientForShopping.getBriefDescription())) {
                                        double exitingAmount = ingredientForShopping.getAmountValue();
                                        double updateAmount = exitingAmount + unitConvert(targetIngredient.getUnit(),targetIngredient.getAmountValue());
                                        double roundedAmount = Math.round(updateAmount*100d)/100d;
                                        Log.d("df", String.valueOf(updateAmount));
                                        ingredientForShopping.setAmountValue(roundedAmount);
                                        updateKey = false;
                                        dailyKey = false;
                                    }
                                }
                                /* After iterating ingredientInShoppingList
                                 * Since there is no exiting ingredient in ingredientInShoppingList
                                 * we should add new one into it
                                 */
                                if (updateKey) {
                                    ingredientShoppingList.add(targetIngredient);
                                    updateKey = false;
                                    ingredientShoppingListAdapter.notifyDataSetChanged();
                                    dailyKey = false;
                                }
                            }
                        }
                    }
                }
                ingredientShoppingListAdapter.notifyDataSetChanged();
            }
        }, 4000);
    }

    /**
     * This method will recalculate the amount by referencing the the general (standard) unit and given unit
     * @param unit A String that represents the unit for a ingredient
     * @param amount A number that represents the amount for a ingredient
     * @return amount * scale A double value contains converted amount
     */
    private Double unitConvert(String unit, Double amount) {
        /* Since we identify the given unit is solid unit
         * We should get scaling number according to standard unit (g) and given unit
         */
        Double scale = 1.0;
        if (unit.equals("kg")) {
            scale = 1000.0;
        }else if (unit.equals("oz")) {
            scale = 28.3495;
        }else if (unit.equals("lb")) {
            scale = 453.592;
        }else if (unit.equals("l")) {
            /* Since we identify the given unit is liquid unit
             * We should get scaling number according to standard unit (ml) and given unit
             */
            scale = 1000.0;
        }

        // return the calculated amount by multiply both given amount and scaling number
        return amount * scale;
    }

    /**
     * This method will unify the unit for ingredients added into IngredientShoppingList
     * decide to change unit either solid standard unit (g) or liquid standard unit (ml)
     * @param targetUnit A String that represents unit for target Ingredient
     * @param storageUnit A String that represents unit for target Ingredient in storage
     * @return unit A string that represents converted standard unit
     */
    private String chooseUnit(String targetUnit, String storageUnit){
        String unit = "null";
        /* since both given units are solid
         * we just return the solid standard unit (g)
         */
        if(targetUnit.equals("g") ||targetUnit.equals("kg") || targetUnit.equals("oz")||targetUnit.equals("lb") ){
            if (storageUnit.equals("kg")|| storageUnit.equals("oz" )|| storageUnit.equals("lb")||storageUnit.equals("g")){
                unit = "g";
            }
        }else if(targetUnit.equals("l" )|| targetUnit.equals("ml")){
            /* since both given units are liquid
             * we just return the solid standard unit (ml)
             */
            if (storageUnit.equals("l")|| storageUnit.equals("ml")){
                unit = "ml";
            }
        }
        return unit;
    }

}

