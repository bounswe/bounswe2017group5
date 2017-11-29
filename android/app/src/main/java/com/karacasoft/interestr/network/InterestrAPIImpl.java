package com.karacasoft.interestr.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.models.Group;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;

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
import okhttp3.ResponseBody;

/**
 * Created by karacasoft on 01.11.2017.
 */

public class InterestrAPIImpl implements InterestrAPI {

    private Token authToken = new Token();

    private static final String PREF_LOGGED_IN = "com.karacasoft.interestr.logged_in";
    private static final String PREF_TOKEN = "com.karacasoft.interestr.token";

    private SharedPreferences sharedPreferences;

    private static final String TAG = "InterestrAPI";
    private static final String API_HOME = "http://35.177.96.220/api/v1";

    private static final String ENDPOINT_GROUPS = API_HOME + "/groups/";
    private static final String ENDPOINT_LOGIN = API_HOME + "/login/";
    private static final String ENDPOINT_JOIN_LEAVE_GROUP = API_HOME + "/users/groups/";

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_POST = "POST";
    private static final String REQUEST_METHOD_PUT = "PUT";
    private static final String REQUEST_METHOD_PATCH = "PATCH";
    private static final String REQUEST_METHOD_DELETE = "DELETE";

    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client;

    private Context mContext;

    private Handler handler;
    private Handler networkHandler;

    private ArrayList<APIJob> jobQueue = new ArrayList<>();

    public InterestrAPIImpl(Context context) {
        this.mContext = context;

        HandlerThread networkThread = new HandlerThread("NETWORK_THREAD");
        networkThread.start();

        handler = new Handler(Looper.getMainLooper());

        networkHandler = new Handler(networkThread.getLooper());

        sharedPreferences = context.getSharedPreferences("api", Context.MODE_PRIVATE);
        if(sharedPreferences.contains(PREF_LOGGED_IN)) {
            if(sharedPreferences.getBoolean(PREF_LOGGED_IN, false)) {
                authToken = new Token(sharedPreferences.getString(PREF_TOKEN, null));
            }
        }
    }

    private static synchronized OkHttpClient getClient() {
        if(client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    private static Response get(String url, @Nullable String auth_token) throws IOException {

        Request.Builder builder = new Request.Builder()
                .url(url);

        if(auth_token != null)
            builder.addHeader("Authorization", "Token " + auth_token);

        return getClient().newCall(builder.build()).execute();
    }

    private static Response post(String url, JSONObject data, @Nullable String auth_token) throws IOException {
        return request("POST", url, data, auth_token);
    }

    private static Response put(String url, JSONObject data, @Nullable String auth_token) throws IOException {
        return request("PUT", url, data, auth_token);
    }

    private static Response patch(String url, JSONObject data, @Nullable String auth_token) throws IOException {
        return request("PATCH", url, data, auth_token);
    }

    private static Response delete(String url, JSONObject data, @Nullable String auth_token) throws IOException {
        return request("DELETE", url, data, auth_token);
    }

    private static Response request(String method, String url, JSONObject data, @Nullable String auth_token) throws IOException {
        RequestBody body;
        if(data != null) {
            body = RequestBody.create(JSON, data.toString());
        } else {
            body = RequestBody.create(JSON, "");
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .method(method, body);

        if(auth_token != null) {
            builder.addHeader("Authorization", "Token " + auth_token);
        }

        return getClient().newCall(builder.build()).execute();
    }


    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void authenticate(Token token) {
        this.authToken = token;
        sharedPreferences.edit()
                .putBoolean(PREF_LOGGED_IN, true)
                .putString(PREF_TOKEN, token.getKey())
                .apply();
    }

    @Override
    public void logout() {
        sharedPreferences.edit()
                .putBoolean(PREF_LOGGED_IN, false)
                .putString(PREF_TOKEN, null)
                .apply();
    }

    @Override
    public void login(String username, String password, Callback<Token> callback) {
        Log.d("APIImpl login","login is called");
        JSONObject data = new JSONObject();

        try {
            data.put("username", username);
            data.put("password", password);
            Log.d("APIImpl login","login data are put successfully");

        } catch (JSONException e) {
            // This should never happen.
            Log.d("APIImpl login","this just happened, login JSON error");
        }

        APIJob<Token> job = new APIJob<Token>(REQUEST_METHOD_POST, ENDPOINT_LOGIN, data, callback) {
            @Override
            protected Token extractData(String data) {

                Token token = null;
                Log.d("APIImpl login","login extract data");
                Log.d("APIImpl login",data);
                try {
                    JSONObject obj = new JSONObject(data);

                    token = new Token(obj.getString("token"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return token;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    @Override
    public void signup(String username, String email, String pass1, String pass2,Callback<User> callback) {
        //TODO
    }

    @Override
    public void createGroup(Group group, final Callback<Group> callback) {

        JSONObject groupData = new JSONObject();

        try {
            groupData.put("name", group.getName());
            groupData.put("description", group.getDescription());
            groupData.put("location", group.getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        APIJob<Group> job = new APIJob<Group>(REQUEST_METHOD_POST, ENDPOINT_GROUPS, groupData, callback) {
            @Override
            protected Group extractData(String data) {
                Group g = null;

                try {
                    JSONObject obj = new JSONObject(data);

                    g = new Group();

                    g.setId(obj.getInt("id"));
                    g.setName(obj.getString("name"));
                    g.setDescription(obj.getString("description"));
                    g.setMemberCount(obj.getInt("size"));
                    g.setPictureUrl(obj.getString("picture"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return g;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    public void updateGroup(Group group, final Callback<Group> callback) {

        JSONObject groupData = new JSONObject();

        try {
            groupData.put("name", group.getName());
            groupData.put("description", group.getDescription());
            groupData.put("location", group.getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        APIJob<Group> job = new APIJob<Group>(REQUEST_METHOD_PUT, ENDPOINT_GROUPS + group.getId() + "/", groupData, callback) {
            @Override
            protected Group extractData(String data) {
                Group g = null;

                try {
                    JSONObject obj = new JSONObject(data);

                    g = new Group();

                    g.setId(obj.getInt("id"));
                    g.setName(obj.getString("name"));
                    g.setDescription(obj.getString("description"));
                    g.setMemberCount(obj.getInt("size"));
                    g.setPictureUrl(obj.getString("picture"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return g;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    @Override
    public void getGroups(final Callback<ArrayList<Group>> callback) {

        APIJob<ArrayList<Group>> job = new APIJob<ArrayList<Group>>(REQUEST_METHOD_GET, ENDPOINT_GROUPS, null, callback) {
            @Override
            protected ArrayList<Group> extractData(String data) {
                ArrayList<Group> groups = new ArrayList<>();

                try {
                    JSONObject object = new JSONObject(data);

                    JSONArray array = object.getJSONArray("results");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Group g = new Group();
                        g.setId(obj.getInt("id"));
                        g.setName(obj.getString("name"));
                        g.setDescription(obj.getString("description"));
                        g.setMemberCount(obj.getInt("size"));
                        g.setPictureUrl(obj.getString("picture"));
                        groups.add(g);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return groups;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    @Override
    public void getGroupDetail(int group_id, Callback<Group> callback) {

        APIJob<Group> job = new APIJob<Group>(
                REQUEST_METHOD_GET,
                ENDPOINT_GROUPS + group_id + "/",
                null,
                callback) {
            @Override
            protected Group extractData(String data) {
                Group g = null;

                try {
                    JSONObject obj = new JSONObject(data);

                    g = new Group();

                    g.setId(obj.getInt("id"));
                    g.setName(obj.getString("name"));
                    g.setDescription(obj.getString("description"));
                    g.setMemberCount(obj.getInt("size"));
                    g.setPictureUrl(obj.getString("picture"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return g;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    @Override
    public void joinGroup(int group_id, Callback<Boolean> callback) {
        APIJob<Boolean> job = new APIJob<Boolean>(REQUEST_METHOD_PUT,
                ENDPOINT_JOIN_LEAVE_GROUP + group_id + "/",
                null, callback) {
            @Override
            protected Boolean extractData(String data) {
                return true;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }

    @Override
    public void leaveGroup(int group_id, Callback<Boolean> callback) {
        APIJob<Boolean> job = new APIJob<Boolean>(REQUEST_METHOD_DELETE,
                ENDPOINT_JOIN_LEAVE_GROUP + group_id + "/",
                null, callback) {
            @Override
            protected Boolean extractData(String data) {
                return true;
            }
        };

        jobQueue.add(job);
        networkHandler.post(job);
    }



    public abstract class APIJob<T> implements Runnable {

        Callback<T> callback;

        String requestMethod;
        String requestEndpoint;
        JSONObject requestData;

        boolean cancelled = false;

        public APIJob(String requestMethod, String requestEndpoint, JSONObject requestData, Callback<T> callback) {
            this.requestMethod = requestMethod;
            this.requestEndpoint = requestEndpoint;
            this.requestData = requestData;

            this.callback = callback;
        }

        private synchronized void returnCallback(boolean error, T retObj, String errorMessage) {
            if(error) {
                runOnUiThread(() -> callback.onError(errorMessage));
            } else {
                runOnUiThread(() -> callback.onResult(new InterestrAPIResult<>(retObj)));
            }
        }


        private void beforeRequest() {

        }

        private void performRequest() {
            boolean isError = false;
            String errorMessage = "";
            T retObj = null;
            try {
                Response r = null;
                switch (requestMethod) {
                    case REQUEST_METHOD_GET:
                        r = get(requestEndpoint, authToken.getKey());
                        break;
                    case REQUEST_METHOD_POST:
                        r = post(requestEndpoint, requestData, authToken.getKey());
                        break;
                    case REQUEST_METHOD_PUT:
                        r = put(requestEndpoint, requestData, authToken.getKey());
                        break;
                    case REQUEST_METHOD_PATCH:
                        r = patch(requestEndpoint, requestData, authToken.getKey());
                        break;
                    case REQUEST_METHOD_DELETE:
                        r = delete(requestEndpoint, requestData, authToken.getKey());
                        break;
                    default:
                        r = request(requestMethod, requestEndpoint, requestData, authToken.getKey());
                        break;
                }

                if(isCancelled()) {
                    Log.d(TAG, "Job cancelled. Request Endpoint: " + this.requestEndpoint + "\n" +
                            "Request Data: " + this.requestData + "\n" +
                            "Request Method: " + this.requestMethod + "\n");
                    return;
                }

                if(r != null) {
                    if (r.isSuccessful()) {
                        ResponseBody rBody = r.body();
                        if (rBody != null) {
                            retObj = extractData(rBody.string());
                            if (retObj == null) {
                                isError = true;
                                errorMessage = rBody.string();
                            }
                        } else {
                            retObj = null;
                        }
                    } else {
                        isError = true;
                        errorMessage = r.message();
                    }
                }

            } catch (IOException e) {
                isError = true;
                errorMessage = mContext.getString(R.string.network_error);
            }

            if(isCancelled()) {
                Log.d(TAG, "Job cancelled. Request Endpoint: " + this.requestEndpoint + "\n" +
                        "Request Data: " + this.requestData + "\n" +
                        "Request Method: " + this.requestMethod + "\n");
                return;
            }
            returnCallback(isError, retObj, errorMessage);

        }

        private void afterRequest() {
            jobQueue.remove(this);
        }

        @Override
        public void run() {
            beforeRequest();

            Log.d("APIImpl request","Sending request");

            performRequest();

            Log.d("APIImpl request","Sent request");

            afterRequest();
        }

        protected abstract T extractData(String data);

        public synchronized void cancel() {
            cancelled = true;
        }

        public synchronized boolean isCancelled() {
            return cancelled;
        }
    }

}
