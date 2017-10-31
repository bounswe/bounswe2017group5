package com.karacasoft.interestr.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.karacasoft.interestr.network.models.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by karacasoft on 01.11.2017.
 */

public class InterestrAPIImpl implements InterestrAPI {

    private Context mContext;
    private Handler handler;

    private static final String API_HOME = "http://35.177.77.44";

    private static final String ENDPOINT_GROUPS = API_HOME + "/api/v1/groups/";

    // TODO create a network thread here

    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client;


    public InterestrAPIImpl(Context context) {
        this.mContext = context;
        handler = new Handler(Looper.getMainLooper());
    }

    private static synchronized OkHttpClient getClient() {
        if(client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    private static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return getClient().newCall(request).execute();
    }

    private static Response post(String url, JSONObject data) throws IOException {
        RequestBody body = RequestBody.create(JSON, data.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return getClient().newCall(request).execute();
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void getGroups(final Callback<ArrayList<Group>> callback) {
        // TODO this is a bad idea
        new Thread(() -> {
            try {
                Response r = get(ENDPOINT_GROUPS);

                if(r.isSuccessful()) {
                    String rBody = r.body().string();

                    JSONArray array = new JSONArray(rBody);

                    ArrayList<Group> groups = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Group g = new Group();
                        g.setId(obj.getInt("id"));
                        g.setName(obj.getString("name"));
                        g.setMemberCount(obj.getInt("size"));
                        g.setPictureUrl(obj.getString("picture"));
                        groups.add(g);
                    }

                    runOnUiThread(() -> callback.onResult(new InterestrAPIResult<ArrayList<Group>>(groups)));

                } else {
                    runOnUiThread(() -> callback.onError("Unidentified Error"));

                }

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("Connection Error"));
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("Response Format Error"));
            }
        }).start();
    }

    @Override
    public void getGroupDetail(int group_id, Callback<Group> callback) {
        new Thread(() -> {
            try {
                Response r = get(ENDPOINT_GROUPS + group_id + "/");

                if(r.isSuccessful()) {
                    String rBody = r.body().string();

                    JSONObject obj = new JSONObject(rBody);

                    Group g = new Group();

                    g.setId(obj.getInt("id"));
                    g.setName(obj.getString("name"));
                    g.setMemberCount(obj.getInt("size"));
                    g.setPictureUrl(obj.getString("picture"));

                    runOnUiThread(() -> callback.onResult(new InterestrAPIResult<Group>(g)));

                } else {
                    runOnUiThread(() -> callback.onError("Unidentified Error"));

                }

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("Connection Error"));
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("Response Format Error"));
            }
        }).start();
    }


}
