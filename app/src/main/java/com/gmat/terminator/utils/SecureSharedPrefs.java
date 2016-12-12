package com.gmat.terminator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Akanksha on 12-Dec-16.
 */

public class SecureSharedPrefs implements SharedPreferences {

    private static final byte[] SECRET_KEY = {'y', 'e', 's', 'r', 'o', 'b', 'o',
            's', 'e', 'c', 'r', 'e', 't', 'k', 'e', 'y' };
    private SharedPreferences mSharedPreferences;

    public SecureSharedPrefs(Context context) {
        if (context != null){
            mSharedPreferences = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        }
    }

    public SecureSharedPrefs(Context context,String prefsName) {
        if (context != null){
            mSharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        }
    }

    public class Editor implements SharedPreferences.Editor {

        protected SharedPreferences.Editor mEditor;

        public Editor() {
            mEditor = mSharedPreferences.edit();
        }

        public Editor putBoolean(String key, boolean value) {
            mEditor.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        public Editor putFloat(String key, float value) {
            mEditor.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        public Editor putInt(String key, int value) {
            mEditor.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        public Editor putLong(String key, long value) {
            mEditor.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        public Editor putString(String key, String value) {
            if (value != null)
                mEditor.putString(key, encrypt(value));
            else
                mEditor.putString(key, value);
            return this;
        }

        public void apply() {
            ((Editor) mEditor).apply();
        }

        public Editor clear() {
            mEditor.clear();
            return this;
        }

        public boolean commit() {
            return mEditor.commit();
        }

        public Editor remove(String key) {
            mEditor.remove(key);
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putStringSet(
                String arg0, Set<String> arg1) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public Editor edit() {
        return new Editor();
    }

    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    public boolean getBoolean(String key, boolean defValue) {
        final String value = mSharedPreferences.getString(key, null);
        return (value != null) ? Boolean.parseBoolean(decrypt(value)) : defValue;
    }

    public float getFloat(String key, float defValue) {
        final String value = mSharedPreferences.getString(key, null);
        return (value != null) ? Float.parseFloat(decrypt(value)) : defValue;
    }

    public int getInt(String key, int defValue) {
        final String value = mSharedPreferences.getString(key, null);
        return (value!=null) ? Integer.parseInt(decrypt(value)) : defValue;
    }

    public long getLong(String key, long defValue) {
        final String value = mSharedPreferences.getString(key, null);
        return (value != null) ? Long.parseLong(decrypt(value)) : defValue;
    }

    public String getString(String key, String defValue) {
        if(mSharedPreferences==null)
            return null;
        final String value = mSharedPreferences.getString(key, null);
        return (value != null) ? decrypt(value) : defValue;
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public void registerOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private String encrypt(String toEncrypt) {
        try {
            //Create your Secret Key Spec, which defines the key transformations
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY, "AES");

            //Get the cipher text
            Cipher cipher = Cipher.getInstance("AES");

            //Initialize the cipher
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            //Encrypt the string into bytes
            byte[ ] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());

            //Convert the encrypted bytes back into a string and return
            return Base64.encodeToString(encryptedBytes, Base64.URL_SAFE);

        } catch( Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decrypt(String encryptedText) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY, "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] toDecrypt = Base64.decode(encryptedText, Base64.URL_SAFE);

            return new String(cipher.doFinal(toDecrypt));

        } catch( Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getStringSet(String arg0, Set<String> arg1) {
        return null;
    }


}
