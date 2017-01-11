package com.example.kishore.fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kishore on 1/7/2017.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://kishoreraju2.000webhostapp.com/login.php";
    private Map<String,String> params;

    public LoginRequest(String username , String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_REQUEST_URL,listener,null);
        params = new HashMap<String,String>();
        params.put("username",username);
        params.put("password",password);
    }
    @Override
    public Map<String,String> getParams(){
        return params;
    }

}
