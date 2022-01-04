package ml.tiemoko.carnetadresse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class DeleteContactActivity extends AppCompatActivity {
    private ListView carnet;
    private ArrayAdapter<Contact> adapter;
    private List<Contact> liste;
    private ContactDaO contactDaO;
    private int itemSelect;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.suppression, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                AlertDialog.Builder my_popup = new AlertDialog.Builder(this);
                my_popup.setTitle("Suppression multiple");
                my_popup.setMessage("Voulez-vous supprimer ?");
                my_popup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray allItemSelect =  carnet.getCheckedItemPositions();
                        for(int i = 0; i < adapter.getCount(); i++){
                            if(allItemSelect.get(i)) contactDaO.supprimer(adapter.getItem(i));
                        }
                        Toast.makeText(getApplicationContext(), "Supprimer !", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                my_popup.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Annuler", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                my_popup.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        carnet = (ListView) findViewById(R.id.delete_listView);
        contactDaO = new ContactDaO(getApplicationContext());
        liste = contactDaO.tousLesContacts();

        adapter = new ArrayAdapter<Contact>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, liste);
        carnet.setAdapter(adapter);

        itemSelect = (int) getIntent().getSerializableExtra("itemSelect");
        carnet.setItemChecked(itemSelect, true);
    }
}