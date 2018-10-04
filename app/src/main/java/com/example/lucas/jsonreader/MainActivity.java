package com.example.lucas.jsonreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

        /*CRIAÇÃO DO OBJETO RETROFIT, RESPONSÁVEL POR MONTAR A URI E CONSTRUIR O JSON QUANDO FOR CHAMADO*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataMainInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /*CRIAÇÃO DE INTERFACE PARA UTILIZAÇÃO DO OBJETO RETROFIT CRIADO ACIMA PARA CHAMAR A URI*/
        DataMainInterface dataMainInterface = retrofit.create(DataMainInterface.class);

        /*CRIAÇÃO DO JSON OBJECT ATRAVÉS DA INTERFACE CRIADA ACIMA*/
        Call<JsonObject> call = dataMainInterface.loadAllData();

        /*REALIZANDO O CALL DO OBJETO SOLICITADO*/
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){

                    /*CASO TENHA CONEXÃO MAS NÃO TENHA NENHUMA RESPOSTA OBTIDA*/
                    Toast.makeText(MainActivity.this,"ERROR: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    /*CRIAÇÃO DO JSONOBJECT UTILIZANDO A BIBLIOTECA GSON PARA OBTER O CORPO DA RESPONSE*/
                    JsonObject jsonObject = response.body();

                    /*CRIAÇÃO DO ARRAY QUE CONTÉM OS DADOS FINAIS DESEJADOS, LOCALIZADO PELA TAG "data"*/
                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");

                    /*LOOP PARA PERCORRER O ARRAY CRIADO E EXIBIR NA LISTVIEW EM TEMPO DE EXECUÇÃO*/
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

            /*CASO HAJA FALHAS NA TENTATIVA DE CALL*/
            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                Toast.makeText(MainActivity.this,"FAILURE ON RESPONSE: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}



