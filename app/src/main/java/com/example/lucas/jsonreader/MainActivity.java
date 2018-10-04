package com.example.lucas.jsonreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);

        arrayList = new ArrayList();
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

//CHAMADA DO OBJETO PARSEJSON PARA LISTAR OS DADOS UTILIZANDO APENAS RECURSOS DO PRÓPRIO ANDROID, SEM NENHUMA BIBLIOTECA ADICIONAL
//        ParseJson pj = new ParseJson();
//        pj.execute();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataMainInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataMainInterface dataMainInterface = retrofit.create(DataMainInterface.class);


        Call<JsonObject> call = dataMainInterface.loadAllData();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"ERROR: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    JsonObject jsonObject = response.body();

                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");

                    for (int i = 0; i < jsonArray.size(); i++){
                        DataList d = new DataList();
                        JsonObject js_data = (JsonObject) jsonArray.get(i);

                        d.id = js_data.get("id").toString();
                        d.name = js_data.get("name").toString();
                        d.pwd = js_data.get("pwd").toString();

                        arrayList.add ("ID: " + d.id
                                + "\nNAME: "+d.name
                                + "\nPWD: "+d.pwd);
                        lv.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                Toast.makeText(MainActivity.this,"FAILURE ON RESPONSE: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

        /*MÉTODO UTILIZADO PARA OBTER OS DADOS SEM NENHUMA BIBLIOTECA ADICIONAL, APENAS COM RECURSOS DO PRÓPRIO ANDROID.*/
//        public class ParseJson extends AsyncTask<String, String, String> {
//
//            @Override
//            protected String doInBackground(String... strings) {
//                String data = "";
//
//                try {
//                    URL url = new URL("https://s3-sa-east-1.amazonaws.com/pontotel-docs/data.json");
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    InputStream inputStream = httpURLConnection.getInputStream();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    String line = "";
//
//                    while (line != null) {
//                        line = bufferedReader.readLine();
//                        data += line;
//                    }
//
//                    JSONObject jo = new JSONObject(data);
//                    JSONArray ja = jo.getJSONArray("data");
//
//                    for (int i = 0; i < ja.length(); i++) {
//
//                        JSONObject jo_parsed = (JSONObject) ja.get(i);
//                        id = jo_parsed.getString("id");
//                        name = jo_parsed.getString("name");
//                        pwd = jo_parsed.getString("pwd");
//                        arrayList.add("ID: " + id +
//                                "\nNAME: " + name +
//                                "\nPWD: " + pwd);
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                lv.setAdapter(adapter);
//            }
//        }

    }



