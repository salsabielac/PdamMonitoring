package com.example.miranda.monitoringpdam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringPDAM extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_pdam);
        ListView listView = (ListView) findViewById(R.id.listview);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, countryList, android.R.layout.simple_list_item_1, new String[] {"country"}, new int[] {android.R.id.text1});
        listView.setAdapter(simpleAdapter);

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode
        this.editor = pref.edit();


        auth = this.pref.getString("name","");


        try {
            getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String cekLogin() throws Exception {
        String auth = "";

        try {
            JSONRPCClient jsonRpc = new JSONRPCClient();

            jsonRpc.setMethod("user.login"); //set method name

            //set parameters
            jsonRpc.addParam("user",  "pkl_poltek");
            jsonRpc.addParam("password",  "pkl_poltek");

            String jsonArray = "["+jsonRpc.connect()+"]";

            if (jsonArray != null) {
                String numericInformation_id = null;
                JSONArray arrayDataFasilitas = new JSONArray(jsonArray);

                for (int i = 0; i < arrayDataFasilitas.length(); i++) {
                    JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
                    String result = list_spk.getString("result");

                    numericInformation_id = result;
                }
                auth = numericInformation_id;
            }
        } catch (Exception e) {
            throw e;
        }
        return auth;
    }

    void getHost() throws Exception {
            String auth_new = cekLogin();
            try {
                JSONRPCClient jsonRpc = new JSONRPCClient();

                jsonRpc.setMethod("host.get"); //set method name

                //set parameters
                jsonRpc.addParam("output",  "extend");

                jsonRpc.setAuth(auth_new);
                String jsonObject = "["+jsonRpc.connect()+"]";
                if (jsonObject != null) {
                    String numericInformation_id = null;
                    JSONArray arrayDataFasilitas = new JSONArray(jsonObject);
                    String result = null;
                    initList(jsonObject);
//                    for (int i = 0; i < arrayDataFasilitas.length(); i++) {
//                        JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
//                        result = list_spk.getString("result");
//                        getDataHost(result);
//                    }
//                    Log.d("HOST_OK", result);
                }
            } catch (Exception e) {
                throw e;
            }
    }

    void getDataHost(String jsonObject){
        if (jsonObject != null) {
            JSONArray arrayDataFasilitas = null;
            try {
                arrayDataFasilitas = new JSONArray(jsonObject);
                String hostid, host, name;
                for (int i = 0; i < arrayDataFasilitas.length(); i++) {
                    JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
                    hostid = list_spk.getString("hostid");
                    host = list_spk.getString("host");
                    name = list_spk.getString("name");

                    //Toast.makeText(this, "NAMA : "+hostid, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    List<Map<String,String>> countryList = new ArrayList<Map<String,String>>();
    private void initList(String localjsonString){

        try{
            JSONObject jsonResponse = new JSONObject(localjsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("hostid");
                String number = jsonChildNode.optString("name");
                String outPut = name + "--->" +number;
                countryList.add(createEmployee("result", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String> createEmployee(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private boolean logout() {
        boolean auth = false;
        try {
            JSONRPCClient jsonRpc = new JSONRPCClient();

            jsonRpc.setMethod("user.logout"); //set method name

            //set parameters
            jsonRpc.setAuth(pref.getString("sessionid",""));

            String jsonArray = "["+jsonRpc.connect()+"]";

            if (jsonArray != null) {
                JSONArray arrayDataFasilitas = new JSONArray(jsonArray);
                for (int i = 0; i < arrayDataFasilitas.length(); i++) {
                    JSONObject result = (JSONObject) arrayDataFasilitas.get(i);

                    auth = Boolean.parseBoolean(result.getString("result"));

                    editor.clear();
                    editor.commit();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return auth;
    }
}