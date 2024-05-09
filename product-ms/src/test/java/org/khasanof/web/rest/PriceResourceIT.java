package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.domain.PriceAsserts.*;
import static org.khasanof.web.rest.TestUtil.createUpdateProxyForBean;
import static org.khasanof.web.rest.TestUtil.sameNumber;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.IntegrationTest;
import org.khasanof.domain.Price;
import org.khasanof.repository.PriceRepository;
import org.khasanof.service.dto.PriceDTO;
import org.khasanof.service.mapper.PriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceResourceIT {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_BASE = false;
    private static final Boolean UPDATED_IS_BASE = true;

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceMockMvc;

    private Price price;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createEntity(EntityManager em) {
        Price price = new Price().price(DEFAULT_PRICE).isBase(DEFAULT_IS_BASE).sku(DEFAULT_SKU);
        return price;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createUpdatedEntity(EntityManager em) {
        Price price = new Price().price(UPDATED_PRICE).isBase(UPDATED_IS_BASE).sku(UPDATED_SKU);
        return price;
    }

    @BeforeEach
    public void initTest() {
        price = createEntity(em);
    }

    @Test
    @Transactional
    void createPrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);
        var returnedPriceDTO = om.readValue(
            restPriceMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PriceDTO.class
        );

        // Validate the Price in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrice = priceMapper.toEntity(returnedPriceDTO);
        assertPriceUpdatableFieldsEquals(returnedPrice, getPersistedPrice(returnedPrice));
    }

    @Test
    @Transactional
    void createPriceWithExistingId() throws Exception {
        // Create the Price with an existing ID
        price.setId(1L);
        PriceDTO priceDTO = priceMapper.toDto(price);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        price.setPrice(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        restPriceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsBaseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        price.setIsBase(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        restPriceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrices() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isBase").value(hasItem(DEFAULT_IS_BASE.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)));
    }

    @Test
    @Transactional
    void getPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get the price
        restPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, price.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(price.getId().intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.isBase").value(DEFAULT_IS_BASE.booleanValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU));
    }

    @Test
    @Transactional
    void getPricesByIdFiltering() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        Long id = price.getId();

        defaultPriceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPriceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPriceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price equals to
        defaultPriceFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price in
        defaultPriceFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is not null
        defaultPriceFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is greater than or equal to
        defaultPriceFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is less than or equal to
        defaultPriceFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is less than
        defaultPriceFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is greater than
        defaultPriceFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllPricesByIsBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where isBase equals to
        defaultPriceFiltering("isBase.equals=" + DEFAULT_IS_BASE, "isBase.equals=" + UPDATED_IS_BASE);
    }

    @Test
    @Transactional
    void getAllPricesByIsBaseIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where isBase in
        defaultPriceFiltering("isBase.in=" + DEFAULT_IS_BASE + "," + UPDATED_IS_BASE, "isBase.in=" + UPDATED_IS_BASE);
    }

    @Test
    @Transactional
    void getAllPricesByIsBaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where isBase is not null
        defaultPriceFiltering("isBase.specified=true", "isBase.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where sku equals to
        defaultPriceFiltering("sku.equals=" + DEFAULT_SKU, "sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllPricesBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where sku in
        defaultPriceFiltering("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU, "sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllPricesBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where sku is not null
        defaultPriceFiltering("sku.specified=true", "sku.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySkuContainsSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where sku contains
        defaultPriceFiltering("sku.contains=" + DEFAULT_SKU, "sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllPricesBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where sku does not contain
        defaultPriceFiltering("sku.doesNotContain=" + UPDATED_SKU, "sku.doesNotContain=" + DEFAULT_SKU);
    }

    private void defaultPriceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPriceShouldBeFound(shouldBeFound);
        defaultPriceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceShouldBeFound(String filter) throws Exception {
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isBase").value(hasItem(DEFAULT_IS_BASE.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)));

        // Check, that the count call also returns 1
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceShouldNotBeFound(String filter) throws Exception {
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrice() throws Exception {
        // Get the price
        restPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the price
        Price updatedPrice = priceRepository.findById(price.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrice are not directly saved in db
        em.detach(updatedPrice);
        updatedPrice.price(UPDATED_PRICE).isBase(UPDATED_IS_BASE).sku(UPDATED_SKU);
        PriceDTO priceDTO = priceMapper.toDto(updatedPrice);

        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPriceToMatchAllProperties(updatedPrice);
    }

    @Test
    @Transactional
    void putNonExistingPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice.price(UPDATED_PRICE);

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPriceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPrice, price), getPersistedPrice(price));
    }

    @Test
    @Transactional
    void fullUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice.price(UPDATED_PRICE).isBase(UPDATED_IS_BASE).sku(UPDATED_SKU);

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPriceUpdatableFieldsEquals(partialUpdatedPrice, getPersistedPrice(partialUpdatedPrice));
    }

    @Test
    @Transactional
    void patchNonExistingPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        price.setId(longCount.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(priceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the price
        restPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, price.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return priceRepository.count();
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

    protected Price getPersistedPrice(Price price) {
        return priceRepository.findById(price.getId()).orElseThrow();
    }

    protected void assertPersistedPriceToMatchAllProperties(Price expectedPrice) {
        assertPriceAllPropertiesEquals(expectedPrice, getPersistedPrice(expectedPrice));
    }

    protected void assertPersistedPriceToMatchUpdatableProperties(Price expectedPrice) {
        assertPriceAllUpdatablePropertiesEquals(expectedPrice, getPersistedPrice(expectedPrice));
    }
}
