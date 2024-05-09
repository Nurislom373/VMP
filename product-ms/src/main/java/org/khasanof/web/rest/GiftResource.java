package org.khasanof.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.repository.GiftRepository;
import org.khasanof.service.GiftQueryService;
import org.khasanof.service.GiftService;
import org.khasanof.service.criteria.GiftCriteria;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.khasanof.domain.Gift}.
 */
@RestController
@RequestMapping("/api/gifts")
public class GiftResource {

    private final Logger log = LoggerFactory.getLogger(GiftResource.class);

    private static final String ENTITY_NAME = "productmsGift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GiftService giftService;

    private final GiftRepository giftRepository;

    private final GiftQueryService giftQueryService;

    public GiftResource(GiftService giftService, GiftRepository giftRepository, GiftQueryService giftQueryService) {
        this.giftService = giftService;
        this.giftRepository = giftRepository;
        this.giftQueryService = giftQueryService;
    }

    /**
     * {@code POST  /gifts} : Create a new gift.
     *
     * @param giftDTO the giftDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new giftDTO, or with status {@code 400 (Bad Request)} if the gift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GiftDTO> createGift(@Valid @RequestBody GiftDTO giftDTO) throws URISyntaxException {
        log.debug("REST request to save Gift : {}", giftDTO);
        if (giftDTO.getId() != null) {
            throw new BadRequestAlertException("A new gift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        giftDTO = giftService.save(giftDTO);
        return ResponseEntity.created(new URI("/api/gifts/" + giftDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, giftDTO.getId().toString()))
            .body(giftDTO);
    }

    /**
     * {@code PUT  /gifts/:id} : Updates an existing gift.
     *
     * @param id the id of the giftDTO to save.
     * @param giftDTO the giftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giftDTO,
     * or with status {@code 400 (Bad Request)} if the giftDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the giftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GiftDTO> updateGift(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GiftDTO giftDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Gift : {}, {}", id, giftDTO);
        if (giftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        giftDTO = giftService.update(giftDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giftDTO.getId().toString()))
            .body(giftDTO);
    }

    /**
     * {@code PATCH  /gifts/:id} : Partial updates given fields of an existing gift, field will ignore if it is null
     *
     * @param id the id of the giftDTO to save.
     * @param giftDTO the giftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giftDTO,
     * or with status {@code 400 (Bad Request)} if the giftDTO is not valid,
     * or with status {@code 404 (Not Found)} if the giftDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the giftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GiftDTO> partialUpdateGift(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GiftDTO giftDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gift partially : {}, {}", id, giftDTO);
        if (giftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GiftDTO> result = giftService.partialUpdate(giftDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giftDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gifts} : get all the gifts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gifts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GiftDTO>> getAllGifts(
        GiftCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Gifts by criteria: {}", criteria);

        Page<GiftDTO> page = giftQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gifts/count} : count all the gifts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGifts(GiftCriteria criteria) {
        log.debug("REST request to count Gifts by criteria: {}", criteria);
        return ResponseEntity.ok().body(giftQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gifts/:id} : get the "id" gift.
     *
     * @param id the id of the giftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the giftDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftDTO> getGift(@PathVariable("id") Long id) {
        log.debug("REST request to get Gift : {}", id);
        Optional<GiftDTO> giftDTO = giftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giftDTO);
    }

    /**
     * {@code DELETE  /gifts/:id} : delete the "id" gift.
     *
     * @param id the id of the giftDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGift(@PathVariable("id") Long id) {
        log.debug("REST request to delete Gift : {}", id);
        giftService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
