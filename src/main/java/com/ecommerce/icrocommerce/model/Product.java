package com.ecommerce.icrocommerce.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;


// @JsonIgnoreProperties(value = {"prixAchat", "id"})
@Entity
//@JsonFilter("monFiltreDynamique")
public class Product {

    @Id
    @GeneratedValue
    private int id;

    @Length(min = 3, max = 20, message = "The 'nom' length must between 3 and 20 characters")
    private String nom;

    @Min(value = 1)
    private int prix;

    // information que nous ne souhaitons pas exposer
    // @JsonIgnore
    private int prixAchat;

    // Constructeur par default
    public Product() {}

    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public String toString(){
        return "Product{"+
                "id=" + id +
                ", nom='"+ nom + '\'' +
                ", prix=" + prix+ '}';
    }
}