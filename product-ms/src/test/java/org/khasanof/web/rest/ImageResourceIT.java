package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.domain.ImageAsserts.*;
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
import org.khasanof.domain.Image;
import org.khasanof.domain.Product;
import org.khasanof.domain.enumeration.ImageStatus;
import org.khasanof.repository.ImageRepository;
import org.khasanof.service.dto.ImageDTO;
import org.khasanof.service.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageResourceIT {

    private static final String DEFAULT_IMAGE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_KEY = "BBBBBBBBBB";

    private static final ImageStatus DEFAULT_STATUS = ImageStatus.ACTIVE;
    private static final ImageStatus UPDATED_STATUS = ImageStatus.DELETED;

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity(EntityManager em) {
        Image image = new Image().imageKey(DEFAULT_IMAGE_KEY).status(DEFAULT_STATUS);
        return image;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createUpdatedEntity(EntityManager em) {
        Image image = new Image().imageKey(UPDATED_IMAGE_KEY).status(UPDATED_STATUS);
        return image;
    }

    @BeforeEach
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    void createImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);
        var returnedImageDTO = om.readValue(
            restImageMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageDTO.class
        );

        // Validate the Image in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImage = imageMapper.toEntity(returnedImageDTO);
        assertImageUpdatableFieldsEquals(returnedImage, getPersistedImage(returnedImage));
    }

    @Test
    @Transactional
    void createImageWithExistingId() throws Exception {
        // Create the Image with an existing ID
        image.setId(1L);
        ImageDTO imageDTO = imageMapper.toDto(image);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImageKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        image.setImageKey(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageKey").value(hasItem(DEFAULT_IMAGE_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc
            .perform(get(ENTITY_API_URL_ID, image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.imageKey").value(DEFAULT_IMAGE_KEY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        Long id = image.getId();

        defaultImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByImageKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where imageKey equals to
        defaultImageFiltering("imageKey.equals=" + DEFAULT_IMAGE_KEY, "imageKey.equals=" + UPDATED_IMAGE_KEY);
    }

    @Test
    @Transactional
    void getAllImagesByImageKeyIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where imageKey in
        defaultImageFiltering("imageKey.in=" + DEFAULT_IMAGE_KEY + "," + UPDATED_IMAGE_KEY, "imageKey.in=" + UPDATED_IMAGE_KEY);
    }

    @Test
    @Transactional
    void getAllImagesByImageKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where imageKey is not null
        defaultImageFiltering("imageKey.specified=true", "imageKey.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImageKeyContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where imageKey contains
        defaultImageFiltering("imageKey.contains=" + DEFAULT_IMAGE_KEY, "imageKey.contains=" + UPDATED_IMAGE_KEY);
    }

    @Test
    @Transactional
    void getAllImagesByImageKeyNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where imageKey does not contain
        defaultImageFiltering("imageKey.doesNotContain=" + UPDATED_IMAGE_KEY, "imageKey.doesNotContain=" + DEFAULT_IMAGE_KEY);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where status equals to
        defaultImageFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where status in
        defaultImageFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where status is not null
        defaultImageFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            imageRepository.saveAndFlush(image);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        image.setProduct(product);
        imageRepository.saveAndFlush(image);
        Long productId = product.getId();
        // Get all the imageList where product equals to productId
        defaultImageShouldBeFound("productId.equals=" + productId);

        // Get all the imageList where product equals to (productId + 1)
        defaultImageShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageShouldBeFound(shouldBeFound);
        defaultImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageKey").value(hasItem(DEFAULT_IMAGE_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage.imageKey(UPDATED_IMAGE_KEY).status(UPDATED_STATUS);
        ImageDTO imageDTO = imageMapper.toDto(updatedImage);

        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageToMatchAllProperties(updatedImage);
    }

    @Test
    @Transactional
    void putNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageWithPatch() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage.imageKey(UPDATED_IMAGE_KEY);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedImage, image), getPersistedImage(image));
    }

    @Test
    @Transactional
    void fullUpdateImageWithPatch() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage.imageKey(UPDATED_IMAGE_KEY).status(UPDATED_STATUS);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(partialUpdatedImage, getPersistedImage(partialUpdatedImage));
    }

    @Test
    @Transactional
    void patchNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the image
        restImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, image.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageRepository.count();
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

    protected Image getPersistedImage(Image image) {
        return imageRepository.findById(image.getId()).orElseThrow();
    }

    protected void assertPersistedImageToMatchAllProperties(Image expectedImage) {
        assertImageAllPropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }

    protected void assertPersistedImageToMatchUpdatableProperties(Image expectedImage) {
        assertImageAllUpdatablePropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }
}
