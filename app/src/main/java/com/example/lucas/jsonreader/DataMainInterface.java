package com.example.lucas.jsonreader;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataMainInterface {
    String BASE_URL = "https://s3-sa-east-1.amazonaws.com/pontotel-docs/";

    @GET("data.json")
    Call<JsonObject> loadAllData(); // Método para obter o Json inteiro, que no caso é um JSON Object
}
