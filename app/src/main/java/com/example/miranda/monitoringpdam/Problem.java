package com.example.miranda.monitoringpdam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Problem extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView textEventid, textSource, textObject, textObjectid, textClock, textNs, textREventid, textRClock, textRNs, textCorrelationid, textUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode

        if (this.pref.getString("userid","").equals("")){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            setContentView(R.layout.activity_login);
            initComponents();
            this.editor = pref.edit();
        }else{
            Intent intent = new Intent(getApplication(), Problem.class);
            startActivity(intent);
            finish();
        }

    }

    private void initComponents() {
        this.textEventid = (TextView) this.findViewById(R.id.textEventid);
        this.textSource = (TextView) this.findViewById(R.id.textSource);
        this.textObject = (TextView) this.findViewById(R.id.textObject);
        this.textObjectid = (TextView) this.findViewById(R.id.textObjectid);
        this.textClock = (TextView) this.findViewById(R.id.textClock);
        this.textNs = (TextView) this.findViewById(R.id.textNs);
        this.textEventid = (TextView) this.findViewById(R.id.textREventid);
        this.textRClock = (TextView) this.findViewById(R.id.textRClock);
        this.textRNs = (TextView) this.findViewById(R.id.textRNs);
        this.textCorrelationid = (TextView) this.findViewById(R.id.textCorrelationid);
        this.textUserid = (TextView) this.findViewById(R.id.textUserid);
    }

//    private String cekProblem() throws Exception{
//        String res = cekProblem();
//
//        String auth = "";
//
//        try {
//            JSONRPCClient jsonRpc = new JSONRPCClient();
//
//            jsonRpc.setMethod("problem.get"); //set method name
//
//            //set parameters
//            jsonRpc.addParam("eventid", textEventid.getText().toString());
//            jsonRpc.addParam("sortfields", textClock.getText().toString());
//            jsonRpc.addParam("sortrder", textValue.getText().toString());
//            jsonRpc.addParam("limit", textNs.getText().toString());
//
//            String jsonArray = "[" + jsonRpc.connect() + "]";
//
//            if (jsonArray != null) {
//                String numericInformation_id = null;
//                JSONArray arrayDataFasilitas = new JSONArray(jsonArray);
//                for (int i = 0; i < arrayDataFasilitas.length(); i++) {
//                    JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
//                    JSONObject result = (JSONObject) list_spk.get("result");
//
//                    numericInformation_id = result.getString("sessionid");
//
//                    this.editor.putString("userid", result.getString("userid"));
//                    this.editor.putString("itemids", result.getString("itemids"));
//                    this.editor.putString("sortfields", result.getString("sortfields"));
//                    this.editor.putString("sortorder", result.getString("sortorder"));
//                    this.editor.putString("limit", result.getString("limit"));
//                    this.editor.putString("sessionid", result.getString("sessionid"));
//
//                    editor.commit();
//                }
//                auth = numericInformation_id;
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//        return auth;
//    }
}
