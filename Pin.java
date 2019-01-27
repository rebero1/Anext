package com.example.enrebero.ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class Pin extends Activity{
    EditText edit;
    ConnectivityManager connection;
    String pin;
    ProgressDialog progressDialog;
    View vl;
    int f21z;



        public void onClicks(View v) {
            Pin.this.vl = v;
            Pin.this.pin = Pin.this.edit.getText().toString();
            if (!Pin.this.haveNetworkConnection() || Pin.this.pin.isEmpty()) {
                AlertDialog.Builder builde = new AlertDialog.Builder(v.getContext());
                builde.setTitle("Ticket");
                builde.setIcon(R.drawable.tike);
                builde.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builde.setMessage("No Network or Pin is empty");
                builde.create().show();
                return;
            }
            Pin.this.progressDialog = new ProgressDialog(v.getContext());
            Pin.this.progressDialog.setMessage("Verifying the Pin ...");
            Pin.this.progressDialog.setProgressStyle(0);
            Pin.this.progressDialog.setCancelable(false);
            Pin.this.progressDialog.show();
            new ReadJSONFeedTask().execute(new String[]{"https://tikeapp.herokuapp.com/result/?pin=" + Pin.this.pin});
        }


    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {



        protected String doInBackground(String... urls) {
            try {
                return readJSONFeed(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(String result) {
            StringBuilder builder = new StringBuilder();
            builder.append("Pin: " + Pin.this.pin + "\n");
            boolean live = false;
            try {
                if (result.contains("owner")) {
                    live = true;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("true")) {
                    builder.append("Status: Not Used\n");
                    builder.append("Owner:" + jsonObject.getString("owner"));
                    builder.append("\nTicket Cost:" + jsonObject.getString("ticket_type"));
                } else if (jsonObject.getString("status").equals("false") && live) {
                    builder.append("Status: Used ");
                    builder.append("\nOwner: " + jsonObject.getString("owner"));
                    builder.append("\nTicket Cost: " + jsonObject.getString("ticket_type"));
                } else if (jsonObject.getString("status").equals("false") && !live) {
                    builder.append("This Ticket is not in Our database");
                }
                AlertDialog.Builder builde = new AlertDialog.Builder(Pin.this.vl.getContext());
                builde.setTitle("Ticket");
                builde.setIcon(R.drawable.tike);
                builde.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builde.setMessage(builder);
                AlertDialog alertDialog = builde.create();
                Pin.this.progressDialog.cancel();
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pin);
            this.connection = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            edit = (EditText) findViewById(R.id.editpin2);

        }

    private String readJSONFeed(String URL) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(URL));
            if (response.getStatusLine().getStatusCode() == ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    stringBuilder.append(line);
                }
            } else {
                Toast.makeText(getBaseContext(), " Failed to download file", Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(getBaseContext(), "In catch place" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e2) {
            Toast.makeText(getBaseContext(), "In catch place" + e2.toString(), Toast.LENGTH_SHORT).show();
        }
        return stringBuilder.toString();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        for (NetworkInfo ni : ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
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
