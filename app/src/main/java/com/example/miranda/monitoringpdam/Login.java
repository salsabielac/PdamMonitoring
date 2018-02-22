package com.example.miranda.monitoringpdam;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

        //deklarasi checkbox pada login
        private CheckBox cekRemember;
        //deklarasi Button yang digunakan pada login
        private Button btnLogin;
        //deklarasi textfield username dan password pada login
        private EditText textUsername;
        private EditText textPassword;
        private SharedPreferences pref;
        private SharedPreferences.Editor editor;


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //deklarasi layout awal yang tampil adalah activity_login
            this.pref = getApplicationContext().getSharedPreferences("SessionID", 0); // 0 - for private mode

            if (this.pref.getString("userid","").equals("")){

                setContentView(R.layout.activity_login);
                initComponents();
                this.editor = pref.edit();
            }else{
                Intent intent = new Intent(getApplication(), MonitoringPDAM.class);
                startActivity(intent);
                finish();
            }

            
        }

        private void initComponents() {
            //memanggil button, checkbox, dan textfield pada layout sesuai dengan id yang sudah dideklarasikan agar bisa tampil
            this.cekRemember = (CheckBox) this.findViewById(R.id.cek_remember);
            this.btnLogin = (Button) this.findViewById(R.id.btn_login);
            this.textUsername = (EditText) this.findViewById(R.id.text_username);
            this.textPassword = (EditText) this.findViewById(R.id.text_password);
        }
        //jika button diklik sesudah textfield diisi dengan lengkap
        public void button_onClick(View view) throws Exception {
            //maka proses login berjalan, code ini memanggil fungsi login
            String res = cekLogin();
            
            if (!res.equalsIgnoreCase("") || !res.equalsIgnoreCase("0")){
                Intent intent = new Intent(getApplication(), MonitoringPDAM.class);
                startActivity(intent);
            }
        }

        String cekLogin() throws Exception {
            String auth = "";

            try {
                JSONRPCClient jsonRpc = new JSONRPCClient();

                jsonRpc.setMethod("user.login"); //set method name

                //set parameters
                jsonRpc.addParam("user",  textUsername.getText().toString());
                jsonRpc.addParam("password",  textPassword.getText().toString());
                jsonRpc.addParam("userData",  "true");

                String jsonArray = "["+jsonRpc.connect()+"]";

                if (jsonArray != null) {
                    String numericInformation_id = null;
                    JSONArray arrayDataFasilitas = new JSONArray(jsonArray);
                    for (int i = 0; i < arrayDataFasilitas.length(); i++) {
                        JSONObject list_spk = (JSONObject) arrayDataFasilitas.get(i);
                        JSONObject result = (JSONObject) list_spk.get("result");

                        numericInformation_id = result.getString("sessionid");

                        this.editor.putString("userid", result.getString("userid"));
                        this.editor.putString("name", result.getString("name"));
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
        //menyimpan data dengan memberi tanda centang pada checkbox
        private void checkSavedCredentials() {
            //deklarasi shared preference
            SharedPreferences handler = this.getPreferences(Context.MODE_PRIVATE);
            String username = handler.getString("username", "");
            String password = handler.getString("password", "");
            boolean loginCorrect = this.checkCredentials(username, password);
            if(loginCorrect)
                this.openHome(username);
        }
        //fungsi login yang merupakan proses dari login
        private void login() {
            String username = this.textUsername.getText().toString();
            String password = this.textPassword.getText().toString();
            //mengecek yang diinputkan harus 2 parameter username dan password
            boolean loginCorrect = this.checkCredentials(username, password);
            if(loginCorrect)
            {
                //jika login benar dan checkboxnya dicentang
                boolean remember = this.cekRemember.isChecked();
                if(remember)
                {
                    //maka akan memanggil kembali fungsi saveCredentials
                    this.saveCredentials();
                }
                //menjalankan fungsi openHome
                this.openHome(username);
            }
            //jika proses login salah
            else
            {
                //tampil pemberitahuan meggunakan Toast
                Toast.makeText(this.getApplicationContext(), "Invalid username and/or password!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        //mengecek apakah username dan password sudah valid
        private boolean checkCredentials(String username, String password) {

            if(username.equals("admin1") && password.equals("admin1") || username.equals("admin2") && password.equals("admin2") )
                return true;
            else
                return false;
        }
        private void saveCredentials() {
            SharedPreferences handler = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = handler.edit();
            editor.putString("username", this.textUsername.getText().toString());
            editor.putString("password", this.textPassword.getText().toString());
            editor.commit();
        }
        private void openHome(String username)
        {
            //halaman setelah login yang terbuka adalah MovieActivity
            Intent i = new Intent(this.getApplicationContext(), MonitoringPDAM.class);
            i.putExtra("username", username);
            this.startActivity(i);
        }
    }
