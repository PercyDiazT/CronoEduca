package com.example.cronoeducapage;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Ver_Horario extends AppCompatActivity {
    UsuarioSQLiteOpenHelper database;
    private LocalDate showedDate;
    private ArrayList<Bloque> bloques;
    private final DateTimeFormatter mainDate = DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));
    private TextView date;
    private ListAdapter listAdapter;
    public int userId;
    public int dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_horario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        date = findViewById(R.id.Dia);
        ImageView left = findViewById(R.id.left);
        ImageView right = findViewById(R.id.right);
        ListView listview = findViewById(R.id.listview);
        LinearLayout add = findViewById(R.id.add);

        bloques = new ArrayList<>();
        listAdapter = new ListAdapter();
        database = new UsuarioSQLiteOpenHelper(this);
        listview.setAdapter(listAdapter);

        showedDate = LocalDate.now();
        RefreshData(userId);

        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                showedDate = LocalDate.of(year, month + 1, day);
                RefreshData(userId);
            }, showedDate.getYear(), showedDate.getMonthValue() - 1, showedDate.getDayOfMonth());
            datePickerDialog.show();
        });

        left.setOnClickListener(v -> {
            showedDate = showedDate.minusDays(1);
            RefreshData(userId);
        });

        right.setOnClickListener(v -> {
            showedDate = showedDate.plusDays(1);
            RefreshData(userId);
        });

        add.setOnClickListener(v -> {
            Intent intent2 = new Intent(Ver_Horario.this, Crear_Bloques.class);
            intent2.putExtra("USER_ID", userId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));
            intent2.putExtra("Date", getDayOfWeekValue(showedDate.format(formatter)));
            System.out.println("dato user:"+userId+" datodia"+getDayOfWeekValue(showedDate.format(formatter)));
            startActivity(intent2);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshData(userId);
    }

    private void RefreshData(int userId) {
        date.setText(formatDayOfWeek(showedDate.format(mainDate)));
        int dayValue = getDayOfWeekValue(showedDate.format(mainDate));
        System.out.println(date+"fecha"+dayValue);
        dia = dayValue;
        ArrayList<Bloque> ts = database.getBloquesForUserAndDay(userId, dayValue);
        Collections.sort(ts);
        bloques = ts;
        listAdapter.notifyDataSetChanged();
    }

    private int getDayOfWeekValue(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "lunes":
                return 0;
            case "martes":
                return 1;
            case "miércoles":
                return 2;
            case "jueves":
                return 3;
            case "viernes":
                return 4;
            case "sábado":
                return 5;
            case "domingo":
                return 6;
            default:
                return -1; // Valor por defecto si el día de la semana no es reconocido
        }
    }

    private String formatDayOfWeek(String dayOfWeek) {
        return dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
    }

    public class ListAdapter extends BaseAdapter {
        public ListAdapter() {
        }

        @Override
        public int getCount() {
            return bloques.size();
        }

        @Override
        public Bloque getItem(int i) {
            return bloques.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.activity_bloque, null);

            TextView from = v.findViewById(R.id.from);
            TextView to = v.findViewById(R.id.to);
            TextView task = v.findViewById(R.id.task);

            Bloque t = bloques.get(i);

            from.setText(t.getDesdeToString());
            to.setText(t.getHastaToString());
            task.setText(t.getBloque());

            GradientDrawable backDrawable = (GradientDrawable) task.getBackground();
            backDrawable.setColor(t.getColorID(Ver_Horario.this));

            task.setOnLongClickListener(v2 -> {
                Intent intent = new Intent(Ver_Horario.this, Crear_Bloques.class);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));
                int dia = getDayOfWeekValue(showedDate.format(formatter));
                System.out.println(dia+"ESTE ES EL DIA CUANDO ACTUALIZO");
                intent.putExtra("USER_ID", userId);
                intent.putExtra("Date", dia);
                intent.putExtra("ID", t.getID());
                intent.putExtra("Task", t.getBloque());
                intent.putExtra("From", t.getDesdeToString());
                intent.putExtra("To", t.getHastaToString());
                intent.putExtra("Color", t.getColor());
                startActivity(intent);
                return true;
            });

            return v;
        }
    }
}
