package ml.tiemoko.carnetadresse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Contact_Add extends AppCompatActivity {
    private EditText nom_prenom;
    private EditText numero;
    private EditText adresse;
    Contact contact;
    boolean editMode;

    ContactDaO contactDaO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__add);
        setTitle("Edition");
        initEditext();

        contact = (Contact) getIntent().getSerializableExtra("nom");

        if(contact != null){
            editMode = true;
            nom_prenom.setText(contact.getName());
            numero.setText(contact.getNum());
            adresse.setText(contact.getAdresse());
        }
        else{
            contact = new Contact();
            editMode = false;
        }
    }

    private void initEditext() {
        nom_prenom = findViewById(R.id._nom_prenom);
        numero = findViewById(R.id._numero);
        adresse = findViewById(R.id._adresse);
        contactDaO = new ContactDaO(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_contact:
                if(nom_prenom.getText().toString().isEmpty() || numero.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.message_controle, Toast.LENGTH_LONG).show();
                    return true;
                }
                nom_prenom.setText(contact.getName());
                numero.setText(contact.getNum());
                adresse.setText(contact.getAdresse());

                if(editMode)
                    contactDaO._modify(contact);
                else
                    contactDaO.ajouter(contact);

                finish();
                /*contactDaO.ajouter(new Contact(2, nom_prenom.getText().toString(), numero.getText().toString(), adresse.getText().toString()));
                finish();
                Intent listContact = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(listContact);*/
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
