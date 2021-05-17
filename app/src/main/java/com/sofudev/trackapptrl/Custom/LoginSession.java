package com.sofudev.trackapptrl.Custom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginSession {
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyLogin" ;
    public static final String UsernameKey = "UsernameKey";
    public static final String IdPartyKey = "IdPartyKey";
    Context _context;
    SharedPreferences.Editor editor;

    public LoginSession(Context context){
        this._context = context;
        sharedpreferences = _context.getSharedPreferences(MyPREFERENCES, 0);
    }

    public void AddLoginSession(String user, String id)
    {
        editor = sharedpreferences.edit();
        editor.putString(UsernameKey, user);
        editor.putString(IdPartyKey, id);
        editor.apply();
    }

    public HashMap<String, String> getLoginSession(){
        HashMap<String, String> user = new HashMap<>();
        // username
        user.put(UsernameKey, sharedpreferences.getString(UsernameKey, null));

        // user id
        user.put(IdPartyKey, sharedpreferences.getString(IdPartyKey, null));

        // return user
        return user;
    }

    public void RemoveLoginSession()
    {
        editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }
}
