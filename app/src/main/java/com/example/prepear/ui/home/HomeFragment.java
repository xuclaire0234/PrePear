package com.example.prepear.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.prepear.R;
import com.example.prepear.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        TextView verifyLinkMessageTextView = view.findViewById(R.id.useremailverification_TextView);
        Button verifyLinkResendButton = view.findViewById(R.id.verifylink_resend_Button);
        Button ingredientStorageButton = view.findViewById(R.id.ingredient_storage_button);
        Button recipeFolderButton = view.findViewById(R.id.recipe_folder_button);
        Button shoppingListButton = view.findViewById(R.id.shopping_list_button);
        Button mealPlanButton = view.findViewById(R.id.meal_planner_button);

        if (! currentUser.isEmailVerified()) {
            verifyLinkMessageTextView.setVisibility(View.VISIBLE);
            verifyLinkResendButton.setVisibility(View.VISIBLE);
            ingredientStorageButton.setEnabled(false);
            recipeFolderButton.setEnabled(false);
            shoppingListButton.setEnabled(false);
            mealPlanButton.setEnabled(false);
            verifyLinkResendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // On below part: send the verification link to user's email
                        currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(v.getContext(), "Verification Email has been sent. " +
                                                "\n Please re-login after successful email verification!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(currentUser.getUid(), "On Failure: Email not sent" + e.getMessage());
                            }
                        });
                }
            });
        } else {
            verifyLinkMessageTextView.setVisibility(View.GONE);
            verifyLinkResendButton.setVisibility(View.GONE);
        }

    }

}