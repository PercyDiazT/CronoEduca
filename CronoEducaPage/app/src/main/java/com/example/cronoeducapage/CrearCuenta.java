package com.example.cronoeducapage;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta extends AppCompatActivity {

    private EditText etNombre, etEmail, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private EditText passwordEditText;
    private EditText passwordEditText2;
    private UsuarioSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        passwordEditText = findViewById(R.id.PasswordRegistrar);
        passwordEditText2 = findViewById(R.id.PasswordRegistrar2);

        dbHelper = new UsuarioSQLiteOpenHelper(this);
        etNombre = findViewById(R.id.editTextText);
        etEmail = findViewById(R.id.Email);

        btnRegistrar = findViewById(R.id.button);
        btnRegistrar.setOnClickListener(this::registrarUsuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void registrarUsuario(View view) {
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = passwordEditText2.getText().toString();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            Toast.makeText(this, "Por favor, complete los campos correctamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        values.put("password", password);

        long id = db.insert("usuarios", null, values);
        if (id > 0) {
            Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void onClickVer1(View view) {

        // Comprobar si la contraseña está actualmente visible
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Si está oculta, mostrar la contraseña
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Si está visible, ocultar la contraseña
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        // Mover el cursor al final del texto
        passwordEditText.setSelection(passwordEditText.getText().length());

    }

    public void onClickVer2(View view) {

        // Comprobar si la contraseña está actualmente visible
        if (passwordEditText2.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Si está oculta, mostrar la contraseña
            passwordEditText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Si está visible, ocultar la contraseña
            passwordEditText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        // Mover el cursor al final del texto
        passwordEditText2.setSelection(passwordEditText2.getText().length());

    }

    public void onClickIniciarSesion(View view) {
        Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
        startActivity(intent);
    }
}
