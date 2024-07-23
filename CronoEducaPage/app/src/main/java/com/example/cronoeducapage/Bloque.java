package com.example.cronoeducapage;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Bloque implements Comparable<Bloque>{
    private int ID;
    private String bloque;
    private int iduser;
    private int fecha;
    private int DayOfWeek;
    private LocalTime desde;
    private LocalTime hasta;
    private String color;
    private String docente;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public Bloque() {
        Calendar calendar = Calendar.getInstance();
        desde = LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        hasta = LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));

    }

    public int getID() {
        return ID;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public int getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getDesdeToString() {
        return desde.format(formatter);
    }
    public LocalTime getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        if (desde != null) {
            this.desde = LocalTime.parse(desde, formatter);
        } else {
            this.desde = null; // O establecer un valor predeterminado si es adecuado
        }
    }

    public String getHastaToString() {
        return hasta.format(formatter);
    }
    public LocalTime getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        if (hasta != null) {
            this.hasta = LocalTime.parse(hasta, formatter);
        } else {
            this.hasta = null; // O establecer un valor predeterminado si es adecuado
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColorID(Context context) {
        int c;
        switch (color) {
            case "Azul":
                c = ResourcesCompat.getColor(context.getResources(), R.color.blue, null);
                break;
            case "Verde":
                c = ResourcesCompat.getColor(context.getResources(), R.color.green, null);
                break;
            case "Rojo":
                c = ResourcesCompat.getColor(context.getResources(), R.color.red, null);
                break;
            case "Amarillo":
                c = ResourcesCompat.getColor(context.getResources(), R.color.yellow, null);
                break;
            case "Naranja":
                c = ResourcesCompat.getColor(context.getResources(), R.color.orange, null);
                break;
            case "Morado":
                c = ResourcesCompat.getColor(context.getResources(), R.color.purple, null);
                break;
            case "Gris":
                c = ResourcesCompat.getColor(context.getResources(), R.color.grey, null);
                break;
            default:
                c = ResourcesCompat.getColor(context.getResources(), R.color.rose, null);
        }
        return c;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public int compareTo(Bloque otroBloque) {
        if (this.desde == null || otroBloque.getDesde() == null) {
            if (this.desde == null) return 1;  // Si 'desde' de este bloque es nulo, va después.
            if (otroBloque.getDesde() == null) return -1;  // Si 'desde' del otro bloque es nulo, va después.
            // Opción 2: Trata los nulos como iguales
            return 0;
        }
        return this.desde.compareTo(otroBloque.getDesde());
    }

}