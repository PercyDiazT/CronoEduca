package com.example.cronoeducapage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewDocentesActivity extends AppCompatActivity {

    ListView listViewDocentes;
    UsuarioSQLiteOpenHelper  myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_docentes);

        listViewDocentes = findViewById(R.id.listViewDocentes);
        myDb = new UsuarioSQLiteOpenHelper(this);
        Intent intent2 = getIntent();
        int userId = intent2.getIntExtra("USER_ID", -1);
        System.out.println("VISTA DOCENTES EN EL USERID: "+userId);

        loadListView(userId);

        listViewDocentes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showContextMenu(position,userId);
                return true;
            }
        });
    }

    private void loadListView(int userId) {
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = myDb.getAllData(userId);

        if (data.getCount() == 0) {
            listData.add("No hay docentes registrados");
        } else {
            while (data.moveToNext()) {
                listData.add(data.getString(1) + " " + data.getString(2)); // Agrega el nombre y apellido a la lista
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listViewDocentes.setAdapter(adapter);
    }

    private void showContextMenu(final int position, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar acción");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Editar
                        showUpdateDialog(position, userId);
                        break;
                    case 1: // Eliminar
                        confirmDeleteDialog(position, userId);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showUpdateDialog(final int position, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(dialogView);

        final EditText editTextNombre = dialogView.findViewById(R.id.editTextNombrez);
        final EditText editTextApellido = dialogView.findViewById(R.id.editTextApellidoz);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        final Cursor data = myDb.getAllData(userId);
        data.moveToPosition(position);
        editTextNombre.setText(data.getString(1));
        editTextApellido.setText(data.getString(2));

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = data.getString(0);
                myDb.updateData(id, editTextNombre.getText().toString(), editTextApellido.getText().toString(),userId);
                loadListView(userId);
                dialog.dismiss();
            }
        });
    }

    private void confirmDeleteDialog(final int position, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Docente");
        builder.setMessage("¿Estás seguro de que deseas eliminar este docente?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor data = myDb.getAllData(userId);
                data.moveToPosition(position);
                String id = data.getString(0);
                myDb.deleteData(id);
                loadListView(userId);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
