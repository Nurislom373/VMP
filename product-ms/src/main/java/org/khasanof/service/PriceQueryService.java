package org.khasanof.service;

import jakarta.persistence.criteria.JoinType;
import org.khasanof.domain.*; // for static metamodels
import org.khasanof.domain.Price;
import org.khasanof.repository.PriceRepository;
import org.khasanof.service.criteria.PriceCriteria;
import org.khasanof.service.dto.PriceDTO;
import org.khasanof.service.mapper.PriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Price} entities in the database.
 * The main input is a {@link PriceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PriceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PriceQueryService extends QueryService<Price> {

    private final Logger log = LoggerFactory.getLogger(PriceQueryService.class);

    private final PriceRepository priceRepository;

    private final PriceMapper priceMapper;

    public PriceQueryService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Return a {@link Page} of {@link PriceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceDTO> findByCriteria(PriceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Price> specification = createSpecification(criteria);
        return priceRepository.findAll(specification, page).map(priceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PriceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Price> specification = createSpecification(criteria);
        return priceRepository.count(specification);
    }

    /**
     * Function to convert {@link PriceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Price> createSpecification(PriceCriteria criteria) {
        Specification<Price> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Price_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Price_.price));
            }
            if (criteria.getIsBase() != null) {
                specification = specification.and(buildSpecification(criteria.getIsBase(), Price_.isBase));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), Price_.sku));
            }
            if (criteria.getVariantsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVariantsId(), root -> root.join(Price_.variants, JoinType.LEFT).get(OptionVariant_.id))
                );
            }
        }
        return specification;
    }
}
