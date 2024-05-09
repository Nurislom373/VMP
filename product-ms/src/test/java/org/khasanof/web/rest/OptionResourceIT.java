package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.domain.OptionAsserts.*;
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
import org.khasanof.domain.Product;
import org.khasanof.domain.enumeration.OptionStatus;
import org.khasanof.repository.OptionRepository;
import org.khasanof.service.dto.OptionDTO;
import org.khasanof.service.mapper.OptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final OptionStatus DEFAULT_STATUS = OptionStatus.ACTIVE;
    private static final OptionStatus UPDATED_STATUS = OptionStatus.DELETED;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return option;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity(EntityManager em) {
        Option option = new Option().name(UPDATED_NAME).status(UPDATED_STATUS);
        return option;
    }

    @BeforeEach
    public void initTest() {
        option = createEntity(em);
    }

    @Test
    @Transactional
    void createOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);
        var returnedOptionDTO = om.readValue(
            restOptionMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OptionDTO.class
        );

        // Validate the Option in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOption = optionMapper.toEntity(returnedOptionDTO);
        assertOptionUpdatableFieldsEquals(returnedOption, getPersistedOption(returnedOption));
    }

    @Test
    @Transactional
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId(1L);
        OptionDTO optionDTO = optionMapper.toDto(option);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        option.setName(null);

        // Create the Option, which fails.
        OptionDTO optionDTO = optionMapper.toDto(option);

        restOptionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOptionsByIdFiltering() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        Long id = option.getId();

        defaultOptionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOptionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOptionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name equals to
        defaultOptionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name in
        defaultOptionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name is not null
        defaultOptionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name contains
        defaultOptionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name does not contain
        defaultOptionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOptionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where status equals to
        defaultOptionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOptionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where status in
        defaultOptionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOptionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where status is not null
        defaultOptionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            optionRepository.saveAndFlush(option);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        option.setProduct(product);
        optionRepository.saveAndFlush(option);
        Long productId = product.getId();
        // Get all the optionList where product equals to productId
        defaultOptionShouldBeFound("productId.equals=" + productId);

        // Get all the optionList where product equals to (productId + 1)
        defaultOptionShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultOptionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOptionShouldBeFound(shouldBeFound);
        defaultOptionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionShouldBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionShouldNotBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption.name(UPDATED_NAME).status(UPDATED_STATUS);
        OptionDTO optionDTO = optionMapper.toDto(updatedOption);

        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOptionToMatchAllProperties(updatedOption);
    }

    @Test
    @Transactional
    void putNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.status(UPDATED_STATUS);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOption, option), getPersistedOption(option));
    }

    @Test
    @Transactional
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.name(UPDATED_NAME).status(UPDATED_STATUS);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(partialUpdatedOption, getPersistedOption(partialUpdatedOption));
    }

    @Test
    @Transactional
    void patchNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the option
        restOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, option.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return optionRepository.count();
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

    protected Option getPersistedOption(Option option) {
        return optionRepository.findById(option.getId()).orElseThrow();
    }

    protected void assertPersistedOptionToMatchAllProperties(Option expectedOption) {
        assertOptionAllPropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }

    protected void assertPersistedOptionToMatchUpdatableProperties(Option expectedOption) {
        assertOptionAllUpdatablePropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }
}
