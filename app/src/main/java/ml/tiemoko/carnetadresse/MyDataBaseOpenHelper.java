package ml.tiemoko.carnetadresse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class MyDataBaseOpenHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "contact.db";;
    private final static int _VERSION = 1;

    public final static String TABLE_NAME = "contact";
    public final static String CONTACT_NAME = "nom";
    public final static String NUMERO = "numero";
    public final static String ADRESSE = "adresse";
    public final static String ID = "idContact";
    public final static Blob photo = null;

    public MyDataBaseOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, _VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "Create Table "+TABLE_NAME+" ("
                +"  idContact integer primary key autoincrement,"
                +"  nom text not null,"
                +"  numero integer not null,"
                +"  adresse text not null"+")";

        db.execSQL(query);
        insertDefault(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSQL = "Drop Table "+TABLE_NAME+"";

        db.execSQL(strSQL);
        this.onCreate(db);
    }

    public void insertDefault(SQLiteDatabase db){
        final String query_1 = "INSERT INTO "+TABLE_NAME+"("+CONTACT_NAME+", "+NUMERO+", "+ADRESSE+") values('Issa Toure', '65436528', 'Bamako coura');";
        final String query_2 = "INSERT INTO "+TABLE_NAME+"("+CONTACT_NAME+", "+NUMERO+", "+ADRESSE+") values('Seydou Diarra', '51354623', 'Hamdallaye')";
        final String query_3 = "INSERT INTO "+TABLE_NAME+"("+CONTACT_NAME+", "+NUMERO+", "+ADRESSE+") values('Madou Toure', '78546528', 'Baco');";
        final String query_4 = "INSERT INTO "+TABLE_NAME+"("+CONTACT_NAME+", "+NUMERO+", "+ADRESSE+") values('Awa Diarra', '63344623', 'Lafiabougou')";
        db.execSQL(query_1);
        db.execSQL(query_2);
        db.execSQL(query_3);
        db.execSQL(query_4);
    }
}
