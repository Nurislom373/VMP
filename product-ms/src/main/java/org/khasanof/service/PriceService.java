package org.khasanof.service;

import java.util.Optional;
import org.khasanof.service.dto.PriceDTO;

/**
 * Service Interface for managing {@link org.khasanof.domain.Price}.
 */
public interface PriceService {
    /**
     * Save a price.
     *
     * @param priceDTO the entity to save.
     * @return the persisted entity.
     */
    PriceDTO save(PriceDTO priceDTO);

    /**
     * Updates a price.
     *
     * @param priceDTO the entity to update.
     * @return the persisted entity.
     */
    PriceDTO update(PriceDTO priceDTO);

    /**
     * Partially updates a price.
     *
     * @param priceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PriceDTO> partialUpdate(PriceDTO priceDTO);

    /**
     * Get the "id" price.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PriceDTO> findOne(Long id);

    /**
     * Delete the "id" price.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
