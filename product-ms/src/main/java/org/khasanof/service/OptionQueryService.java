package org.khasanof.service;

import jakarta.persistence.criteria.JoinType;
import org.khasanof.domain.*; // for static metamodels
import org.khasanof.domain.Option;
import org.khasanof.repository.OptionRepository;
import org.khasanof.service.criteria.OptionCriteria;
import org.khasanof.service.dto.OptionDTO;
import org.khasanof.service.mapper.OptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Option} entities in the database.
 * The main input is a {@link OptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionQueryService extends QueryService<Option> {

    private final Logger log = LoggerFactory.getLogger(OptionQueryService.class);

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public OptionQueryService(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    /**
     * Return a {@link Page} of {@link OptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionDTO> findByCriteria(OptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.findAll(specification, page).map(optionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.count(specification);
    }

    /**
     * Function to convert {@link OptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Option> createSpecification(OptionCriteria criteria) {
        Specification<Option> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Option_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Option_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Option_.status));
            }
            if (criteria.getVariantsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVariantsId(), root -> root.join(Option_.variants, JoinType.LEFT).get(OptionVariant_.id))
                );
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(Option_.product, JoinType.LEFT).get(Product_.id))
                );
            }
        }
        return specification;
    }
}
