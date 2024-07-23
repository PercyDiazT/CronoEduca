package com.example.cronoeducapage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipal extends AppCompatActivity {
    TextView nombreuser;
    UsuarioSQLiteOpenHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombreuser = findViewById(R.id.NombreUser);

        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        database = new UsuarioSQLiteOpenHelper(this);
        nombreuser.setText(database.getUserNameById(userId));
    }

    public void onClick(View v) {
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        System.out.println("User id Menu ="+userId);

        Intent intent = new Intent(MenuPrincipal.this, Ver_Horario.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
    public void onClickDocentes(View v) {
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        Intent intent = new Intent(MenuPrincipal.this, Interfaz_Docente.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
    public void onClickCerrar(View v) {
        Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickCursos(View v) {
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        Intent intent = new Intent(MenuPrincipal.this, Crear_Curso.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }


    public void onClickTradu(View view) {
        Intent intent = new Intent(MenuPrincipal.this, Traductor.class);
        startActivity(intent);
    }
}