package com.motionishealth.application.training.android.Utils;

import android.util.Log;
import android.util.Patterns;

public class Utils {
    /**
     * Método para comprobar si el email es válido o no.
     * @param email email a comprobar
     * @param TAG RAG para el log
     * @return boolean
     */
    public static boolean checkEmail(String email, String TAG) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.i(TAG, "Correo válido");
            return true;
        } else {
            Log.w(TAG, "Correo no válido");
            return false;
        }
    }

    /**
     * Método para comprobar si dos contraseñas son válidas.
     * Son válidas si tienen mas de 6 carácteres y son iguales.
     * @param password Contraseña
     * @param repeatedPassword Contraseña repetida
     * @param TAG Tag para Log
     * @return boolean
     */
    public static boolean checkPasswords(String password, String repeatedPassword, String TAG) {
        if (password.trim().length() < 6 || repeatedPassword.trim().length() < 6) {
            Log.w(TAG, "Contraseña menor de 6 caracteres");
            return false;
        }
        if (!password.equals(repeatedPassword)) {
            Log.w(TAG, "Contraseñas distintas");
            return false;
        }
        Log.i(TAG, "Contraseñas válidas");
        return true;
    }
}
