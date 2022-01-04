package ml.tiemoko.carnetadresse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListContactDetaille extends AppCompatActivity {
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact_detaille);

        final ContactDaO contactDaO = new ContactDaO(getApplicationContext());
        final List<Contact> liste = contactDaO.tousLesContacts();

        List<HashMap<String,String>> element = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> el;

        for(Contact l:liste)
        {
            el = new HashMap<String, String>();
            el.put("Nom", l.getName());
            el.put("Numero", l.getNum());
            el.put("Adresse", l.getAdresse());
            element.add(el);
        }

        //Pour l'affichage de plusieurs éléments dans une ligne de la liste (SimpleAdapter)
        adapter = new SimpleAdapter(this, element, android.R.layout.simple_list_item_2, new String[]{"Nom", "Numero"}, new int[]{android.R.id.text1, android.R.id.text2});
        final ListView detaille = (ListView) findViewById(R.id.listDetaille);
        detaille.setAdapter(adapter);

        detaille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = liste.get(position);
                Intent detailContactActivity = new Intent(ListContactDetaille.this, ContactDetailactivity.class);
                detailContactActivity.putExtra("contact", c);
                startActivity(detailContactActivity);
            }
        });

    }
}