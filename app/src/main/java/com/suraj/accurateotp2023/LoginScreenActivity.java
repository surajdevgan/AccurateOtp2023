package com.suraj.accurateotp2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.suraj.accurateotp2023.util.ConnectionCheck;
import com.suraj.accurateotp2023.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreenActivity extends AppCompatActivity {

    CardView cardView;
    EditText Phone;
    ProgressBar pb;
    RequestQueue requestQueue;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String name = "";
    String phone ="";
    String count="", Rights = "";

    int id=0;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences=getSharedPreferences(Util.AcuPrefs,MODE_PRIVATE);
        editor=preferences.edit();

        Phone = findViewById(R.id.Phone);
        cardView= findViewById(R.id.cardView);
        pb = findViewById(R.id.progressbar);
        requestQueue= Volley.newRequestQueue(this);
        cardView.setOnClickListener(view -> {

            String phone = Phone.getText().toString().trim();
            if(phone.isEmpty())
            {
                Phone.setError("Phone is required");
                Phone.requestFocus();
                return;
            }
            if (ConnectionCheck.isConnected(connectivityManager,networkInfo, LoginScreenActivity.this)) {
                loginUser();
            }else {
                Toast.makeText(LoginScreenActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }

        });
    }

    void loginUser(){

        pb.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Util.login1, response -> {
            Log.w("ass",""+response);
            try {
                JSONObject object=new JSONObject(response);
                JSONArray array=object.getJSONArray("students");
                String message=object.getString("message");
                if (message.contains("Login Sucessful")){
                    pb.setVisibility(View.GONE);
                    for (int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);
                        id = object1.getInt("User_ID");
                        Log.w("jkjkjkjkjk",""+id);
                        name = object1.getString("User_name");
                        phone = object1.getString("Phone");
                        count = object1.getString("Count");
                        Rights = object1.getString("Rights");
                        Log.w("Rights",Rights);


                    }
                    editor.putString(Util.Name,name);
                    editor.putString(Util.Phone,phone);
                    editor.putString(Util.count,count);
                    editor.putInt(Util.id,id);
                    editor.putString(Util.Rights, Rights);
                    editor.apply();
                    startActivity(new Intent(LoginScreenActivity.this,MainActivity.class));
                    finish();
                }else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(LoginScreenActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                pb.setVisibility(View.GONE);
                Toast.makeText(LoginScreenActivity.this,"Login Fail "+e, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginScreenActivity.this,"Login Fail" +error, Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("Phone",Phone.getText().toString());
                map.put("Flag","off");
                return map;
            }
        };
        requestQueue.add(request);
    }



}
