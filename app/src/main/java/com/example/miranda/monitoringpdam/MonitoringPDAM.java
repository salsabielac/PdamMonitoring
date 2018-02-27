package com.example.miranda.monitoringpdam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miranda.monitoringpdam.util.ConstructNavigationViewUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringPDAM extends AppCompatActivity implements ConstructNavigationViewUtil.NavigationViewListener{

    private static String TAG = MonitoringPDAM.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String auth;
    private View myView;
    ListView listView;

    ArrayList<HashMap<String, String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_pdam);

        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navView = (NavigationView)findViewById(R.id.nav_view);
        listView = (ListView) findViewById(R.id.listview);
        listData = new ArrayList<HashMap<String, String>>();

        this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode
        this.editor = pref.edit();

        auth = this.pref.getString("name","");

        try {
            getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ConstructNavigationViewUtil navigationView = new ConstructNavigationViewUtil(navView);
        navigationView.setNavigationViewListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(arg2);
                String hostid = map.get("hostid");
                String host = map.get("host");
                String name = map.get("name");
                Toast.makeText(MonitoringPDAM.this, ""+name, Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(MonitoringPDAM.this, Item.class);
                myIntent.putExtra("hostid", hostid);
                myIntent.putExtra("host", host);
                myIntent.putExtra("name", name);
                startActivity(myIntent);
            }
        });
    }

    private void loadData(){
        MyAdapter adapter = new MyAdapter(
                this, listData,
                R.layout.detail_host, new String[]{"hostid"},
                new int[]{R.id.textHostId});
        listView.setAdapter(adapter);
    }

//    lv.setOnItemClickListener(new OnItemClickListener(){
//        @Override
//        public void onItemClick(AdapterView<?>SimpleAdapter,View myView, int position){
//            ItemClicked item = SimpleAdapter.getItemAtPosition(position);
//
//            Intent intent = new Intent(Activity.this,Item.class);
//            //based on item add info to intent
//            startActivity(intent);
//        }
//    });


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
                String hostid = list_spk.getString("hostid");
                String host = list_spk.getString("host");
                String name = list_spk.getString("name");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("hostid", hostid);
                map.put("host", host);
                map.put("name", name);
                Log.d("HASIL_LOOP", hostid);
                listData.add(map);
            }

            loadData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onItemLongClick(final AdapterView<?> adapter, View v, int pos,
//                                   final long id) {
////tampilkan alert dialog
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_view);
//        dialog.setTitle("Action");
//        dialog.show();
//        final Food b = (Food) getListAdapter().getItem(pos);
//        eventButton = (Button) dialog.findViewById(R.id.button_event);
//        itemButton = (Button) dialog.findViewById(R.id.button_event);
////apabila tombol edit diklik
//        eventButton.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v) {
//// TODO Auto-generated method stub
//                        switchToEvent(b.getId());
//                        dialog.dismiss();
//                    }
//                }
//        );
////apabila tombol delete di klik
//        itemButton.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v) {
//// Delete food
//                        dataSource.deleteFood(b.getId());
//                        dialog.dismiss();
//                        finish();
//                        startActivity(getIntent());
//                    }
//                }
//        );
//        return true;
//    }


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

    @Override
    public void onNavigationViewMenuClicked(MenuItem item) {
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.nav_host:
                drawerLayout.closeDrawers();
                break;

            case R.id.nav_problem:
                startActivity(new Intent(this, Problem.class));
                break;
            case R.id.nav_about:
                //startActivity(new Intent(this, MessagesActivity.class));
                break;
            case R.id.logout:
                //startActivity(new Intent(this, MessagesActivity.class));
                break;
        }
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
                v = vi.inflate(R.layout.detail_host, null);
            }

            TextView textHostId, textHost, textName;
            textHostId = (TextView) v.findViewById(R.id.textHostId);
            textHost = (TextView) v.findViewById(R.id.textHost);
            textName = (TextView) v.findViewById(R.id.textName);

            final String hostId = results.get(position).get("hostid");
            final String host = results.get(position).get("host");
            final String name = results.get(position).get("name");

            textHostId.setText(hostId);
            textHost.setText(host);
            textName.setText(name);

            return v;
        }
    }

}