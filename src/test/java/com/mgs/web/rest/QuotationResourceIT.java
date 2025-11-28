package com.mgs.web.rest;

import static com.mgs.domain.QuotationAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mgs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Lead;
import com.mgs.domain.Quotation;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.enumeration.DiscountType;
import com.mgs.domain.enumeration.QuotationStatus;
import com.mgs.repository.QuotationRepository;
import com.mgs.service.dto.QuotationDTO;
import com.mgs.service.mapper.QuotationMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link QuotationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuotationResourceIT {

    private static final String DEFAULT_QUOTE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_QUOTE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESTIMATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ESTIMATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_VALID_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UNTIL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_UNTIL = LocalDate.ofEpochDay(-1L);

    private static final QuotationStatus DEFAULT_STATUS = QuotationStatus.DRAFT;
    private static final QuotationStatus UPDATED_STATUS = QuotationStatus.SENT;

    private static final Integer DEFAULT_REVISION_NUMBER = 1;
    private static final Integer UPDATED_REVISION_NUMBER = 2;
    private static final Integer SMALLER_REVISION_NUMBER = 1 - 1;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_SUBTOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ITEM_DISCOUNT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_DISCOUNT_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_DISCOUNT_TOTAL = new BigDecimal(1 - 1);

    private static final DiscountType DEFAULT_GLOBAL_DISCOUNT_TYPE = DiscountType.PERCENT;
    private static final DiscountType UPDATED_GLOBAL_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final BigDecimal DEFAULT_GLOBAL_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_GLOBAL_DISCOUNT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_GLOBAL_DISCOUNT_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GLOBAL_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GLOBAL_DISCOUNT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GLOBAL_DISCOUNT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAXABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAXABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_TAX = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SHIPPING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIPPING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SHIPPING_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OTHER_CHARGES_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OTHER_CHARGES_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_OTHER_CHARGES_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ROUND_OFF_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROUND_OFF_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ROUND_OFF_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_HEADER_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_HEADER_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_TERMS_AND_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_TERMS_AND_CONDITIONS = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PDF_TEMPLATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PDF_TEMPLATE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quotations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    private Quotation insertedQuotation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createEntity(EntityManager em) {
        Quotation quotation = new Quotation()
            .quoteNumber(DEFAULT_QUOTE_NUMBER)
            .estimateDate(DEFAULT_ESTIMATE_DATE)
            .validUntil(DEFAULT_VALID_UNTIL)
            .status(DEFAULT_STATUS)
            .revisionNumber(DEFAULT_REVISION_NUMBER)
            .currency(DEFAULT_CURRENCY)
            .subtotal(DEFAULT_SUBTOTAL)
            .itemDiscountTotal(DEFAULT_ITEM_DISCOUNT_TOTAL)
            .globalDiscountType(DEFAULT_GLOBAL_DISCOUNT_TYPE)
            .globalDiscountValue(DEFAULT_GLOBAL_DISCOUNT_VALUE)
            .globalDiscountAmount(DEFAULT_GLOBAL_DISCOUNT_AMOUNT)
            .taxableAmount(DEFAULT_TAXABLE_AMOUNT)
            .totalTax(DEFAULT_TOTAL_TAX)
            .shippingAmount(DEFAULT_SHIPPING_AMOUNT)
            .otherChargesAmount(DEFAULT_OTHER_CHARGES_AMOUNT)
            .roundOffAmount(DEFAULT_ROUND_OFF_AMOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .title(DEFAULT_TITLE)
            .headerNotes(DEFAULT_HEADER_NOTES)
            .footerNotes(DEFAULT_FOOTER_NOTES)
            .termsAndConditions(DEFAULT_TERMS_AND_CONDITIONS)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .lastSentAt(DEFAULT_LAST_SENT_AT)
            .pdfTemplateCode(DEFAULT_PDF_TEMPLATE_CODE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        quotation.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        quotation.setCustomer(customer);
        return quotation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createUpdatedEntity(EntityManager em) {
        Quotation updatedQuotation = new Quotation()
            .quoteNumber(UPDATED_QUOTE_NUMBER)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .revisionNumber(UPDATED_REVISION_NUMBER)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .itemDiscountTotal(UPDATED_ITEM_DISCOUNT_TOTAL)
            .globalDiscountType(UPDATED_GLOBAL_DISCOUNT_TYPE)
            .globalDiscountValue(UPDATED_GLOBAL_DISCOUNT_VALUE)
            .globalDiscountAmount(UPDATED_GLOBAL_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .totalTax(UPDATED_TOTAL_TAX)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .otherChargesAmount(UPDATED_OTHER_CHARGES_AMOUNT)
            .roundOffAmount(UPDATED_ROUND_OFF_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .title(UPDATED_TITLE)
            .headerNotes(UPDATED_HEADER_NOTES)
            .footerNotes(UPDATED_FOOTER_NOTES)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .lastSentAt(UPDATED_LAST_SENT_AT)
            .pdfTemplateCode(UPDATED_PDF_TEMPLATE_CODE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedQuotation.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        updatedQuotation.setCustomer(customer);
        return updatedQuotation;
    }

    @BeforeEach
    void initTest() {
        quotation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuotation != null) {
            quotationRepository.delete(insertedQuotation);
            insertedQuotation = null;
        }
    }

    @Test
    @Transactional
    void createQuotation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);
        var returnedQuotationDTO = om.readValue(
            restQuotationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotationDTO.class
        );

        // Validate the Quotation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotation = quotationMapper.toEntity(returnedQuotationDTO);
        assertQuotationUpdatableFieldsEquals(returnedQuotation, getPersistedQuotation(returnedQuotation));

        insertedQuotation = returnedQuotation;
    }

    @Test
    @Transactional
    void createQuotationWithExistingId() throws Exception {
        // Create the Quotation with an existing ID
        quotation.setId(1L);
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuoteNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setQuoteNumber(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstimateDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setEstimateDate(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setStatus(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setCurrency(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setSubtotal(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setTotalAmount(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotations() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quoteNumber").value(hasItem(DEFAULT_QUOTE_NUMBER)))
            .andExpect(jsonPath("$.[*].estimateDate").value(hasItem(DEFAULT_ESTIMATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].revisionNumber").value(hasItem(DEFAULT_REVISION_NUMBER)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].itemDiscountTotal").value(hasItem(sameNumber(DEFAULT_ITEM_DISCOUNT_TOTAL))))
            .andExpect(jsonPath("$.[*].globalDiscountType").value(hasItem(DEFAULT_GLOBAL_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].globalDiscountValue").value(hasItem(sameNumber(DEFAULT_GLOBAL_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].globalDiscountAmount").value(hasItem(sameNumber(DEFAULT_GLOBAL_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTax").value(hasItem(sameNumber(DEFAULT_TOTAL_TAX))))
            .andExpect(jsonPath("$.[*].shippingAmount").value(hasItem(sameNumber(DEFAULT_SHIPPING_AMOUNT))))
            .andExpect(jsonPath("$.[*].otherChargesAmount").value(hasItem(sameNumber(DEFAULT_OTHER_CHARGES_AMOUNT))))
            .andExpect(jsonPath("$.[*].roundOffAmount").value(hasItem(sameNumber(DEFAULT_ROUND_OFF_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].headerNotes").value(hasItem(DEFAULT_HEADER_NOTES)))
            .andExpect(jsonPath("$.[*].footerNotes").value(hasItem(DEFAULT_FOOTER_NOTES)))
            .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].lastSentAt").value(hasItem(DEFAULT_LAST_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].pdfTemplateCode").value(hasItem(DEFAULT_PDF_TEMPLATE_CODE)));
    }

    @Test
    @Transactional
    void getQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL_ID, quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.quoteNumber").value(DEFAULT_QUOTE_NUMBER))
            .andExpect(jsonPath("$.estimateDate").value(DEFAULT_ESTIMATE_DATE.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.revisionNumber").value(DEFAULT_REVISION_NUMBER))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.subtotal").value(sameNumber(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.itemDiscountTotal").value(sameNumber(DEFAULT_ITEM_DISCOUNT_TOTAL)))
            .andExpect(jsonPath("$.globalDiscountType").value(DEFAULT_GLOBAL_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.globalDiscountValue").value(sameNumber(DEFAULT_GLOBAL_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.globalDiscountAmount").value(sameNumber(DEFAULT_GLOBAL_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.taxableAmount").value(sameNumber(DEFAULT_TAXABLE_AMOUNT)))
            .andExpect(jsonPath("$.totalTax").value(sameNumber(DEFAULT_TOTAL_TAX)))
            .andExpect(jsonPath("$.shippingAmount").value(sameNumber(DEFAULT_SHIPPING_AMOUNT)))
            .andExpect(jsonPath("$.otherChargesAmount").value(sameNumber(DEFAULT_OTHER_CHARGES_AMOUNT)))
            .andExpect(jsonPath("$.roundOffAmount").value(sameNumber(DEFAULT_ROUND_OFF_AMOUNT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.headerNotes").value(DEFAULT_HEADER_NOTES))
            .andExpect(jsonPath("$.footerNotes").value(DEFAULT_FOOTER_NOTES))
            .andExpect(jsonPath("$.termsAndConditions").value(DEFAULT_TERMS_AND_CONDITIONS))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.lastSentAt").value(DEFAULT_LAST_SENT_AT.toString()))
            .andExpect(jsonPath("$.pdfTemplateCode").value(DEFAULT_PDF_TEMPLATE_CODE));
    }

    @Test
    @Transactional
    void getQuotationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        Long id = quotation.getId();

        defaultQuotationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuotationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuotationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuoteNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quoteNumber equals to
        defaultQuotationFiltering("quoteNumber.equals=" + DEFAULT_QUOTE_NUMBER, "quoteNumber.equals=" + UPDATED_QUOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuoteNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quoteNumber in
        defaultQuotationFiltering(
            "quoteNumber.in=" + DEFAULT_QUOTE_NUMBER + "," + UPDATED_QUOTE_NUMBER,
            "quoteNumber.in=" + UPDATED_QUOTE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByQuoteNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quoteNumber is not null
        defaultQuotationFiltering("quoteNumber.specified=true", "quoteNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByQuoteNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quoteNumber contains
        defaultQuotationFiltering("quoteNumber.contains=" + DEFAULT_QUOTE_NUMBER, "quoteNumber.contains=" + UPDATED_QUOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuoteNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quoteNumber does not contain
        defaultQuotationFiltering(
            "quoteNumber.doesNotContain=" + UPDATED_QUOTE_NUMBER,
            "quoteNumber.doesNotContain=" + DEFAULT_QUOTE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate equals to
        defaultQuotationFiltering("estimateDate.equals=" + DEFAULT_ESTIMATE_DATE, "estimateDate.equals=" + UPDATED_ESTIMATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate in
        defaultQuotationFiltering(
            "estimateDate.in=" + DEFAULT_ESTIMATE_DATE + "," + UPDATED_ESTIMATE_DATE,
            "estimateDate.in=" + UPDATED_ESTIMATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is not null
        defaultQuotationFiltering("estimateDate.specified=true", "estimateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is greater than or equal to
        defaultQuotationFiltering(
            "estimateDate.greaterThanOrEqual=" + DEFAULT_ESTIMATE_DATE,
            "estimateDate.greaterThanOrEqual=" + UPDATED_ESTIMATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is less than or equal to
        defaultQuotationFiltering(
            "estimateDate.lessThanOrEqual=" + DEFAULT_ESTIMATE_DATE,
            "estimateDate.lessThanOrEqual=" + SMALLER_ESTIMATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is less than
        defaultQuotationFiltering("estimateDate.lessThan=" + UPDATED_ESTIMATE_DATE, "estimateDate.lessThan=" + DEFAULT_ESTIMATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is greater than
        defaultQuotationFiltering("estimateDate.greaterThan=" + SMALLER_ESTIMATE_DATE, "estimateDate.greaterThan=" + DEFAULT_ESTIMATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil equals to
        defaultQuotationFiltering("validUntil.equals=" + DEFAULT_VALID_UNTIL, "validUntil.equals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil in
        defaultQuotationFiltering(
            "validUntil.in=" + DEFAULT_VALID_UNTIL + "," + UPDATED_VALID_UNTIL,
            "validUntil.in=" + UPDATED_VALID_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil is not null
        defaultQuotationFiltering("validUntil.specified=true", "validUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil is greater than or equal to
        defaultQuotationFiltering(
            "validUntil.greaterThanOrEqual=" + DEFAULT_VALID_UNTIL,
            "validUntil.greaterThanOrEqual=" + UPDATED_VALID_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil is less than or equal to
        defaultQuotationFiltering("validUntil.lessThanOrEqual=" + DEFAULT_VALID_UNTIL, "validUntil.lessThanOrEqual=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil is less than
        defaultQuotationFiltering("validUntil.lessThan=" + UPDATED_VALID_UNTIL, "validUntil.lessThan=" + DEFAULT_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidUntilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validUntil is greater than
        defaultQuotationFiltering("validUntil.greaterThan=" + SMALLER_VALID_UNTIL, "validUntil.greaterThan=" + DEFAULT_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where status equals to
        defaultQuotationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where status in
        defaultQuotationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where status is not null
        defaultQuotationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber equals to
        defaultQuotationFiltering("revisionNumber.equals=" + DEFAULT_REVISION_NUMBER, "revisionNumber.equals=" + UPDATED_REVISION_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber in
        defaultQuotationFiltering(
            "revisionNumber.in=" + DEFAULT_REVISION_NUMBER + "," + UPDATED_REVISION_NUMBER,
            "revisionNumber.in=" + UPDATED_REVISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber is not null
        defaultQuotationFiltering("revisionNumber.specified=true", "revisionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber is greater than or equal to
        defaultQuotationFiltering(
            "revisionNumber.greaterThanOrEqual=" + DEFAULT_REVISION_NUMBER,
            "revisionNumber.greaterThanOrEqual=" + UPDATED_REVISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber is less than or equal to
        defaultQuotationFiltering(
            "revisionNumber.lessThanOrEqual=" + DEFAULT_REVISION_NUMBER,
            "revisionNumber.lessThanOrEqual=" + SMALLER_REVISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber is less than
        defaultQuotationFiltering(
            "revisionNumber.lessThan=" + UPDATED_REVISION_NUMBER,
            "revisionNumber.lessThan=" + DEFAULT_REVISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRevisionNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where revisionNumber is greater than
        defaultQuotationFiltering(
            "revisionNumber.greaterThan=" + SMALLER_REVISION_NUMBER,
            "revisionNumber.greaterThan=" + DEFAULT_REVISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency equals to
        defaultQuotationFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency in
        defaultQuotationFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency is not null
        defaultQuotationFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency contains
        defaultQuotationFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency does not contain
        defaultQuotationFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal equals to
        defaultQuotationFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal in
        defaultQuotationFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal is not null
        defaultQuotationFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal is greater than or equal to
        defaultQuotationFiltering("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal is less than or equal to
        defaultQuotationFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal is less than
        defaultQuotationFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subtotal is greater than
        defaultQuotationFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal equals to
        defaultQuotationFiltering(
            "itemDiscountTotal.equals=" + DEFAULT_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.equals=" + UPDATED_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal in
        defaultQuotationFiltering(
            "itemDiscountTotal.in=" + DEFAULT_ITEM_DISCOUNT_TOTAL + "," + UPDATED_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.in=" + UPDATED_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal is not null
        defaultQuotationFiltering("itemDiscountTotal.specified=true", "itemDiscountTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal is greater than or equal to
        defaultQuotationFiltering(
            "itemDiscountTotal.greaterThanOrEqual=" + DEFAULT_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.greaterThanOrEqual=" + UPDATED_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal is less than or equal to
        defaultQuotationFiltering(
            "itemDiscountTotal.lessThanOrEqual=" + DEFAULT_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.lessThanOrEqual=" + SMALLER_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal is less than
        defaultQuotationFiltering(
            "itemDiscountTotal.lessThan=" + UPDATED_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.lessThan=" + DEFAULT_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByItemDiscountTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where itemDiscountTotal is greater than
        defaultQuotationFiltering(
            "itemDiscountTotal.greaterThan=" + SMALLER_ITEM_DISCOUNT_TOTAL,
            "itemDiscountTotal.greaterThan=" + DEFAULT_ITEM_DISCOUNT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountType equals to
        defaultQuotationFiltering(
            "globalDiscountType.equals=" + DEFAULT_GLOBAL_DISCOUNT_TYPE,
            "globalDiscountType.equals=" + UPDATED_GLOBAL_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountType in
        defaultQuotationFiltering(
            "globalDiscountType.in=" + DEFAULT_GLOBAL_DISCOUNT_TYPE + "," + UPDATED_GLOBAL_DISCOUNT_TYPE,
            "globalDiscountType.in=" + UPDATED_GLOBAL_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountType is not null
        defaultQuotationFiltering("globalDiscountType.specified=true", "globalDiscountType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue equals to
        defaultQuotationFiltering(
            "globalDiscountValue.equals=" + DEFAULT_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.equals=" + UPDATED_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue in
        defaultQuotationFiltering(
            "globalDiscountValue.in=" + DEFAULT_GLOBAL_DISCOUNT_VALUE + "," + UPDATED_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.in=" + UPDATED_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue is not null
        defaultQuotationFiltering("globalDiscountValue.specified=true", "globalDiscountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue is greater than or equal to
        defaultQuotationFiltering(
            "globalDiscountValue.greaterThanOrEqual=" + DEFAULT_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.greaterThanOrEqual=" + UPDATED_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue is less than or equal to
        defaultQuotationFiltering(
            "globalDiscountValue.lessThanOrEqual=" + DEFAULT_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.lessThanOrEqual=" + SMALLER_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue is less than
        defaultQuotationFiltering(
            "globalDiscountValue.lessThan=" + UPDATED_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.lessThan=" + DEFAULT_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountValue is greater than
        defaultQuotationFiltering(
            "globalDiscountValue.greaterThan=" + SMALLER_GLOBAL_DISCOUNT_VALUE,
            "globalDiscountValue.greaterThan=" + DEFAULT_GLOBAL_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount equals to
        defaultQuotationFiltering(
            "globalDiscountAmount.equals=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.equals=" + UPDATED_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount in
        defaultQuotationFiltering(
            "globalDiscountAmount.in=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT + "," + UPDATED_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.in=" + UPDATED_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount is not null
        defaultQuotationFiltering("globalDiscountAmount.specified=true", "globalDiscountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount is greater than or equal to
        defaultQuotationFiltering(
            "globalDiscountAmount.greaterThanOrEqual=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.greaterThanOrEqual=" + UPDATED_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount is less than or equal to
        defaultQuotationFiltering(
            "globalDiscountAmount.lessThanOrEqual=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.lessThanOrEqual=" + SMALLER_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount is less than
        defaultQuotationFiltering(
            "globalDiscountAmount.lessThan=" + UPDATED_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.lessThan=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByGlobalDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where globalDiscountAmount is greater than
        defaultQuotationFiltering(
            "globalDiscountAmount.greaterThan=" + SMALLER_GLOBAL_DISCOUNT_AMOUNT,
            "globalDiscountAmount.greaterThan=" + DEFAULT_GLOBAL_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount equals to
        defaultQuotationFiltering("taxableAmount.equals=" + DEFAULT_TAXABLE_AMOUNT, "taxableAmount.equals=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount in
        defaultQuotationFiltering(
            "taxableAmount.in=" + DEFAULT_TAXABLE_AMOUNT + "," + UPDATED_TAXABLE_AMOUNT,
            "taxableAmount.in=" + UPDATED_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount is not null
        defaultQuotationFiltering("taxableAmount.specified=true", "taxableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount is greater than or equal to
        defaultQuotationFiltering(
            "taxableAmount.greaterThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT,
            "taxableAmount.greaterThanOrEqual=" + UPDATED_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount is less than or equal to
        defaultQuotationFiltering(
            "taxableAmount.lessThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT,
            "taxableAmount.lessThanOrEqual=" + SMALLER_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount is less than
        defaultQuotationFiltering("taxableAmount.lessThan=" + UPDATED_TAXABLE_AMOUNT, "taxableAmount.lessThan=" + DEFAULT_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTaxableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where taxableAmount is greater than
        defaultQuotationFiltering(
            "taxableAmount.greaterThan=" + SMALLER_TAXABLE_AMOUNT,
            "taxableAmount.greaterThan=" + DEFAULT_TAXABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax equals to
        defaultQuotationFiltering("totalTax.equals=" + DEFAULT_TOTAL_TAX, "totalTax.equals=" + UPDATED_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax in
        defaultQuotationFiltering("totalTax.in=" + DEFAULT_TOTAL_TAX + "," + UPDATED_TOTAL_TAX, "totalTax.in=" + UPDATED_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax is not null
        defaultQuotationFiltering("totalTax.specified=true", "totalTax.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax is greater than or equal to
        defaultQuotationFiltering("totalTax.greaterThanOrEqual=" + DEFAULT_TOTAL_TAX, "totalTax.greaterThanOrEqual=" + UPDATED_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax is less than or equal to
        defaultQuotationFiltering("totalTax.lessThanOrEqual=" + DEFAULT_TOTAL_TAX, "totalTax.lessThanOrEqual=" + SMALLER_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax is less than
        defaultQuotationFiltering("totalTax.lessThan=" + UPDATED_TOTAL_TAX, "totalTax.lessThan=" + DEFAULT_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTax is greater than
        defaultQuotationFiltering("totalTax.greaterThan=" + SMALLER_TOTAL_TAX, "totalTax.greaterThan=" + DEFAULT_TOTAL_TAX);
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount equals to
        defaultQuotationFiltering("shippingAmount.equals=" + DEFAULT_SHIPPING_AMOUNT, "shippingAmount.equals=" + UPDATED_SHIPPING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount in
        defaultQuotationFiltering(
            "shippingAmount.in=" + DEFAULT_SHIPPING_AMOUNT + "," + UPDATED_SHIPPING_AMOUNT,
            "shippingAmount.in=" + UPDATED_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount is not null
        defaultQuotationFiltering("shippingAmount.specified=true", "shippingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount is greater than or equal to
        defaultQuotationFiltering(
            "shippingAmount.greaterThanOrEqual=" + DEFAULT_SHIPPING_AMOUNT,
            "shippingAmount.greaterThanOrEqual=" + UPDATED_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount is less than or equal to
        defaultQuotationFiltering(
            "shippingAmount.lessThanOrEqual=" + DEFAULT_SHIPPING_AMOUNT,
            "shippingAmount.lessThanOrEqual=" + SMALLER_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount is less than
        defaultQuotationFiltering(
            "shippingAmount.lessThan=" + UPDATED_SHIPPING_AMOUNT,
            "shippingAmount.lessThan=" + DEFAULT_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByShippingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where shippingAmount is greater than
        defaultQuotationFiltering(
            "shippingAmount.greaterThan=" + SMALLER_SHIPPING_AMOUNT,
            "shippingAmount.greaterThan=" + DEFAULT_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount equals to
        defaultQuotationFiltering(
            "otherChargesAmount.equals=" + DEFAULT_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.equals=" + UPDATED_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount in
        defaultQuotationFiltering(
            "otherChargesAmount.in=" + DEFAULT_OTHER_CHARGES_AMOUNT + "," + UPDATED_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.in=" + UPDATED_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount is not null
        defaultQuotationFiltering("otherChargesAmount.specified=true", "otherChargesAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount is greater than or equal to
        defaultQuotationFiltering(
            "otherChargesAmount.greaterThanOrEqual=" + DEFAULT_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.greaterThanOrEqual=" + UPDATED_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount is less than or equal to
        defaultQuotationFiltering(
            "otherChargesAmount.lessThanOrEqual=" + DEFAULT_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.lessThanOrEqual=" + SMALLER_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount is less than
        defaultQuotationFiltering(
            "otherChargesAmount.lessThan=" + UPDATED_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.lessThan=" + DEFAULT_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByOtherChargesAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where otherChargesAmount is greater than
        defaultQuotationFiltering(
            "otherChargesAmount.greaterThan=" + SMALLER_OTHER_CHARGES_AMOUNT,
            "otherChargesAmount.greaterThan=" + DEFAULT_OTHER_CHARGES_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount equals to
        defaultQuotationFiltering("roundOffAmount.equals=" + DEFAULT_ROUND_OFF_AMOUNT, "roundOffAmount.equals=" + UPDATED_ROUND_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount in
        defaultQuotationFiltering(
            "roundOffAmount.in=" + DEFAULT_ROUND_OFF_AMOUNT + "," + UPDATED_ROUND_OFF_AMOUNT,
            "roundOffAmount.in=" + UPDATED_ROUND_OFF_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount is not null
        defaultQuotationFiltering("roundOffAmount.specified=true", "roundOffAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount is greater than or equal to
        defaultQuotationFiltering(
            "roundOffAmount.greaterThanOrEqual=" + DEFAULT_ROUND_OFF_AMOUNT,
            "roundOffAmount.greaterThanOrEqual=" + UPDATED_ROUND_OFF_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount is less than or equal to
        defaultQuotationFiltering(
            "roundOffAmount.lessThanOrEqual=" + DEFAULT_ROUND_OFF_AMOUNT,
            "roundOffAmount.lessThanOrEqual=" + SMALLER_ROUND_OFF_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount is less than
        defaultQuotationFiltering(
            "roundOffAmount.lessThan=" + UPDATED_ROUND_OFF_AMOUNT,
            "roundOffAmount.lessThan=" + DEFAULT_ROUND_OFF_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByRoundOffAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where roundOffAmount is greater than
        defaultQuotationFiltering(
            "roundOffAmount.greaterThan=" + SMALLER_ROUND_OFF_AMOUNT,
            "roundOffAmount.greaterThan=" + DEFAULT_ROUND_OFF_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount equals to
        defaultQuotationFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount in
        defaultQuotationFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount is not null
        defaultQuotationFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount is greater than or equal to
        defaultQuotationFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount is less than or equal to
        defaultQuotationFiltering(
            "totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount is less than
        defaultQuotationFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalAmount is greater than
        defaultQuotationFiltering("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT, "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title equals to
        defaultQuotationFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuotationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title in
        defaultQuotationFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuotationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title is not null
        defaultQuotationFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title contains
        defaultQuotationFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuotationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title does not contain
        defaultQuotationFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllQuotationsByHeaderNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where headerNotes equals to
        defaultQuotationFiltering("headerNotes.equals=" + DEFAULT_HEADER_NOTES, "headerNotes.equals=" + UPDATED_HEADER_NOTES);
    }

    @Test
    @Transactional
    void getAllQuotationsByHeaderNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where headerNotes in
        defaultQuotationFiltering(
            "headerNotes.in=" + DEFAULT_HEADER_NOTES + "," + UPDATED_HEADER_NOTES,
            "headerNotes.in=" + UPDATED_HEADER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByHeaderNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where headerNotes is not null
        defaultQuotationFiltering("headerNotes.specified=true", "headerNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByHeaderNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where headerNotes contains
        defaultQuotationFiltering("headerNotes.contains=" + DEFAULT_HEADER_NOTES, "headerNotes.contains=" + UPDATED_HEADER_NOTES);
    }

    @Test
    @Transactional
    void getAllQuotationsByHeaderNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where headerNotes does not contain
        defaultQuotationFiltering(
            "headerNotes.doesNotContain=" + UPDATED_HEADER_NOTES,
            "headerNotes.doesNotContain=" + DEFAULT_HEADER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByFooterNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where footerNotes equals to
        defaultQuotationFiltering("footerNotes.equals=" + DEFAULT_FOOTER_NOTES, "footerNotes.equals=" + UPDATED_FOOTER_NOTES);
    }

    @Test
    @Transactional
    void getAllQuotationsByFooterNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where footerNotes in
        defaultQuotationFiltering(
            "footerNotes.in=" + DEFAULT_FOOTER_NOTES + "," + UPDATED_FOOTER_NOTES,
            "footerNotes.in=" + UPDATED_FOOTER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByFooterNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where footerNotes is not null
        defaultQuotationFiltering("footerNotes.specified=true", "footerNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByFooterNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where footerNotes contains
        defaultQuotationFiltering("footerNotes.contains=" + DEFAULT_FOOTER_NOTES, "footerNotes.contains=" + UPDATED_FOOTER_NOTES);
    }

    @Test
    @Transactional
    void getAllQuotationsByFooterNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where footerNotes does not contain
        defaultQuotationFiltering(
            "footerNotes.doesNotContain=" + UPDATED_FOOTER_NOTES,
            "footerNotes.doesNotContain=" + DEFAULT_FOOTER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTermsAndConditionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where termsAndConditions equals to
        defaultQuotationFiltering(
            "termsAndConditions.equals=" + DEFAULT_TERMS_AND_CONDITIONS,
            "termsAndConditions.equals=" + UPDATED_TERMS_AND_CONDITIONS
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTermsAndConditionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where termsAndConditions in
        defaultQuotationFiltering(
            "termsAndConditions.in=" + DEFAULT_TERMS_AND_CONDITIONS + "," + UPDATED_TERMS_AND_CONDITIONS,
            "termsAndConditions.in=" + UPDATED_TERMS_AND_CONDITIONS
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTermsAndConditionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where termsAndConditions is not null
        defaultQuotationFiltering("termsAndConditions.specified=true", "termsAndConditions.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTermsAndConditionsContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where termsAndConditions contains
        defaultQuotationFiltering(
            "termsAndConditions.contains=" + DEFAULT_TERMS_AND_CONDITIONS,
            "termsAndConditions.contains=" + UPDATED_TERMS_AND_CONDITIONS
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTermsAndConditionsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where termsAndConditions does not contain
        defaultQuotationFiltering(
            "termsAndConditions.doesNotContain=" + UPDATED_TERMS_AND_CONDITIONS,
            "termsAndConditions.doesNotContain=" + DEFAULT_TERMS_AND_CONDITIONS
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber equals to
        defaultQuotationFiltering(
            "referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber in
        defaultQuotationFiltering(
            "referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.in=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber is not null
        defaultQuotationFiltering("referenceNumber.specified=true", "referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber contains
        defaultQuotationFiltering(
            "referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber does not contain
        defaultQuotationFiltering(
            "referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByLastSentAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where lastSentAt equals to
        defaultQuotationFiltering("lastSentAt.equals=" + DEFAULT_LAST_SENT_AT, "lastSentAt.equals=" + UPDATED_LAST_SENT_AT);
    }

    @Test
    @Transactional
    void getAllQuotationsByLastSentAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where lastSentAt in
        defaultQuotationFiltering(
            "lastSentAt.in=" + DEFAULT_LAST_SENT_AT + "," + UPDATED_LAST_SENT_AT,
            "lastSentAt.in=" + UPDATED_LAST_SENT_AT
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByLastSentAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where lastSentAt is not null
        defaultQuotationFiltering("lastSentAt.specified=true", "lastSentAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfTemplateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfTemplateCode equals to
        defaultQuotationFiltering(
            "pdfTemplateCode.equals=" + DEFAULT_PDF_TEMPLATE_CODE,
            "pdfTemplateCode.equals=" + UPDATED_PDF_TEMPLATE_CODE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfTemplateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfTemplateCode in
        defaultQuotationFiltering(
            "pdfTemplateCode.in=" + DEFAULT_PDF_TEMPLATE_CODE + "," + UPDATED_PDF_TEMPLATE_CODE,
            "pdfTemplateCode.in=" + UPDATED_PDF_TEMPLATE_CODE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfTemplateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfTemplateCode is not null
        defaultQuotationFiltering("pdfTemplateCode.specified=true", "pdfTemplateCode.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfTemplateCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfTemplateCode contains
        defaultQuotationFiltering(
            "pdfTemplateCode.contains=" + DEFAULT_PDF_TEMPLATE_CODE,
            "pdfTemplateCode.contains=" + UPDATED_PDF_TEMPLATE_CODE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfTemplateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfTemplateCode does not contain
        defaultQuotationFiltering(
            "pdfTemplateCode.doesNotContain=" + UPDATED_PDF_TEMPLATE_CODE,
            "pdfTemplateCode.doesNotContain=" + DEFAULT_PDF_TEMPLATE_CODE
        );
    }

    @Test
    @Transactional
    void getAllQuotationsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        quotation.setTenant(tenant);
        quotationRepository.saveAndFlush(quotation);
        Long tenantId = tenant.getId();
        // Get all the quotationList where tenant equals to tenantId
        defaultQuotationShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the quotationList where tenant equals to (tenantId + 1)
        defaultQuotationShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        quotation.setCustomer(customer);
        quotationRepository.saveAndFlush(quotation);
        Long customerId = customer.getId();
        // Get all the quotationList where customer equals to customerId
        defaultQuotationShouldBeFound("customerId.equals=" + customerId);

        // Get all the quotationList where customer equals to (customerId + 1)
        defaultQuotationShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        quotation.setContact(contact);
        quotationRepository.saveAndFlush(quotation);
        Long contactId = contact.getId();
        // Get all the quotationList where contact equals to contactId
        defaultQuotationShouldBeFound("contactId.equals=" + contactId);

        // Get all the quotationList where contact equals to (contactId + 1)
        defaultQuotationShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationsByLeadIsEqualToSomething() throws Exception {
        Lead lead;
        if (TestUtil.findAll(em, Lead.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            lead = LeadResourceIT.createEntity(em);
        } else {
            lead = TestUtil.findAll(em, Lead.class).get(0);
        }
        em.persist(lead);
        em.flush();
        quotation.setLead(lead);
        quotationRepository.saveAndFlush(quotation);
        Long leadId = lead.getId();
        // Get all the quotationList where lead equals to leadId
        defaultQuotationShouldBeFound("leadId.equals=" + leadId);

        // Get all the quotationList where lead equals to (leadId + 1)
        defaultQuotationShouldNotBeFound("leadId.equals=" + (leadId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationsByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        quotation.setCreatedByUser(createdByUser);
        quotationRepository.saveAndFlush(quotation);
        Long createdByUserId = createdByUser.getId();
        // Get all the quotationList where createdByUser equals to createdByUserId
        defaultQuotationShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the quotationList where createdByUser equals to (createdByUserId + 1)
        defaultQuotationShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    private void defaultQuotationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuotationShouldBeFound(shouldBeFound);
        defaultQuotationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuotationShouldBeFound(String filter) throws Exception {
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quoteNumber").value(hasItem(DEFAULT_QUOTE_NUMBER)))
            .andExpect(jsonPath("$.[*].estimateDate").value(hasItem(DEFAULT_ESTIMATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].revisionNumber").value(hasItem(DEFAULT_REVISION_NUMBER)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].itemDiscountTotal").value(hasItem(sameNumber(DEFAULT_ITEM_DISCOUNT_TOTAL))))
            .andExpect(jsonPath("$.[*].globalDiscountType").value(hasItem(DEFAULT_GLOBAL_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].globalDiscountValue").value(hasItem(sameNumber(DEFAULT_GLOBAL_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].globalDiscountAmount").value(hasItem(sameNumber(DEFAULT_GLOBAL_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTax").value(hasItem(sameNumber(DEFAULT_TOTAL_TAX))))
            .andExpect(jsonPath("$.[*].shippingAmount").value(hasItem(sameNumber(DEFAULT_SHIPPING_AMOUNT))))
            .andExpect(jsonPath("$.[*].otherChargesAmount").value(hasItem(sameNumber(DEFAULT_OTHER_CHARGES_AMOUNT))))
            .andExpect(jsonPath("$.[*].roundOffAmount").value(hasItem(sameNumber(DEFAULT_ROUND_OFF_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].headerNotes").value(hasItem(DEFAULT_HEADER_NOTES)))
            .andExpect(jsonPath("$.[*].footerNotes").value(hasItem(DEFAULT_FOOTER_NOTES)))
            .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].lastSentAt").value(hasItem(DEFAULT_LAST_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].pdfTemplateCode").value(hasItem(DEFAULT_PDF_TEMPLATE_CODE)));

        // Check, that the count call also returns 1
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuotationShouldNotBeFound(String filter) throws Exception {
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuotation() throws Exception {
        // Get the quotation
        restQuotationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findById(quotation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotation are not directly saved in db
        em.detach(updatedQuotation);
        updatedQuotation
            .quoteNumber(UPDATED_QUOTE_NUMBER)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .revisionNumber(UPDATED_REVISION_NUMBER)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .itemDiscountTotal(UPDATED_ITEM_DISCOUNT_TOTAL)
            .globalDiscountType(UPDATED_GLOBAL_DISCOUNT_TYPE)
            .globalDiscountValue(UPDATED_GLOBAL_DISCOUNT_VALUE)
            .globalDiscountAmount(UPDATED_GLOBAL_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .totalTax(UPDATED_TOTAL_TAX)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .otherChargesAmount(UPDATED_OTHER_CHARGES_AMOUNT)
            .roundOffAmount(UPDATED_ROUND_OFF_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .title(UPDATED_TITLE)
            .headerNotes(UPDATED_HEADER_NOTES)
            .footerNotes(UPDATED_FOOTER_NOTES)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .lastSentAt(UPDATED_LAST_SENT_AT)
            .pdfTemplateCode(UPDATED_PDF_TEMPLATE_CODE);
        QuotationDTO quotationDTO = quotationMapper.toDto(updatedQuotation);

        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotationToMatchAllProperties(updatedQuotation);
    }

    @Test
    @Transactional
    void putNonExistingQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .status(UPDATED_STATUS)
            .globalDiscountValue(UPDATED_GLOBAL_DISCOUNT_VALUE)
            .globalDiscountAmount(UPDATED_GLOBAL_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .roundOffAmount(UPDATED_ROUND_OFF_AMOUNT)
            .title(UPDATED_TITLE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .pdfTemplateCode(UPDATED_PDF_TEMPLATE_CODE);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotation, quotation),
            getPersistedQuotation(quotation)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .quoteNumber(UPDATED_QUOTE_NUMBER)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .validUntil(UPDATED_VALID_UNTIL)
            .status(UPDATED_STATUS)
            .revisionNumber(UPDATED_REVISION_NUMBER)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .itemDiscountTotal(UPDATED_ITEM_DISCOUNT_TOTAL)
            .globalDiscountType(UPDATED_GLOBAL_DISCOUNT_TYPE)
            .globalDiscountValue(UPDATED_GLOBAL_DISCOUNT_VALUE)
            .globalDiscountAmount(UPDATED_GLOBAL_DISCOUNT_AMOUNT)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .totalTax(UPDATED_TOTAL_TAX)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .otherChargesAmount(UPDATED_OTHER_CHARGES_AMOUNT)
            .roundOffAmount(UPDATED_ROUND_OFF_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .title(UPDATED_TITLE)
            .headerNotes(UPDATED_HEADER_NOTES)
            .footerNotes(UPDATED_FOOTER_NOTES)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .lastSentAt(UPDATED_LAST_SENT_AT)
            .pdfTemplateCode(UPDATED_PDF_TEMPLATE_CODE);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationUpdatableFieldsEquals(partialUpdatedQuotation, getPersistedQuotation(partialUpdatedQuotation));
    }

    @Test
    @Transactional
    void patchNonExistingQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotation
        restQuotationMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotationRepository.count();
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

    protected Quotation getPersistedQuotation(Quotation quotation) {
        return quotationRepository.findById(quotation.getId()).orElseThrow();
    }

    protected void assertPersistedQuotationToMatchAllProperties(Quotation expectedQuotation) {
        assertQuotationAllPropertiesEquals(expectedQuotation, getPersistedQuotation(expectedQuotation));
    }

    protected void assertPersistedQuotationToMatchUpdatableProperties(Quotation expectedQuotation) {
        assertQuotationAllUpdatablePropertiesEquals(expectedQuotation, getPersistedQuotation(expectedQuotation));
    }
}
