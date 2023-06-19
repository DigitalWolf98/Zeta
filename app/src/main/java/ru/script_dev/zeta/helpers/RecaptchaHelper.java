package ru.script_dev.zeta.helpers;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.safetynet.SafetyNetClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RecaptchaHelper {

    private final String PUBLIC_KEY = "6LcqcOUkAAAAAD3Qadq4Kvgt8LBir9mq-3LXvPEU";
    private final String SECRET_KEY = "6LcqcOUkAAAAAPfXO6MKBPRRPSbS3MwisGqXb31G";
    private final GoogleSignInClient googleSignInClient;
    private final SafetyNetClient safetyNetClient;

    public RecaptchaHelper(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);

        safetyNetClient = SafetyNet.getClient(context);
    }

    public boolean verifyRecaptcha() throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        safetyNetClient.verifyWithRecaptcha(PUBLIC_KEY)
        .addOnSuccessListener((SafetyNetApi.RecaptchaTokenResponse response) -> {
            future.complete(true);
        })
        .addOnFailureListener((Exception error) -> {
            future.complete(false);
        })
        .addOnCanceledListener(() -> {
            future.complete(false);
        });

        return future.get();
    }
}