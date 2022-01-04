package ml.tiemoko.carnetadresse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailactivity extends AppCompatActivity {
    TextView contactName;
    TextView contactNum;
    TextView contacAdresse;
    ContactDaO contactDaO;
    Contact Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detailactivity);

        contactName = (TextView) findViewById(R.id.contactName);
        contactNum = (TextView) findViewById(R.id.contactNum);
        contacAdresse = (TextView) findViewById(R.id.contactAdress);
        Item = (Contact) getIntent().getSerializableExtra("contact");

        contactName.setText(Item.getName());
        contactNum.setText(Item.getNum());
        contacAdresse.setText(Item.getAdresse());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_DetailsContact:
                contactDaO = new ContactDaO(this);
                contactDaO.supprimer(Item);
                MainActivity.getInstance().finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            case R.id.modify_DetailsContact:
                Intent modify_contact = new Intent(getApplicationContext(), Contact_Add.class);
                modify_contact.putExtra("nom", Item);
                startActivity(modify_contact);
                Toast.makeText(getApplicationContext(), "Modifier", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
