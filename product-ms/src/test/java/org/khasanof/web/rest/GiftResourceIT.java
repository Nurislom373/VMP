package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.domain.GiftAsserts.*;
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
import org.khasanof.domain.Gift;
import org.khasanof.domain.Product;
import org.khasanof.domain.enumeration.GiftStatus;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.Visibility;
import org.khasanof.repository.GiftRepository;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.service.mapper.GiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GiftResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GiftResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PUBLIC;
    private static final Visibility UPDATED_VISIBILITY = Visibility.HIDDEN;

    private static final Level DEFAULT_LEVEL = Level.NEW;
    private static final Level UPDATED_LEVEL = Level.RECOMMENDED;

    private static final Long DEFAULT_STOCK = 1L;
    private static final Long UPDATED_STOCK = 2L;
    private static final Long SMALLER_STOCK = 1L - 1L;

    private static final Boolean DEFAULT_UNLIMITED_STOCK = false;
    private static final Boolean UPDATED_UNLIMITED_STOCK = true;

    private static final GiftStatus DEFAULT_STATUS = GiftStatus.NEW;
    private static final GiftStatus UPDATED_STATUS = GiftStatus.ACTIVE;

    private static final String ENTITY_API_URL = "/api/gifts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private GiftMapper giftMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGiftMockMvc;

    private Gift gift;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gift createEntity(EntityManager em) {
        Gift gift = new Gift()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .visibility(DEFAULT_VISIBILITY)
            .level(DEFAULT_LEVEL)
            .stock(DEFAULT_STOCK)
            .unlimitedStock(DEFAULT_UNLIMITED_STOCK)
            .status(DEFAULT_STATUS);
        return gift;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gift createUpdatedEntity(EntityManager em) {
        Gift gift = new Gift()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .visibility(UPDATED_VISIBILITY)
            .level(UPDATED_LEVEL)
            .stock(UPDATED_STOCK)
            .unlimitedStock(UPDATED_UNLIMITED_STOCK)
            .status(UPDATED_STATUS);
        return gift;
    }

    @BeforeEach
    public void initTest() {
        gift = createEntity(em);
    }

    @Test
    @Transactional
    void createGift() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);
        var returnedGiftDTO = om.readValue(
            restGiftMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GiftDTO.class
        );

        // Validate the Gift in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGift = giftMapper.toEntity(returnedGiftDTO);
        assertGiftUpdatableFieldsEquals(returnedGift, getPersistedGift(returnedGift));
    }

    @Test
    @Transactional
    void createGiftWithExistingId() throws Exception {
        // Create the Gift with an existing ID
        gift.setId(1L);
        GiftDTO giftDTO = giftMapper.toDto(gift);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiftMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gift.setName(null);

        // Create the Gift, which fails.
        GiftDTO giftDTO = giftMapper.toDto(gift);

        restGiftMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gift.setStock(null);

        // Create the Gift, which fails.
        GiftDTO giftDTO = giftMapper.toDto(gift);

        restGiftMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnlimitedStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gift.setUnlimitedStock(null);

        // Create the Gift, which fails.
        GiftDTO giftDTO = giftMapper.toDto(gift);

        restGiftMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGifts() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList
        restGiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].unlimitedStock").value(hasItem(DEFAULT_UNLIMITED_STOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get the gift
        restGiftMockMvc
            .perform(get(ENTITY_API_URL_ID, gift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gift.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.intValue()))
            .andExpect(jsonPath("$.unlimitedStock").value(DEFAULT_UNLIMITED_STOCK.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getGiftsByIdFiltering() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        Long id = gift.getId();

        defaultGiftFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGiftFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGiftFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGiftsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name equals to
        defaultGiftFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiftsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name in
        defaultGiftFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiftsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name is not null
        defaultGiftFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByNameContainsSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name contains
        defaultGiftFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiftsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name does not contain
        defaultGiftFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllGiftsByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where visibility equals to
        defaultGiftFiltering("visibility.equals=" + DEFAULT_VISIBILITY, "visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void getAllGiftsByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where visibility in
        defaultGiftFiltering("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY, "visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void getAllGiftsByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where visibility is not null
        defaultGiftFiltering("visibility.specified=true", "visibility.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where level equals to
        defaultGiftFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllGiftsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where level in
        defaultGiftFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllGiftsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where level is not null
        defaultGiftFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock equals to
        defaultGiftFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock in
        defaultGiftFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock is not null
        defaultGiftFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock is greater than or equal to
        defaultGiftFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock is less than or equal to
        defaultGiftFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock is less than
        defaultGiftFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where stock is greater than
        defaultGiftFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByUnlimitedStockIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where unlimitedStock equals to
        defaultGiftFiltering("unlimitedStock.equals=" + DEFAULT_UNLIMITED_STOCK, "unlimitedStock.equals=" + UPDATED_UNLIMITED_STOCK);
    }

    @Test
    @Transactional
    void getAllGiftsByUnlimitedStockIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where unlimitedStock in
        defaultGiftFiltering(
            "unlimitedStock.in=" + DEFAULT_UNLIMITED_STOCK + "," + UPDATED_UNLIMITED_STOCK,
            "unlimitedStock.in=" + UPDATED_UNLIMITED_STOCK
        );
    }

    @Test
    @Transactional
    void getAllGiftsByUnlimitedStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where unlimitedStock is not null
        defaultGiftFiltering("unlimitedStock.specified=true", "unlimitedStock.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where status equals to
        defaultGiftFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGiftsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where status in
        defaultGiftFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGiftsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where status is not null
        defaultGiftFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllGiftsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            giftRepository.saveAndFlush(gift);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        gift.addProduct(product);
        giftRepository.saveAndFlush(gift);
        Long productId = product.getId();
        // Get all the giftList where product equals to productId
        defaultGiftShouldBeFound("productId.equals=" + productId);

        // Get all the giftList where product equals to (productId + 1)
        defaultGiftShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultGiftFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGiftShouldBeFound(shouldBeFound);
        defaultGiftShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGiftShouldBeFound(String filter) throws Exception {
        restGiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].unlimitedStock").value(hasItem(DEFAULT_UNLIMITED_STOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restGiftMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGiftShouldNotBeFound(String filter) throws Exception {
        restGiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGiftMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGift() throws Exception {
        // Get the gift
        restGiftMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gift
        Gift updatedGift = giftRepository.findById(gift.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGift are not directly saved in db
        em.detach(updatedGift);
        updatedGift
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .visibility(UPDATED_VISIBILITY)
            .level(UPDATED_LEVEL)
            .stock(UPDATED_STOCK)
            .unlimitedStock(UPDATED_UNLIMITED_STOCK)
            .status(UPDATED_STATUS);
        GiftDTO giftDTO = giftMapper.toDto(updatedGift);

        restGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, giftDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(giftDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGiftToMatchAllProperties(updatedGift);
    }

    @Test
    @Transactional
    void putNonExistingGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, giftDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(giftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(giftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGiftWithPatch() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gift using partial update
        Gift partialUpdatedGift = new Gift();
        partialUpdatedGift.setId(gift.getId());

        partialUpdatedGift.name(UPDATED_NAME).status(UPDATED_STATUS);

        restGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGift.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGift))
            )
            .andExpect(status().isOk());

        // Validate the Gift in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGiftUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGift, gift), getPersistedGift(gift));
    }

    @Test
    @Transactional
    void fullUpdateGiftWithPatch() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gift using partial update
        Gift partialUpdatedGift = new Gift();
        partialUpdatedGift.setId(gift.getId());

        partialUpdatedGift
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .visibility(UPDATED_VISIBILITY)
            .level(UPDATED_LEVEL)
            .stock(UPDATED_STOCK)
            .unlimitedStock(UPDATED_UNLIMITED_STOCK)
            .status(UPDATED_STATUS);

        restGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGift.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGift))
            )
            .andExpect(status().isOk());

        // Validate the Gift in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGiftUpdatableFieldsEquals(partialUpdatedGift, getPersistedGift(partialUpdatedGift));
    }

    @Test
    @Transactional
    void patchNonExistingGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, giftDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(giftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(giftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gift.setId(longCount.incrementAndGet());

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(giftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gift
        restGiftMockMvc
            .perform(delete(ENTITY_API_URL_ID, gift.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return giftRepository.count();
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

    protected Gift getPersistedGift(Gift gift) {
        return giftRepository.findById(gift.getId()).orElseThrow();
    }

    protected void assertPersistedGiftToMatchAllProperties(Gift expectedGift) {
        assertGiftAllPropertiesEquals(expectedGift, getPersistedGift(expectedGift));
    }

    protected void assertPersistedGiftToMatchUpdatableProperties(Gift expectedGift) {
        assertGiftAllUpdatablePropertiesEquals(expectedGift, getPersistedGift(expectedGift));
    }
}
