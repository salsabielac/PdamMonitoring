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

public class Event extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String auth;
    private View myView;
    ListView listView;

    ArrayList<HashMap<String, String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        listView = (ListView) findViewById(R.id.listview);
        listData = new ArrayList<HashMap<String, String>>();

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode
        this.editor = pref.edit();


        auth = this.pref.getString("name","");


        try {
            getEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(arg2);
                String eventid = map.get("eventid");
                String source = map.get("source");
                String object = map.get("object");
                String clock = map.get("clock");
                Toast.makeText(Event.this, ""+eventid, Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(Event.this, Problem.class);
                myIntent.putExtra("eventid", eventid);
                myIntent.putExtra("souce", source);
                myIntent.putExtra("object", object);
                myIntent.putExtra("clock", clock);
                startActivity(myIntent);
            }
        });
    }

    private void loadData(){
        Event.MyAdapter adapter = new Event.MyAdapter(
                this, listData,
                R.layout.detail_event, new String[]{"eventid"},
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

    void getEvent() throws Exception {
        String auth_new = cekLogin();
        try {
            JSONRPCClient jsonRpc = new JSONRPCClient();

            jsonRpc.setMethod("event.get"); //set method name

            //set parameters
            jsonRpc.addParam("output",  "extend");
            jsonRpc.addParam("select_acknowledges",  "extend");
            jsonRpc.addParam("triggerids",  "");
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
                String source = list_spk.getString("source");
                String object = list_spk.getString("object");
                String clock = list_spk.getString("clock");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eventid", eventid);
                map.put("source", source);
                map.put("object", object);
                map.put("clock", clock);
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
                v = vi.inflate(R.layout.detail_event, null);
            }

            TextView textEventId, textSource, textObject, textClock;
            textEventId = (TextView) v.findViewById(R.id.textEventId);
            textSource = (TextView) v.findViewById(R.id.textSource);
            textObject = (TextView) v.findViewById(R.id.textObject);
            textClock = (TextView) v.findViewById(R.id.textClock);

            final String eventId = results.get(position).get("eventid");
            final String source = results.get(position).get("source");
            final String object = results.get(position).get("object");
            final String clock = results.get(position).get("clock");

            textEventId.setText(eventId);
            textSource.setText(source);
            textObject.setText(object);
            textClock.setText(clock);

            return v;
        }
    }
}
