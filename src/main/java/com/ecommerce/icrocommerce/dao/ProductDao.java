package com.ecommerce.icrocommerce.dao;

import com.ecommerce.icrocommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {
    public List<Product> findAll();
    public Product findById(int id);
    public Product save(Product product);
    public List<Product> findByPrixGreaterThan(int prixLimit);
    public List<Product> findByNomLike(String recherche);

    /*
    // TODO: upgrade spring version
    // delete n'est pas supporté dans cette version de sprint,
    // il faut upgrade de version ou utiliser la solution si dessous.
    // De plus, sprint a besoin de l'annotation @Transactional,
    // car les ressourses sont partagés et l'opération est rejeté
    @Transactional
    @Modifying
    @Query("delete from Product where id = :id")
    */
    public void deleteById(int id);

    @Query("SELECT p FROM Product p WHERE p.prix > :prixLimit")
    public List<Product> chercherUnProduitCher(@Param("prixLimit") int prixLimit);

}
