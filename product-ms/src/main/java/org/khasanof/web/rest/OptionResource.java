package org.khasanof.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.repository.OptionRepository;
import org.khasanof.service.OptionQueryService;
import org.khasanof.service.OptionService;
import org.khasanof.service.criteria.OptionCriteria;
import org.khasanof.service.dto.OptionDTO;
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
 * REST controller for managing {@link org.khasanof.domain.Option}.
 */
@RestController
@RequestMapping("/api/options")
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);

    private static final String ENTITY_NAME = "productmsOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionService optionService;

    private final OptionRepository optionRepository;

    private final OptionQueryService optionQueryService;

    public OptionResource(OptionService optionService, OptionRepository optionRepository, OptionQueryService optionQueryService) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.optionQueryService = optionQueryService;
    }

    /**
     * {@code POST  /options} : Create a new option.
     *
     * @param optionDTO the optionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new optionDTO, or with status {@code 400 (Bad Request)} if the option has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OptionDTO> createOption(@Valid @RequestBody OptionDTO optionDTO) throws URISyntaxException {
        log.debug("REST request to save Option : {}", optionDTO);
        if (optionDTO.getId() != null) {
            throw new BadRequestAlertException("A new option cannot already have an ID", ENTITY_NAME, "idexists");
        }
        optionDTO = optionService.save(optionDTO);
        return ResponseEntity.created(new URI("/api/options/" + optionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, optionDTO.getId().toString()))
            .body(optionDTO);
    }

    /**
     * {@code PUT  /options/:id} : Updates an existing option.
     *
     * @param id the id of the optionDTO to save.
     * @param optionDTO the optionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionDTO,
     * or with status {@code 400 (Bad Request)} if the optionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the optionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OptionDTO> updateOption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OptionDTO optionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Option : {}, {}", id, optionDTO);
        if (optionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        optionDTO = optionService.update(optionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionDTO.getId().toString()))
            .body(optionDTO);
    }

    /**
     * {@code PATCH  /options/:id} : Partial updates given fields of an existing option, field will ignore if it is null
     *
     * @param id the id of the optionDTO to save.
     * @param optionDTO the optionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionDTO,
     * or with status {@code 400 (Bad Request)} if the optionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the optionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the optionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OptionDTO> partialUpdateOption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OptionDTO optionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Option partially : {}, {}", id, optionDTO);
        if (optionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OptionDTO> result = optionService.partialUpdate(optionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /options} : get all the options.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of options in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OptionDTO>> getAllOptions(
        OptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Options by criteria: {}", criteria);

        Page<OptionDTO> page = optionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /options/count} : count all the options.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOptions(OptionCriteria criteria) {
        log.debug("REST request to count Options by criteria: {}", criteria);
        return ResponseEntity.ok().body(optionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /options/:id} : get the "id" option.
     *
     * @param id the id of the optionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the optionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OptionDTO> getOption(@PathVariable("id") Long id) {
        log.debug("REST request to get Option : {}", id);
        Optional<OptionDTO> optionDTO = optionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionDTO);
    }

    /**
     * {@code DELETE  /options/:id} : delete the "id" option.
     *
     * @param id the id of the optionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable("id") Long id) {
        log.debug("REST request to delete Option : {}", id);
        optionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
