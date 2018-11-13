package com.otacom.imei;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Permissoes {

    private static final String TAG = "PERMISSIONS_ALL CLASS";

    private static final String[] PERMISSIONS_ALL = new String[]{
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET
    };

    private static ArrayList<String> permissionsRejected = new ArrayList<>();

    // The request code used in ActivityCompat.requestPermissions()
    // and returned in the Activity's onRequestPermissionsResult()
    public static final int REQUEST_CODE_PERMISSION_ALL = 101;
    public static final int REQUEST_CODE_ONLY_ONE_PERMISSION = 100;

    private Activity activity;

    /**
     * Método STATIC para ser chamada sem a necessidade de instanciar o objeto
     * <p>
     * Activity que chamar este método deve implementar onRequestPermissionsResult
     * como a
     *
     * @param activity Activity context
     */
    public static void permit(Activity activity) {
        Permissoes p = new Permissoes(activity);
        p.getPermissoes();
    }

    private Permissoes(@NonNull Activity activity) {
        this.activity = activity;
    }

    private void getPermissoes() {
        Log.i(TAG, "GET PERMISSIONS_ALL");


        AsyncTask.execute(() -> {
            while (permissoesRejeitadas()) {                                // Checa se todas estão liberadas
                solicitaCadaPermissao(
                        permissionsRejected.toArray(new String[0])          // Checa cada Permissão
                );
                //solicitaCadaPermissao(permissionsRejected.toArray(new String[permissionsRejected.size()]));
            }
        });
    }

    private boolean permissoesRejeitadas() {

        boolean rejeitouAlguma = true;
        permissionsRejected.clear();

        // Checa todas as Permissions
        for (String permission : PERMISSIONS_ALL) {

            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, permission + " ----------- NOT GRANTED ");
                permissionsRejected.add(permission);
            } else {
                Log.i(TAG, permission + " -------------- OK");
            }
        }

        // Se rejeitadas vazio -> todas estão liberadas
        if (permissionsRejected.isEmpty())
            rejeitouAlguma = false;

        return rejeitouAlguma;
    }

    private void solicitaCadaPermissao(String... permissions) {
        Log.i(TAG, "SOLICITA CADA PERMISSAO");

        for (String permission : permissions) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Log.i(TAG, permission + " HAS BEEN RATIONALE REQUESTED");
                // Show an explanation to the user *asynchronously* --
                // don't block this thread waiting for the user's response!

                // After the user sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setMessage("Permita este acesso para usar o aplicativo!")
                        .setPositiveButton("Allow", (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE_PERMISSION_ALL))
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();

            } else { // No explanation needed; request the permission
                Log.i(TAG, permission + " HAS BEEN REQUESTED");
                /** @param REQUEST_CODE_PERMISSION_ALL is an app-defined int constant.
                 *         The callback method gets the result of the request. */

                //ActivityCompat.requestPermissions(_activity, PERMISSIONS_ALL, REQUEST_CODE_PERMISSION_ALL);
                // Vamos pedir uma permissão de cada vez
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE_PERMISSION_ALL);
            }


        }
    }

    /**
     * Solicita todas as permissões rejeitadas de uma só vez, resultado será obtivo na Activity por
     * meio do método /////////////// onRequestPermissionsResult /////////////////////////
     *
     * @param permissions
     */
    private void solicitaPermissoes(String... permissions) {

        // No explanation needed; request the permission
        /** @param REQUEST_CODE_PERMISSION_ALL is an app-defined int constant.
         *         The callback method gets the result of the request. */

        //ActivityCompat.requestPermissions(_activity, PERMISSIONS_ALL, REQUEST_CODE_PERMISSION_ALL);
        // Vamos pedir uma permissão de cada vez
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_ALL);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        String description = "\n\nON PERMISSIONS_ALL RESULT" +
                "\nRequest Code = " + requestCode +
                "\nPermissions = " + Arrays.toString(permissions) +
                "\nGrant Results = " + Arrays.toString(grantResults) +
                "\n";

        switch (requestCode) {
            case Permissoes.REQUEST_CODE_PERMISSION_ALL:
                Log.i(TAG, "Request Result: All Permissions Granted");
                //Toast.makeText(permissions., "All Permissions Granted", Toast.LENGTH_LONG).show();
                break;

            case Permissoes.REQUEST_CODE_ONLY_ONE_PERMISSION:
                Log.i(TAG, "Permissions Granted => " + Arrays.toString(permissions));
                //Toast.makeText(activity, "Permissions Granted => " + Arrays.toString(permissions), Toast.LENGTH_LONG).show();
                break;

            default:
                Log.i(TAG, "Request Code != ALL");
                //Toast.makeText(activity, "Request Code != ALL", Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, description);
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSION_ALL:
//                for (String perms : PERMISSIONS_ALL) {
//                    if (!hasPermission(perms)) {
//                        permissionsRejected.add(perms);
//                    }
//                }
//
//                if (permissionsRejected.size() > 0) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                            String msg = "These permissions are mandatory for the application. Please allow access.";
//                            showMessageOKCancel(msg,
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions(permissionsRejected.toArray(
//                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
//                                            }
//                                        }
//                                    });
//                            return;
//                        }
//                    }
//                } else {
//                    Toast.makeText(context, "Permissions garanted.", Toast.LENGTH_LONG).show();
//                    launchCamera();
//                }
//                break;
//        }
//    }
//
//    if(shouldShowRequestPermissionRationale(permissions[i])){
//        new AlertDialog.Builder(this)
//                .setMessage("Your error message here")
//                .setPositiveButton("Allow", (dialog, which) -> requestMultiplePermissions())
//                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//                .create()
//                .show();
//    }
//return;
}
