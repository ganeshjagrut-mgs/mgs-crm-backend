package com.mgs.web.rest;

import static com.mgs.domain.QuotationItemAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mgs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Product;
import com.mgs.domain.Quotation;
import com.mgs.domain.QuotationItem;
import com.mgs.domain.Tenant;
import com.mgs.domain.enumeration.DiscountType;
import com.mgs.repository.QuotationItemRepository;
import com.mgs.service.dto.QuotationItemDTO;
import com.mgs.service.mapper.QuotationItemMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuotationItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuotationItemResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_LABEL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAXABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAXABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAX_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAX_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LINE_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_LINE_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_LINE_TOTAL = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/quotation-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationItemMapper quotationItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationItemMockMvc;

    private QuotationItem quotationItem;

    private QuotationItem insertedQuotationItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationItem createEntity(EntityManager em) {
        QuotationItem quotationItem = new QuotationItem()
            .productName(DEFAULT_PRODUCT_NAME)
            .productSku(DEFAULT_PRODUCT_SKU)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .unitLabel(DEFAULT_UNIT_LABEL)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .taxableAmount(DEFAULT_TAXABLE_AMOUNT)
            .taxRate(DEFAULT_TAX_RATE)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .lineTotal(DEFAULT_LINE_TOTAL)
            .sortOrder(DEFAULT_SORT_ORDER);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        quotationItem.setTenant(tenant);
        // Add required entity
        Quotation quotation;
        if (TestUtil.findAll(em, Quotation.class).isEmpty()) {
            quotation = QuotationResourceIT.createEntity(em);
            em.persist(quotation);
            em.flush();
        } else {
            quotation = TestUtil.findAll(em, Quotation.class).get(0);
        }
        quotationItem.setQuotation(quotation);
        return quotationItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationItem createUpdatedEntity(EntityManager em) {
        QuotationItem updatedQuotationItem = new QuotationItem()
            .productName(UPDATED_PRODUCT_NAME)
            .productSku(UPDATED_PRODUCT_SKU)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitLabel(UPDATED_UNIT_LABEL)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .lineTotal(UPDATED_LINE_TOTAL)
            .sortOrder(UPDATED_SORT_ORDER);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedQuotationItem.setTenant(tenant);
        // Add required entity
        Quotation quotation;
        if (TestUtil.findAll(em, Quotation.class).isEmpty()) {
            quotation = QuotationResourceIT.createUpdatedEntity(em);
            em.persist(quotation);
            em.flush();
        } else {
            quotation = TestUtil.findAll(em, Quotation.class).get(0);
        }
        updatedQuotationItem.setQuotation(quotation);
        return updatedQuotationItem;
    }

    @BeforeEach
    void initTest() {
        quotationItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuotationItem != null) {
            quotationItemRepository.delete(insertedQuotationItem);
            insertedQuotationItem = null;
        }
    }

    @Test
    @Transactional
    void createQuotationItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);
        var returnedQuotationItemDTO = om.readValue(
            restQuotationItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotationItemDTO.class
        );

        // Validate the QuotationItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotationItem = quotationItemMapper.toEntity(returnedQuotationItemDTO);
        assertQuotationItemUpdatableFieldsEquals(returnedQuotationItem, getPersistedQuotationItem(returnedQuotationItem));

        insertedQuotationItem = returnedQuotationItem;
    }

    @Test
    @Transactional
    void createQuotationItemWithExistingId() throws Exception {
        // Create the QuotationItem with an existing ID
        quotationItem.setId(1L);
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotationItem.setProductName(null);

        // Create the QuotationItem, which fails.
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        restQuotationItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotationItem.setQuantity(null);

        // Create the QuotationItem, which fails.
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        restQuotationItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotationItem.setUnitPrice(null);

        // Create the QuotationItem, which fails.
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        restQuotationItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotationItems() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productSku").value(hasItem(DEFAULT_PRODUCT_SKU)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitLabel").value(hasItem(DEFAULT_UNIT_LABEL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(sameNumber(DEFAULT_TAX_RATE))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].lineTotal").value(hasItem(sameNumber(DEFAULT_LINE_TOTAL))))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    void getQuotationItem() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get the quotationItem
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL_ID, quotationItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotationItem.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.productSku").value(DEFAULT_PRODUCT_SKU))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.unitLabel").value(DEFAULT_UNIT_LABEL))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountValue").value(sameNumber(DEFAULT_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.discountAmount").value(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.taxableAmount").value(sameNumber(DEFAULT_TAXABLE_AMOUNT)))
            .andExpect(jsonPath("$.taxRate").value(sameNumber(DEFAULT_TAX_RATE)))
            .andExpect(jsonPath("$.taxAmount").value(sameNumber(DEFAULT_TAX_AMOUNT)))
            .andExpect(jsonPath("$.lineTotal").value(sameNumber(DEFAULT_LINE_TOTAL)))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getQuotationItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        Long id = quotationItem.getId();

        defaultQuotationItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuotationItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuotationItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productName equals to
        defaultQuotationItemFiltering("productName.equals=" + DEFAULT_PRODUCT_NAME, "productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productName in
        defaultQuotationItemFiltering(
            "productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME,
            "productName.in=" + UPDATED_PRODUCT_NAME
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productName is not null
        defaultQuotationItemFiltering("productName.specified=true", "productName.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productName contains
        defaultQuotationItemFiltering("productName.contains=" + DEFAULT_PRODUCT_NAME, "productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productName does not contain
        defaultQuotationItemFiltering(
            "productName.doesNotContain=" + UPDATED_PRODUCT_NAME,
            "productName.doesNotContain=" + DEFAULT_PRODUCT_NAME
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductSkuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productSku equals to
        defaultQuotationItemFiltering("productSku.equals=" + DEFAULT_PRODUCT_SKU, "productSku.equals=" + UPDATED_PRODUCT_SKU);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductSkuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productSku in
        defaultQuotationItemFiltering(
            "productSku.in=" + DEFAULT_PRODUCT_SKU + "," + UPDATED_PRODUCT_SKU,
            "productSku.in=" + UPDATED_PRODUCT_SKU
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductSkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productSku is not null
        defaultQuotationItemFiltering("productSku.specified=true", "productSku.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductSkuContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productSku contains
        defaultQuotationItemFiltering("productSku.contains=" + DEFAULT_PRODUCT_SKU, "productSku.contains=" + UPDATED_PRODUCT_SKU);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductSkuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productSku does not contain
        defaultQuotationItemFiltering(
            "productSku.doesNotContain=" + UPDATED_PRODUCT_SKU,
            "productSku.doesNotContain=" + DEFAULT_PRODUCT_SKU
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productDescription equals to
        defaultQuotationItemFiltering(
            "productDescription.equals=" + DEFAULT_PRODUCT_DESCRIPTION,
            "productDescription.equals=" + UPDATED_PRODUCT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productDescription in
        defaultQuotationItemFiltering(
            "productDescription.in=" + DEFAULT_PRODUCT_DESCRIPTION + "," + UPDATED_PRODUCT_DESCRIPTION,
            "productDescription.in=" + UPDATED_PRODUCT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productDescription is not null
        defaultQuotationItemFiltering("productDescription.specified=true", "productDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productDescription contains
        defaultQuotationItemFiltering(
            "productDescription.contains=" + DEFAULT_PRODUCT_DESCRIPTION,
            "productDescription.contains=" + UPDATED_PRODUCT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where productDescription does not contain
        defaultQuotationItemFiltering(
            "productDescription.doesNotContain=" + UPDATED_PRODUCT_DESCRIPTION,
            "productDescription.doesNotContain=" + DEFAULT_PRODUCT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitLabel equals to
        defaultQuotationItemFiltering("unitLabel.equals=" + DEFAULT_UNIT_LABEL, "unitLabel.equals=" + UPDATED_UNIT_LABEL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitLabel in
        defaultQuotationItemFiltering(
            "unitLabel.in=" + DEFAULT_UNIT_LABEL + "," + UPDATED_UNIT_LABEL,
            "unitLabel.in=" + UPDATED_UNIT_LABEL
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitLabel is not null
        defaultQuotationItemFiltering("unitLabel.specified=true", "unitLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitLabel contains
        defaultQuotationItemFiltering("unitLabel.contains=" + DEFAULT_UNIT_LABEL, "unitLabel.contains=" + UPDATED_UNIT_LABEL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitLabel does not contain
        defaultQuotationItemFiltering("unitLabel.doesNotContain=" + UPDATED_UNIT_LABEL, "unitLabel.doesNotContain=" + DEFAULT_UNIT_LABEL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity equals to
        defaultQuotationItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity in
        defaultQuotationItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity is not null
        defaultQuotationItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity is greater than or equal to
        defaultQuotationItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity is less than or equal to
        defaultQuotationItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity is less than
        defaultQuotationItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where quantity is greater than
        defaultQuotationItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice equals to
        defaultQuotationItemFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice in
        defaultQuotationItemFiltering(
            "unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE,
            "unitPrice.in=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice is not null
        defaultQuotationItemFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice is greater than or equal to
        defaultQuotationItemFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice is less than or equal to
        defaultQuotationItemFiltering("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE, "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice is less than
        defaultQuotationItemFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where unitPrice is greater than
        defaultQuotationItemFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountType equals to
        defaultQuotationItemFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountType in
        defaultQuotationItemFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountType is not null
        defaultQuotationItemFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue equals to
        defaultQuotationItemFiltering("discountValue.equals=" + DEFAULT_DISCOUNT_VALUE, "discountValue.equals=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue in
        defaultQuotationItemFiltering(
            "discountValue.in=" + DEFAULT_DISCOUNT_VALUE + "," + UPDATED_DISCOUNT_VALUE,
            "discountValue.in=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue is not null
        defaultQuotationItemFiltering("discountValue.specified=true", "discountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue is greater than or equal to
        defaultQuotationItemFiltering(
            "discountValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue is less than or equal to
        defaultQuotationItemFiltering(
            "discountValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.lessThanOrEqual=" + SMALLER_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue is less than
        defaultQuotationItemFiltering(
            "discountValue.lessThan=" + UPDATED_DISCOUNT_VALUE,
            "discountValue.lessThan=" + DEFAULT_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountValue is greater than
        defaultQuotationItemFiltering(
            "discountValue.greaterThan=" + SMALLER_DISCOUNT_VALUE,
            "discountValue.greaterThan=" + DEFAULT_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount equals to
        defaultQuotationItemFiltering(
            "discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount in
        defaultQuotationItemFiltering(
            "discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT,
            "discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount is not null
        defaultQuotationItemFiltering("discountAmount.specified=true", "discountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount is greater than or equal to
        defaultQuotationItemFiltering(
            "discountAmount.greaterThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.greaterThanOrEqual=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount is less than or equal to
        defaultQuotationItemFiltering(
            "discountAmount.lessThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.lessThanOrEqual=" + SMALLER_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount is less than
        defaultQuotationItemFiltering(
            "discountAmount.lessThan=" + UPDATED_DISCOUNT_AMOUNT,
            "discountAmount.lessThan=" + DEFAULT_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where discountAmount is greater than
        defaultQuotationItemFiltering(
            "discountAmount.greaterThan=" + SMALLER_DISCOUNT_AMOUNT,
            "discountAmount.greaterThan=" + DEFAULT_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount equals to
        defaultQuotationItemFiltering("taxableAmount.equals=" + DEFAULT_TAXABLE_AMOUNT, "taxableAmount.equals=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount in
        defaultQuotationItemFiltering(
            "taxableAmount.in=" + DEFAULT_TAXABLE_AMOUNT + "," + UPDATED_TAXABLE_AMOUNT,
            "taxableAmount.in=" + UPDATED_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount is not null
        defaultQuotationItemFiltering("taxableAmount.specified=true", "taxableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount is greater than or equal to
        defaultQuotationItemFiltering(
            "taxableAmount.greaterThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT,
            "taxableAmount.greaterThanOrEqual=" + UPDATED_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount is less than or equal to
        defaultQuotationItemFiltering(
            "taxableAmount.lessThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT,
            "taxableAmount.lessThanOrEqual=" + SMALLER_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount is less than
        defaultQuotationItemFiltering(
            "taxableAmount.lessThan=" + UPDATED_TAXABLE_AMOUNT,
            "taxableAmount.lessThan=" + DEFAULT_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxableAmount is greater than
        defaultQuotationItemFiltering(
            "taxableAmount.greaterThan=" + SMALLER_TAXABLE_AMOUNT,
            "taxableAmount.greaterThan=" + DEFAULT_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate equals to
        defaultQuotationItemFiltering("taxRate.equals=" + DEFAULT_TAX_RATE, "taxRate.equals=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate in
        defaultQuotationItemFiltering("taxRate.in=" + DEFAULT_TAX_RATE + "," + UPDATED_TAX_RATE, "taxRate.in=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate is not null
        defaultQuotationItemFiltering("taxRate.specified=true", "taxRate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate is greater than or equal to
        defaultQuotationItemFiltering("taxRate.greaterThanOrEqual=" + DEFAULT_TAX_RATE, "taxRate.greaterThanOrEqual=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate is less than or equal to
        defaultQuotationItemFiltering("taxRate.lessThanOrEqual=" + DEFAULT_TAX_RATE, "taxRate.lessThanOrEqual=" + SMALLER_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate is less than
        defaultQuotationItemFiltering("taxRate.lessThan=" + UPDATED_TAX_RATE, "taxRate.lessThan=" + DEFAULT_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxRate is greater than
        defaultQuotationItemFiltering("taxRate.greaterThan=" + SMALLER_TAX_RATE, "taxRate.greaterThan=" + DEFAULT_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount equals to
        defaultQuotationItemFiltering("taxAmount.equals=" + DEFAULT_TAX_AMOUNT, "taxAmount.equals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount in
        defaultQuotationItemFiltering(
            "taxAmount.in=" + DEFAULT_TAX_AMOUNT + "," + UPDATED_TAX_AMOUNT,
            "taxAmount.in=" + UPDATED_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount is not null
        defaultQuotationItemFiltering("taxAmount.specified=true", "taxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount is greater than or equal to
        defaultQuotationItemFiltering(
            "taxAmount.greaterThanOrEqual=" + DEFAULT_TAX_AMOUNT,
            "taxAmount.greaterThanOrEqual=" + UPDATED_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount is less than or equal to
        defaultQuotationItemFiltering("taxAmount.lessThanOrEqual=" + DEFAULT_TAX_AMOUNT, "taxAmount.lessThanOrEqual=" + SMALLER_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount is less than
        defaultQuotationItemFiltering("taxAmount.lessThan=" + UPDATED_TAX_AMOUNT, "taxAmount.lessThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where taxAmount is greater than
        defaultQuotationItemFiltering("taxAmount.greaterThan=" + SMALLER_TAX_AMOUNT, "taxAmount.greaterThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal equals to
        defaultQuotationItemFiltering("lineTotal.equals=" + DEFAULT_LINE_TOTAL, "lineTotal.equals=" + UPDATED_LINE_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal in
        defaultQuotationItemFiltering(
            "lineTotal.in=" + DEFAULT_LINE_TOTAL + "," + UPDATED_LINE_TOTAL,
            "lineTotal.in=" + UPDATED_LINE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal is not null
        defaultQuotationItemFiltering("lineTotal.specified=true", "lineTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal is greater than or equal to
        defaultQuotationItemFiltering(
            "lineTotal.greaterThanOrEqual=" + DEFAULT_LINE_TOTAL,
            "lineTotal.greaterThanOrEqual=" + UPDATED_LINE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal is less than or equal to
        defaultQuotationItemFiltering("lineTotal.lessThanOrEqual=" + DEFAULT_LINE_TOTAL, "lineTotal.lessThanOrEqual=" + SMALLER_LINE_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal is less than
        defaultQuotationItemFiltering("lineTotal.lessThan=" + UPDATED_LINE_TOTAL, "lineTotal.lessThan=" + DEFAULT_LINE_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByLineTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where lineTotal is greater than
        defaultQuotationItemFiltering("lineTotal.greaterThan=" + SMALLER_LINE_TOTAL, "lineTotal.greaterThan=" + DEFAULT_LINE_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder equals to
        defaultQuotationItemFiltering("sortOrder.equals=" + DEFAULT_SORT_ORDER, "sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder in
        defaultQuotationItemFiltering(
            "sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER,
            "sortOrder.in=" + UPDATED_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder is not null
        defaultQuotationItemFiltering("sortOrder.specified=true", "sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder is greater than or equal to
        defaultQuotationItemFiltering(
            "sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER,
            "sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder is less than or equal to
        defaultQuotationItemFiltering("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER, "sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder is less than
        defaultQuotationItemFiltering("sortOrder.lessThan=" + UPDATED_SORT_ORDER, "sortOrder.lessThan=" + DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllQuotationItemsBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        // Get all the quotationItemList where sortOrder is greater than
        defaultQuotationItemFiltering("sortOrder.greaterThan=" + SMALLER_SORT_ORDER, "sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllQuotationItemsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            quotationItemRepository.saveAndFlush(quotationItem);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        quotationItem.setTenant(tenant);
        quotationItemRepository.saveAndFlush(quotationItem);
        Long tenantId = tenant.getId();
        // Get all the quotationItemList where tenant equals to tenantId
        defaultQuotationItemShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the quotationItemList where tenant equals to (tenantId + 1)
        defaultQuotationItemShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationItemsByQuotationIsEqualToSomething() throws Exception {
        Quotation quotation;
        if (TestUtil.findAll(em, Quotation.class).isEmpty()) {
            quotationItemRepository.saveAndFlush(quotationItem);
            quotation = QuotationResourceIT.createEntity(em);
        } else {
            quotation = TestUtil.findAll(em, Quotation.class).get(0);
        }
        em.persist(quotation);
        em.flush();
        quotationItem.setQuotation(quotation);
        quotationItemRepository.saveAndFlush(quotationItem);
        Long quotationId = quotation.getId();
        // Get all the quotationItemList where quotation equals to quotationId
        defaultQuotationItemShouldBeFound("quotationId.equals=" + quotationId);

        // Get all the quotationItemList where quotation equals to (quotationId + 1)
        defaultQuotationItemShouldNotBeFound("quotationId.equals=" + (quotationId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            quotationItemRepository.saveAndFlush(quotationItem);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        quotationItem.setProduct(product);
        quotationItemRepository.saveAndFlush(quotationItem);
        Long productId = product.getId();
        // Get all the quotationItemList where product equals to productId
        defaultQuotationItemShouldBeFound("productId.equals=" + productId);

        // Get all the quotationItemList where product equals to (productId + 1)
        defaultQuotationItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultQuotationItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuotationItemShouldBeFound(shouldBeFound);
        defaultQuotationItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuotationItemShouldBeFound(String filter) throws Exception {
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productSku").value(hasItem(DEFAULT_PRODUCT_SKU)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitLabel").value(hasItem(DEFAULT_UNIT_LABEL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(sameNumber(DEFAULT_TAX_RATE))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].lineTotal").value(hasItem(sameNumber(DEFAULT_LINE_TOTAL))))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuotationItemShouldNotBeFound(String filter) throws Exception {
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuotationItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuotationItem() throws Exception {
        // Get the quotationItem
        restQuotationItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotationItem() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationItem
        QuotationItem updatedQuotationItem = quotationItemRepository.findById(quotationItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotationItem are not directly saved in db
        em.detach(updatedQuotationItem);
        updatedQuotationItem
            .productName(UPDATED_PRODUCT_NAME)
            .productSku(UPDATED_PRODUCT_SKU)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitLabel(UPDATED_UNIT_LABEL)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .lineTotal(UPDATED_LINE_TOTAL)
            .sortOrder(UPDATED_SORT_ORDER);
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(updatedQuotationItem);

        restQuotationItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotationItemToMatchAllProperties(updatedQuotationItem);
    }

    @Test
    @Transactional
    void putNonExistingQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotationItemWithPatch() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationItem using partial update
        QuotationItem partialUpdatedQuotationItem = new QuotationItem();
        partialUpdatedQuotationItem.setId(quotationItem.getId());

        partialUpdatedQuotationItem
            .productName(UPDATED_PRODUCT_NAME)
            .productSku(UPDATED_PRODUCT_SKU)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitLabel(UPDATED_UNIT_LABEL)
            .quantity(UPDATED_QUANTITY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .lineTotal(UPDATED_LINE_TOTAL)
            .sortOrder(UPDATED_SORT_ORDER);

        restQuotationItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotationItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotationItem))
            )
            .andExpect(status().isOk());

        // Validate the QuotationItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotationItem, quotationItem),
            getPersistedQuotationItem(quotationItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotationItemWithPatch() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationItem using partial update
        QuotationItem partialUpdatedQuotationItem = new QuotationItem();
        partialUpdatedQuotationItem.setId(quotationItem.getId());

        partialUpdatedQuotationItem
            .productName(UPDATED_PRODUCT_NAME)
            .productSku(UPDATED_PRODUCT_SKU)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitLabel(UPDATED_UNIT_LABEL)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .lineTotal(UPDATED_LINE_TOTAL)
            .sortOrder(UPDATED_SORT_ORDER);

        restQuotationItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotationItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotationItem))
            )
            .andExpect(status().isOk());

        // Validate the QuotationItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationItemUpdatableFieldsEquals(partialUpdatedQuotationItem, getPersistedQuotationItem(partialUpdatedQuotationItem));
    }

    @Test
    @Transactional
    void patchNonExistingQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotationItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotationItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationItem.setId(longCount.incrementAndGet());

        // Create the QuotationItem
        QuotationItemDTO quotationItemDTO = quotationItemMapper.toDto(quotationItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotationItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotationItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotationItem() throws Exception {
        // Initialize the database
        insertedQuotationItem = quotationItemRepository.saveAndFlush(quotationItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotationItem
        restQuotationItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotationItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotationItemRepository.count();
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

    protected QuotationItem getPersistedQuotationItem(QuotationItem quotationItem) {
        return quotationItemRepository.findById(quotationItem.getId()).orElseThrow();
    }

    protected void assertPersistedQuotationItemToMatchAllProperties(QuotationItem expectedQuotationItem) {
        assertQuotationItemAllPropertiesEquals(expectedQuotationItem, getPersistedQuotationItem(expectedQuotationItem));
    }

    protected void assertPersistedQuotationItemToMatchUpdatableProperties(QuotationItem expectedQuotationItem) {
        assertQuotationItemAllUpdatablePropertiesEquals(expectedQuotationItem, getPersistedQuotationItem(expectedQuotationItem));
    }
}
