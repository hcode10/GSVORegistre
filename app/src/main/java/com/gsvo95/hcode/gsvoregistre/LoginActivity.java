package com.gsvo95.hcode.gsvoregistre;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Random;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private static LinearLayout BackApp;
    private static EditText TextNad;
    private static AutoCompleteTextView TextObjetVisite;
    private static AutoCompleteTextView TextName;
    private static ProgressBar pg_bar;
    public static DrawImageView signature;
    private TextView reset;
    private String nad;
    private ImageView putain;
    private static Button btn_valid;
    public static Context context;
    public byte click = 0;

    String objet_visite[]={
            "Bénévole", "Alphabétisation", "Atelier Participatif"
    };


    boolean etape1 = false;
    byte long_click = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //findViewById(R.id.signature).requestFocus();
        // Set up the login form.
        TextNad = (EditText) findViewById(R.id.nad);
        TextObjetVisite = (AutoCompleteTextView) findViewById(R.id.objet_visite);
        TextName = (AutoCompleteTextView) findViewById(R.id.name);
        reset = (TextView) findViewById(R.id.textView);
        //mLoginFormView = findViewById(R.id.login_form);
        signature = (DrawImageView)findViewById(R.id.signature);
        btn_valid = (Button)findViewById(R.id.btn_valid);
        pg_bar = (ProgressBar)findViewById(R.id.pg_bar);
        putain = (ImageView)findViewById(R.id.putain);
        BackApp = (LinearLayout)findViewById(R.id.backgroundApp);

        TextName.requestFocus();

        context = getApplicationContext();

        hide_pg();
        TextObjetVisite.setAdapter(new ArrayAdapter<String>(this, R.layout.text_custom_view, objet_visite));

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("nad");
            TextObjetVisite.setText(j);
        }

        TextObjetVisite.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                View v = LoginActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });





        TextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                int last_space_index = selection.lastIndexOf(" ");

                String name = selection.substring(0, last_space_index);
                String nad = selection.substring(last_space_index);

                TextName.setText(name.trim());

                TextNad.setText(nad.trim());

                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMinute = rightNow.get(Calendar.MINUTE);


                if(name.equals("Casadio Dimitri")) {
                    TextObjetVisite.setText("Activité Salariale");
                } else if(name.equals("Santais Quentin") || name.equals("Martinez Corentin") || name.equals("Montardy Luca") || name.equals("Salamov Djokhar") || name.equals("Simas Melissa")){
                    reset.setText("Signature : (J'peux pas faire semblant d't'aimer mais j'peux t'haïr sincèrement)");
                    TextObjetVisite.setText("Service Civique");
                    BackApp.setBackgroundColor(Color.RED);
                    if(currentHour <= 10){

                        if(currentHour == 10 && currentMinute <= 30){
                            //Intent intent = new Intent(LoginActivity.this, FullscreenActivity.class);
                            //startActivity(intent);
                        } else if(currentHour < 10){
                            //Intent intent = new Intent(LoginActivity.this, FullscreenActivity.class);
                            //startActivity(intent);
                        }

                    }

                }

                //Toast.makeText(LoginActivity.this, "Name : " + name + " Nad : "+ nad, Toast.LENGTH_LONG).show();
            }
        });



        putain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click >= 5){
                    click = 0;
                    Toast.makeText(LoginActivity.this, "La reponse est ;)", Toast.LENGTH_LONG).show();
                    etape1 = true;
                    //Intent intent = new Intent(LoginActivity.this, FullscreenActivity.class);
                    //startActivity(intent);
                } else if(click > 7) {
                    Toast.makeText(LoginActivity.this, "Nope", Toast.LENGTH_LONG).show();
                    etape1  = false;
                } else {
                    click += 1;
                }

            }

        });


        putain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(long_click > 3 && etape1 == true){
                    etape1 = false;
                    long_click = 0;
                    //Intent intent = new Intent(LoginActivity.this, FullscreenActivity.class);
                    //startActivity(intent);
                } else {
                    long_click++;
                }


                return false;
            }
        });

        btn_valid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click = 0;
                reset.setText("Signature :");
                if(TextName.length() < 2){
                    Toast.makeText(LoginActivity.this, "Veuillez renseigner votre Nom et Prénom !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextObjetVisite.length() < 2){
                    Toast.makeText(LoginActivity.this, "Veuillez renseigner l'objet de votre visite !", Toast.LENGTH_LONG).show();
                    return;
                }
                btn_valid.setEnabled(false);
                btn_valid.setVisibility(View.GONE);
                aff_pg();

                verifyStoragePermissions(LoginActivity.this);

                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut = null;

                int min = 65;
                int max = 999999;

                Random r = new Random();
                int value = r.nextInt(max - min + 1) + min;

                String filename = value + ".png";
                File file = new File(path, filename); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                Log.e("image", "Image path : " + file.getAbsolutePath());
                try {
                    fOut = new FileOutputStream(file);
                    signature.getCanvasBitmap().compress(Bitmap.CompressFormat.PNG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(),file.getName(),file.getName());
                } catch (Exception e) {
                    Log.e("image", e.getMessage());
                    e.printStackTrace();

                }


                String uri = Uri.parse("http://gsvo95.fr/api_gsvo/visite.php")
                        .buildUpon()
                        .appendQueryParameter("nad", TextNad.getText().toString())
                        .appendQueryParameter("objet", TextObjetVisite.getText().toString())
                        .appendQueryParameter("v2", "true")
                        .build().toString();

                Log.e("uri", uri);

                SetVisite save = new SetVisite();
                save.execute(uri);



            }
        });

        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.reset();
            }
        });


        verifyStoragePermissions(this);

        if(ConnexionInternet.isConnectedInternet(LoginActivity.this)){
            GetAdherents test = new GetAdherents();
            test.execute();

            GetObjetVisite Objet = new GetObjetVisite();
            Objet.execute();
        } else {
            GetAdherents.SetCachedAdherents();
            GetObjetVisite.SetCachedObjetVisite();

            Toast.makeText(this, "Vous n'étes pas connecter a internet !", Toast.LENGTH_LONG).show();
        }


    }

    private void ShowAlert(){
        AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmer la signature")
                .setMessage("Veuillez confirmer que vous êtes bien Fidji " + getEmojiByUnicode(0x1f609))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private static void hide_pg(){
        pg_bar.setVisibility(View.GONE);
    }

    private static void aff_pg(){
        pg_bar.setVisibility(View.VISIBLE);
    }



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void setDropdownAdherents(String[] adherents){
        TextName.setAdapter(new ArrayAdapter<String>(context, R.layout.hcode_dropdown_1line, adherents));
    }

    public static void setObjetVisite(String[] objet_visite){
        TextObjetVisite.setAdapter(new ArrayAdapter<String>(context, R.layout.hcode_dropdown_1line, objet_visite));
    }

    public static void savedVisite(boolean success){

        if (!success){
        try {

            Calendar rightNow = Calendar.getInstance();
            String add0 = "";
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);
            if (currentMinute < 10)add0 = "0";
            String ToWrite = new String("N°Adhérent : " + TextNad.getText() + " Heure : " + currentHour + "h" + add0 + currentMinute + "\r\n");
            //Toast.makeText(LoginActivity.context, ToWrite, Toast.LENGTH_LONG).show();


            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "savedSign.txt");
            Toast.makeText(LoginActivity.context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();


            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fOut, StandardCharsets.UTF_8);


            osw.append(ToWrite);


            osw.flush();
            osw.close();


        } catch (IOException ex){
            Log.e("ERRR", "Could not create file", ex);
        }
        }

        BackApp.setBackgroundColor(Color.parseColor("#0099cc"));

        TextNad.setText("");
        TextObjetVisite.setText("");
        TextName.setText("");

        TextName.requestFocus();

        signature.reset();
        btn_valid.setEnabled(true);
        btn_valid.setVisibility(View.VISIBLE);
        hide_pg();
    }

    @Override
    public void onResume(){
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("nad", nad);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        nad = savedInstanceState.getString("nad");

        TextNad.setText(nad);
    }





}