/**
 * Classname: CustomizeNumberOfServingsFragment
 * Version Information: 1.0.0
 * Date: 11/16/2022
 * Author: Jiayin He
 * Copyright Notice:
 */

package com.example.prepear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  This class defines the customize number of servings activity that allows user to edit the number
 *  of servings.
 */
public class CustomizeNumberOfServingsFragment extends DialogFragment {
    private EditText customizeNumberOfServingsEditText;
    private OnFragmentInteractionListener listener;

    /**
     *  This method defines an interface of methods that the ViewRecipeTypeMealActivity needs to implement
     *  in order to respond to the user clicking Confirm buttons.
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(String newCustomizedNumberOfServings);
    }

    /**
     * This method is called when a Fragment is first attached to a host Activity.
     * Use this method to check if the Activity has implemented the required listener callback
     * for the fragment.
     * @param context the {@link Context} fragment been attached to
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method brings the original customized number of servings from ViewRecipeTypeMealActivity to the fragment.
     * @param customizeNumberOfServings the original customized number of servings that is of type {@link String}
     * @return the newly created fragment
     */
    public static CustomizeNumberOfServingsFragment newInstance(String customizeNumberOfServings) {
        Bundle args = new Bundle();
        args.putString("number of servings", customizeNumberOfServings);

        CustomizeNumberOfServingsFragment fragment = new CustomizeNumberOfServingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method creates the customize number of servings fragment.
     * @param savedInstanceState a {@link Bundle} object that is used to restore themselves to a previous state
     * @return a {@link Dialog} object to build the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // gets the old customized number of servings passed from ViewRecipeTypeMealActivity
        Bundle args = getArguments();
        String oldCustomizedNumberOfServing = args.getString("number of servings");

        // sets up the fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_customize_number_of_servings, null);
        customizeNumberOfServingsEditText = view.findViewById(R.id.customize_number_of_servings_EditText);
        customizeNumberOfServingsEditText.setText(oldCustomizedNumberOfServing);

        // sets up the customized number of servings word counter
        TextView customizedNumberOfServingsWordCount = view.findViewById(R.id.customizes_number_of_servings_word_count);
        final TextWatcher customizedNumberOfServingsTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customizedNumberOfServingsWordCount.setText(String.valueOf(5 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        customizeNumberOfServingsEditText.addTextChangedListener(customizedNumberOfServingsTextEditorWatcher);

        // sets up the cancel and confirm button of fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Scale Ingredients")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }

    /**
     * This method checks if the edittext is left blank and prevents fragment from closing if it is.
     */
    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener((View v) -> {
                Boolean wantToCloseDialog = true;
                String newCustomizedNumberOfServings = customizeNumberOfServingsEditText.getText().toString();

                // checks if the edittext for editing customized number of servings is left blank
                if (newCustomizedNumberOfServings.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "You did not enter the customized number of servings.",
                            Toast.LENGTH_LONG).show();
                    wantToCloseDialog = false;
                } else {
                    listener.onConfirmPressed(newCustomizedNumberOfServings);
                }

                // prevents fragment from closing if the edittext for editing customized number of servings is left blank
                if (wantToCloseDialog) {
                    d.dismiss();
                }
            });
        }
    }
}