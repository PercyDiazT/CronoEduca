package com.example.cronoeducapage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.LayoutInflater;

import java.util.ArrayList;

public class Ver_Cursos extends AppCompatActivity {

    ListView listViewCursos;
    UsuarioSQLiteOpenHelper  myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_cursos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        listViewCursos = findViewById(R.id.listViewCursos);
        myDb = new UsuarioSQLiteOpenHelper(this);

        loadListView(userId);

        listViewCursos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showContextMenu(position,userId);
                return true;
            }
        });
    }
    private void loadListView(int userId) {
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = myDb.getAllCursos(userId);

        if (data.getCount() == 0) {
            listData.add("No hay cursos registrados");
        } else {
            while (data.moveToNext()) {
                listData.add(data.getString(2) + " " + data.getString(3)); // Agrega el nombre y apellido a la lista
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listViewCursos.setAdapter(adapter);
    }

    private void showContextMenu(final int position, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar acción");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Editar
                        showUpdateDialog(position,userId);
                        break;
                    case 1: // Eliminar
                        confirmDeleteDialog(position,userId);
                        break;
                }
            }
        });
        builder.show();
    }


    private void showUpdateDialog(final int position, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_cursos, null);
        builder.setView(dialogView);

        final EditText editTextNombreCurso = dialogView.findViewById(R.id.editTextNombreCursoz);
        final EditText editTextGrado = dialogView.findViewById(R.id.editTextGrupoz);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate1);

        final Cursor data = myDb.getAllCursos(userId);
        data.moveToPosition(position);
        editTextNombreCurso.setText(data.getString(2));
        editTextGrado.setText(data.getString(3));

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = data.getString(0);
                myDb.updateCurso(id, userId, editTextNombreCurso.getText().toString(), editTextGrado.getText().toString());
                loadListView(userId);
                dialog.dismiss();
            }
        });
    }

    private void confirmDeleteDialog(final int position, int userid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Curso");
        builder.setMessage("¿Estás seguro de que deseas eliminar este curso?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor data = myDb.getAllCursos(userid);
                data.moveToPosition(position);
                String id = data.getString(0);
                myDb.deleteCurso(id,userid);
                loadListView(userid);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}