package com.gsvo95.hcode.gsvoregistre;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_APPEND;

/**
 * Created by hcode on 25/05/2017.
 */

public class SetVisite extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String result = "";
        Log.e("web", "Lancement de la requete !");
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                }
                in.close();
            }

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("web", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("web", e.getMessage());
        }

        finally {
            urlConnection.disconnect();
        }
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("web", "Objet Visite : " + result);

        if(result.equals("arriver")){
            Toast.makeText(LoginActivity.context, "Arrivée enregistrer", Toast.LENGTH_LONG).show();
            LoginActivity.savedVisite(true);
        } else if(result.equals("depart")){
            Toast.makeText(LoginActivity.context, "Départ enregistrer", Toast.LENGTH_LONG).show();
            LoginActivity.savedVisite(true);
        } else {
            Toast.makeText(LoginActivity.context, "Visite enregistrer", Toast.LENGTH_LONG).show();
            LoginActivity.savedVisite(false);
        }



        super.onPostExecute(result);
    }
}
