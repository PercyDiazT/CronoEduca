package com.example.cronoeducapage;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private UsuarioSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);
        dbHelper = new UsuarioSQLiteOpenHelper(this);

        etEmail.setText("");
        etPassword.setText("");

    }

    public void onClickMenu(View v) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (dbHelper.usuarioRegistrado(email, password)) {
            int userId = dbHelper.obtenerIdUsuario(email,password);
            Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Credenciales inválidas o usuario no registrado", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
        startActivity(intent);
    }

    public void onClickVer1(View view) {

        // Comprobar si la contraseña está actualmente visible
        if (etPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Si está oculta, mostrar la contraseña
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Si está visible, ocultar la contraseña
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        // Mover el cursor al final del texto
        etPassword.setSelection(etPassword.getText().length());

    }

}
