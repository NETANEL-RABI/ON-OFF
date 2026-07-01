package com.example.videoblocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {

    private EditText etCode;
    private Button btnUnlock;
    private String savedCode;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        etCode = findViewById(R.id.etCode);
        btnUnlock = findViewById(R.id.btnUnlock);

        prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE);
        // קוד ברירת מחדל ראשוני הוא 123456 אם ההורה עדיין לא שינה אותו
        savedCode = prefs.getString("pinCode", "123456"); 

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = etCode.getText().toString();
                
                if (enteredCode.equals(savedCode)) {
                    // קוד נכון! בודקים אם המשתמש ניסה להיכנס למסך ההגדרות הראשי
                    boolean fromMain = getIntent().getBooleanExtra("from_main", false);
                    if (fromMain) {
                        // אם הוא ניסה להיכנס למסך הראשי, נכבה זמנית את החסימה כדי שלא ייווצר לופ ונפתח לו את המסך
                        prefs.edit().putBoolean("isBlockerOn", false).apply();
                        Intent mainIntent = new Intent(LockScreenActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                    }
                    finish(); // סוגר את מסך החסימה ומאפשר גישה
                } else {
                    Toast.makeText(LockScreenActivity.this, "קוד שגוי!", Toast.LENGTH_SHORT).show();
                    // קוד שגוי - זורק את המשתמש מיד למסך הבית של הטלפון
                    goToHomeScreen();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // מניעת עקיפה: לחיצה על כפתור "חזור" במכשיר לא תסגור את המסך, אלא תזרוק למסך הבית
        goToHomeScreen();
    }

    private void goToHomeScreen() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }
}
