package org.khasanof.service;

import java.util.Optional;
import org.khasanof.service.dto.OptionDTO;

/**
 * Service Interface for managing {@link org.khasanof.domain.Option}.
 */
public interface OptionService {
    /**
     * Save a option.
     *
     * @param optionDTO the entity to save.
     * @return the persisted entity.
     */
    OptionDTO save(OptionDTO optionDTO);

    /**
     * Updates a option.
     *
     * @param optionDTO the entity to update.
     * @return the persisted entity.
     */
    OptionDTO update(OptionDTO optionDTO);

    /**
     * Partially updates a option.
     *
     * @param optionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OptionDTO> partialUpdate(OptionDTO optionDTO);

    /**
     * Get the "id" option.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OptionDTO> findOne(Long id);

    /**
     * Delete the "id" option.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
