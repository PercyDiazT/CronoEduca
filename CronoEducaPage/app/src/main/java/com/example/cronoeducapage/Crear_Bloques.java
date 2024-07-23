package com.example.cronoeducapage;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.List;

public class Crear_Bloques extends AppCompatActivity {

    private Bloque bloque;
    Spinner cursoSpinner;
    Spinner docenteSpinner;
    TextView from;
    TextView to;
    Spinner color;
    Button submit;
    TextView delete;
    UsuarioSQLiteOpenHelper database;
    int userId;
    int date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_bloques);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cursoSpinner = findViewById(R.id.task);
        //docenteSpinner = findViewById(R.id.docentes);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        color = findViewById(R.id.color);
        submit = findViewById(R.id.submit);
        delete = findViewById(R.id.delete);


        database = new UsuarioSQLiteOpenHelper(this);
        bloque = new Bloque();

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        date = intent.getIntExtra("Date", -1);
        System.out.println("Crear este es el date:" + date);
        System.out.println("este es el user id:" + userId);
        bloque.setID(database.getNextID(userId, date));
        System.out.println("este es el bloque id: " + bloque.getID());

        String[] colors = {"Rosa", "Azul", "Verde", "Rojo", "Amarillo", "Naranja", "Morado", "Gris"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        color.setAdapter(adapter);

        List<String> cursosList = database.getAllCursosArray(userId);
        ArrayAdapter<String> cursoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cursosList);
        cursoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cursoSpinner.setAdapter(cursoAdapter);
/*
        List<String> docenteList = database.getAllDocentesArray(userId);
        ArrayAdapter<String> docenteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, docenteList);
        docenteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cursoSpinner.setAdapter(docenteAdapter);*/


        if (getIntent().hasExtra("Task")) {
            bloque.setID(getIntent().getIntExtra("ID", 0));
            bloque.setBloque(getIntent().getStringExtra("Task"));
            bloque.setDesde(getIntent().getStringExtra("From"));
            bloque.setHasta(getIntent().getStringExtra("To"));
            bloque.setColor(getIntent().getStringExtra("Color"));
            color.setSelection(adapter.getPosition(bloque.getColor()));
            GradientDrawable background = (GradientDrawable) color.getBackground();
            background.setColor(bloque.getColorID(Crear_Bloques.this));
            userId = getIntent().getIntExtra("USER_ID", -1);
            date = getIntent().getIntExtra("Date", -1);
            //SELECCIONA LA POSICION DEL SPINNER SEGUN LO GUARDADO EN BLOQUE PUEDES USARLO EN DOCENTES
            int position = 0;
            for (int i = 0; i < cursoAdapter.getCount(); i++) {
                if (cursoAdapter.getItem(i).equals(bloque.getBloque())) {
                    position = i;
                    break;
                }
            }
            cursoSpinner.setSelection(position);
            from.setText(bloque.getDesdeToString());
            to.setText(bloque.getHastaToString());

            submit.setOnClickListener(v-> {
                System.out.println(userId +"mas"+ date+"DATOS PAR ACTUALIZAR");

                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Seleccione el tiempo desde", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Seleccione el tiempo hasta", Toast.LENGTH_SHORT).show();
                    return;
                }
                bloque.setBloque(cursoSpinner.getSelectedItem().toString());
                database.updateBloque(bloque, userId, date);
                mostrartabla(userId+""+date+"");
                Toast.makeText(this, "Bloque actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deleteCurso(bloque.getID(),userId, date);
                Toast.makeText(this, "Bloque eliminado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            submit.setOnClickListener(v-> {
                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Seleccione el tiempo desde", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Seleccione el tiempo hasta", Toast.LENGTH_SHORT).show();
                    return;
                }
                bloque.setBloque(cursoSpinner.getSelectedItem().toString());
                bloque.setDocente(cursoSpinner.getSelectedItem().toString());
                database.addBloque(userId, bloque.getBloque(),date,bloque.getDesdeToString(),bloque.getHastaToString(),bloque.getColor());
                Toast.makeText(this, "Bloque agregado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setVisibility(View.GONE);

        }


        from.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                from.setText(ho+":"+min);
                bloque.setDesde(ho+":"+min);
            }, bloque.getDesde().getHour(), bloque.getDesde().getMinute(), true);
            timePickerDialog.show();
        });

        to.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                to.setText(ho+":"+min);
                bloque.setHasta(ho+":"+min);
            }, bloque.getHasta().getHour(), bloque.getHasta().getMinute(), true);
            timePickerDialog.show();
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloque.setColor(color.getSelectedItem().toString());
                GradientDrawable background = (GradientDrawable) color.getBackground();
                background.setColor(bloque.getColorID(Crear_Bloques.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("USER_ID", userId);
        outState.putInt("Date", date);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userId = savedInstanceState.getInt("USER_ID", -1);
        date = savedInstanceState.getInt("Date", -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mostrartabla(String NombreTabla){
        List<String[]> usuarios = database.getAllRecordsFromTable(NombreTabla);
        for (String[] record : usuarios) {
            for (String field : record) {
                System.out.print(field + " ");
            }
            System.out.println();
        }

    }

}