package org.khasanof.service;

import java.util.Optional;
import org.khasanof.service.dto.OptionVariantDTO;

/**
 * Service Interface for managing {@link org.khasanof.domain.OptionVariant}.
 */
public interface OptionVariantService {
    /**
     * Save a optionVariant.
     *
     * @param optionVariantDTO the entity to save.
     * @return the persisted entity.
     */
    OptionVariantDTO save(OptionVariantDTO optionVariantDTO);

    /**
     * Updates a optionVariant.
     *
     * @param optionVariantDTO the entity to update.
     * @return the persisted entity.
     */
    OptionVariantDTO update(OptionVariantDTO optionVariantDTO);

    /**
     * Partially updates a optionVariant.
     *
     * @param optionVariantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OptionVariantDTO> partialUpdate(OptionVariantDTO optionVariantDTO);

    /**
     * Get the "id" optionVariant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OptionVariantDTO> findOne(Long id);

    /**
     * Delete the "id" optionVariant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
