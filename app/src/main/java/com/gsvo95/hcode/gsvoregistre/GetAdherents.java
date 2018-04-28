package com.gsvo95.hcode.gsvoregistre;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hcode on 25/05/2017.
 */

public class GetAdherents extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String result = "";
        Log.e("web", "Lancement de la requete !");
        try {
            URL url = new URL("http://gsvo95.fr/api_gsvo/visiteur.php");
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null){
                        result += line;
                    }

                }
                in.close();
            } else {
                Log.e("web", "Code : " + code);
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
        //Log.e("web", "Adhérents : " + result);
        writeToFile(result, LoginActivity.context);

        String[] adherents = result.split("@");

        LoginActivity.setDropdownAdherents(adherents);

        Log.e("web", "Nombre d'Adhérents : " + adherents.length);
        super.onPostExecute(result);
    }

    public static void SetCachedAdherents(){
        String[] adherents = readFromFile(LoginActivity.context).split("@");
        LoginActivity.setDropdownAdherents(adherents);
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("adherents.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("adherents.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("err", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("err", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
