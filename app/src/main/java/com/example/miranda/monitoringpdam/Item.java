package com.example.miranda.monitoringpdam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Item extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String auth;
    private View myView;
    ListView listView;

    ArrayList<HashMap<String, String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_item);

        listView = (ListView) findViewById(R.id.listview);
        listData = new ArrayList<HashMap<String, String>>();

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode
        this.editor = pref.edit();


        auth = this.pref.getString("name","");


        try {
            getItem();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(arg2);
                String itemid = map.get("itemid");
                String name = map.get("name");
                String key_ = map.get("key_");
                String lastvalue = map.get("lastvalue");
                String status = map.get("status");
                Toast.makeText(Item.this, ""+name, Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(Item.this, Event.class);
                myIntent.putExtra("itemid", itemid);
                myIntent.putExtra("name", name);
                myIntent.putExtra("key_", key_);
                myIntent.putExtra("lastvalue", lastvalue);
                myIntent.putExtra("status", status);
                startActivity(myIntent);
            }
        });
    }

    private void loadData(){
        Item.MyAdapter adapter = new Item.MyAdapter(
                this, listData,
                R.layout.activity_history_item, new String[]{"itemid"},
                new int[]{R.id.textItemId});
        listView.setAdapter(adapter);
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

    void getItem() throws Exception {
        String auth_new = cekLogin();
        try {
            JSONRPCClient jsonRpc = new JSONRPCClient();

            jsonRpc.setMethod("item.get"); //set method name

            //set parameters
            jsonRpc.addParam("output",  "extend");
            jsonRpc.addParam("hostids",  "");

            jsonRpc.setAuth(auth_new);
            String jsonObject = "["+jsonRpc.connect()+"]";
            if (jsonObject != null) {

                JSONArray arrayDataFasilitas = new JSONArray(jsonObject);
                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
                    String result = list_spk.getString("result");
                    JSONArray arrayHost = new JSONArray(result);
                    initList(arrayHost);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void initList(JSONArray localjsonString){
        try {
            for (int i = 0; i < localjsonString.length(); i++) {
                JSONObject list_spk = (JSONObject) localjsonString.get(i);
                String itemid = list_spk.getString("itemid");
                String name = list_spk.getString("name");
                String key_ = list_spk.getString("key_");
                String lastvalue = list_spk.getString("lastvalue");
                String status = list_spk.getString("status");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("itemid", itemid);
                map.put("name", name);
                map.put("key_", key_);
                map.put("lastvalue", lastvalue);
                map.put("status", status);

                Log.d("HASIL_LOOP", itemid);
                listData.add(map);
            }

            loadData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    public class MyAdapter extends SimpleAdapter {

        private ArrayList<HashMap<String, String>> results;

        public MyAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.results = data;
        }

        public View getView(int position, View view, ViewGroup parent){
            View v = view;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_history_item, null);
            }

            TextView textItemId, textName, textKey_, textLastvalue, textStatus;
            textItemId = (TextView) v.findViewById(R.id.textItemId);
            textName = (TextView) v.findViewById(R.id.textName);
            textKey_ = (TextView) v.findViewById(R.id.textKey_);
            textLastvalue = (TextView) v.findViewById(R.id.textLastvalue);
            textStatus = (TextView) v.findViewById(R.id.textStatus);

            final String itemId = results.get(position).get("itemid");
            final String name = results.get(position).get("name");
            final String key_ = results.get(position).get("key_");
            final String lastvalue = results.get(position).get("lastvalue");
            final String status = results.get(position).get("status");

            textItemId.setText(itemId);
            textName.setText(name);
            textKey_.setText(key_);
            textLastvalue.setText(lastvalue);
            textStatus.setText(status);

            return v;
        }
    }
}

