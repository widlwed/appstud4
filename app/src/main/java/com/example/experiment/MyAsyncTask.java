package com.example.experiment;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    private DatabaseHelper dbHelper;
    private String lastSong = "";

    public MyAsyncTask(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            while (true) {
                String response = getCurrentSong(urls[0]);
                if (response != null) {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("result").equals("success")) {
                        String info = jsonResponse.getString("info");
                        String[] parts = info.split(" – ");
                        String artist = parts[0];
                        String title = parts[1];

                        if (!lastSong.equals(title)) {
                            dbHelper.addSong(artist, title);
                            lastSong = title;
                        }
                    }
                }
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            Log.e("MyAsyncTask", "Error: ", e);
        }
        return null;
    }


    private String getCurrentSong(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String postData = "login=4707login&password=4707pass";
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            Log.e("MyAsyncTask", "Error fetching song: ", e);
            return null;
        }
    }
}

