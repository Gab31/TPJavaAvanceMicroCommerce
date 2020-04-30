package com.ecommerce.icrocommerce.web.controller;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.ecommerce.icrocommerce.dao.ProductDao;
import com.ecommerce.icrocommerce.model.Product;
import com.ecommerce.icrocommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.icrocommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.Console;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "API pour les opérations CRUD sur les porduits")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    // Récupérer la liste complète des produits
    /*
    @RequestMapping(value = "/produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listeDeNosFiltres);
        return produitsFiltres;
    }*/

    @ApiOperation(value = "Récupérer un produit grace a son id")
    @GetMapping(value = "/produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {
        Product produit = productDao.findById(id);

        if (produit == null){
            throw new ProduitIntrouvableException("Le produit avec l'id "+ id +" est INTROUVABLE.");
        }
        return produit;
    }

    // Récupérer les produits ayant un prix supérieur à la valeur fournit
    @GetMapping(value="/test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit){
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    // Récupérer les produits dont le mot fournit est contenu dans le nom
    @GetMapping(value="/test/produits/recherche/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche){
        return productDao.findByNomLike("%"+recherche+"%");
    }

    // Ajoute un produit
    @PostMapping(value = "/produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded = productDao.save(product);

        if(product.getPrix() == 0) {
            throw new ProduitGratuitException("Tu peux pas faire ça gratuit frère ! Abuse pas !");
        }

        if(productAdded==null){
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Supprime un produit a partir de son id
    @RequestMapping(value = "produits/{id}", method = RequestMethod.DELETE)
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    // Modifie un produit a partir de son id
    @PutMapping(value="/produits")
    public void updateProduit(@RequestBody Product product){
        productDao.save(product);
    }

    // Récupérer les produits ayant un prix supérieur à la valeur fournit
    @GetMapping(value = "/produits/expensive/{prixLimit}")
    public List<Product> findExpensiveProduct(@PathVariable("prixLimit") int prixLimit){
        return productDao.chercherUnProduitCher(prixLimit);
    }

    @GetMapping(value="/produits/adminproduits")
    public Map<String,Integer> calculerMargeProduit() {
        List<Product> produits = productDao.findAll();
        Map<String,Integer> map = new HashMap<>();
        for(Product p: produits) {
            map.put(p.toString(),p.getPrix() - p.getPrixAchat());
        }
        return map;
    }

    @GetMapping(value="/produits/trier")
    public List<Product> trierProduitsParOrdreAlphabetique() {
        return productDao.findByOrderByNomAsc();
    }
}
