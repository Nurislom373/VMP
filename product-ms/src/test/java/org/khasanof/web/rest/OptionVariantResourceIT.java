package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.domain.OptionVariantAsserts.*;
import static org.khasanof.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.IntegrationTest;
import org.khasanof.domain.Option;
import org.khasanof.domain.OptionVariant;
import org.khasanof.domain.Price;
import org.khasanof.domain.enumeration.OptionVariantStatus;
import org.khasanof.repository.OptionVariantRepository;
import org.khasanof.service.dto.OptionVariantDTO;
import org.khasanof.service.mapper.OptionVariantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OptionVariantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionVariantResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final OptionVariantStatus DEFAULT_STATUS = OptionVariantStatus.ACTIVE;
    private static final OptionVariantStatus UPDATED_STATUS = OptionVariantStatus.DELETED;

    private static final String ENTITY_API_URL = "/api/option-variants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OptionVariantRepository optionVariantRepository;

    @Autowired
    private OptionVariantMapper optionVariantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionVariantMockMvc;

    private OptionVariant optionVariant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionVariant createEntity(EntityManager em) {
        OptionVariant optionVariant = new OptionVariant().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return optionVariant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionVariant createUpdatedEntity(EntityManager em) {
        OptionVariant optionVariant = new OptionVariant().name(UPDATED_NAME).status(UPDATED_STATUS);
        return optionVariant;
    }

    @BeforeEach
    public void initTest() {
        optionVariant = createEntity(em);
    }

    @Test
    @Transactional
    void createOptionVariant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);
        var returnedOptionVariantDTO = om.readValue(
            restOptionVariantMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(optionVariantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OptionVariantDTO.class
        );

        // Validate the OptionVariant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOptionVariant = optionVariantMapper.toEntity(returnedOptionVariantDTO);
        assertOptionVariantUpdatableFieldsEquals(returnedOptionVariant, getPersistedOptionVariant(returnedOptionVariant));
    }

    @Test
    @Transactional
    void createOptionVariantWithExistingId() throws Exception {
        // Create the OptionVariant with an existing ID
        optionVariant.setId(1L);
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionVariantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        optionVariant.setName(null);

        // Create the OptionVariant, which fails.
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        restOptionVariantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOptionVariants() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getOptionVariant() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get the optionVariant
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL_ID, optionVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(optionVariant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOptionVariantsByIdFiltering() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        Long id = optionVariant.getId();

        defaultOptionVariantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOptionVariantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOptionVariantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where name equals to
        defaultOptionVariantFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where name in
        defaultOptionVariantFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where name is not null
        defaultOptionVariantFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionVariantsByNameContainsSomething() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where name contains
        defaultOptionVariantFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where name does not contain
        defaultOptionVariantFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where status equals to
        defaultOptionVariantFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where status in
        defaultOptionVariantFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOptionVariantsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        // Get all the optionVariantList where status is not null
        defaultOptionVariantFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionVariantsByPriceIsEqualToSomething() throws Exception {
        Price price;
        if (TestUtil.findAll(em, Price.class).isEmpty()) {
            optionVariantRepository.saveAndFlush(optionVariant);
            price = PriceResourceIT.createEntity(em);
        } else {
            price = TestUtil.findAll(em, Price.class).get(0);
        }
        em.persist(price);
        em.flush();
        optionVariant.setPrice(price);
        optionVariantRepository.saveAndFlush(optionVariant);
        Long priceId = price.getId();
        // Get all the optionVariantList where price equals to priceId
        defaultOptionVariantShouldBeFound("priceId.equals=" + priceId);

        // Get all the optionVariantList where price equals to (priceId + 1)
        defaultOptionVariantShouldNotBeFound("priceId.equals=" + (priceId + 1));
    }

    @Test
    @Transactional
    void getAllOptionVariantsByOptionIsEqualToSomething() throws Exception {
        Option option;
        if (TestUtil.findAll(em, Option.class).isEmpty()) {
            optionVariantRepository.saveAndFlush(optionVariant);
            option = OptionResourceIT.createEntity(em);
        } else {
            option = TestUtil.findAll(em, Option.class).get(0);
        }
        em.persist(option);
        em.flush();
        optionVariant.setOption(option);
        optionVariantRepository.saveAndFlush(optionVariant);
        Long optionId = option.getId();
        // Get all the optionVariantList where option equals to optionId
        defaultOptionVariantShouldBeFound("optionId.equals=" + optionId);

        // Get all the optionVariantList where option equals to (optionId + 1)
        defaultOptionVariantShouldNotBeFound("optionId.equals=" + (optionId + 1));
    }

    private void defaultOptionVariantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOptionVariantShouldBeFound(shouldBeFound);
        defaultOptionVariantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionVariantShouldBeFound(String filter) throws Exception {
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionVariantShouldNotBeFound(String filter) throws Exception {
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionVariantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOptionVariant() throws Exception {
        // Get the optionVariant
        restOptionVariantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOptionVariant() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionVariant
        OptionVariant updatedOptionVariant = optionVariantRepository.findById(optionVariant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOptionVariant are not directly saved in db
        em.detach(updatedOptionVariant);
        updatedOptionVariant.name(UPDATED_NAME).status(UPDATED_STATUS);
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(updatedOptionVariant);

        restOptionVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionVariantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isOk());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOptionVariantToMatchAllProperties(updatedOptionVariant);
    }

    @Test
    @Transactional
    void putNonExistingOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionVariantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionVariantWithPatch() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionVariant using partial update
        OptionVariant partialUpdatedOptionVariant = new OptionVariant();
        partialUpdatedOptionVariant.setId(optionVariant.getId());

        partialUpdatedOptionVariant.status(UPDATED_STATUS);

        restOptionVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionVariant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOptionVariant))
            )
            .andExpect(status().isOk());

        // Validate the OptionVariant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionVariantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOptionVariant, optionVariant),
            getPersistedOptionVariant(optionVariant)
        );
    }

    @Test
    @Transactional
    void fullUpdateOptionVariantWithPatch() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionVariant using partial update
        OptionVariant partialUpdatedOptionVariant = new OptionVariant();
        partialUpdatedOptionVariant.setId(optionVariant.getId());

        partialUpdatedOptionVariant.name(UPDATED_NAME).status(UPDATED_STATUS);

        restOptionVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionVariant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOptionVariant))
            )
            .andExpect(status().isOk());

        // Validate the OptionVariant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionVariantUpdatableFieldsEquals(partialUpdatedOptionVariant, getPersistedOptionVariant(partialUpdatedOptionVariant));
    }

    @Test
    @Transactional
    void patchNonExistingOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionVariantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOptionVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionVariant.setId(longCount.incrementAndGet());

        // Create the OptionVariant
        OptionVariantDTO optionVariantDTO = optionVariantMapper.toDto(optionVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionVariantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionVariantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionVariant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOptionVariant() throws Exception {
        // Initialize the database
        optionVariantRepository.saveAndFlush(optionVariant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the optionVariant
        restOptionVariantMockMvc
            .perform(delete(ENTITY_API_URL_ID, optionVariant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return optionVariantRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OptionVariant getPersistedOptionVariant(OptionVariant optionVariant) {
        return optionVariantRepository.findById(optionVariant.getId()).orElseThrow();
    }

    protected void assertPersistedOptionVariantToMatchAllProperties(OptionVariant expectedOptionVariant) {
        assertOptionVariantAllPropertiesEquals(expectedOptionVariant, getPersistedOptionVariant(expectedOptionVariant));
    }

    protected void assertPersistedOptionVariantToMatchUpdatableProperties(OptionVariant expectedOptionVariant) {
        assertOptionVariantAllUpdatablePropertiesEquals(expectedOptionVariant, getPersistedOptionVariant(expectedOptionVariant));
    }
}
