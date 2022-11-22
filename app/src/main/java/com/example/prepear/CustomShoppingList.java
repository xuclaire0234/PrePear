package com.example.prepear;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomShoppingList extends ArrayAdapter<IngredientInRecipe> {
    private ArrayList<IngredientInRecipe> ingredientsInShoppingList;
    private Context context;

    public CustomShoppingList(Context context, ArrayList<IngredientInRecipe> ingredientsInShoppingList) {
        super(context, 0, ingredientsInShoppingList);
        this.ingredientsInShoppingList = ingredientsInShoppingList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        /* check error */
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredient_in_shopping_list, parent, false);
        }

        IngredientInRecipe ingredientInShoppingList = ingredientsInShoppingList.get(position);

        /* connects the the layout with the views */
        ListView ingredientInShoppingListView = view.findViewById(R.id.ingredient_listview);
        TextView briefDescriptionTextView = view.findViewById(R.id.brief_description_TextView_shopping);
        TextView amountTextView = view.findViewById(R.id.amount_TextView_shopping);
        TextView unitTextView = view.findViewById(R.id.unit_TextView_shopping);
        TextView ingredientCategoryTextView = view.findViewById(R.id.ingredient_category_TextView_shopping);
        CheckBox shoppingListCheckBox = view.findViewById(R.id.ingredient_in_shopping_list_CheckBox);


        /* sets the detailed information to the view */
        briefDescriptionTextView.setText(ingredientInShoppingList.getBriefDescription());
        amountTextView.setText(String.valueOf(ingredientInShoppingList.getAmountString()));
        unitTextView.setText(ingredientInShoppingList.getUnit());
        ingredientCategoryTextView.setText(ingredientInShoppingList.getIngredientCategory());
        shoppingListCheckBox.setChecked(false);
        shoppingListCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                On below:
                show first pop up the dialogFragment
                then, compare actual amount with needed amount
                if actual amount < needed amount -> setChecked(false) -> update amount -> store the actual amount into db
                if actual amount >= needed amount -> setChecked(true) -> directly store in the db
                 */
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                ShoppingListClickboxFragment.newInstance(ingredientInShoppingList)
                        .show(fm, "ADD_INGREDIENT_DETAILS");

                final String TAG = "Ingredient Storage";
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = db.collection("Ingredient Storage");

                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                            FirebaseFirestoreException error) {
                        /*
                         * Loop through all the documents in the collection named "Ingredient Storage"
                         */
                        Double actualAmount = 0.0;
                        String description = ingredientInShoppingList.getBriefDescription();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Log.d(TAG, String.valueOf(doc.getData().get("description")));

                            /*
                             * Get description and category attributes
                             */
                            String descriptionIngredientInStorage = (String) doc.getData().get("description");
                            Double amountIngredientInStorage = new Double(doc.getData().get("amount").toString());

                            if (descriptionIngredientInStorage.equals(description)) {
                                if (amountIngredientInStorage >= Double.parseDouble(ingredientInShoppingList.getAmountString())) {
                                    shoppingListCheckBox.setChecked(true);
                                } else {
                                    shoppingListCheckBox.setChecked(false);
                                }
                            }
                        }
                    }
                });
            }
        });

//        // sets each ingredient object on listview to direct to ShoppingListViewIngredientFragment
//        ingredientInShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                FragmentActivity activity = (FragmentActivity)(context);
//                FragmentManager fm = activity.getSupportFragmentManager();
//                ShoppingListViewIngredientFragment.newInstance(ingredientsInShoppingList.get(position))
//                        .show(fm, "VIEW_INGREDIENT");
//            }
//        });
        return view;
    }
}
