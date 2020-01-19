package com.example.appsense;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkActivity {

    public void send() {
        int a = 3;
    }

    public void sendPost(final String name, final float[] values, final int channel, final String type, final int scale) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    float[] sensor_values = values;

//                    URL url = new URL("http://192.168.1.100:5000/");
                    URL url = new URL("http://192.168.43.217:5001/");
//                      URL url = new URL("http://172.16.12.37:5000/");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("timestamp", 1488873360);
//                    jsonParam.put("uname", message.getUser());
//                    jsonParam.put("message", message.getMessage());
                    jsonParam.put("name", name);
                    jsonParam.put("x", sensor_values[0]);
                    jsonParam.put("y", sensor_values[1]);
                    jsonParam.put("z", sensor_values[2]);
                    jsonParam.put("channel", channel);
                    jsonParam.put("type", type);
                    jsonParam.put("scale", scale);

                    Log.i("JSON", jsonParam.toString());
//                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                    os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//                    os.writeBytes(jsonParam.toString());

//                    os.flush();
//                    os.close();

                    OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                    wr.write(jsonParam.toString());

                    wr.flush();
                    wr.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
