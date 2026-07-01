package com.example.videoblocker;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;

public class VideoBlockerService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";

            // קריאת מצב החסימה מהזיכרון המאובטח של המכשיר
            SharedPreferences prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE);
            boolean isBlockerOn = prefs.getBoolean("isBlockerOn", false);

            if (isBlockerOn) {
                // 1. זיהוי אפליקציות וידאו, נגנים וגלריה לפי מילות מפתח בשם החבילה
                boolean isVideo = packageName.contains("video") || packageName.contains("gallery") || packageName.contains("player");
                
                // 2. זיהוי ניסיון כניסה להגדרות המכשיר או למנהל ההתקנות (למניעת עקיפה/מחיקה)
                boolean isSettings = packageName.equals("com.android.settings") || packageName.contains("packageinstaller");

                if (isVideo || isSettings) {
                    // הקפצת מסך הנעילה שחוסם את התוכן
                    Intent lockIntent = new Intent(this, LockScreenActivity.class);
                    lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(lockIntent);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        // פונקציית חובה של המערכת, נשארת ריקה
    }
}
