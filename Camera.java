package com.example.enrebero.ticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Camera extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zXingScannerView = new ZXingScannerView(this);
        setContentView(this.zXingScannerView);

    }

    protected void onResume() {
        this.zXingScannerView.setResultHandler(this);
        try {
            this.zXingScannerView.startCamera();
            this.zXingScannerView.setActivated(true);
            this.zXingScannerView.setFocusable(true);

        } catch (SecurityException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setMessage("SORRY ENABLE CAMERA IN SETTINGS,CLICK ON PERMISSION  AND ENABLE CAMERA ");
            builder.create().show();
        }
        super.onResume();
    }

    protected void onPause() {
        this.zXingScannerView.stopCamera();

        super.onPause();
    }

    public void handleResult(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(result.getText().toString());
        builder.setTitle("Ticket Scanner Result");
        builder.setIcon(R.drawable.tike);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
