package org.khasanof.service;

import java.util.Optional;
import org.khasanof.service.dto.GiftDTO;

/**
 * Service Interface for managing {@link org.khasanof.domain.Gift}.
 */
public interface GiftService {
    /**
     * Save a gift.
     *
     * @param giftDTO the entity to save.
     * @return the persisted entity.
     */
    GiftDTO save(GiftDTO giftDTO);

    /**
     * Updates a gift.
     *
     * @param giftDTO the entity to update.
     * @return the persisted entity.
     */
    GiftDTO update(GiftDTO giftDTO);

    /**
     * Partially updates a gift.
     *
     * @param giftDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GiftDTO> partialUpdate(GiftDTO giftDTO);

    /**
     * Get the "id" gift.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GiftDTO> findOne(Long id);

    /**
     * Delete the "id" gift.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
