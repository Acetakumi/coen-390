package com.example.eventcounter;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;


public class SharedPreferenceHelper {

    private static final String PREF_NAME = "EventCounterPrefs";
    private SharedPreferences prefs;

    public SharedPreferenceHelper(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ---------- SETTINGS KEYS ----------
    private static final String KEY_BTN1 = "btn1";
    private static final String KEY_BTN2 = "btn2";
    private static final String KEY_BTN3 = "btn3";
    private static final String KEY_MAX  = "maxEvents";

    // ---------- COUNT KEYS ----------
    private static final String KEY_COUNT1 = "count1";
    private static final String KEY_COUNT2 = "count2";
    private static final String KEY_COUNT3 = "count3";
    private static final String KEY_TOTAL  = "total";

    // ---------- HISTORY ----------
    private static final String KEY_HISTORY = "history";

    // ===== SETTINGS =====
    public void saveSettings(Settings s) {
        SharedPreferences.Editor e = prefs.edit();
        e.putString(KEY_BTN1, s.getButton1Name());
        e.putString(KEY_BTN2, s.getButton2Name());
        e.putString(KEY_BTN3, s.getButton3Name());
        e.putInt(KEY_MAX, s.getMaxEvents());
        e.apply();
    }

    public Settings getSettings() {
        Settings s = new Settings();
        s.setButton1Name(prefs.getString(KEY_BTN1, null));
        s.setButton2Name(prefs.getString(KEY_BTN2, null));
        s.setButton3Name(prefs.getString(KEY_BTN3, null));
        s.setMaxEvents(prefs.getInt(KEY_MAX, 0));
        return s;
    }

    public boolean hasSettings() {
        return getSettings().isComplete();
    }

    // ===== COUNTS =====
    public int getCount(int num) {
        switch (num) {
            case 1: return prefs.getInt(KEY_COUNT1, 0);
            case 2: return prefs.getInt(KEY_COUNT2, 0);
            case 3: return prefs.getInt(KEY_COUNT3, 0);
        }
        return 0;
    }

    public void incrementCount(int num) {
        SharedPreferences.Editor e = prefs.edit();
        int current = getCount(num);
        if (num == 1) e.putInt(KEY_COUNT1, current + 1);
        if (num == 2) e.putInt(KEY_COUNT2, current + 1);
        if (num == 3) e.putInt(KEY_COUNT3, current + 1);
        e.apply();
    }

    public int getTotal() {
        return prefs.getInt(KEY_TOTAL, 0);
    }

    public void incrementTotal() {
        prefs.edit().putInt(KEY_TOTAL, getTotal() + 1).apply();
    }

    // ===== HISTORY =====
    public String getHistoryCSV() {
        return prefs.getString(KEY_HISTORY, "");
    }

    public void addHistory(int num) {
        String current = getHistoryCSV();
        current = current.isEmpty() ? String.valueOf(num) : current + "," + num;
        prefs.edit().putString(KEY_HISTORY, current).apply();
    }

    public List<Integer> getHistoryList() {
        String csv = getHistoryCSV();
        List<Integer> out = new ArrayList<>();
        if (csv == null || csv.trim().isEmpty()) return out;

        String[] parts = csv.split(",");
        for (String p : parts) {
            try {
                out.add(Integer.parseInt(p.trim()));
            } catch (Exception ignored) {}
        }
        return out;
    }

    public void clearAllCountsAndHistory() {
        SharedPreferences.Editor e = prefs.edit();
        e.remove(KEY_COUNT1);
        e.remove(KEY_COUNT2);
        e.remove(KEY_COUNT3);
        e.remove(KEY_TOTAL);
        e.remove(KEY_HISTORY);
        e.apply();
    }


    // ===== LIMIT CHECK =====
    public boolean canAddEvent() {
        Settings s = getSettings();
        return getTotal() < s.getMaxEvents();
    }
}


