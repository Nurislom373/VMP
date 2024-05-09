package org.khasanof.service;

import java.util.Optional;
import org.khasanof.service.dto.TagDTO;

/**
 * Service Interface for managing {@link org.khasanof.domain.Tag}.
 */
public interface TagService {
    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save.
     * @return the persisted entity.
     */
    TagDTO save(TagDTO tagDTO);

    /**
     * Updates a tag.
     *
     * @param tagDTO the entity to update.
     * @return the persisted entity.
     */
    TagDTO update(TagDTO tagDTO);

    /**
     * Partially updates a tag.
     *
     * @param tagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagDTO> partialUpdate(TagDTO tagDTO);

    /**
     * Get the "id" tag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagDTO> findOne(Long id);

    /**
     * Delete the "id" tag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
