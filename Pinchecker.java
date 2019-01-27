package com.example.enrebero.ticket;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class Pinchecker extends AppCompatActivity {
    ConnectivityManager connection;
    EditText editText;
    EditText editText2;
    String f26m;
    ProgressDialog progressDialog;
    String f27v;
    View vp;



    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {



        private ReadJSONFeedTask() {
        }

        protected String doInBackground(String... urls) {

             try{   return  readJSONFeed(urls[0]);}
             catch (IOException D)
             {

             }
return null;


        }
        protected void onPostExecute(String result) {
            try {
                progressDialog.cancel();

                if (new JSONObject(result).getString("status").equals("true")) {
                    Intent intent = new Intent(Pinchecker.this, Main2Activity.class);

                     startActivity(intent);
                    return;
                } else {
                    Builder builder = new Builder(Pinchecker.this);
                    builder.setTitle("Ticket");
                    builder.setIcon(R.drawable.tike);
                    builder.setMessage("Sorry, You do not have an account");
                    builder.setPositiveButton("OK", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinchecker);
        this.connection = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        editText = (EditText) findViewById(R.id.editusername);
        editText2 = (EditText) findViewById(R.id.editpin);
    }

    public void login(View view) {
        this.vp = view;
        this.f27v = this.editText.getText().toString();
        this.f26m = this.editText2.getText().toString();
        editText.setText("");
        editText2.setText("");
        Builder builde;
        if (this.f26m.isEmpty() || this.f27v.isEmpty()) {
            builde = new Builder(this.vp.getContext());
            builde.setTitle(" Ticket");
            builde.setIcon(R.drawable.tike);
            builde.setPositiveButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builde.setMessage("User Name or Password is empty\nPlease enter values!");
            builde.create().show();
        } else if (haveNetworkConnection()) {
            this.progressDialog = new ProgressDialog(view.getContext());
            this.progressDialog.setMessage(" Verifying the Account ...");
            this.progressDialog.setProgressStyle(0);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            new ReadJSONFeedTask().execute(new String[]{"https://tikeapp.herokuapp.com/applogin/?username=" + this.f27v + "&password=" + this.f26m});
        } else {
            builde = new Builder(this.vp.getContext());
            builde.setTitle("  Ticket");
            builde.setIcon(R.drawable.tike);
            builde.setPositiveButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builde.setMessage("Check your connection and try again");
            builde.create().show();
        }
    }

    private String readJSONFeed(String URL) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(URL));
            if (response.getStatusLine().getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    stringBuilder.append(line);
                }
            } else {
                Toast.makeText(getBaseContext(), " Failed to download file",Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(getBaseContext(), "In catch place" + e.toString(),Toast.LENGTH_SHORT).show();
        } catch (Exception e2) {
            Toast.makeText(getBaseContext(), "In catch place" + e2.toString(), Toast.LENGTH_SHORT).show();
        }
        return stringBuilder.toString();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        for (NetworkInfo ni : ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                haveConnectedWifi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                haveConnectedMobile = true;
            }
        }
        if (haveConnectedWifi || haveConnectedMobile) {
            return true;
        }
        return false;
    }
}
