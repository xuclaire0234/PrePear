package com.example.prepear.ui.Ingredient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IngredientViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IngredientViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ingredient fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}