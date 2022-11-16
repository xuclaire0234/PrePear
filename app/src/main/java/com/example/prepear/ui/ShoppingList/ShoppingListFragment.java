package com.example.prepear.ui.ShoppingList;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.example.prepear.AddEditIngredientActivity;
import com.example.prepear.Ingredient;
import com.example.prepear.IngredientInRecipe;
import com.example.prepear.R;
import com.example.prepear.RecipeAddEditIngredientFragment;
import com.example.prepear.databinding.FragmentRecipeBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private FragmentRecipeBinding binding;
    private String startDate, endDate, newDate;
    final String[] sortItemSpinnerContent = {"  ","Description", "Category"};
    private ArrayList<IngredientInRecipe> ingredientList;
    private DatePickerDialog dialog;

    TextView fromDateText, toDateText;
    Spinner sortItemSpinner;
    ImageButton sortOrderButton;
    Button confirmButton;
    ListView ingredientView;

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
        ingredientView = view.findViewById(R.id.ingredient_listview);

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
                    MealPlanDailyIngredientCount count = new MealPlanDailyIngredientCount("2022-11-14");

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
}

