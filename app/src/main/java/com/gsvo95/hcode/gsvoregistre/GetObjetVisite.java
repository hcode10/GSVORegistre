package com.gsvo95.hcode.gsvoregistre;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hcode on 25/05/2017.
 */

public class GetObjetVisite extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String result = "";
        Log.e("web", "Lancement de la requete !");
        try {
            URL url = new URL("http://gsvo95.fr/api_gsvo/objet_visite.php");
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

        String[] objet_visite = result.split(",");

        LoginActivity.setObjetVisite(objet_visite);

        super.onPostExecute(result);
    }

    public static void SetCachedObjetVisite(){
        String result = "Bénévolat,Service Civique,Activité Salariale,FLE,Alphabétisation,EPN,Informatique,Stage,Visite,Repair Café,Réparation";
        String[] objet_visite = result.split(",");
        LoginActivity.setObjetVisite(objet_visite);
    }
}
