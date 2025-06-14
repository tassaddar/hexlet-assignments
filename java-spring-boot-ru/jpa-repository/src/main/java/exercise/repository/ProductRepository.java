package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import exercise.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    @Query("""
    SELECT p FROM Product p
    WHERE (:min IS NULL OR p.price >= :min)
      AND (:max IS NULL OR p.price <= :max)
    ORDER BY p.price ASC
""")
    List<Product> findByPriceRange(@Param("min") Integer min, @Param("max") Integer max);

    // END
}
