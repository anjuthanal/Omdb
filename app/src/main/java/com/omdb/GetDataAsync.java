package com.omdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDataAsync extends AsyncTask<Void, Void, Void> {

    public static final String GET_EPISODES = "getepisodes";
    public static final String GET_EPISODES_URL = "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1";
    public static final String GET_DETAILS = "getdetails";
    private HashMap<String, String> detailMap;

    public static String getGetDetailsUrl(String imdbID) {
        return "http://www.omdbapi.com/?i=" + imdbID + "&plot=short&r=json";
    }


    private Context mContext;
    private String typeOfRequest;
    private ProgressDialog progressDialog;
    private SetOnGetEpisodes setOnGetEpisodes;
    private OkHttpClient client;
    private Gson gson;
    private Request request;
    private String imdbID;
    private GameofThrones gameofThrones;
    private Details details;
    SetOnGetDetails setOnGetDetails;

    public GetDataAsync(Context mContext, String typeOfRequest, SetOnGetEpisodes setOnGetEpisodes) {
        this.mContext = mContext;
        this.typeOfRequest = typeOfRequest;
        this.setOnGetEpisodes = setOnGetEpisodes;
        initRequest();
    }

    public GetDataAsync(Context mContext, String typeOfRequest, SetOnGetDetails setOnGetDetails, String imdbID) {
        this.mContext = mContext;
        this.typeOfRequest = typeOfRequest;
        this.setOnGetDetails = setOnGetDetails;
        this.imdbID = imdbID;
        initRequest();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext, R.style.progressDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void initRequest() {
        gson = new Gson();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS);

        client = builder.build();

        if (typeOfRequest.equalsIgnoreCase(GET_EPISODES)) {
            request = new Request.Builder()
                    .url(GET_EPISODES_URL)
                    .build();
        } else if (typeOfRequest.equalsIgnoreCase(GET_DETAILS)) {
            request = new Request.Builder()
                    .url(getGetDetailsUrl(imdbID))
                    .build();
        }
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            Response response = client.newCall(request).execute();
            if (typeOfRequest.equalsIgnoreCase(GET_EPISODES)) {
                gameofThrones = gson.fromJson(response.body().string(), GameofThrones.class);
            }
            if (typeOfRequest.equalsIgnoreCase(GET_DETAILS)) {
                Type type = new TypeToken<HashMap<String, String>>() {
                }.getType();
                detailMap = gson.fromJson(response.body().string(), type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        if (typeOfRequest.equalsIgnoreCase(GET_EPISODES)) {
            setOnGetEpisodes.onGetEpisodes(gameofThrones);
        }
        if (typeOfRequest.equalsIgnoreCase(GET_DETAILS)) {
            setOnGetDetails.onGetDetails(detailMap);
        }
    }

    /**
     * This interface is used to send episodes to list activity
     */
    public interface SetOnGetEpisodes {
        void onGetEpisodes(GameofThrones gameofThrones);
    }

    /**
     * This interface is used to send details to details activity
     */
    public interface SetOnGetDetails {
        void onGetDetails(HashMap<String, String> details);
    }
}
