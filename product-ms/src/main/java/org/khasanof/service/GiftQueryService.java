package org.khasanof.service;

import jakarta.persistence.criteria.JoinType;
import org.khasanof.domain.*; // for static metamodels
import org.khasanof.domain.Gift;
import org.khasanof.repository.GiftRepository;
import org.khasanof.service.criteria.GiftCriteria;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.service.mapper.GiftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Gift} entities in the database.
 * The main input is a {@link GiftCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GiftDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiftQueryService extends QueryService<Gift> {

    private final Logger log = LoggerFactory.getLogger(GiftQueryService.class);

    private final GiftRepository giftRepository;

    private final GiftMapper giftMapper;

    public GiftQueryService(GiftRepository giftRepository, GiftMapper giftMapper) {
        this.giftRepository = giftRepository;
        this.giftMapper = giftMapper;
    }

    /**
     * Return a {@link Page} of {@link GiftDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GiftDTO> findByCriteria(GiftCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gift> specification = createSpecification(criteria);
        return giftRepository.findAll(specification, page).map(giftMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GiftCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gift> specification = createSpecification(criteria);
        return giftRepository.count(specification);
    }

    /**
     * Function to convert {@link GiftCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gift> createSpecification(GiftCriteria criteria) {
        Specification<Gift> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gift_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Gift_.name));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), Gift_.visibility));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), Gift_.level));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Gift_.stock));
            }
            if (criteria.getUnlimitedStock() != null) {
                specification = specification.and(buildSpecification(criteria.getUnlimitedStock(), Gift_.unlimitedStock));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Gift_.status));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(Gift_.products, JoinType.LEFT).get(Product_.id))
                );
            }
        }
        return specification;
    }
}
