package com.example.cronoeducapage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Crear_Curso extends AppCompatActivity {
    UsuarioSQLiteOpenHelper myDb;
    EditText editNombreCurso, editGrupo;
    Button btnGuardar, btnVer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_curso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDb = new UsuarioSQLiteOpenHelper(this);
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        editNombreCurso = (EditText)findViewById(R.id.editTextNombreCurso);
        editGrupo = (EditText)findViewById(R.id.editTextGrupo);
        btnGuardar = (Button)findViewById(R.id.buttonGuardar);
        btnVer = (Button)findViewById(R.id.buttonVerCursos);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertCurso(userId,editNombreCurso.getText().toString(), editGrupo.getText().toString());
                if(isInserted) {
                    Toast.makeText(Crear_Curso.this, "Curso Agregado", Toast.LENGTH_LONG).show();
                    editNombreCurso.setText(" ");
                    editGrupo.setText(" ");

                }
                else
                    Toast.makeText(Crear_Curso.this, "Ocurrió un error, curso no agregado", Toast.LENGTH_LONG).show();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Crear_Curso.this, Ver_Cursos.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

    }
}