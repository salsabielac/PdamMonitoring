package com.example.miranda.monitoringpdam.services;

/**
 * Created by Miranda on 08/02/2018.
 */

import com.example.miranda.monitoringpdam.models.History;
import com.example.miranda.monitoringpdam.models.Hasil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HistoryService {
    @GET("tv/top_rated?api_key=c35275da131e2b6f83728a1ae715eaea&language=en-US&page=4")
    Call<Hasil> getResultTv();

    Call<History> createProfil(History itemProfil);
}
