package org.khasanof.service;

import jakarta.persistence.criteria.JoinType;
import org.khasanof.domain.*; // for static metamodels
import org.khasanof.domain.OptionVariant;
import org.khasanof.repository.OptionVariantRepository;
import org.khasanof.service.criteria.OptionVariantCriteria;
import org.khasanof.service.dto.OptionVariantDTO;
import org.khasanof.service.mapper.OptionVariantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OptionVariant} entities in the database.
 * The main input is a {@link OptionVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OptionVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionVariantQueryService extends QueryService<OptionVariant> {

    private final Logger log = LoggerFactory.getLogger(OptionVariantQueryService.class);

    private final OptionVariantRepository optionVariantRepository;

    private final OptionVariantMapper optionVariantMapper;

    public OptionVariantQueryService(OptionVariantRepository optionVariantRepository, OptionVariantMapper optionVariantMapper) {
        this.optionVariantRepository = optionVariantRepository;
        this.optionVariantMapper = optionVariantMapper;
    }

    /**
     * Return a {@link Page} of {@link OptionVariantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionVariantDTO> findByCriteria(OptionVariantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OptionVariant> specification = createSpecification(criteria);
        return optionVariantRepository.findAll(specification, page).map(optionVariantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionVariantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OptionVariant> specification = createSpecification(criteria);
        return optionVariantRepository.count(specification);
    }

    /**
     * Function to convert {@link OptionVariantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OptionVariant> createSpecification(OptionVariantCriteria criteria) {
        Specification<OptionVariant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OptionVariant_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OptionVariant_.name));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), OptionVariant_.stock));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OptionVariant_.status));
            }
            if (criteria.getPriceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPriceId(), root -> root.join(OptionVariant_.price, JoinType.LEFT).get(Price_.id))
                );
            }
            if (criteria.getOptionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOptionId(), root -> root.join(OptionVariant_.option, JoinType.LEFT).get(Option_.id))
                );
            }
        }
        return specification;
    }
}
