package com.saini.alpha;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import java.util.Random;
import android.widget.EditText; // Import the EditText class
import android.graphics.Color;
import java.util.ArrayList;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText inputField;

    private static final String SHARED_PREFS_KEY = "MyPrefs";
    private static final String ARRAYLIST_KEY = "ArrayListKey";
    private static ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrieveArrayList();

        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        inputField = findViewById(R.id.inputField2);

        EditText editText = findViewById(R.id.editTextText);
        editText.setText(idea());
        editText.setEnabled(false);
        editText.setTextColor(Color.WHITE);

        fab.setOnClickListener(new View.OnClickListener() {

            public boolean isInt(char ch){
                int num = ch-'0';
                return num >= 0 && num < 10;
            }
            @Override
            public void onClick(View v) {
                if (inputField.getVisibility() == View.VISIBLE) {

                    String item = inputField.getText().toString().trim();
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
                } else {

                    inputField.setVisibility(View.VISIBLE);

                }
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