package com.aston.mto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aston.mto.models.Main;
import com.aston.mto.models.OWM;
import com.aston.mto.utils.Constant;
import com.aston.mto.utils.FastDialog;
import com.aston.mto.utils.Network;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private TextView textViewCity;
    private TextView textViewTemperature;
    private ImageView imageViewIcon;
    private Button buttonOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = (EditText) findViewById(R.id.editTextCity);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);
        buttonOk = (Button) findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Network.isNetworkAvailable(MainActivity.this)) {
                    if(!editTextCity.getText().toString().isEmpty()) {
                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        String url = String.format(Constant.URL, editTextCity.getText().toString());

// Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String json) {
                                        Gson myGson = new Gson();
                                        OWM myOWM = myGson.fromJson(json, OWM.class);

                                        textViewCity.setText(myOWM.getName());
                                        textViewTemperature.setText(myOWM.getMain().getTemp());

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

// Add the request to the RequestQueue.
                        queue.add(stringRequest);

                    } else {
                        FastDialog.showDialog(MainActivity.this, FastDialog.SIMPLE_DIALOG, "Veuillez saisir une ville");
                    }

                } else {
                    FastDialog.showDialog(MainActivity.this, FastDialog.SIMPLE_DIALOG, "Aucune connexion");
                }
            }
        });

    }
}
