package org.khasanof.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.repository.OptionVariantRepository;
import org.khasanof.service.OptionVariantQueryService;
import org.khasanof.service.OptionVariantService;
import org.khasanof.service.criteria.OptionVariantCriteria;
import org.khasanof.service.dto.OptionVariantDTO;
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
 * REST controller for managing {@link org.khasanof.domain.OptionVariant}.
 */
@RestController
@RequestMapping("/api/option-variants")
public class OptionVariantResource {

    private final Logger log = LoggerFactory.getLogger(OptionVariantResource.class);

    private static final String ENTITY_NAME = "productmsOptionVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionVariantService optionVariantService;

    private final OptionVariantRepository optionVariantRepository;

    private final OptionVariantQueryService optionVariantQueryService;

    public OptionVariantResource(
        OptionVariantService optionVariantService,
        OptionVariantRepository optionVariantRepository,
        OptionVariantQueryService optionVariantQueryService
    ) {
        this.optionVariantService = optionVariantService;
        this.optionVariantRepository = optionVariantRepository;
        this.optionVariantQueryService = optionVariantQueryService;
    }

    /**
     * {@code POST  /option-variants} : Create a new optionVariant.
     *
     * @param optionVariantDTO the optionVariantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new optionVariantDTO, or with status {@code 400 (Bad Request)} if the optionVariant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OptionVariantDTO> createOptionVariant(@Valid @RequestBody OptionVariantDTO optionVariantDTO)
        throws URISyntaxException {
        log.debug("REST request to save OptionVariant : {}", optionVariantDTO);
        if (optionVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new optionVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        optionVariantDTO = optionVariantService.save(optionVariantDTO);
        return ResponseEntity.created(new URI("/api/option-variants/" + optionVariantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, optionVariantDTO.getId().toString()))
            .body(optionVariantDTO);
    }

    /**
     * {@code PUT  /option-variants/:id} : Updates an existing optionVariant.
     *
     * @param id the id of the optionVariantDTO to save.
     * @param optionVariantDTO the optionVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionVariantDTO,
     * or with status {@code 400 (Bad Request)} if the optionVariantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the optionVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OptionVariantDTO> updateOptionVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OptionVariantDTO optionVariantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OptionVariant : {}, {}", id, optionVariantDTO);
        if (optionVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        optionVariantDTO = optionVariantService.update(optionVariantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionVariantDTO.getId().toString()))
            .body(optionVariantDTO);
    }

    /**
     * {@code PATCH  /option-variants/:id} : Partial updates given fields of an existing optionVariant, field will ignore if it is null
     *
     * @param id the id of the optionVariantDTO to save.
     * @param optionVariantDTO the optionVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionVariantDTO,
     * or with status {@code 400 (Bad Request)} if the optionVariantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the optionVariantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the optionVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OptionVariantDTO> partialUpdateOptionVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OptionVariantDTO optionVariantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OptionVariant partially : {}, {}", id, optionVariantDTO);
        if (optionVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OptionVariantDTO> result = optionVariantService.partialUpdate(optionVariantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionVariantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /option-variants} : get all the optionVariants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of optionVariants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OptionVariantDTO>> getAllOptionVariants(
        OptionVariantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OptionVariants by criteria: {}", criteria);

        Page<OptionVariantDTO> page = optionVariantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /option-variants/count} : count all the optionVariants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOptionVariants(OptionVariantCriteria criteria) {
        log.debug("REST request to count OptionVariants by criteria: {}", criteria);
        return ResponseEntity.ok().body(optionVariantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /option-variants/:id} : get the "id" optionVariant.
     *
     * @param id the id of the optionVariantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the optionVariantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OptionVariantDTO> getOptionVariant(@PathVariable("id") Long id) {
        log.debug("REST request to get OptionVariant : {}", id);
        Optional<OptionVariantDTO> optionVariantDTO = optionVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionVariantDTO);
    }

    /**
     * {@code DELETE  /option-variants/:id} : delete the "id" optionVariant.
     *
     * @param id the id of the optionVariantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOptionVariant(@PathVariable("id") Long id) {
        log.debug("REST request to delete OptionVariant : {}", id);
        optionVariantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
