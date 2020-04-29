package com.riyad.go4lunch;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

abstract class SharedPreferenceLiveData<T> extends LiveData<T> {

    SharedPreferences sharedPreferences;
    String key;
    T defValue;

    public SharedPreferenceLiveData(T value, SharedPreferences sharedPreferences, String key, T defValue) {
        super(value);
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defValue = defValue;
    }

    public SharedPreferenceLiveData(SharedPreferences sharedPreferences, String key, T defValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(SharedPreferenceLiveData.this.key.equals(key)){
                        setValue(getValueFromPreferences(key, defValue));
                    }
                }
            };
    abstract T getValueFromPreferences(String key, T defValue);


    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defValue));
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

    private String getValueFromPreferences(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }
}
