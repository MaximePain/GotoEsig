package fr.esigelec.gotoesig.model;

public class User {
    String Email;
    String Nom;
    String Phone;
    String Prenom;
    String Ville;
    String Image;

    public User(String email, String nom, String phone, String prenom, String ville, String image) {
        Email = email;
        Nom = nom;
        Phone = phone;
        Prenom = prenom;
        Ville = ville;
        Image = image;
    }

    public User(){}

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
