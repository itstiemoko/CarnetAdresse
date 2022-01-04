package ml.tiemoko.carnetadresse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String VERSION_APP = "1.2";
    private static final int REQUEST_CODE_AJOUT = 1;
    private static final int REQUEST_CODE_MODIFY = 2;
    static MainActivity activity;
    private ListView carnet;
    private ArrayAdapter<Contact> adapter;
    public List<Contact> liste;
    public ContactDaO contactDaO;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.creer:
                Intent creerActivity = new Intent(getApplicationContext(), Contact_Add.class);
                startActivityForResult(creerActivity, REQUEST_CODE_AJOUT);
                return true;
            case R.id.detailsContact:
                Intent contact_details = new Intent(getApplicationContext(), ListContactDetaille.class);
                startActivity(contact_details);
                return true;
            case R.id.help:
                AlertDialog.Builder my_popup = new AlertDialog.Builder(activity);
                my_popup.setTitle(R.string.help);
//                my_popup.setMessage(R.string.helpInfo);
                my_popup.setMessage(getString(R.string.app_name)+" "+getString(R.string.version)+" "+VERSION_APP+"\n"+getString(R.string.helpInfo));
                my_popup.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Merci", Toast.LENGTH_SHORT).show();
                    }
                });
                my_popup.show();
                return true;
            case R.id.myContact:
                Toast.makeText(getApplicationContext(), "Importer les contacts", Toast.LENGTH_SHORT).show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, 1);
                }
                else{
                    getContactFromPhone();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Method to get phone's contact
    private void getContactFromPhone(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String address = "Bamako";
            contactDaO.ajouter(new Contact(5, name, num, address));
        }
        cursor.close();
        //chargerList();
    }

    //Obtenir la sortie de la permission lire contact
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getContactFromPhone();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        carnet = (ListView) findViewById(R.id.carnet);

        chargerList();

        carnet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = liste.get(position);
                Intent detailContactActivity = new Intent(MainActivity.this, ContactDetailactivity.class);
                detailContactActivity.putExtra("contact", c);
                startActivity(detailContactActivity);
            }
        });

        registerForContextMenu(carnet);

        /*carnet.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemSelect = position;
                Intent deleteActivity = new Intent(getApplicationContext(), DeleteContactActivity.class);
                deleteActivity.putExtra("itemSelect", itemSelect);
                startActivityForResult(deleteActivity, 1);
                return true;
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = itemInfo.position;
        switch(item.getItemId()){
            case R.id.delete_item:
                final AlertDialog.Builder my_popup = new AlertDialog.Builder(activity);
                my_popup.setTitle("Suppression");
                my_popup.setMessage(getString(R.string.msg_suppression, adapter.getItem(position).getName()));
                my_popup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    private List<Contact> liste;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        contactDaO.supprimer(adapter.getItem(position));
                        /*chargerList();
                        Toast.makeText(getApplicationContext(), "Supprimer", Toast.LENGTH_SHORT).show();*/
                    }
                });
                my_popup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Suppression annulee", Toast.LENGTH_SHORT).show();
                    }
                });
                my_popup.show();
                break;
            case R.id.modify_item:
                Contact contact = liste.get(position);
                Intent modify_contact = new Intent(getApplicationContext(), Contact_Add.class);
                modify_contact.putExtra("nom", contact);
                startActivityForResult(modify_contact, REQUEST_CODE_MODIFY);
                //startActivity(modify_contact);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
        return  true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_AJOUT){
            chargerList();
        }
    }
    public void chargerList(){
        contactDaO = new ContactDaO(getApplicationContext());
        liste = contactDaO.tousLesContacts();

        //Pour l'affichage d'un seul élément dans une ligne de la liste
        adapter = new ArrayAdapter<Contact>(getApplicationContext(), android.R.layout.simple_list_item_1, liste);
        carnet.setAdapter(adapter);
    }
    public static MainActivity getInstance(){
        return activity;
    }
}
