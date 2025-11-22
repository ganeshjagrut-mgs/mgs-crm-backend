package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Quotation;
import com.crm.domain.User;
import com.crm.domain.enumeration.DiscountLevelTypeEnum;
import com.crm.domain.enumeration.DiscountTypeEnum;
import com.crm.domain.enumeration.PDFGenerationStatus;
import com.crm.domain.enumeration.PriceDataSourceEnum;
import com.crm.domain.enumeration.TestReportEmailStatus;
import com.crm.repository.QuotationRepository;
import com.crm.service.dto.QuotationDTO;
import com.crm.service.mapper.QuotationMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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

    private static final String DEFAULT_QUOTATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_QUOTATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_QUOTATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_REFERENCE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REFERENCE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ESTIMATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALIDITY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDITY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDITIONAL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_NOTE = "BBBBBBBBBB";

    private static final DiscountLevelTypeEnum DEFAULT_DISCOUNT_LEVEL_TYPE = DiscountLevelTypeEnum.ITEM_LEVEL;
    private static final DiscountLevelTypeEnum UPDATED_DISCOUNT_LEVEL_TYPE = DiscountLevelTypeEnum.TRANSACTION_LEVEL;

    private static final DiscountTypeEnum DEFAULT_DISCOUNT_TYPE = DiscountTypeEnum.PERCENTAGE;
    private static final DiscountTypeEnum UPDATED_DISCOUNT_TYPE = DiscountTypeEnum.FIXED_AMOUNT;

    private static final Double DEFAULT_DISCOUNT_TYPE_VALUE = 1D;
    private static final Double UPDATED_DISCOUNT_TYPE_VALUE = 2D;
    private static final Double SMALLER_DISCOUNT_TYPE_VALUE = 1D - 1D;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Double DEFAULT_SUB_TOTAL = 1D;
    private static final Double UPDATED_SUB_TOTAL = 2D;
    private static final Double SMALLER_SUB_TOTAL = 1D - 1D;

    private static final Double DEFAULT_GRAND_TOTAL = 1D;
    private static final Double UPDATED_GRAND_TOTAL = 2D;
    private static final Double SMALLER_GRAND_TOTAL = 1D - 1D;

    private static final Double DEFAULT_TOTAL_TAX_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_TAX_AMOUNT = 2D;
    private static final Double SMALLER_TOTAL_TAX_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_ADJUSTMENT_AMOUNT = 1D;
    private static final Double UPDATED_ADJUSTMENT_AMOUNT = 2D;
    private static final Double SMALLER_ADJUSTMENT_AMOUNT = 1D - 1D;

    private static final String DEFAULT_STATUS_REASON = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_REASON = "BBBBBBBBBB";

    private static final PDFGenerationStatus DEFAULT_PDF_GENERATION_STATUS = PDFGenerationStatus.PENDING;
    private static final PDFGenerationStatus UPDATED_PDF_GENERATION_STATUS = PDFGenerationStatus.IN_PROGRESS;

    private static final TestReportEmailStatus DEFAULT_EMAIL_STATUS = TestReportEmailStatus.NOT_SENT;
    private static final TestReportEmailStatus UPDATED_EMAIL_STATUS = TestReportEmailStatus.SENT;

    private static final String DEFAULT_EMAIL_FAILURE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_FAILURE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_PARAGRAPH = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_PARAGRAPH = "BBBBBBBBBB";

    private static final UUID DEFAULT_CORRELATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_CORRELATION_ID = UUID.randomUUID();

    private static final Instant DEFAULT_APPROVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PriceDataSourceEnum DEFAULT_PRICE_DATA_SOURCE = PriceDataSourceEnum.MANUAL;
    private static final PriceDataSourceEnum UPDATED_PRICE_DATA_SOURCE = PriceDataSourceEnum.PRICE_LIST;

    private static final String ENTITY_API_URL = "/api/quotations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createEntity(EntityManager em) {
        Quotation quotation = new Quotation()
            .quotationNumber(DEFAULT_QUOTATION_NUMBER)
            .quotationDate(DEFAULT_QUOTATION_DATE)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .referenceDate(DEFAULT_REFERENCE_DATE)
            .estimateDate(DEFAULT_ESTIMATE_DATE)
            .subject(DEFAULT_SUBJECT)
            .validity(DEFAULT_VALIDITY)
            .additionalNote(DEFAULT_ADDITIONAL_NOTE)
            .discountLevelType(DEFAULT_DISCOUNT_LEVEL_TYPE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountTypeValue(DEFAULT_DISCOUNT_TYPE_VALUE)
            .currency(DEFAULT_CURRENCY)
            .subTotal(DEFAULT_SUB_TOTAL)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .totalTaxAmount(DEFAULT_TOTAL_TAX_AMOUNT)
            .adjustmentAmount(DEFAULT_ADJUSTMENT_AMOUNT)
            .statusReason(DEFAULT_STATUS_REASON)
            .pdfGenerationStatus(DEFAULT_PDF_GENERATION_STATUS)
            .emailStatus(DEFAULT_EMAIL_STATUS)
            .emailFailureReason(DEFAULT_EMAIL_FAILURE_REASON)
            .customParagraph(DEFAULT_CUSTOM_PARAGRAPH)
            .correlationId(DEFAULT_CORRELATION_ID)
            .approvedAt(DEFAULT_APPROVED_AT)
            .priceDataSource(DEFAULT_PRICE_DATA_SOURCE);
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
        Quotation quotation = new Quotation()
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .subject(UPDATED_SUBJECT)
            .validity(UPDATED_VALIDITY)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .discountLevelType(UPDATED_DISCOUNT_LEVEL_TYPE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountTypeValue(UPDATED_DISCOUNT_TYPE_VALUE)
            .currency(UPDATED_CURRENCY)
            .subTotal(UPDATED_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .adjustmentAmount(UPDATED_ADJUSTMENT_AMOUNT)
            .statusReason(UPDATED_STATUS_REASON)
            .pdfGenerationStatus(UPDATED_PDF_GENERATION_STATUS)
            .emailStatus(UPDATED_EMAIL_STATUS)
            .emailFailureReason(UPDATED_EMAIL_FAILURE_REASON)
            .customParagraph(UPDATED_CUSTOM_PARAGRAPH)
            .correlationId(UPDATED_CORRELATION_ID)
            .approvedAt(UPDATED_APPROVED_AT)
            .priceDataSource(UPDATED_PRICE_DATA_SOURCE);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        quotation.setCustomer(customer);
        return quotation;
    }

    @BeforeEach
    public void initTest() {
        quotation = createEntity(em);
    }

    @Test
    @Transactional
    void createQuotation() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();
        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);
        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isCreated());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate + 1);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(DEFAULT_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(DEFAULT_QUOTATION_DATE);
        assertThat(testQuotation.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testQuotation.getReferenceDate()).isEqualTo(DEFAULT_REFERENCE_DATE);
        assertThat(testQuotation.getEstimateDate()).isEqualTo(DEFAULT_ESTIMATE_DATE);
        assertThat(testQuotation.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testQuotation.getValidity()).isEqualTo(DEFAULT_VALIDITY);
        assertThat(testQuotation.getAdditionalNote()).isEqualTo(DEFAULT_ADDITIONAL_NOTE);
        assertThat(testQuotation.getDiscountLevelType()).isEqualTo(DEFAULT_DISCOUNT_LEVEL_TYPE);
        assertThat(testQuotation.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testQuotation.getDiscountTypeValue()).isEqualTo(DEFAULT_DISCOUNT_TYPE_VALUE);
        assertThat(testQuotation.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testQuotation.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testQuotation.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testQuotation.getTotalTaxAmount()).isEqualTo(DEFAULT_TOTAL_TAX_AMOUNT);
        assertThat(testQuotation.getAdjustmentAmount()).isEqualTo(DEFAULT_ADJUSTMENT_AMOUNT);
        assertThat(testQuotation.getStatusReason()).isEqualTo(DEFAULT_STATUS_REASON);
        assertThat(testQuotation.getPdfGenerationStatus()).isEqualTo(DEFAULT_PDF_GENERATION_STATUS);
        assertThat(testQuotation.getEmailStatus()).isEqualTo(DEFAULT_EMAIL_STATUS);
        assertThat(testQuotation.getEmailFailureReason()).isEqualTo(DEFAULT_EMAIL_FAILURE_REASON);
        assertThat(testQuotation.getCustomParagraph()).isEqualTo(DEFAULT_CUSTOM_PARAGRAPH);
        assertThat(testQuotation.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
        assertThat(testQuotation.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testQuotation.getPriceDataSource()).isEqualTo(DEFAULT_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void createQuotationWithExistingId() throws Exception {
        // Create the Quotation with an existing ID
        quotation.setId(1L);
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuotations() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quotationNumber").value(hasItem(DEFAULT_QUOTATION_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationDate").value(hasItem(DEFAULT_QUOTATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceDate").value(hasItem(DEFAULT_REFERENCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimateDate").value(hasItem(DEFAULT_ESTIMATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].validity").value(hasItem(DEFAULT_VALIDITY.toString())))
            .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].discountLevelType").value(hasItem(DEFAULT_DISCOUNT_LEVEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountTypeValue").value(hasItem(DEFAULT_DISCOUNT_TYPE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTaxAmount").value(hasItem(DEFAULT_TOTAL_TAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].adjustmentAmount").value(hasItem(DEFAULT_ADJUSTMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].statusReason").value(hasItem(DEFAULT_STATUS_REASON)))
            .andExpect(jsonPath("$.[*].pdfGenerationStatus").value(hasItem(DEFAULT_PDF_GENERATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emailStatus").value(hasItem(DEFAULT_EMAIL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emailFailureReason").value(hasItem(DEFAULT_EMAIL_FAILURE_REASON)))
            .andExpect(jsonPath("$.[*].customParagraph").value(hasItem(DEFAULT_CUSTOM_PARAGRAPH.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].priceDataSource").value(hasItem(DEFAULT_PRICE_DATA_SOURCE.toString())));
    }

    @Test
    @Transactional
    void getQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL_ID, quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.quotationNumber").value(DEFAULT_QUOTATION_NUMBER))
            .andExpect(jsonPath("$.quotationDate").value(DEFAULT_QUOTATION_DATE.toString()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.referenceDate").value(DEFAULT_REFERENCE_DATE.toString()))
            .andExpect(jsonPath("$.estimateDate").value(DEFAULT_ESTIMATE_DATE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.validity").value(DEFAULT_VALIDITY.toString()))
            .andExpect(jsonPath("$.additionalNote").value(DEFAULT_ADDITIONAL_NOTE.toString()))
            .andExpect(jsonPath("$.discountLevelType").value(DEFAULT_DISCOUNT_LEVEL_TYPE.toString()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountTypeValue").value(DEFAULT_DISCOUNT_TYPE_VALUE.doubleValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.grandTotal").value(DEFAULT_GRAND_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.totalTaxAmount").value(DEFAULT_TOTAL_TAX_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.adjustmentAmount").value(DEFAULT_ADJUSTMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.statusReason").value(DEFAULT_STATUS_REASON))
            .andExpect(jsonPath("$.pdfGenerationStatus").value(DEFAULT_PDF_GENERATION_STATUS.toString()))
            .andExpect(jsonPath("$.emailStatus").value(DEFAULT_EMAIL_STATUS.toString()))
            .andExpect(jsonPath("$.emailFailureReason").value(DEFAULT_EMAIL_FAILURE_REASON))
            .andExpect(jsonPath("$.customParagraph").value(DEFAULT_CUSTOM_PARAGRAPH.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()))
            .andExpect(jsonPath("$.approvedAt").value(DEFAULT_APPROVED_AT.toString()))
            .andExpect(jsonPath("$.priceDataSource").value(DEFAULT_PRICE_DATA_SOURCE.toString()));
    }

    @Test
    @Transactional
    void getQuotationsByIdFiltering() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        Long id = quotation.getId();

        defaultQuotationShouldBeFound("id.equals=" + id);
        defaultQuotationShouldNotBeFound("id.notEquals=" + id);

        defaultQuotationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuotationShouldNotBeFound("id.greaterThan=" + id);

        defaultQuotationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuotationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber equals to DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.equals=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber equals to UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.equals=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber in DEFAULT_QUOTATION_NUMBER or UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.in=" + DEFAULT_QUOTATION_NUMBER + "," + UPDATED_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber equals to UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.in=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber is not null
        defaultQuotationShouldBeFound("quotationNumber.specified=true");

        // Get all the quotationList where quotationNumber is null
        defaultQuotationShouldNotBeFound("quotationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationNumberContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber contains DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.contains=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber contains UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.contains=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationNumber does not contain DEFAULT_QUOTATION_NUMBER
        defaultQuotationShouldNotBeFound("quotationNumber.doesNotContain=" + DEFAULT_QUOTATION_NUMBER);

        // Get all the quotationList where quotationNumber does not contain UPDATED_QUOTATION_NUMBER
        defaultQuotationShouldBeFound("quotationNumber.doesNotContain=" + UPDATED_QUOTATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate equals to DEFAULT_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.equals=" + DEFAULT_QUOTATION_DATE);

        // Get all the quotationList where quotationDate equals to UPDATED_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.equals=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate in DEFAULT_QUOTATION_DATE or UPDATED_QUOTATION_DATE
        defaultQuotationShouldBeFound("quotationDate.in=" + DEFAULT_QUOTATION_DATE + "," + UPDATED_QUOTATION_DATE);

        // Get all the quotationList where quotationDate equals to UPDATED_QUOTATION_DATE
        defaultQuotationShouldNotBeFound("quotationDate.in=" + UPDATED_QUOTATION_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where quotationDate is not null
        defaultQuotationShouldBeFound("quotationDate.specified=true");

        // Get all the quotationList where quotationDate is null
        defaultQuotationShouldNotBeFound("quotationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber equals to DEFAULT_REFERENCE_NUMBER
        defaultQuotationShouldBeFound("referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the quotationList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultQuotationShouldNotBeFound("referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber in DEFAULT_REFERENCE_NUMBER or UPDATED_REFERENCE_NUMBER
        defaultQuotationShouldBeFound("referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER);

        // Get all the quotationList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultQuotationShouldNotBeFound("referenceNumber.in=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber is not null
        defaultQuotationShouldBeFound("referenceNumber.specified=true");

        // Get all the quotationList where referenceNumber is null
        defaultQuotationShouldNotBeFound("referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber contains DEFAULT_REFERENCE_NUMBER
        defaultQuotationShouldBeFound("referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the quotationList where referenceNumber contains UPDATED_REFERENCE_NUMBER
        defaultQuotationShouldNotBeFound("referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceNumber does not contain DEFAULT_REFERENCE_NUMBER
        defaultQuotationShouldNotBeFound("referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the quotationList where referenceNumber does not contain UPDATED_REFERENCE_NUMBER
        defaultQuotationShouldBeFound("referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceDate equals to DEFAULT_REFERENCE_DATE
        defaultQuotationShouldBeFound("referenceDate.equals=" + DEFAULT_REFERENCE_DATE);

        // Get all the quotationList where referenceDate equals to UPDATED_REFERENCE_DATE
        defaultQuotationShouldNotBeFound("referenceDate.equals=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceDate in DEFAULT_REFERENCE_DATE or UPDATED_REFERENCE_DATE
        defaultQuotationShouldBeFound("referenceDate.in=" + DEFAULT_REFERENCE_DATE + "," + UPDATED_REFERENCE_DATE);

        // Get all the quotationList where referenceDate equals to UPDATED_REFERENCE_DATE
        defaultQuotationShouldNotBeFound("referenceDate.in=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByReferenceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where referenceDate is not null
        defaultQuotationShouldBeFound("referenceDate.specified=true");

        // Get all the quotationList where referenceDate is null
        defaultQuotationShouldNotBeFound("referenceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate equals to DEFAULT_ESTIMATE_DATE
        defaultQuotationShouldBeFound("estimateDate.equals=" + DEFAULT_ESTIMATE_DATE);

        // Get all the quotationList where estimateDate equals to UPDATED_ESTIMATE_DATE
        defaultQuotationShouldNotBeFound("estimateDate.equals=" + UPDATED_ESTIMATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate in DEFAULT_ESTIMATE_DATE or UPDATED_ESTIMATE_DATE
        defaultQuotationShouldBeFound("estimateDate.in=" + DEFAULT_ESTIMATE_DATE + "," + UPDATED_ESTIMATE_DATE);

        // Get all the quotationList where estimateDate equals to UPDATED_ESTIMATE_DATE
        defaultQuotationShouldNotBeFound("estimateDate.in=" + UPDATED_ESTIMATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotationsByEstimateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where estimateDate is not null
        defaultQuotationShouldBeFound("estimateDate.specified=true");

        // Get all the quotationList where estimateDate is null
        defaultQuotationShouldNotBeFound("estimateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subject equals to DEFAULT_SUBJECT
        defaultQuotationShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the quotationList where subject equals to UPDATED_SUBJECT
        defaultQuotationShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultQuotationShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the quotationList where subject equals to UPDATED_SUBJECT
        defaultQuotationShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subject is not null
        defaultQuotationShouldBeFound("subject.specified=true");

        // Get all the quotationList where subject is null
        defaultQuotationShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subject contains DEFAULT_SUBJECT
        defaultQuotationShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the quotationList where subject contains UPDATED_SUBJECT
        defaultQuotationShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subject does not contain DEFAULT_SUBJECT
        defaultQuotationShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the quotationList where subject does not contain UPDATED_SUBJECT
        defaultQuotationShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidityIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validity equals to DEFAULT_VALIDITY
        defaultQuotationShouldBeFound("validity.equals=" + DEFAULT_VALIDITY);

        // Get all the quotationList where validity equals to UPDATED_VALIDITY
        defaultQuotationShouldNotBeFound("validity.equals=" + UPDATED_VALIDITY);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidityIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validity in DEFAULT_VALIDITY or UPDATED_VALIDITY
        defaultQuotationShouldBeFound("validity.in=" + DEFAULT_VALIDITY + "," + UPDATED_VALIDITY);

        // Get all the quotationList where validity equals to UPDATED_VALIDITY
        defaultQuotationShouldNotBeFound("validity.in=" + UPDATED_VALIDITY);
    }

    @Test
    @Transactional
    void getAllQuotationsByValidityIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validity is not null
        defaultQuotationShouldBeFound("validity.specified=true");

        // Get all the quotationList where validity is null
        defaultQuotationShouldNotBeFound("validity.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountLevelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountLevelType equals to DEFAULT_DISCOUNT_LEVEL_TYPE
        defaultQuotationShouldBeFound("discountLevelType.equals=" + DEFAULT_DISCOUNT_LEVEL_TYPE);

        // Get all the quotationList where discountLevelType equals to UPDATED_DISCOUNT_LEVEL_TYPE
        defaultQuotationShouldNotBeFound("discountLevelType.equals=" + UPDATED_DISCOUNT_LEVEL_TYPE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountLevelTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountLevelType in DEFAULT_DISCOUNT_LEVEL_TYPE or UPDATED_DISCOUNT_LEVEL_TYPE
        defaultQuotationShouldBeFound("discountLevelType.in=" + DEFAULT_DISCOUNT_LEVEL_TYPE + "," + UPDATED_DISCOUNT_LEVEL_TYPE);

        // Get all the quotationList where discountLevelType equals to UPDATED_DISCOUNT_LEVEL_TYPE
        defaultQuotationShouldNotBeFound("discountLevelType.in=" + UPDATED_DISCOUNT_LEVEL_TYPE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountLevelTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountLevelType is not null
        defaultQuotationShouldBeFound("discountLevelType.specified=true");

        // Get all the quotationList where discountLevelType is null
        defaultQuotationShouldNotBeFound("discountLevelType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultQuotationShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the quotationList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultQuotationShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultQuotationShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the quotationList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultQuotationShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountType is not null
        defaultQuotationShouldBeFound("discountType.specified=true");

        // Get all the quotationList where discountType is null
        defaultQuotationShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue equals to DEFAULT_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.equals=" + DEFAULT_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue equals to UPDATED_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.equals=" + UPDATED_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue in DEFAULT_DISCOUNT_TYPE_VALUE or UPDATED_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.in=" + DEFAULT_DISCOUNT_TYPE_VALUE + "," + UPDATED_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue equals to UPDATED_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.in=" + UPDATED_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue is not null
        defaultQuotationShouldBeFound("discountTypeValue.specified=true");

        // Get all the quotationList where discountTypeValue is null
        defaultQuotationShouldNotBeFound("discountTypeValue.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue is greater than or equal to DEFAULT_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue is greater than or equal to UPDATED_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue is less than or equal to DEFAULT_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue is less than or equal to SMALLER_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.lessThanOrEqual=" + SMALLER_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue is less than DEFAULT_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.lessThan=" + DEFAULT_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue is less than UPDATED_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.lessThan=" + UPDATED_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByDiscountTypeValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountTypeValue is greater than DEFAULT_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldNotBeFound("discountTypeValue.greaterThan=" + DEFAULT_DISCOUNT_TYPE_VALUE);

        // Get all the quotationList where discountTypeValue is greater than SMALLER_DISCOUNT_TYPE_VALUE
        defaultQuotationShouldBeFound("discountTypeValue.greaterThan=" + SMALLER_DISCOUNT_TYPE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency equals to DEFAULT_CURRENCY
        defaultQuotationShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the quotationList where currency equals to UPDATED_CURRENCY
        defaultQuotationShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultQuotationShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the quotationList where currency equals to UPDATED_CURRENCY
        defaultQuotationShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency is not null
        defaultQuotationShouldBeFound("currency.specified=true");

        // Get all the quotationList where currency is null
        defaultQuotationShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency contains DEFAULT_CURRENCY
        defaultQuotationShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the quotationList where currency contains UPDATED_CURRENCY
        defaultQuotationShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where currency does not contain DEFAULT_CURRENCY
        defaultQuotationShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the quotationList where currency does not contain UPDATED_CURRENCY
        defaultQuotationShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the quotationList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is not null
        defaultQuotationShouldBeFound("subTotal.specified=true");

        // Get all the quotationList where subTotal is null
        defaultQuotationShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is greater than or equal to DEFAULT_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.greaterThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal is greater than or equal to UPDATED_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.greaterThanOrEqual=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is less than or equal to DEFAULT_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.lessThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal is less than or equal to SMALLER_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.lessThanOrEqual=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is less than DEFAULT_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.lessThan=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal is less than UPDATED_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.lessThan=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsBySubTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is greater than DEFAULT_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.greaterThan=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal is greater than SMALLER_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.greaterThan=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal equals to DEFAULT_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.equals=" + DEFAULT_GRAND_TOTAL);

        // Get all the quotationList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.equals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal in DEFAULT_GRAND_TOTAL or UPDATED_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.in=" + DEFAULT_GRAND_TOTAL + "," + UPDATED_GRAND_TOTAL);

        // Get all the quotationList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.in=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal is not null
        defaultQuotationShouldBeFound("grandTotal.specified=true");

        // Get all the quotationList where grandTotal is null
        defaultQuotationShouldNotBeFound("grandTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal is greater than or equal to DEFAULT_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.greaterThanOrEqual=" + DEFAULT_GRAND_TOTAL);

        // Get all the quotationList where grandTotal is greater than or equal to UPDATED_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.greaterThanOrEqual=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal is less than or equal to DEFAULT_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.lessThanOrEqual=" + DEFAULT_GRAND_TOTAL);

        // Get all the quotationList where grandTotal is less than or equal to SMALLER_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.lessThanOrEqual=" + SMALLER_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal is less than DEFAULT_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.lessThan=" + DEFAULT_GRAND_TOTAL);

        // Get all the quotationList where grandTotal is less than UPDATED_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.lessThan=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByGrandTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where grandTotal is greater than DEFAULT_GRAND_TOTAL
        defaultQuotationShouldNotBeFound("grandTotal.greaterThan=" + DEFAULT_GRAND_TOTAL);

        // Get all the quotationList where grandTotal is greater than SMALLER_GRAND_TOTAL
        defaultQuotationShouldBeFound("grandTotal.greaterThan=" + SMALLER_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount equals to DEFAULT_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.equals=" + DEFAULT_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount equals to UPDATED_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.equals=" + UPDATED_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount in DEFAULT_TOTAL_TAX_AMOUNT or UPDATED_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.in=" + DEFAULT_TOTAL_TAX_AMOUNT + "," + UPDATED_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount equals to UPDATED_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.in=" + UPDATED_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount is not null
        defaultQuotationShouldBeFound("totalTaxAmount.specified=true");

        // Get all the quotationList where totalTaxAmount is null
        defaultQuotationShouldNotBeFound("totalTaxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount is greater than or equal to DEFAULT_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount is greater than or equal to UPDATED_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.greaterThanOrEqual=" + UPDATED_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount is less than or equal to DEFAULT_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.lessThanOrEqual=" + DEFAULT_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount is less than or equal to SMALLER_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.lessThanOrEqual=" + SMALLER_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount is less than DEFAULT_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.lessThan=" + DEFAULT_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount is less than UPDATED_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.lessThan=" + UPDATED_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByTotalTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where totalTaxAmount is greater than DEFAULT_TOTAL_TAX_AMOUNT
        defaultQuotationShouldNotBeFound("totalTaxAmount.greaterThan=" + DEFAULT_TOTAL_TAX_AMOUNT);

        // Get all the quotationList where totalTaxAmount is greater than SMALLER_TOTAL_TAX_AMOUNT
        defaultQuotationShouldBeFound("totalTaxAmount.greaterThan=" + SMALLER_TOTAL_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount equals to DEFAULT_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.equals=" + DEFAULT_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount equals to UPDATED_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.equals=" + UPDATED_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount in DEFAULT_ADJUSTMENT_AMOUNT or UPDATED_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.in=" + DEFAULT_ADJUSTMENT_AMOUNT + "," + UPDATED_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount equals to UPDATED_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.in=" + UPDATED_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount is not null
        defaultQuotationShouldBeFound("adjustmentAmount.specified=true");

        // Get all the quotationList where adjustmentAmount is null
        defaultQuotationShouldNotBeFound("adjustmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount is greater than or equal to DEFAULT_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.greaterThanOrEqual=" + DEFAULT_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount is greater than or equal to UPDATED_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.greaterThanOrEqual=" + UPDATED_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount is less than or equal to DEFAULT_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.lessThanOrEqual=" + DEFAULT_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount is less than or equal to SMALLER_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.lessThanOrEqual=" + SMALLER_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount is less than DEFAULT_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.lessThan=" + DEFAULT_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount is less than UPDATED_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.lessThan=" + UPDATED_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByAdjustmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where adjustmentAmount is greater than DEFAULT_ADJUSTMENT_AMOUNT
        defaultQuotationShouldNotBeFound("adjustmentAmount.greaterThan=" + DEFAULT_ADJUSTMENT_AMOUNT);

        // Get all the quotationList where adjustmentAmount is greater than SMALLER_ADJUSTMENT_AMOUNT
        defaultQuotationShouldBeFound("adjustmentAmount.greaterThan=" + SMALLER_ADJUSTMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where statusReason equals to DEFAULT_STATUS_REASON
        defaultQuotationShouldBeFound("statusReason.equals=" + DEFAULT_STATUS_REASON);

        // Get all the quotationList where statusReason equals to UPDATED_STATUS_REASON
        defaultQuotationShouldNotBeFound("statusReason.equals=" + UPDATED_STATUS_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusReasonIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where statusReason in DEFAULT_STATUS_REASON or UPDATED_STATUS_REASON
        defaultQuotationShouldBeFound("statusReason.in=" + DEFAULT_STATUS_REASON + "," + UPDATED_STATUS_REASON);

        // Get all the quotationList where statusReason equals to UPDATED_STATUS_REASON
        defaultQuotationShouldNotBeFound("statusReason.in=" + UPDATED_STATUS_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where statusReason is not null
        defaultQuotationShouldBeFound("statusReason.specified=true");

        // Get all the quotationList where statusReason is null
        defaultQuotationShouldNotBeFound("statusReason.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusReasonContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where statusReason contains DEFAULT_STATUS_REASON
        defaultQuotationShouldBeFound("statusReason.contains=" + DEFAULT_STATUS_REASON);

        // Get all the quotationList where statusReason contains UPDATED_STATUS_REASON
        defaultQuotationShouldNotBeFound("statusReason.contains=" + UPDATED_STATUS_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByStatusReasonNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where statusReason does not contain DEFAULT_STATUS_REASON
        defaultQuotationShouldNotBeFound("statusReason.doesNotContain=" + DEFAULT_STATUS_REASON);

        // Get all the quotationList where statusReason does not contain UPDATED_STATUS_REASON
        defaultQuotationShouldBeFound("statusReason.doesNotContain=" + UPDATED_STATUS_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfGenerationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfGenerationStatus equals to DEFAULT_PDF_GENERATION_STATUS
        defaultQuotationShouldBeFound("pdfGenerationStatus.equals=" + DEFAULT_PDF_GENERATION_STATUS);

        // Get all the quotationList where pdfGenerationStatus equals to UPDATED_PDF_GENERATION_STATUS
        defaultQuotationShouldNotBeFound("pdfGenerationStatus.equals=" + UPDATED_PDF_GENERATION_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfGenerationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfGenerationStatus in DEFAULT_PDF_GENERATION_STATUS or UPDATED_PDF_GENERATION_STATUS
        defaultQuotationShouldBeFound("pdfGenerationStatus.in=" + DEFAULT_PDF_GENERATION_STATUS + "," + UPDATED_PDF_GENERATION_STATUS);

        // Get all the quotationList where pdfGenerationStatus equals to UPDATED_PDF_GENERATION_STATUS
        defaultQuotationShouldNotBeFound("pdfGenerationStatus.in=" + UPDATED_PDF_GENERATION_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByPdfGenerationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where pdfGenerationStatus is not null
        defaultQuotationShouldBeFound("pdfGenerationStatus.specified=true");

        // Get all the quotationList where pdfGenerationStatus is null
        defaultQuotationShouldNotBeFound("pdfGenerationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailStatus equals to DEFAULT_EMAIL_STATUS
        defaultQuotationShouldBeFound("emailStatus.equals=" + DEFAULT_EMAIL_STATUS);

        // Get all the quotationList where emailStatus equals to UPDATED_EMAIL_STATUS
        defaultQuotationShouldNotBeFound("emailStatus.equals=" + UPDATED_EMAIL_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailStatus in DEFAULT_EMAIL_STATUS or UPDATED_EMAIL_STATUS
        defaultQuotationShouldBeFound("emailStatus.in=" + DEFAULT_EMAIL_STATUS + "," + UPDATED_EMAIL_STATUS);

        // Get all the quotationList where emailStatus equals to UPDATED_EMAIL_STATUS
        defaultQuotationShouldNotBeFound("emailStatus.in=" + UPDATED_EMAIL_STATUS);
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailStatus is not null
        defaultQuotationShouldBeFound("emailStatus.specified=true");

        // Get all the quotationList where emailStatus is null
        defaultQuotationShouldNotBeFound("emailStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailFailureReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailFailureReason equals to DEFAULT_EMAIL_FAILURE_REASON
        defaultQuotationShouldBeFound("emailFailureReason.equals=" + DEFAULT_EMAIL_FAILURE_REASON);

        // Get all the quotationList where emailFailureReason equals to UPDATED_EMAIL_FAILURE_REASON
        defaultQuotationShouldNotBeFound("emailFailureReason.equals=" + UPDATED_EMAIL_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailFailureReasonIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailFailureReason in DEFAULT_EMAIL_FAILURE_REASON or UPDATED_EMAIL_FAILURE_REASON
        defaultQuotationShouldBeFound("emailFailureReason.in=" + DEFAULT_EMAIL_FAILURE_REASON + "," + UPDATED_EMAIL_FAILURE_REASON);

        // Get all the quotationList where emailFailureReason equals to UPDATED_EMAIL_FAILURE_REASON
        defaultQuotationShouldNotBeFound("emailFailureReason.in=" + UPDATED_EMAIL_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailFailureReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailFailureReason is not null
        defaultQuotationShouldBeFound("emailFailureReason.specified=true");

        // Get all the quotationList where emailFailureReason is null
        defaultQuotationShouldNotBeFound("emailFailureReason.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailFailureReasonContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailFailureReason contains DEFAULT_EMAIL_FAILURE_REASON
        defaultQuotationShouldBeFound("emailFailureReason.contains=" + DEFAULT_EMAIL_FAILURE_REASON);

        // Get all the quotationList where emailFailureReason contains UPDATED_EMAIL_FAILURE_REASON
        defaultQuotationShouldNotBeFound("emailFailureReason.contains=" + UPDATED_EMAIL_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByEmailFailureReasonNotContainsSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where emailFailureReason does not contain DEFAULT_EMAIL_FAILURE_REASON
        defaultQuotationShouldNotBeFound("emailFailureReason.doesNotContain=" + DEFAULT_EMAIL_FAILURE_REASON);

        // Get all the quotationList where emailFailureReason does not contain UPDATED_EMAIL_FAILURE_REASON
        defaultQuotationShouldBeFound("emailFailureReason.doesNotContain=" + UPDATED_EMAIL_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllQuotationsByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultQuotationShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the quotationList where correlationId equals to UPDATED_CORRELATION_ID
        defaultQuotationShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllQuotationsByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultQuotationShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the quotationList where correlationId equals to UPDATED_CORRELATION_ID
        defaultQuotationShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllQuotationsByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where correlationId is not null
        defaultQuotationShouldBeFound("correlationId.specified=true");

        // Get all the quotationList where correlationId is null
        defaultQuotationShouldNotBeFound("correlationId.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where approvedAt equals to DEFAULT_APPROVED_AT
        defaultQuotationShouldBeFound("approvedAt.equals=" + DEFAULT_APPROVED_AT);

        // Get all the quotationList where approvedAt equals to UPDATED_APPROVED_AT
        defaultQuotationShouldNotBeFound("approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllQuotationsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where approvedAt in DEFAULT_APPROVED_AT or UPDATED_APPROVED_AT
        defaultQuotationShouldBeFound("approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT);

        // Get all the quotationList where approvedAt equals to UPDATED_APPROVED_AT
        defaultQuotationShouldNotBeFound("approvedAt.in=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllQuotationsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where approvedAt is not null
        defaultQuotationShouldBeFound("approvedAt.specified=true");

        // Get all the quotationList where approvedAt is null
        defaultQuotationShouldNotBeFound("approvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByPriceDataSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where priceDataSource equals to DEFAULT_PRICE_DATA_SOURCE
        defaultQuotationShouldBeFound("priceDataSource.equals=" + DEFAULT_PRICE_DATA_SOURCE);

        // Get all the quotationList where priceDataSource equals to UPDATED_PRICE_DATA_SOURCE
        defaultQuotationShouldNotBeFound("priceDataSource.equals=" + UPDATED_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void getAllQuotationsByPriceDataSourceIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where priceDataSource in DEFAULT_PRICE_DATA_SOURCE or UPDATED_PRICE_DATA_SOURCE
        defaultQuotationShouldBeFound("priceDataSource.in=" + DEFAULT_PRICE_DATA_SOURCE + "," + UPDATED_PRICE_DATA_SOURCE);

        // Get all the quotationList where priceDataSource equals to UPDATED_PRICE_DATA_SOURCE
        defaultQuotationShouldNotBeFound("priceDataSource.in=" + UPDATED_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void getAllQuotationsByPriceDataSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where priceDataSource is not null
        defaultQuotationShouldBeFound("priceDataSource.specified=true");

        // Get all the quotationList where priceDataSource is null
        defaultQuotationShouldNotBeFound("priceDataSource.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotationsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        quotation.setUser(user);
        quotationRepository.saveAndFlush(quotation);
        Long userId = user.getId();
        // Get all the quotationList where user equals to userId
        defaultQuotationShouldBeFound("userId.equals=" + userId);

        // Get all the quotationList where user equals to (userId + 1)
        defaultQuotationShouldNotBeFound("userId.equals=" + (userId + 1));
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
    void getAllQuotationsByPaymentTermIsEqualToSomething() throws Exception {
        MasterStaticType paymentTerm;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            paymentTerm = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            paymentTerm = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(paymentTerm);
        em.flush();
        quotation.setPaymentTerm(paymentTerm);
        quotationRepository.saveAndFlush(quotation);
        Long paymentTermId = paymentTerm.getId();
        // Get all the quotationList where paymentTerm equals to paymentTermId
        defaultQuotationShouldBeFound("paymentTermId.equals=" + paymentTermId);

        // Get all the quotationList where paymentTerm equals to (paymentTermId + 1)
        defaultQuotationShouldNotBeFound("paymentTermId.equals=" + (paymentTermId + 1));
    }

    @Test
    @Transactional
    void getAllQuotationsByQuotationStatusIsEqualToSomething() throws Exception {
        MasterStaticType quotationStatus;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            quotationRepository.saveAndFlush(quotation);
            quotationStatus = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            quotationStatus = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(quotationStatus);
        em.flush();
        quotation.setQuotationStatus(quotationStatus);
        quotationRepository.saveAndFlush(quotation);
        Long quotationStatusId = quotationStatus.getId();
        // Get all the quotationList where quotationStatus equals to quotationStatusId
        defaultQuotationShouldBeFound("quotationStatusId.equals=" + quotationStatusId);

        // Get all the quotationList where quotationStatus equals to (quotationStatusId + 1)
        defaultQuotationShouldNotBeFound("quotationStatusId.equals=" + (quotationStatusId + 1));
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
            .andExpect(jsonPath("$.[*].quotationNumber").value(hasItem(DEFAULT_QUOTATION_NUMBER)))
            .andExpect(jsonPath("$.[*].quotationDate").value(hasItem(DEFAULT_QUOTATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceDate").value(hasItem(DEFAULT_REFERENCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimateDate").value(hasItem(DEFAULT_ESTIMATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].validity").value(hasItem(DEFAULT_VALIDITY.toString())))
            .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].discountLevelType").value(hasItem(DEFAULT_DISCOUNT_LEVEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountTypeValue").value(hasItem(DEFAULT_DISCOUNT_TYPE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTaxAmount").value(hasItem(DEFAULT_TOTAL_TAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].adjustmentAmount").value(hasItem(DEFAULT_ADJUSTMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].statusReason").value(hasItem(DEFAULT_STATUS_REASON)))
            .andExpect(jsonPath("$.[*].pdfGenerationStatus").value(hasItem(DEFAULT_PDF_GENERATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emailStatus").value(hasItem(DEFAULT_EMAIL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emailFailureReason").value(hasItem(DEFAULT_EMAIL_FAILURE_REASON)))
            .andExpect(jsonPath("$.[*].customParagraph").value(hasItem(DEFAULT_CUSTOM_PARAGRAPH.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].priceDataSource").value(hasItem(DEFAULT_PRICE_DATA_SOURCE.toString())));

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
        quotationRepository.saveAndFlush(quotation);

        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findById(quotation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotation are not directly saved in db
        em.detach(updatedQuotation);
        updatedQuotation
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .subject(UPDATED_SUBJECT)
            .validity(UPDATED_VALIDITY)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .discountLevelType(UPDATED_DISCOUNT_LEVEL_TYPE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountTypeValue(UPDATED_DISCOUNT_TYPE_VALUE)
            .currency(UPDATED_CURRENCY)
            .subTotal(UPDATED_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .adjustmentAmount(UPDATED_ADJUSTMENT_AMOUNT)
            .statusReason(UPDATED_STATUS_REASON)
            .pdfGenerationStatus(UPDATED_PDF_GENERATION_STATUS)
            .emailStatus(UPDATED_EMAIL_STATUS)
            .emailFailureReason(UPDATED_EMAIL_FAILURE_REASON)
            .customParagraph(UPDATED_CUSTOM_PARAGRAPH)
            .correlationId(UPDATED_CORRELATION_ID)
            .approvedAt(UPDATED_APPROVED_AT)
            .priceDataSource(UPDATED_PRICE_DATA_SOURCE);
        QuotationDTO quotationDTO = quotationMapper.toDto(updatedQuotation);

        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(UPDATED_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(UPDATED_QUOTATION_DATE);
        assertThat(testQuotation.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testQuotation.getReferenceDate()).isEqualTo(UPDATED_REFERENCE_DATE);
        assertThat(testQuotation.getEstimateDate()).isEqualTo(UPDATED_ESTIMATE_DATE);
        assertThat(testQuotation.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testQuotation.getValidity()).isEqualTo(UPDATED_VALIDITY);
        assertThat(testQuotation.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
        assertThat(testQuotation.getDiscountLevelType()).isEqualTo(UPDATED_DISCOUNT_LEVEL_TYPE);
        assertThat(testQuotation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testQuotation.getDiscountTypeValue()).isEqualTo(UPDATED_DISCOUNT_TYPE_VALUE);
        assertThat(testQuotation.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testQuotation.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testQuotation.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testQuotation.getTotalTaxAmount()).isEqualTo(UPDATED_TOTAL_TAX_AMOUNT);
        assertThat(testQuotation.getAdjustmentAmount()).isEqualTo(UPDATED_ADJUSTMENT_AMOUNT);
        assertThat(testQuotation.getStatusReason()).isEqualTo(UPDATED_STATUS_REASON);
        assertThat(testQuotation.getPdfGenerationStatus()).isEqualTo(UPDATED_PDF_GENERATION_STATUS);
        assertThat(testQuotation.getEmailStatus()).isEqualTo(UPDATED_EMAIL_STATUS);
        assertThat(testQuotation.getEmailFailureReason()).isEqualTo(UPDATED_EMAIL_FAILURE_REASON);
        assertThat(testQuotation.getCustomParagraph()).isEqualTo(UPDATED_CUSTOM_PARAGRAPH);
        assertThat(testQuotation.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
        assertThat(testQuotation.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testQuotation.getPriceDataSource()).isEqualTo(UPDATED_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void putNonExistingQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .validity(UPDATED_VALIDITY)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .discountLevelType(UPDATED_DISCOUNT_LEVEL_TYPE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountTypeValue(UPDATED_DISCOUNT_TYPE_VALUE)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .adjustmentAmount(UPDATED_ADJUSTMENT_AMOUNT)
            .statusReason(UPDATED_STATUS_REASON)
            .emailFailureReason(UPDATED_EMAIL_FAILURE_REASON)
            .customParagraph(UPDATED_CUSTOM_PARAGRAPH);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(UPDATED_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(UPDATED_QUOTATION_DATE);
        assertThat(testQuotation.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testQuotation.getReferenceDate()).isEqualTo(UPDATED_REFERENCE_DATE);
        assertThat(testQuotation.getEstimateDate()).isEqualTo(UPDATED_ESTIMATE_DATE);
        assertThat(testQuotation.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testQuotation.getValidity()).isEqualTo(UPDATED_VALIDITY);
        assertThat(testQuotation.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
        assertThat(testQuotation.getDiscountLevelType()).isEqualTo(UPDATED_DISCOUNT_LEVEL_TYPE);
        assertThat(testQuotation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testQuotation.getDiscountTypeValue()).isEqualTo(UPDATED_DISCOUNT_TYPE_VALUE);
        assertThat(testQuotation.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testQuotation.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testQuotation.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testQuotation.getTotalTaxAmount()).isEqualTo(UPDATED_TOTAL_TAX_AMOUNT);
        assertThat(testQuotation.getAdjustmentAmount()).isEqualTo(UPDATED_ADJUSTMENT_AMOUNT);
        assertThat(testQuotation.getStatusReason()).isEqualTo(UPDATED_STATUS_REASON);
        assertThat(testQuotation.getPdfGenerationStatus()).isEqualTo(DEFAULT_PDF_GENERATION_STATUS);
        assertThat(testQuotation.getEmailStatus()).isEqualTo(DEFAULT_EMAIL_STATUS);
        assertThat(testQuotation.getEmailFailureReason()).isEqualTo(UPDATED_EMAIL_FAILURE_REASON);
        assertThat(testQuotation.getCustomParagraph()).isEqualTo(UPDATED_CUSTOM_PARAGRAPH);
        assertThat(testQuotation.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
        assertThat(testQuotation.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testQuotation.getPriceDataSource()).isEqualTo(DEFAULT_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void fullUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .quotationNumber(UPDATED_QUOTATION_NUMBER)
            .quotationDate(UPDATED_QUOTATION_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .estimateDate(UPDATED_ESTIMATE_DATE)
            .subject(UPDATED_SUBJECT)
            .validity(UPDATED_VALIDITY)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .discountLevelType(UPDATED_DISCOUNT_LEVEL_TYPE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountTypeValue(UPDATED_DISCOUNT_TYPE_VALUE)
            .currency(UPDATED_CURRENCY)
            .subTotal(UPDATED_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .adjustmentAmount(UPDATED_ADJUSTMENT_AMOUNT)
            .statusReason(UPDATED_STATUS_REASON)
            .pdfGenerationStatus(UPDATED_PDF_GENERATION_STATUS)
            .emailStatus(UPDATED_EMAIL_STATUS)
            .emailFailureReason(UPDATED_EMAIL_FAILURE_REASON)
            .customParagraph(UPDATED_CUSTOM_PARAGRAPH)
            .correlationId(UPDATED_CORRELATION_ID)
            .approvedAt(UPDATED_APPROVED_AT)
            .priceDataSource(UPDATED_PRICE_DATA_SOURCE);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getQuotationNumber()).isEqualTo(UPDATED_QUOTATION_NUMBER);
        assertThat(testQuotation.getQuotationDate()).isEqualTo(UPDATED_QUOTATION_DATE);
        assertThat(testQuotation.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testQuotation.getReferenceDate()).isEqualTo(UPDATED_REFERENCE_DATE);
        assertThat(testQuotation.getEstimateDate()).isEqualTo(UPDATED_ESTIMATE_DATE);
        assertThat(testQuotation.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testQuotation.getValidity()).isEqualTo(UPDATED_VALIDITY);
        assertThat(testQuotation.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
        assertThat(testQuotation.getDiscountLevelType()).isEqualTo(UPDATED_DISCOUNT_LEVEL_TYPE);
        assertThat(testQuotation.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testQuotation.getDiscountTypeValue()).isEqualTo(UPDATED_DISCOUNT_TYPE_VALUE);
        assertThat(testQuotation.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testQuotation.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testQuotation.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testQuotation.getTotalTaxAmount()).isEqualTo(UPDATED_TOTAL_TAX_AMOUNT);
        assertThat(testQuotation.getAdjustmentAmount()).isEqualTo(UPDATED_ADJUSTMENT_AMOUNT);
        assertThat(testQuotation.getStatusReason()).isEqualTo(UPDATED_STATUS_REASON);
        assertThat(testQuotation.getPdfGenerationStatus()).isEqualTo(UPDATED_PDF_GENERATION_STATUS);
        assertThat(testQuotation.getEmailStatus()).isEqualTo(UPDATED_EMAIL_STATUS);
        assertThat(testQuotation.getEmailFailureReason()).isEqualTo(UPDATED_EMAIL_FAILURE_REASON);
        assertThat(testQuotation.getCustomParagraph()).isEqualTo(UPDATED_CUSTOM_PARAGRAPH);
        assertThat(testQuotation.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
        assertThat(testQuotation.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testQuotation.getPriceDataSource()).isEqualTo(UPDATED_PRICE_DATA_SOURCE);
    }

    @Test
    @Transactional
    void patchNonExistingQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quotationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        int databaseSizeBeforeDelete = quotationRepository.findAll().size();

        // Delete the quotation
        restQuotationMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
