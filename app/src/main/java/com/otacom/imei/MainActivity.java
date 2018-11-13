package com.otacom.imei;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.otacom.imei.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Permissoes.permit(this);


        Imei imei = new Imei(this);
        binding.setImei(imei);
        Toast.makeText(this, String.valueOf(imei.getIdentificador()), Toast.LENGTH_LONG).show();


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Permissoes.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
