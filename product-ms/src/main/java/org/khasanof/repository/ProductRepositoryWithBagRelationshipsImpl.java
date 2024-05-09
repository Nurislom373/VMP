package org.khasanof.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.khasanof.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProductRepositoryWithBagRelationshipsImpl implements ProductRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PRODUCTS_PARAMETER = "products";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Product> fetchBagRelationships(Optional<Product> product) {
        return product.map(this::fetchTags).map(this::fetchGifts);
    }

    @Override
    public Page<Product> fetchBagRelationships(Page<Product> products) {
        return new PageImpl<>(fetchBagRelationships(products.getContent()), products.getPageable(), products.getTotalElements());
    }

    @Override
    public List<Product> fetchBagRelationships(List<Product> products) {
        return Optional.of(products).map(this::fetchTags).map(this::fetchGifts).orElse(Collections.emptyList());
    }

    Product fetchTags(Product result) {
        return entityManager
            .createQuery("select product from Product product left join fetch product.tags where product.id = :id", Product.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Product> fetchTags(List<Product> products) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, products.size()).forEach(index -> order.put(products.get(index).getId(), index));
        List<Product> result = entityManager
            .createQuery("select product from Product product left join fetch product.tags where product in :products", Product.class)
            .setParameter(PRODUCTS_PARAMETER, products)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Product fetchGifts(Product result) {
        return entityManager
            .createQuery("select product from Product product left join fetch product.gifts where product.id = :id", Product.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Product> fetchGifts(List<Product> products) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, products.size()).forEach(index -> order.put(products.get(index).getId(), index));
        List<Product> result = entityManager
            .createQuery("select product from Product product left join fetch product.gifts where product in :products", Product.class)
            .setParameter(PRODUCTS_PARAMETER, products)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
