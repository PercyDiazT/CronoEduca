package com.example.cronoeducapage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios2.db";
    // Incrementa este número si has modificado la estructura
    private static final String TABLE_NAME = "docentes";
    private static final String COL_2 = "NOMBRE";
    private static final String COL_3 = "APELLIDO";
    private static final String COL_4 = "USERID";

    public UsuarioSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE docentes(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOMBRE TEXT," +
                "APELLIDO TEXT," +
                "USERID INTEGER," +
                "FOREIGN KEY(USERID) REFERENCES usuarios(id))");

        db.execSQL("CREATE TABLE cursos(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USERID INTEGER," +
                "NOMBRE TEXT," +
                "GRUPO TEXT," +
                "FOREIGN KEY(USERID) REFERENCES usuarios(id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void checkTable(int userId, int dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS '"+ userId+dayOfWeek +"'(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "startTime TEXT," +
                "endTime TEXT," +
                "color TEXT" +
                ");");
    }

    public boolean usuarioRegistrado(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE email = ? AND password = ?", new String[]{email, password});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public String getUserNameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;

        Cursor cursor = db.rawQuery("SELECT nombre FROM usuarios WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("nombre");
            if (columnIndex >= 0) {
                userName = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        db.close();
        return userName;
    }




    public int obtenerIdUsuario(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE email = ? AND password = ?", new String[]{email, password});
        int userId = -1; // Valor predeterminado si no se encuentra el usuario
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            if (idColumnIndex != -1) {
                userId = cursor.getInt(idColumnIndex);
            }
        }
        cursor.close();
        db.close();
        return userId;
    }
    public long addBloque(int userId, String name, int dayOfWeek, String startTime, String endTime, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("color", color);
        return db.insert("'"+userId+dayOfWeek+"'", null, values);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Bloque> getBloquesForUserAndDay(int userId, int dayOfWeek) {
        checkTable(userId,dayOfWeek);
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Bloque> bloques = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM '"+userId+dayOfWeek+"';", null);
        if (cursor.moveToFirst()) {
            do {
                Bloque bloque = new Bloque();
                bloque.setID(cursor.getInt(0));
                bloque.setBloque(cursor.getString(1));
                bloque.setDesde(cursor.getString(2));
                bloque.setHasta(cursor.getString(3));
                bloque.setColor(cursor.getString(4));
                bloques.add(bloque);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bloques;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBloque(Bloque bloque, int userId,int dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE '"+userId+dayOfWeek+"' SET " +
                "`name` = '" + bloque.getBloque() + "', " +
                "`startTime` = '" + bloque.getDesdeToString() + "', " +
                "`endTime` = '" + bloque.getHastaToString() + "', " +
                "`color` = '" + bloque.getColor() + "' " +
                "WHERE `id` = " + bloque.getID() + ";";
        db.execSQL(update);
    }
    public void deleteCurso(int id, int userId, int dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM '"+userId+dayOfWeek+"' WHERE `id` = " + id + ";";
        db.execSQL(delete);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getNextID(int userId, int dayOfWeek) {
        ArrayList<Bloque> tasks = getBloquesForUserAndDay(userId, dayOfWeek);
        int id = 0;
        int size = tasks.size();
        if (size != 0) {
            int lastIndex = tasks.size()-1;
            id = tasks.get(lastIndex).getID()+1;
        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String[]> getAllRecordsFromTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String[]> records = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM '" + tableName+"'", null);
            if (cursor.moveToFirst()) {
                int columnCount = cursor.getColumnCount();
                do {
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = cursor.getString(i);
                    }
                    records.add(row);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return records;
    }

    //PARA DOCENTES:

    public boolean insertData(String nombre, String apellido, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nombre);
        contentValues.put(COL_3, apellido);
        contentValues.put(COL_4, userId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE USERID = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateData(String id, String nombre, String apellido, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nombre);
        contentValues.put(COL_3, apellido);
        db.update(TABLE_NAME, contentValues, "ID = ? AND USERID = ?", new String[]{id, String.valueOf(userId)});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id
        });
    }

    public boolean insertCurso(int userId, String nombre, String grupo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERID", userId);
        contentValues.put("NOMBRE", nombre);
        contentValues.put("GRUPO", grupo);
        long result = db.insert("cursos", null, contentValues);
        return result != -1;
    }

    public Cursor getAllCursos(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + "cursos"+" WHERE " + "USERID" + " = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateCurso(String id, int userid, String nombre, String grupo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMBRE", nombre);
        contentValues.put("GRUPO", grupo);
        db.update("cursos", contentValues, "ID = ? AND USERID = ?", new String[]{id, String.valueOf(userid)});
        return true;
    }

    public Integer deleteCurso(String id, int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("cursos", "ID = ? AND USERID = ?", new String[]{id, String.valueOf(userid)});
    }

    public ArrayList<String> getAllCursosArray(int userId) {
        ArrayList<String> cursosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT NOMBRE, GRUPO FROM cursos WHERE USERID = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int nombreIndex = cursor.getColumnIndex("NOMBRE");
            int grupoIndex = cursor.getColumnIndex("GRUPO");
            do {
                String nombre = cursor.getString(nombreIndex);
                String grupo = cursor.getString(grupoIndex);
                String cursoString = nombre + " " + grupo; // Concatenar nombre y grupo
                cursosList.add(cursoString);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cursosList;
    }
    public ArrayList<String> getAllDocentesArray(int userId) {
        ArrayList<String> docentesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT NOMBRE, APELLIDO FROM docentes WHERE USERID = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int nombreIndex = cursor.getColumnIndex("NOMBRE");
            int apellidoIndex = cursor.getColumnIndex("APELLIDO");
            do {
                String nombre = cursor.getString(nombreIndex);
                String apellido = cursor.getString(apellidoIndex);
                String docenteString = nombre + " " + apellido; // Concatenar nombre y apellido
                docentesList.add(docenteString);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return docentesList;
    }



}

