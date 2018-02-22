package com.example.miranda.monitoringpdam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Miranda on 21/02/2018.
 */

public class History extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView textItemid, textClock, textValue, textNs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode

        if (this.pref.getString("userid","").equals("")){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            setContentView(R.layout.activity_login);
            initComponents();
            this.editor = pref.edit();
        }else{
            Intent intent = new Intent(getApplication(), History.class);
            startActivity(intent);
            finish();
        }


    }

    private String cekHistory() throws Exception{
        String res = cekHistory();

            String auth = "";

            try {
                JSONRPCClient jsonRpc = new JSONRPCClient();

                jsonRpc.setMethod("history.get"); //set method name

                //set parameters
                jsonRpc.addParam("output", textItemid.getText().toString());
                jsonRpc.addParam("itemids", textItemid.getText().toString());
                jsonRpc.addParam("sortfields", textClock.getText().toString());
                jsonRpc.addParam("sortrder", textValue.getText().toString());
                jsonRpc.addParam("limit", textNs.getText().toString());

                String jsonArray = "[" + jsonRpc.connect() + "]";

                if (jsonArray != null) {
                    String numericInformation_id = null;
                    JSONArray arrayDataFasilitas = new JSONArray(jsonArray);
                    for (int i = 0; i < arrayDataFasilitas.length(); i++) {
                        JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
                        JSONObject result = (JSONObject) list_spk.get("result");

                        numericInformation_id = result.getString("sessionid");

                        this.editor.putString("userid", result.getString("userid"));
                        this.editor.putString("itemids", result.getString("itemids"));
                        this.editor.putString("sortfields", result.getString("sortfields"));
                        this.editor.putString("sortorder", result.getString("sortorder"));
                        this.editor.putString("limit", result.getString("limit"));
                        this.editor.putString("sessionid", result.getString("sessionid"));

                        editor.commit();
                    }
                    auth = numericInformation_id;
                }
            } catch (Exception e) {
                throw e;
            }
            return auth;
        }

    private void initComponents() {
        this.textItemid = (TextView) this.findViewById(R.id.textItemid);
        this.textClock = (TextView) this.findViewById(R.id.textClock);
        this.textValue = (TextView) this.findViewById(R.id.textValue);
        this.textNs = (TextView) this.findViewById(R.id.textNs);
    }




}
