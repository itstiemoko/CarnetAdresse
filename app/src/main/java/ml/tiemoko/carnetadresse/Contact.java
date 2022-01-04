package ml.tiemoko.carnetadresse;

import java.io.Serializable;

public class Contact implements Serializable {
    //Paramètre du contact
    private long id;
    private String name;
    private String adresse;
    private String num;

    //Constructor
    public Contact(long id, String nom, String numero, String adresse)
    {
        this.id = id;
        this.name = nom;
        this.adresse = adresse;
        this.num = numero;
    }

    public Contact() {

    }

    //Les getters et les setters
    public long getId() { return this.id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return this.name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() { return this.adresse; }

    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getNum() { return this.num; }

    public void setNum(String num) { this.num = num; }

    //Cette méthode retourne le nom du contact
    public String toString(){ return this.name; }
}
