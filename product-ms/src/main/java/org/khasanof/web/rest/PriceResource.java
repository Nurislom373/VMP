package org.khasanof.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.repository.PriceRepository;
import org.khasanof.service.PriceQueryService;
import org.khasanof.service.PriceService;
import org.khasanof.service.criteria.PriceCriteria;
import org.khasanof.service.dto.PriceDTO;
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
 * REST controller for managing {@link org.khasanof.domain.Price}.
 */
@RestController
@RequestMapping("/api/prices")
public class PriceResource {

    private final Logger log = LoggerFactory.getLogger(PriceResource.class);

    private static final String ENTITY_NAME = "productmsPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceService priceService;

    private final PriceRepository priceRepository;

    private final PriceQueryService priceQueryService;

    public PriceResource(PriceService priceService, PriceRepository priceRepository, PriceQueryService priceQueryService) {
        this.priceService = priceService;
        this.priceRepository = priceRepository;
        this.priceQueryService = priceQueryService;
    }

    /**
     * {@code POST  /prices} : Create a new price.
     *
     * @param priceDTO the priceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceDTO, or with status {@code 400 (Bad Request)} if the price has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PriceDTO> createPrice(@Valid @RequestBody PriceDTO priceDTO) throws URISyntaxException {
        log.debug("REST request to save Price : {}", priceDTO);
        if (priceDTO.getId() != null) {
            throw new BadRequestAlertException("A new price cannot already have an ID", ENTITY_NAME, "idexists");
        }
        priceDTO = priceService.save(priceDTO);
        return ResponseEntity.created(new URI("/api/prices/" + priceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, priceDTO.getId().toString()))
            .body(priceDTO);
    }

    /**
     * {@code PUT  /prices/:id} : Updates an existing price.
     *
     * @param id the id of the priceDTO to save.
     * @param priceDTO the priceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceDTO,
     * or with status {@code 400 (Bad Request)} if the priceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PriceDTO> updatePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceDTO priceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Price : {}, {}", id, priceDTO);
        if (priceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        priceDTO = priceService.update(priceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceDTO.getId().toString()))
            .body(priceDTO);
    }

    /**
     * {@code PATCH  /prices/:id} : Partial updates given fields of an existing price, field will ignore if it is null
     *
     * @param id the id of the priceDTO to save.
     * @param priceDTO the priceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceDTO,
     * or with status {@code 400 (Bad Request)} if the priceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the priceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceDTO> partialUpdatePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceDTO priceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Price partially : {}, {}", id, priceDTO);
        if (priceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceDTO> result = priceService.partialUpdate(priceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prices} : get all the prices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PriceDTO>> getAllPrices(
        PriceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Prices by criteria: {}", criteria);

        Page<PriceDTO> page = priceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prices/count} : count all the prices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPrices(PriceCriteria criteria) {
        log.debug("REST request to count Prices by criteria: {}", criteria);
        return ResponseEntity.ok().body(priceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prices/:id} : get the "id" price.
     *
     * @param id the id of the priceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PriceDTO> getPrice(@PathVariable("id") Long id) {
        log.debug("REST request to get Price : {}", id);
        Optional<PriceDTO> priceDTO = priceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceDTO);
    }

    /**
     * {@code DELETE  /prices/:id} : delete the "id" price.
     *
     * @param id the id of the priceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable("id") Long id) {
        log.debug("REST request to delete Price : {}", id);
        priceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
