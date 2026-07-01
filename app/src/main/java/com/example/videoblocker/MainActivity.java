package com.example.videoblocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etNewCode;
    private Button btnSaveCode, btnOpenSettings;
    private Switch switchBlock;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE);
        boolean isBlockerOn = prefs.getBoolean("isBlockerOn", false);

        // הגנה מפני פריצה: אם החסימה פעילה, אל תציג את מסך השינויים אלא שלח למסך הנעילה
        if (isBlockerOn) {
            Intent lockIntent = new Intent(this, LockScreenActivity.class);
            lockIntent.putExtra("from_main", true); // סימון שהגענו מהמסך הראשי
            startActivity(lockIntent);
            finish();
            return;
        }

        etNewCode = findViewById(R.id.etNewCode);
        btnSaveCode = findViewById(R.id.btnSaveCode);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);
        switchBlock = findViewById(R.id.switchBlock);

        // טעינת המצב הנוכחי של המתג מהזיכרון
        switchBlock.setChecked(isBlockerOn);

        // שמירת קוד חדש בן 6 ספרות
        btnSaveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etNewCode.getText().toString();
                if (code.length() == 6) {
                    prefs.edit().putString("pinCode", code).apply();
                    Toast.makeText(MainActivity.this, "הקוד החדש נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "שגיאה: הקוד חייב להכיל בדיוק 6 ספרות", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // שינוי מצב החסימה (הפעלה או כיבוי)
        switchBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean("isBlockerOn", switchBlock.isChecked()).apply();
            }
        });

        // כפתור שמוביל ישירות להגדרות הנגישות של המכשיר להפעלה ראשונית
        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });
    }
}
