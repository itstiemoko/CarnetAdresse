package ml.tiemoko.carnetadresse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static ml.tiemoko.carnetadresse.MyDataBaseOpenHelper.ADRESSE;
import static ml.tiemoko.carnetadresse.MyDataBaseOpenHelper.CONTACT_NAME;
import static ml.tiemoko.carnetadresse.MyDataBaseOpenHelper.ID;
import static ml.tiemoko.carnetadresse.MyDataBaseOpenHelper.NUMERO;
import static ml.tiemoko.carnetadresse.MyDataBaseOpenHelper.TABLE_NAME;

public class ContactDaO {
    MyDataBaseOpenHelper maBase;
    SQLiteDatabase bd;

    public ContactDaO(Context context){
        maBase = new MyDataBaseOpenHelper(context);
        bd = maBase.getWritableDatabase();
    }

    public List<Contact> tousLesContacts(){
        final String query = "Select * from "+ TABLE_NAME+";";
        Cursor cursor = bd.rawQuery(query, null);

        List<Contact> listContact = new ArrayList<>();

        while(cursor.moveToNext()){
            listContact.add(new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        cursor.close();

        return listContact;
    }

    public void ajouter(Contact contact){
        final String query = "Insert into "+ TABLE_NAME+"("+ CONTACT_NAME+", "+ NUMERO+", "+ ADRESSE+") values('"+contact.getName()+"', '"+contact.getNum()+"', '"+contact.getAdresse()+"')";
        bd.execSQL(query);
    }

    public void supprimer(Contact contact){
        bd.delete(TABLE_NAME, ID+"= ?", new String[]{""+contact.getId()});
    }

    public void _modify(Contact contact){

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName());
        values.put(NUMERO, contact.getNum());
        values.put(ADRESSE, contact.getAdresse());
        bd.update(TABLE_NAME, values, ID+"=?", new String[]{""+contact.getId()});
    }

    //Fonction de rechercher un contact
    public List<Contact> rechercher(String info){
        final List<Contact> searchList = new ArrayList<>();
        String query = "Select "+ID+","+CONTACT_NAME+","+ADRESSE+" From "+TABLE_NAME+" Where "+CONTACT_NAME+" LIKE ? or "+NUMERO+" LIKE ? or "+ADRESSE+" LIKE ?";
        String inf = "%"+info+"%";

        Cursor cursor = bd.rawQuery(query, new String[]{inf, inf, inf});
        while(cursor.moveToNext()){
            searchList.add(new Contact(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        cursor.close();

        return searchList;
    }

    //Methode pour fermer la base
    public void closeBD(){
        if(bd.isOpen())
            bd.close();
    }
}
