package org.khasanof.service;

import jakarta.persistence.criteria.JoinType;
import org.khasanof.domain.*; // for static metamodels
import org.khasanof.domain.Product;
import org.khasanof.repository.ProductRepository;
import org.khasanof.service.criteria.ProductCriteria;
import org.khasanof.service.dto.ProductDTO;
import org.khasanof.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.fetchBagRelationships(productRepository.findAll(specification, page)).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), Product_.visibility));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), Product_.level));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Product_.stock));
            }
            if (criteria.getUnlimitedStock() != null) {
                specification = specification.and(buildSpecification(criteria.getUnlimitedStock(), Product_.unlimitedStock));
            }
            if (criteria.getHasOptions() != null) {
                specification = specification.and(buildSpecification(criteria.getHasOptions(), Product_.hasOptions));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Product_.status));
            }
            if (criteria.getOptionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOptionId(), root -> root.join(Product_.options, JoinType.LEFT).get(Option_.id))
                );
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(Product_.images, JoinType.LEFT).get(Image_.id))
                );
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTagId(), root -> root.join(Product_.tags, JoinType.LEFT).get(Tag_.id))
                );
            }
            if (criteria.getGiftId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGiftId(), root -> root.join(Product_.gifts, JoinType.LEFT).get(Gift_.id))
                );
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(Product_.category, JoinType.LEFT).get(Category_.id))
                );
            }
        }
        return specification;
    }
}
