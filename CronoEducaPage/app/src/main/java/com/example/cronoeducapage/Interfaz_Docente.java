package com.example.cronoeducapage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Interfaz_Docente extends AppCompatActivity {
    UsuarioSQLiteOpenHelper myDb;
    EditText editNombre, editApellido;
    Button btnGuardar, btnVer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_docente);

        myDb = new UsuarioSQLiteOpenHelper(this);

        editNombre = (EditText)findViewById(R.id.editTextNombres);
        editApellido = (EditText)findViewById(R.id.editTextApellidos);
        btnGuardar = (Button)findViewById(R.id.buttonGuardar);
        btnVer = (Button)findViewById(R.id.buttonVerDocentes);
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        System.out.println("AGREGAR DOCENTE EN EL USERID: "+userId);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(editNombre.getText().toString(), editApellido.getText().toString(),userId);
                if(isInserted) {
                    editNombre.setText("");
                    editApellido.setText("");
                    Toast.makeText(Interfaz_Docente.this, "Datos Insertados", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Interfaz_Docente.this, "Ocurrió un error", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Interfaz_Docente.this, ViewDocentesActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

    }

    public void showMessage(String title, String Message){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

