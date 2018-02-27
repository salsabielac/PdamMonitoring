package com.example.miranda.monitoringpdam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
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

public class Problem extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String auth;
    private View myView;
    ListView listView;

    ArrayList<HashMap<String, String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        listView = (ListView) findViewById(R.id.listview);
        listData = new ArrayList<HashMap<String, String>>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode
        this.editor = pref.edit();


        auth = this.pref.getString("name","");


        try {
            getProblem();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(arg2);
                String eventid = map.get("eventid");
                String clock = map.get("clock");
                String userid = map.get("userid");
//                String hostids = map.get("hostids");
                Toast.makeText(Problem.this, ""+eventid, Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(Problem.this, MonitoringPDAM.class);
                myIntent.putExtra("eventid", eventid);
                myIntent.putExtra("clock", clock);
                myIntent.putExtra("userid", userid);
//                myIntent.putExtra("hostids", hostids);

                startActivity(myIntent);
            }
        });

    }

    private void loadData(){
        Problem.MyAdapter adapter = new Problem.MyAdapter(
                this, listData,
                R.layout.detail_problem, new String[]{"eventid"},
                new int[]{R.id.textEventId});
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

    void getProblem() throws Exception {
        String auth_new = cekLogin();
        try {
            JSONRPCClient jsonRpc = new JSONRPCClient();

            jsonRpc.setMethod("problem.get"); //set method name

            //set parameters
            jsonRpc.addParam("output",  "extend");
            jsonRpc.addParam("selectAcknowledges",  "extend");
            jsonRpc.addParam("selectTags",  "extend");
//            jsonRpc.addParam("objectids",  "");
            jsonRpc.addParam("recent",  "true");
            jsonRpc.addParam("sortfield",  "eventid");
            jsonRpc.addParam("sortorder",  "desc");

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
                String eventid = list_spk.getString("eventid");
                String clock = list_spk.getString("clock");
                String userid = list_spk.getString("userid");
//                String hostids = list_spk.getString("hostids");


                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eventid", eventid);
                map.put("clock", clock);
                map.put("userid", userid);
//                map.put("hostids", hostids);

                Log.d("HASIL_LOOP", eventid);
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
                v = vi.inflate(R.layout.detail_problem, null);
            }

            TextView textEventId, textClock, textUserid, textHostids;
            textEventId = (TextView) v.findViewById(R.id.textEventId);
            textClock = (TextView) v.findViewById(R.id.textClock);
            textUserid = (TextView) v.findViewById(R.id.textUserid);
//            textHostids = (TextView) v.findViewById(R.id.textHostids);

            final String eventId = results.get(position).get("eventid");
            final String clock = results.get(position).get("clock");
            final String userid = results.get(position).get("userid");
//            final String hostids = results.get(position).get("hostids");


            textEventId.setText(eventId);
            textClock.setText(clock);
            textUserid.setText(userid);
//            textHostids.setText(hostids);

            return v;
        }
    }
}
