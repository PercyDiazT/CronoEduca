package com.example.cronoeducapage;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import  com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import  com.google.mlkit.nl.translate.TranslatorOptions;

public class Traductor extends AppCompatActivity {
    private EditText editTextLetters;
    private Button btnTranslate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_traductor);
        editTextLetters = findViewById(R.id.editTextTranslate);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextLetters.getText().toString())){
                    Toast.makeText(Traductor.this, "No text allowed", Toast.LENGTH_SHORT).show();
                }else{
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setTargetLanguage("en")
                            .setSourceLanguage("es")
                            .build();
                    Translator translator = Translation.getClient(options);

                    String sourceText = editTextLetters.getText().toString();
                    ProgressDialog progressDialog = new ProgressDialog(Traductor.this);
                    progressDialog.setMessage("Downloading the translation model...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });


                    Task<String> result = translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(Traductor.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Traductor.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

}