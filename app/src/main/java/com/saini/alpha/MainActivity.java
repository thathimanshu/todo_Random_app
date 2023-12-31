package com.saini.alpha;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import java.util.Random;
import android.widget.EditText; // Import the EditText class
import android.graphics.Color;
import java.util.ArrayList;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText inputField;
    private ImageButton imageButton2;
    private static final String SHARED_PREFS_KEY = "MyPrefs";
    private static final String ARRAYLIST_KEY = "ArrayListKey";

    private static final String SELECTED_IMAGE_INDEX_KEY = "SelectedImageIndexKey";
    private static final int DEFAULT_IMAGE_INDEX = -1;
    private static ArrayList<String> arr;
    public boolean isInt(char ch){
        int num = ch-'0';
        return num >= 0 && num < 10;
    }
    private Button button2;
    private ImageView imageView2;
    private int[] imageResources = {

            R.drawable.t2,
            R.drawable.t3,
            R.drawable.t4,
            R.drawable.t5,
            R.drawable.t6,
            R.drawable.t7
    };

    private void setRandomImage() {
        Random random = new Random();
        int randomIndex = random.nextInt(imageResources.length);
        Drawable imageDrawable = getResources().getDrawable(imageResources[randomIndex]);
        imageView2.setImageDrawable(imageDrawable);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SELECTED_IMAGE_INDEX_KEY, randomIndex);
        editor.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrieveArrayList();

        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        inputField = findViewById(R.id.inputField2);
        imageButton2 = findViewById(R.id.imageButton2);

        EditText editText = findViewById(R.id.editTextText);
        editText.setText(idea());
        editText.setEnabled(false);
        editText.setTextColor(Color.WHITE);

        button2 = findViewById(R.id.button2);
        imageView2 = findViewById(R.id.imageView2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandomImage();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        int selectedImageIndex = sharedPreferences.getInt(SELECTED_IMAGE_INDEX_KEY, DEFAULT_IMAGE_INDEX);

        if (selectedImageIndex == DEFAULT_IMAGE_INDEX) {
            // Set the default image if the selected image index is not found in SharedPreferences
            Drawable defaultImageDrawable = getResources().getDrawable(R.drawable.t4);
            imageView2.setImageDrawable(defaultImageDrawable);
        } else if (selectedImageIndex >= 0 && selectedImageIndex < imageResources.length) {
            // Set the selected image if it exists in SharedPreferences
            Drawable selectedImageDrawable = getResources().getDrawable(imageResources[selectedImageIndex]);
            imageView2.setImageDrawable(selectedImageDrawable);
        }

        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (inputField.getVisibility() != View.VISIBLE) {
                    imageButton2.setVisibility(View.VISIBLE);
                    inputField.setVisibility(View.VISIBLE);
                } else{
                    inputField.setVisibility(View.GONE);
                    imageButton2.setVisibility(View.GONE);


                }
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = inputField.getText().toString().trim();
                imageButton2.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputField.getWindowToken(), 0);
                if(!item.isEmpty()){
                    if(isInt(item.charAt(0))){
                        int num = 0;
                        for(char ch:item.toCharArray()){
                            num = num*10 + ch-'0';
                        }
                        arr.remove(num-1);
                        Toast.makeText(MainActivity.this, "Item remove from list", Toast.LENGTH_SHORT).show();
                    } else{
                        arr.add(item);
                        Toast.makeText(MainActivity.this, "Item added to list", Toast.LENGTH_SHORT).show();
                    }
                    saveArrayList(); // Save the ArrayList to SharedPreferences after changes

                }

                inputField.setVisibility(View.GONE);
                inputField.getText().clear();
            }
        });



        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String newIdea = idea();
            editText.setText(newIdea);
        });
    }
    private void retrieveArrayList() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        String arrayListJson = sharedPreferences.getString(ARRAYLIST_KEY, null);

        if (arrayListJson != null) {
            Gson gson = new Gson();
            TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
            arr = gson.fromJson(arrayListJson, typeToken.getType());
        } else {
            arr = new ArrayList<>();
        }
    }

    // Save the ArrayList to SharedPreferences
    private void saveArrayList() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String arrayListJson = gson.toJson(arr);

        editor.putString(ARRAYLIST_KEY, arrayListJson);
        editor.apply();
    }
    public static String idea(){
        if(arr.size()==0) return "<<---List is Empty--->>";
        Random random = new Random();
        int idx = random.nextInt(arr.size());
        String str = Integer.toString(idx+1) +". "+ arr.get(idx);
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}