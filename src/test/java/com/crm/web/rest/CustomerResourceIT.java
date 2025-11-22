package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Address;
import com.crm.domain.Customer;
import com.crm.domain.CustomerCompany;
import com.crm.domain.Department;
import com.crm.domain.MasterCategory;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Tenant;
import com.crm.domain.User;
import com.crm.domain.enumeration.AccountManagement;
import com.crm.domain.enumeration.AccountType;
import com.crm.repository.CustomerRepository;
import com.crm.service.CustomerService;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.mapper.CustomerMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_CITY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_AREA = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_MAX_INVOICE_AMOUNT = 1D;
    private static final Double UPDATED_MAX_INVOICE_AMOUNT = 2D;
    private static final Double SMALLER_MAX_INVOICE_AMOUNT = 1D - 1D;

    private static final String DEFAULT_GST_NO = "AAAAAAAAAA";
    private static final String UPDATED_GST_NO = "BBBBBBBBBB";

    private static final String DEFAULT_PAN_NO = "AAAAAAAAAA";
    private static final String UPDATED_PAN_NO = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_TAX_NO = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TAX_NO = "BBBBBBBBBB";

    private static final String DEFAULT_TAN_NO = "AAAAAAAAAA";
    private static final String UPDATED_TAN_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_FIELD_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_FIELD_DATA = "BBBBBBBBBB";

    private static final UUID DEFAULT_CORRELATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_CORRELATION_ID = UUID.randomUUID();

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_GST_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GST_STATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GST_STATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GST_STATE_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO = false;
    private static final Boolean UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO = true;

    private static final Boolean DEFAULT_IS_BLOCK = false;
    private static final Boolean UPDATED_IS_BLOCK = true;

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.DIRECT;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.INDIRECT;

    private static final AccountManagement DEFAULT_ACCOUNT_MANAGEMENT = AccountManagement.SELF_MANAGED;
    private static final AccountManagement UPDATED_ACCOUNT_MANAGEMENT = AccountManagement.AGENCY_MANAGED;

    private static final Double DEFAULT_REVENUE_POTENTIAL = 1D;
    private static final Double UPDATED_REVENUE_POTENTIAL = 2D;
    private static final Double SMALLER_REVENUE_POTENTIAL = 1D - 1D;

    private static final Double DEFAULT_SAMPLE_POTENTIAL = 1D;
    private static final Double UPDATED_SAMPLE_POTENTIAL = 2D;
    private static final Double SMALLER_SAMPLE_POTENTIAL = 1D - 1D;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_PIPELINE = 1;
    private static final Integer UPDATED_TOTAL_PIPELINE = 2;
    private static final Integer SMALLER_TOTAL_PIPELINE = 1 - 1;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Autowired
    private CustomerMapper customerMapper;

    @Mock
    private CustomerService customerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .companyCity(DEFAULT_COMPANY_CITY)
            .companyArea(DEFAULT_COMPANY_AREA)
            .website(DEFAULT_WEBSITE)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .currencyType(DEFAULT_CURRENCY_TYPE)
            .maxInvoiceAmount(DEFAULT_MAX_INVOICE_AMOUNT)
            .gstNo(DEFAULT_GST_NO)
            .panNo(DEFAULT_PAN_NO)
            .serviceTaxNo(DEFAULT_SERVICE_TAX_NO)
            .tanNo(DEFAULT_TAN_NO)
            .customFieldData(DEFAULT_CUSTOM_FIELD_DATA)
            .correlationId(DEFAULT_CORRELATION_ID)
            .accountNo(DEFAULT_ACCOUNT_NO)
            .gstStateName(DEFAULT_GST_STATE_NAME)
            .gstStateCode(DEFAULT_GST_STATE_CODE)
            .isSubmitSampleWithoutPO(DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO)
            .isBlock(DEFAULT_IS_BLOCK)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountManagement(DEFAULT_ACCOUNT_MANAGEMENT)
            .revenuePotential(DEFAULT_REVENUE_POTENTIAL)
            .samplePotential(DEFAULT_SAMPLE_POTENTIAL)
            .remarks(DEFAULT_REMARKS)
            .totalPipeline(DEFAULT_TOTAL_PIPELINE)
            .type(DEFAULT_TYPE);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyArea(UPDATED_COMPANY_AREA)
            .website(UPDATED_WEBSITE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .currencyType(UPDATED_CURRENCY_TYPE)
            .maxInvoiceAmount(UPDATED_MAX_INVOICE_AMOUNT)
            .gstNo(UPDATED_GST_NO)
            .panNo(UPDATED_PAN_NO)
            .serviceTaxNo(UPDATED_SERVICE_TAX_NO)
            .tanNo(UPDATED_TAN_NO)
            .customFieldData(UPDATED_CUSTOM_FIELD_DATA)
            .correlationId(UPDATED_CORRELATION_ID)
            .accountNo(UPDATED_ACCOUNT_NO)
            .gstStateName(UPDATED_GST_STATE_NAME)
            .gstStateCode(UPDATED_GST_STATE_CODE)
            .isSubmitSampleWithoutPO(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO)
            .isBlock(UPDATED_IS_BLOCK)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountManagement(UPDATED_ACCOUNT_MANAGEMENT)
            .revenuePotential(UPDATED_REVENUE_POTENTIAL)
            .samplePotential(UPDATED_SAMPLE_POTENTIAL)
            .remarks(UPDATED_REMARKS)
            .totalPipeline(UPDATED_TOTAL_PIPELINE)
            .type(UPDATED_TYPE);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomer.getCompanyCity()).isEqualTo(DEFAULT_COMPANY_CITY);
        assertThat(testCustomer.getCompanyArea()).isEqualTo(DEFAULT_COMPANY_AREA);
        assertThat(testCustomer.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getCurrencyType()).isEqualTo(DEFAULT_CURRENCY_TYPE);
        assertThat(testCustomer.getMaxInvoiceAmount()).isEqualTo(DEFAULT_MAX_INVOICE_AMOUNT);
        assertThat(testCustomer.getGstNo()).isEqualTo(DEFAULT_GST_NO);
        assertThat(testCustomer.getPanNo()).isEqualTo(DEFAULT_PAN_NO);
        assertThat(testCustomer.getServiceTaxNo()).isEqualTo(DEFAULT_SERVICE_TAX_NO);
        assertThat(testCustomer.getTanNo()).isEqualTo(DEFAULT_TAN_NO);
        assertThat(testCustomer.getCustomFieldData()).isEqualTo(DEFAULT_CUSTOM_FIELD_DATA);
        assertThat(testCustomer.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
        assertThat(testCustomer.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testCustomer.getGstStateName()).isEqualTo(DEFAULT_GST_STATE_NAME);
        assertThat(testCustomer.getGstStateCode()).isEqualTo(DEFAULT_GST_STATE_CODE);
        assertThat(testCustomer.getIsSubmitSampleWithoutPO()).isEqualTo(DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO);
        assertThat(testCustomer.getIsBlock()).isEqualTo(DEFAULT_IS_BLOCK);
        assertThat(testCustomer.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testCustomer.getAccountManagement()).isEqualTo(DEFAULT_ACCOUNT_MANAGEMENT);
        assertThat(testCustomer.getRevenuePotential()).isEqualTo(DEFAULT_REVENUE_POTENTIAL);
        assertThat(testCustomer.getSamplePotential()).isEqualTo(DEFAULT_SAMPLE_POTENTIAL);
        assertThat(testCustomer.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testCustomer.getTotalPipeline()).isEqualTo(DEFAULT_TOTAL_PIPELINE);
        assertThat(testCustomer.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].companyCity").value(hasItem(DEFAULT_COMPANY_CITY)))
            .andExpect(jsonPath("$.[*].companyArea").value(hasItem(DEFAULT_COMPANY_AREA)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].currencyType").value(hasItem(DEFAULT_CURRENCY_TYPE)))
            .andExpect(jsonPath("$.[*].maxInvoiceAmount").value(hasItem(DEFAULT_MAX_INVOICE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].gstNo").value(hasItem(DEFAULT_GST_NO)))
            .andExpect(jsonPath("$.[*].panNo").value(hasItem(DEFAULT_PAN_NO)))
            .andExpect(jsonPath("$.[*].serviceTaxNo").value(hasItem(DEFAULT_SERVICE_TAX_NO)))
            .andExpect(jsonPath("$.[*].tanNo").value(hasItem(DEFAULT_TAN_NO)))
            .andExpect(jsonPath("$.[*].customFieldData").value(hasItem(DEFAULT_CUSTOM_FIELD_DATA.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].gstStateName").value(hasItem(DEFAULT_GST_STATE_NAME)))
            .andExpect(jsonPath("$.[*].gstStateCode").value(hasItem(DEFAULT_GST_STATE_CODE)))
            .andExpect(jsonPath("$.[*].isSubmitSampleWithoutPO").value(hasItem(DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO.booleanValue())))
            .andExpect(jsonPath("$.[*].isBlock").value(hasItem(DEFAULT_IS_BLOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountManagement").value(hasItem(DEFAULT_ACCOUNT_MANAGEMENT.toString())))
            .andExpect(jsonPath("$.[*].revenuePotential").value(hasItem(DEFAULT_REVENUE_POTENTIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].samplePotential").value(hasItem(DEFAULT_SAMPLE_POTENTIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].totalPipeline").value(hasItem(DEFAULT_TOTAL_PIPELINE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.companyCity").value(DEFAULT_COMPANY_CITY))
            .andExpect(jsonPath("$.companyArea").value(DEFAULT_COMPANY_AREA))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.currencyType").value(DEFAULT_CURRENCY_TYPE))
            .andExpect(jsonPath("$.maxInvoiceAmount").value(DEFAULT_MAX_INVOICE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.gstNo").value(DEFAULT_GST_NO))
            .andExpect(jsonPath("$.panNo").value(DEFAULT_PAN_NO))
            .andExpect(jsonPath("$.serviceTaxNo").value(DEFAULT_SERVICE_TAX_NO))
            .andExpect(jsonPath("$.tanNo").value(DEFAULT_TAN_NO))
            .andExpect(jsonPath("$.customFieldData").value(DEFAULT_CUSTOM_FIELD_DATA.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID.toString()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO))
            .andExpect(jsonPath("$.gstStateName").value(DEFAULT_GST_STATE_NAME))
            .andExpect(jsonPath("$.gstStateCode").value(DEFAULT_GST_STATE_CODE))
            .andExpect(jsonPath("$.isSubmitSampleWithoutPO").value(DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO.booleanValue()))
            .andExpect(jsonPath("$.isBlock").value(DEFAULT_IS_BLOCK.booleanValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountManagement").value(DEFAULT_ACCOUNT_MANAGEMENT.toString()))
            .andExpect(jsonPath("$.revenuePotential").value(DEFAULT_REVENUE_POTENTIAL.doubleValue()))
            .andExpect(jsonPath("$.samplePotential").value(DEFAULT_SAMPLE_POTENTIAL.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()))
            .andExpect(jsonPath("$.totalPipeline").value(DEFAULT_TOTAL_PIPELINE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name equals to DEFAULT_NAME
        defaultCustomerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name is not null
        defaultCustomerShouldBeFound("name.specified=true");

        // Get all the customerList where name is null
        defaultCustomerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name contains DEFAULT_NAME
        defaultCustomerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerList where name contains UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name does not contain DEFAULT_NAME
        defaultCustomerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerList where name does not contain UPDATED_NAME
        defaultCustomerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where description equals to DEFAULT_DESCRIPTION
        defaultCustomerShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the customerList where description equals to UPDATED_DESCRIPTION
        defaultCustomerShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCustomerShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the customerList where description equals to UPDATED_DESCRIPTION
        defaultCustomerShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where description is not null
        defaultCustomerShouldBeFound("description.specified=true");

        // Get all the customerList where description is null
        defaultCustomerShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where description contains DEFAULT_DESCRIPTION
        defaultCustomerShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the customerList where description contains UPDATED_DESCRIPTION
        defaultCustomerShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where description does not contain DEFAULT_DESCRIPTION
        defaultCustomerShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the customerList where description does not contain UPDATED_DESCRIPTION
        defaultCustomerShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyCityIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyCity equals to DEFAULT_COMPANY_CITY
        defaultCustomerShouldBeFound("companyCity.equals=" + DEFAULT_COMPANY_CITY);

        // Get all the customerList where companyCity equals to UPDATED_COMPANY_CITY
        defaultCustomerShouldNotBeFound("companyCity.equals=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyCityIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyCity in DEFAULT_COMPANY_CITY or UPDATED_COMPANY_CITY
        defaultCustomerShouldBeFound("companyCity.in=" + DEFAULT_COMPANY_CITY + "," + UPDATED_COMPANY_CITY);

        // Get all the customerList where companyCity equals to UPDATED_COMPANY_CITY
        defaultCustomerShouldNotBeFound("companyCity.in=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyCity is not null
        defaultCustomerShouldBeFound("companyCity.specified=true");

        // Get all the customerList where companyCity is null
        defaultCustomerShouldNotBeFound("companyCity.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyCityContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyCity contains DEFAULT_COMPANY_CITY
        defaultCustomerShouldBeFound("companyCity.contains=" + DEFAULT_COMPANY_CITY);

        // Get all the customerList where companyCity contains UPDATED_COMPANY_CITY
        defaultCustomerShouldNotBeFound("companyCity.contains=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyCityNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyCity does not contain DEFAULT_COMPANY_CITY
        defaultCustomerShouldNotBeFound("companyCity.doesNotContain=" + DEFAULT_COMPANY_CITY);

        // Get all the customerList where companyCity does not contain UPDATED_COMPANY_CITY
        defaultCustomerShouldBeFound("companyCity.doesNotContain=" + UPDATED_COMPANY_CITY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyArea equals to DEFAULT_COMPANY_AREA
        defaultCustomerShouldBeFound("companyArea.equals=" + DEFAULT_COMPANY_AREA);

        // Get all the customerList where companyArea equals to UPDATED_COMPANY_AREA
        defaultCustomerShouldNotBeFound("companyArea.equals=" + UPDATED_COMPANY_AREA);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyAreaIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyArea in DEFAULT_COMPANY_AREA or UPDATED_COMPANY_AREA
        defaultCustomerShouldBeFound("companyArea.in=" + DEFAULT_COMPANY_AREA + "," + UPDATED_COMPANY_AREA);

        // Get all the customerList where companyArea equals to UPDATED_COMPANY_AREA
        defaultCustomerShouldNotBeFound("companyArea.in=" + UPDATED_COMPANY_AREA);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyArea is not null
        defaultCustomerShouldBeFound("companyArea.specified=true");

        // Get all the customerList where companyArea is null
        defaultCustomerShouldNotBeFound("companyArea.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyAreaContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyArea contains DEFAULT_COMPANY_AREA
        defaultCustomerShouldBeFound("companyArea.contains=" + DEFAULT_COMPANY_AREA);

        // Get all the customerList where companyArea contains UPDATED_COMPANY_AREA
        defaultCustomerShouldNotBeFound("companyArea.contains=" + UPDATED_COMPANY_AREA);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyAreaNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyArea does not contain DEFAULT_COMPANY_AREA
        defaultCustomerShouldNotBeFound("companyArea.doesNotContain=" + DEFAULT_COMPANY_AREA);

        // Get all the customerList where companyArea does not contain UPDATED_COMPANY_AREA
        defaultCustomerShouldBeFound("companyArea.doesNotContain=" + UPDATED_COMPANY_AREA);
    }

    @Test
    @Transactional
    void getAllCustomersByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where website equals to DEFAULT_WEBSITE
        defaultCustomerShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the customerList where website equals to UPDATED_WEBSITE
        defaultCustomerShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomersByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultCustomerShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the customerList where website equals to UPDATED_WEBSITE
        defaultCustomerShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomersByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where website is not null
        defaultCustomerShouldBeFound("website.specified=true");

        // Get all the customerList where website is null
        defaultCustomerShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where website contains DEFAULT_WEBSITE
        defaultCustomerShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the customerList where website contains UPDATED_WEBSITE
        defaultCustomerShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomersByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where website does not contain DEFAULT_WEBSITE
        defaultCustomerShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the customerList where website does not contain UPDATED_WEBSITE
        defaultCustomerShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName is not null
        defaultCustomerShouldBeFound("customerName.specified=true");

        // Get all the customerList where customerName is null
        defaultCustomerShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName contains UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCurrencyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where currencyType equals to DEFAULT_CURRENCY_TYPE
        defaultCustomerShouldBeFound("currencyType.equals=" + DEFAULT_CURRENCY_TYPE);

        // Get all the customerList where currencyType equals to UPDATED_CURRENCY_TYPE
        defaultCustomerShouldNotBeFound("currencyType.equals=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCurrencyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where currencyType in DEFAULT_CURRENCY_TYPE or UPDATED_CURRENCY_TYPE
        defaultCustomerShouldBeFound("currencyType.in=" + DEFAULT_CURRENCY_TYPE + "," + UPDATED_CURRENCY_TYPE);

        // Get all the customerList where currencyType equals to UPDATED_CURRENCY_TYPE
        defaultCustomerShouldNotBeFound("currencyType.in=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCurrencyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where currencyType is not null
        defaultCustomerShouldBeFound("currencyType.specified=true");

        // Get all the customerList where currencyType is null
        defaultCustomerShouldNotBeFound("currencyType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCurrencyTypeContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where currencyType contains DEFAULT_CURRENCY_TYPE
        defaultCustomerShouldBeFound("currencyType.contains=" + DEFAULT_CURRENCY_TYPE);

        // Get all the customerList where currencyType contains UPDATED_CURRENCY_TYPE
        defaultCustomerShouldNotBeFound("currencyType.contains=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCurrencyTypeNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where currencyType does not contain DEFAULT_CURRENCY_TYPE
        defaultCustomerShouldNotBeFound("currencyType.doesNotContain=" + DEFAULT_CURRENCY_TYPE);

        // Get all the customerList where currencyType does not contain UPDATED_CURRENCY_TYPE
        defaultCustomerShouldBeFound("currencyType.doesNotContain=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount equals to DEFAULT_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.equals=" + DEFAULT_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount equals to UPDATED_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.equals=" + UPDATED_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount in DEFAULT_MAX_INVOICE_AMOUNT or UPDATED_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.in=" + DEFAULT_MAX_INVOICE_AMOUNT + "," + UPDATED_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount equals to UPDATED_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.in=" + UPDATED_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount is not null
        defaultCustomerShouldBeFound("maxInvoiceAmount.specified=true");

        // Get all the customerList where maxInvoiceAmount is null
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount is greater than or equal to DEFAULT_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.greaterThanOrEqual=" + DEFAULT_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount is greater than or equal to UPDATED_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.greaterThanOrEqual=" + UPDATED_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount is less than or equal to DEFAULT_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.lessThanOrEqual=" + DEFAULT_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount is less than or equal to SMALLER_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.lessThanOrEqual=" + SMALLER_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount is less than DEFAULT_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.lessThan=" + DEFAULT_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount is less than UPDATED_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.lessThan=" + UPDATED_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByMaxInvoiceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where maxInvoiceAmount is greater than DEFAULT_MAX_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("maxInvoiceAmount.greaterThan=" + DEFAULT_MAX_INVOICE_AMOUNT);

        // Get all the customerList where maxInvoiceAmount is greater than SMALLER_MAX_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("maxInvoiceAmount.greaterThan=" + SMALLER_MAX_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustomersByGstNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstNo equals to DEFAULT_GST_NO
        defaultCustomerShouldBeFound("gstNo.equals=" + DEFAULT_GST_NO);

        // Get all the customerList where gstNo equals to UPDATED_GST_NO
        defaultCustomerShouldNotBeFound("gstNo.equals=" + UPDATED_GST_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByGstNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstNo in DEFAULT_GST_NO or UPDATED_GST_NO
        defaultCustomerShouldBeFound("gstNo.in=" + DEFAULT_GST_NO + "," + UPDATED_GST_NO);

        // Get all the customerList where gstNo equals to UPDATED_GST_NO
        defaultCustomerShouldNotBeFound("gstNo.in=" + UPDATED_GST_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByGstNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstNo is not null
        defaultCustomerShouldBeFound("gstNo.specified=true");

        // Get all the customerList where gstNo is null
        defaultCustomerShouldNotBeFound("gstNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByGstNoContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstNo contains DEFAULT_GST_NO
        defaultCustomerShouldBeFound("gstNo.contains=" + DEFAULT_GST_NO);

        // Get all the customerList where gstNo contains UPDATED_GST_NO
        defaultCustomerShouldNotBeFound("gstNo.contains=" + UPDATED_GST_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByGstNoNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstNo does not contain DEFAULT_GST_NO
        defaultCustomerShouldNotBeFound("gstNo.doesNotContain=" + DEFAULT_GST_NO);

        // Get all the customerList where gstNo does not contain UPDATED_GST_NO
        defaultCustomerShouldBeFound("gstNo.doesNotContain=" + UPDATED_GST_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByPanNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where panNo equals to DEFAULT_PAN_NO
        defaultCustomerShouldBeFound("panNo.equals=" + DEFAULT_PAN_NO);

        // Get all the customerList where panNo equals to UPDATED_PAN_NO
        defaultCustomerShouldNotBeFound("panNo.equals=" + UPDATED_PAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByPanNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where panNo in DEFAULT_PAN_NO or UPDATED_PAN_NO
        defaultCustomerShouldBeFound("panNo.in=" + DEFAULT_PAN_NO + "," + UPDATED_PAN_NO);

        // Get all the customerList where panNo equals to UPDATED_PAN_NO
        defaultCustomerShouldNotBeFound("panNo.in=" + UPDATED_PAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByPanNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where panNo is not null
        defaultCustomerShouldBeFound("panNo.specified=true");

        // Get all the customerList where panNo is null
        defaultCustomerShouldNotBeFound("panNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPanNoContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where panNo contains DEFAULT_PAN_NO
        defaultCustomerShouldBeFound("panNo.contains=" + DEFAULT_PAN_NO);

        // Get all the customerList where panNo contains UPDATED_PAN_NO
        defaultCustomerShouldNotBeFound("panNo.contains=" + UPDATED_PAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByPanNoNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where panNo does not contain DEFAULT_PAN_NO
        defaultCustomerShouldNotBeFound("panNo.doesNotContain=" + DEFAULT_PAN_NO);

        // Get all the customerList where panNo does not contain UPDATED_PAN_NO
        defaultCustomerShouldBeFound("panNo.doesNotContain=" + UPDATED_PAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByServiceTaxNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where serviceTaxNo equals to DEFAULT_SERVICE_TAX_NO
        defaultCustomerShouldBeFound("serviceTaxNo.equals=" + DEFAULT_SERVICE_TAX_NO);

        // Get all the customerList where serviceTaxNo equals to UPDATED_SERVICE_TAX_NO
        defaultCustomerShouldNotBeFound("serviceTaxNo.equals=" + UPDATED_SERVICE_TAX_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByServiceTaxNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where serviceTaxNo in DEFAULT_SERVICE_TAX_NO or UPDATED_SERVICE_TAX_NO
        defaultCustomerShouldBeFound("serviceTaxNo.in=" + DEFAULT_SERVICE_TAX_NO + "," + UPDATED_SERVICE_TAX_NO);

        // Get all the customerList where serviceTaxNo equals to UPDATED_SERVICE_TAX_NO
        defaultCustomerShouldNotBeFound("serviceTaxNo.in=" + UPDATED_SERVICE_TAX_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByServiceTaxNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where serviceTaxNo is not null
        defaultCustomerShouldBeFound("serviceTaxNo.specified=true");

        // Get all the customerList where serviceTaxNo is null
        defaultCustomerShouldNotBeFound("serviceTaxNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByServiceTaxNoContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where serviceTaxNo contains DEFAULT_SERVICE_TAX_NO
        defaultCustomerShouldBeFound("serviceTaxNo.contains=" + DEFAULT_SERVICE_TAX_NO);

        // Get all the customerList where serviceTaxNo contains UPDATED_SERVICE_TAX_NO
        defaultCustomerShouldNotBeFound("serviceTaxNo.contains=" + UPDATED_SERVICE_TAX_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByServiceTaxNoNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where serviceTaxNo does not contain DEFAULT_SERVICE_TAX_NO
        defaultCustomerShouldNotBeFound("serviceTaxNo.doesNotContain=" + DEFAULT_SERVICE_TAX_NO);

        // Get all the customerList where serviceTaxNo does not contain UPDATED_SERVICE_TAX_NO
        defaultCustomerShouldBeFound("serviceTaxNo.doesNotContain=" + UPDATED_SERVICE_TAX_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByTanNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tanNo equals to DEFAULT_TAN_NO
        defaultCustomerShouldBeFound("tanNo.equals=" + DEFAULT_TAN_NO);

        // Get all the customerList where tanNo equals to UPDATED_TAN_NO
        defaultCustomerShouldNotBeFound("tanNo.equals=" + UPDATED_TAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByTanNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tanNo in DEFAULT_TAN_NO or UPDATED_TAN_NO
        defaultCustomerShouldBeFound("tanNo.in=" + DEFAULT_TAN_NO + "," + UPDATED_TAN_NO);

        // Get all the customerList where tanNo equals to UPDATED_TAN_NO
        defaultCustomerShouldNotBeFound("tanNo.in=" + UPDATED_TAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByTanNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tanNo is not null
        defaultCustomerShouldBeFound("tanNo.specified=true");

        // Get all the customerList where tanNo is null
        defaultCustomerShouldNotBeFound("tanNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTanNoContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tanNo contains DEFAULT_TAN_NO
        defaultCustomerShouldBeFound("tanNo.contains=" + DEFAULT_TAN_NO);

        // Get all the customerList where tanNo contains UPDATED_TAN_NO
        defaultCustomerShouldNotBeFound("tanNo.contains=" + UPDATED_TAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByTanNoNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tanNo does not contain DEFAULT_TAN_NO
        defaultCustomerShouldNotBeFound("tanNo.doesNotContain=" + DEFAULT_TAN_NO);

        // Get all the customerList where tanNo does not contain UPDATED_TAN_NO
        defaultCustomerShouldBeFound("tanNo.doesNotContain=" + UPDATED_TAN_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where correlationId equals to DEFAULT_CORRELATION_ID
        defaultCustomerShouldBeFound("correlationId.equals=" + DEFAULT_CORRELATION_ID);

        // Get all the customerList where correlationId equals to UPDATED_CORRELATION_ID
        defaultCustomerShouldNotBeFound("correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where correlationId in DEFAULT_CORRELATION_ID or UPDATED_CORRELATION_ID
        defaultCustomerShouldBeFound("correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID);

        // Get all the customerList where correlationId equals to UPDATED_CORRELATION_ID
        defaultCustomerShouldNotBeFound("correlationId.in=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where correlationId is not null
        defaultCustomerShouldBeFound("correlationId.specified=true");

        // Get all the customerList where correlationId is null
        defaultCustomerShouldNotBeFound("correlationId.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAccountNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountNo equals to DEFAULT_ACCOUNT_NO
        defaultCustomerShouldBeFound("accountNo.equals=" + DEFAULT_ACCOUNT_NO);

        // Get all the customerList where accountNo equals to UPDATED_ACCOUNT_NO
        defaultCustomerShouldNotBeFound("accountNo.equals=" + UPDATED_ACCOUNT_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountNo in DEFAULT_ACCOUNT_NO or UPDATED_ACCOUNT_NO
        defaultCustomerShouldBeFound("accountNo.in=" + DEFAULT_ACCOUNT_NO + "," + UPDATED_ACCOUNT_NO);

        // Get all the customerList where accountNo equals to UPDATED_ACCOUNT_NO
        defaultCustomerShouldNotBeFound("accountNo.in=" + UPDATED_ACCOUNT_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountNo is not null
        defaultCustomerShouldBeFound("accountNo.specified=true");

        // Get all the customerList where accountNo is null
        defaultCustomerShouldNotBeFound("accountNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAccountNoContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountNo contains DEFAULT_ACCOUNT_NO
        defaultCustomerShouldBeFound("accountNo.contains=" + DEFAULT_ACCOUNT_NO);

        // Get all the customerList where accountNo contains UPDATED_ACCOUNT_NO
        defaultCustomerShouldNotBeFound("accountNo.contains=" + UPDATED_ACCOUNT_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountNoNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountNo does not contain DEFAULT_ACCOUNT_NO
        defaultCustomerShouldNotBeFound("accountNo.doesNotContain=" + DEFAULT_ACCOUNT_NO);

        // Get all the customerList where accountNo does not contain UPDATED_ACCOUNT_NO
        defaultCustomerShouldBeFound("accountNo.doesNotContain=" + UPDATED_ACCOUNT_NO);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateName equals to DEFAULT_GST_STATE_NAME
        defaultCustomerShouldBeFound("gstStateName.equals=" + DEFAULT_GST_STATE_NAME);

        // Get all the customerList where gstStateName equals to UPDATED_GST_STATE_NAME
        defaultCustomerShouldNotBeFound("gstStateName.equals=" + UPDATED_GST_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateName in DEFAULT_GST_STATE_NAME or UPDATED_GST_STATE_NAME
        defaultCustomerShouldBeFound("gstStateName.in=" + DEFAULT_GST_STATE_NAME + "," + UPDATED_GST_STATE_NAME);

        // Get all the customerList where gstStateName equals to UPDATED_GST_STATE_NAME
        defaultCustomerShouldNotBeFound("gstStateName.in=" + UPDATED_GST_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateName is not null
        defaultCustomerShouldBeFound("gstStateName.specified=true");

        // Get all the customerList where gstStateName is null
        defaultCustomerShouldNotBeFound("gstStateName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateName contains DEFAULT_GST_STATE_NAME
        defaultCustomerShouldBeFound("gstStateName.contains=" + DEFAULT_GST_STATE_NAME);

        // Get all the customerList where gstStateName contains UPDATED_GST_STATE_NAME
        defaultCustomerShouldNotBeFound("gstStateName.contains=" + UPDATED_GST_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateName does not contain DEFAULT_GST_STATE_NAME
        defaultCustomerShouldNotBeFound("gstStateName.doesNotContain=" + DEFAULT_GST_STATE_NAME);

        // Get all the customerList where gstStateName does not contain UPDATED_GST_STATE_NAME
        defaultCustomerShouldBeFound("gstStateName.doesNotContain=" + UPDATED_GST_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateCode equals to DEFAULT_GST_STATE_CODE
        defaultCustomerShouldBeFound("gstStateCode.equals=" + DEFAULT_GST_STATE_CODE);

        // Get all the customerList where gstStateCode equals to UPDATED_GST_STATE_CODE
        defaultCustomerShouldNotBeFound("gstStateCode.equals=" + UPDATED_GST_STATE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateCode in DEFAULT_GST_STATE_CODE or UPDATED_GST_STATE_CODE
        defaultCustomerShouldBeFound("gstStateCode.in=" + DEFAULT_GST_STATE_CODE + "," + UPDATED_GST_STATE_CODE);

        // Get all the customerList where gstStateCode equals to UPDATED_GST_STATE_CODE
        defaultCustomerShouldNotBeFound("gstStateCode.in=" + UPDATED_GST_STATE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateCode is not null
        defaultCustomerShouldBeFound("gstStateCode.specified=true");

        // Get all the customerList where gstStateCode is null
        defaultCustomerShouldNotBeFound("gstStateCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateCodeContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateCode contains DEFAULT_GST_STATE_CODE
        defaultCustomerShouldBeFound("gstStateCode.contains=" + DEFAULT_GST_STATE_CODE);

        // Get all the customerList where gstStateCode contains UPDATED_GST_STATE_CODE
        defaultCustomerShouldNotBeFound("gstStateCode.contains=" + UPDATED_GST_STATE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomersByGstStateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where gstStateCode does not contain DEFAULT_GST_STATE_CODE
        defaultCustomerShouldNotBeFound("gstStateCode.doesNotContain=" + DEFAULT_GST_STATE_CODE);

        // Get all the customerList where gstStateCode does not contain UPDATED_GST_STATE_CODE
        defaultCustomerShouldBeFound("gstStateCode.doesNotContain=" + UPDATED_GST_STATE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomersByIsSubmitSampleWithoutPOIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isSubmitSampleWithoutPO equals to DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO
        defaultCustomerShouldBeFound("isSubmitSampleWithoutPO.equals=" + DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO);

        // Get all the customerList where isSubmitSampleWithoutPO equals to UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO
        defaultCustomerShouldNotBeFound("isSubmitSampleWithoutPO.equals=" + UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO);
    }

    @Test
    @Transactional
    void getAllCustomersByIsSubmitSampleWithoutPOIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isSubmitSampleWithoutPO in DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO or UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO
        defaultCustomerShouldBeFound(
            "isSubmitSampleWithoutPO.in=" + DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO + "," + UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO
        );

        // Get all the customerList where isSubmitSampleWithoutPO equals to UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO
        defaultCustomerShouldNotBeFound("isSubmitSampleWithoutPO.in=" + UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO);
    }

    @Test
    @Transactional
    void getAllCustomersByIsSubmitSampleWithoutPOIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isSubmitSampleWithoutPO is not null
        defaultCustomerShouldBeFound("isSubmitSampleWithoutPO.specified=true");

        // Get all the customerList where isSubmitSampleWithoutPO is null
        defaultCustomerShouldNotBeFound("isSubmitSampleWithoutPO.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByIsBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isBlock equals to DEFAULT_IS_BLOCK
        defaultCustomerShouldBeFound("isBlock.equals=" + DEFAULT_IS_BLOCK);

        // Get all the customerList where isBlock equals to UPDATED_IS_BLOCK
        defaultCustomerShouldNotBeFound("isBlock.equals=" + UPDATED_IS_BLOCK);
    }

    @Test
    @Transactional
    void getAllCustomersByIsBlockIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isBlock in DEFAULT_IS_BLOCK or UPDATED_IS_BLOCK
        defaultCustomerShouldBeFound("isBlock.in=" + DEFAULT_IS_BLOCK + "," + UPDATED_IS_BLOCK);

        // Get all the customerList where isBlock equals to UPDATED_IS_BLOCK
        defaultCustomerShouldNotBeFound("isBlock.in=" + UPDATED_IS_BLOCK);
    }

    @Test
    @Transactional
    void getAllCustomersByIsBlockIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isBlock is not null
        defaultCustomerShouldBeFound("isBlock.specified=true");

        // Get all the customerList where isBlock is null
        defaultCustomerShouldNotBeFound("isBlock.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountType equals to DEFAULT_ACCOUNT_TYPE
        defaultCustomerShouldBeFound("accountType.equals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the customerList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultCustomerShouldNotBeFound("accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountType in DEFAULT_ACCOUNT_TYPE or UPDATED_ACCOUNT_TYPE
        defaultCustomerShouldBeFound("accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE);

        // Get all the customerList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultCustomerShouldNotBeFound("accountType.in=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountType is not null
        defaultCustomerShouldBeFound("accountType.specified=true");

        // Get all the customerList where accountType is null
        defaultCustomerShouldNotBeFound("accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAccountManagementIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountManagement equals to DEFAULT_ACCOUNT_MANAGEMENT
        defaultCustomerShouldBeFound("accountManagement.equals=" + DEFAULT_ACCOUNT_MANAGEMENT);

        // Get all the customerList where accountManagement equals to UPDATED_ACCOUNT_MANAGEMENT
        defaultCustomerShouldNotBeFound("accountManagement.equals=" + UPDATED_ACCOUNT_MANAGEMENT);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountManagementIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountManagement in DEFAULT_ACCOUNT_MANAGEMENT or UPDATED_ACCOUNT_MANAGEMENT
        defaultCustomerShouldBeFound("accountManagement.in=" + DEFAULT_ACCOUNT_MANAGEMENT + "," + UPDATED_ACCOUNT_MANAGEMENT);

        // Get all the customerList where accountManagement equals to UPDATED_ACCOUNT_MANAGEMENT
        defaultCustomerShouldNotBeFound("accountManagement.in=" + UPDATED_ACCOUNT_MANAGEMENT);
    }

    @Test
    @Transactional
    void getAllCustomersByAccountManagementIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where accountManagement is not null
        defaultCustomerShouldBeFound("accountManagement.specified=true");

        // Get all the customerList where accountManagement is null
        defaultCustomerShouldNotBeFound("accountManagement.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential equals to DEFAULT_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.equals=" + DEFAULT_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential equals to UPDATED_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.equals=" + UPDATED_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential in DEFAULT_REVENUE_POTENTIAL or UPDATED_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.in=" + DEFAULT_REVENUE_POTENTIAL + "," + UPDATED_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential equals to UPDATED_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.in=" + UPDATED_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential is not null
        defaultCustomerShouldBeFound("revenuePotential.specified=true");

        // Get all the customerList where revenuePotential is null
        defaultCustomerShouldNotBeFound("revenuePotential.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential is greater than or equal to DEFAULT_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.greaterThanOrEqual=" + DEFAULT_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential is greater than or equal to UPDATED_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.greaterThanOrEqual=" + UPDATED_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential is less than or equal to DEFAULT_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.lessThanOrEqual=" + DEFAULT_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential is less than or equal to SMALLER_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.lessThanOrEqual=" + SMALLER_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential is less than DEFAULT_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.lessThan=" + DEFAULT_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential is less than UPDATED_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.lessThan=" + UPDATED_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByRevenuePotentialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where revenuePotential is greater than DEFAULT_REVENUE_POTENTIAL
        defaultCustomerShouldNotBeFound("revenuePotential.greaterThan=" + DEFAULT_REVENUE_POTENTIAL);

        // Get all the customerList where revenuePotential is greater than SMALLER_REVENUE_POTENTIAL
        defaultCustomerShouldBeFound("revenuePotential.greaterThan=" + SMALLER_REVENUE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential equals to DEFAULT_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.equals=" + DEFAULT_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential equals to UPDATED_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.equals=" + UPDATED_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential in DEFAULT_SAMPLE_POTENTIAL or UPDATED_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.in=" + DEFAULT_SAMPLE_POTENTIAL + "," + UPDATED_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential equals to UPDATED_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.in=" + UPDATED_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential is not null
        defaultCustomerShouldBeFound("samplePotential.specified=true");

        // Get all the customerList where samplePotential is null
        defaultCustomerShouldNotBeFound("samplePotential.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential is greater than or equal to DEFAULT_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.greaterThanOrEqual=" + DEFAULT_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential is greater than or equal to UPDATED_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.greaterThanOrEqual=" + UPDATED_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential is less than or equal to DEFAULT_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.lessThanOrEqual=" + DEFAULT_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential is less than or equal to SMALLER_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.lessThanOrEqual=" + SMALLER_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential is less than DEFAULT_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.lessThan=" + DEFAULT_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential is less than UPDATED_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.lessThan=" + UPDATED_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersBySamplePotentialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where samplePotential is greater than DEFAULT_SAMPLE_POTENTIAL
        defaultCustomerShouldNotBeFound("samplePotential.greaterThan=" + DEFAULT_SAMPLE_POTENTIAL);

        // Get all the customerList where samplePotential is greater than SMALLER_SAMPLE_POTENTIAL
        defaultCustomerShouldBeFound("samplePotential.greaterThan=" + SMALLER_SAMPLE_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline equals to DEFAULT_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.equals=" + DEFAULT_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline equals to UPDATED_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.equals=" + UPDATED_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline in DEFAULT_TOTAL_PIPELINE or UPDATED_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.in=" + DEFAULT_TOTAL_PIPELINE + "," + UPDATED_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline equals to UPDATED_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.in=" + UPDATED_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline is not null
        defaultCustomerShouldBeFound("totalPipeline.specified=true");

        // Get all the customerList where totalPipeline is null
        defaultCustomerShouldNotBeFound("totalPipeline.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline is greater than or equal to DEFAULT_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.greaterThanOrEqual=" + DEFAULT_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline is greater than or equal to UPDATED_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.greaterThanOrEqual=" + UPDATED_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline is less than or equal to DEFAULT_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.lessThanOrEqual=" + DEFAULT_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline is less than or equal to SMALLER_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.lessThanOrEqual=" + SMALLER_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline is less than DEFAULT_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.lessThan=" + DEFAULT_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline is less than UPDATED_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.lessThan=" + UPDATED_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPipelineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPipeline is greater than DEFAULT_TOTAL_PIPELINE
        defaultCustomerShouldNotBeFound("totalPipeline.greaterThan=" + DEFAULT_TOTAL_PIPELINE);

        // Get all the customerList where totalPipeline is greater than SMALLER_TOTAL_PIPELINE
        defaultCustomerShouldBeFound("totalPipeline.greaterThan=" + SMALLER_TOTAL_PIPELINE);
    }

    @Test
    @Transactional
    void getAllCustomersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type equals to DEFAULT_TYPE
        defaultCustomerShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the customerList where type equals to UPDATED_TYPE
        defaultCustomerShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCustomerShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the customerList where type equals to UPDATED_TYPE
        defaultCustomerShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type is not null
        defaultCustomerShouldBeFound("type.specified=true");

        // Get all the customerList where type is null
        defaultCustomerShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTypeContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type contains DEFAULT_TYPE
        defaultCustomerShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the customerList where type contains UPDATED_TYPE
        defaultCustomerShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type does not contain DEFAULT_TYPE
        defaultCustomerShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the customerList where type does not contain UPDATED_TYPE
        defaultCustomerShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressesIsEqualToSomething() throws Exception {
        Address addresses;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            addresses = AddressResourceIT.createEntity(em);
        } else {
            addresses = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(addresses);
        em.flush();
        customer.addAddresses(addresses);
        customerRepository.saveAndFlush(customer);
        Long addressesId = addresses.getId();
        // Get all the customerList where addresses equals to addressesId
        defaultCustomerShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the customerList where addresses equals to (addressesId + 1)
        defaultCustomerShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyIsEqualToSomething() throws Exception {
        CustomerCompany company;
        if (TestUtil.findAll(em, CustomerCompany.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            company = CustomerCompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, CustomerCompany.class).get(0);
        }
        em.persist(company);
        em.flush();
        customer.setCompany(company);
        customerRepository.saveAndFlush(customer);
        Long companyId = company.getId();
        // Get all the customerList where company equals to companyId
        defaultCustomerShouldBeFound("companyId.equals=" + companyId);

        // Get all the customerList where company equals to (companyId + 1)
        defaultCustomerShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);
        Long userId = user.getId();
        // Get all the customerList where user equals to userId
        defaultCustomerShouldBeFound("userId.equals=" + userId);

        // Get all the customerList where user equals to (userId + 1)
        defaultCustomerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsEqualToSomething() throws Exception {
        MasterStaticType customerType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            customerType = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            customerType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(customerType);
        em.flush();
        customer.setCustomerType(customerType);
        customerRepository.saveAndFlush(customer);
        Long customerTypeId = customerType.getId();
        // Get all the customerList where customerType equals to customerTypeId
        defaultCustomerShouldBeFound("customerTypeId.equals=" + customerTypeId);

        // Get all the customerList where customerType equals to (customerTypeId + 1)
        defaultCustomerShouldNotBeFound("customerTypeId.equals=" + (customerTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerStatusIsEqualToSomething() throws Exception {
        MasterStaticType customerStatus;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            customerStatus = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            customerStatus = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(customerStatus);
        em.flush();
        customer.setCustomerStatus(customerStatus);
        customerRepository.saveAndFlush(customer);
        Long customerStatusId = customerStatus.getId();
        // Get all the customerList where customerStatus equals to customerStatusId
        defaultCustomerShouldBeFound("customerStatusId.equals=" + customerStatusId);

        // Get all the customerList where customerStatus equals to (customerStatusId + 1)
        defaultCustomerShouldNotBeFound("customerStatusId.equals=" + (customerStatusId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByOwnershipTypeIsEqualToSomething() throws Exception {
        MasterStaticType ownershipType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            ownershipType = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            ownershipType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(ownershipType);
        em.flush();
        customer.setOwnershipType(ownershipType);
        customerRepository.saveAndFlush(customer);
        Long ownershipTypeId = ownershipType.getId();
        // Get all the customerList where ownershipType equals to ownershipTypeId
        defaultCustomerShouldBeFound("ownershipTypeId.equals=" + ownershipTypeId);

        // Get all the customerList where ownershipType equals to (ownershipTypeId + 1)
        defaultCustomerShouldNotBeFound("ownershipTypeId.equals=" + (ownershipTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByIndustryTypeIsEqualToSomething() throws Exception {
        MasterStaticType industryType;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            industryType = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            industryType = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(industryType);
        em.flush();
        customer.setIndustryType(industryType);
        customerRepository.saveAndFlush(customer);
        Long industryTypeId = industryType.getId();
        // Get all the customerList where industryType equals to industryTypeId
        defaultCustomerShouldBeFound("industryTypeId.equals=" + industryTypeId);

        // Get all the customerList where industryType equals to (industryTypeId + 1)
        defaultCustomerShouldNotBeFound("industryTypeId.equals=" + (industryTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerCategoryIsEqualToSomething() throws Exception {
        MasterStaticType customerCategory;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            customerCategory = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            customerCategory = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(customerCategory);
        em.flush();
        customer.setCustomerCategory(customerCategory);
        customerRepository.saveAndFlush(customer);
        Long customerCategoryId = customerCategory.getId();
        // Get all the customerList where customerCategory equals to customerCategoryId
        defaultCustomerShouldBeFound("customerCategoryId.equals=" + customerCategoryId);

        // Get all the customerList where customerCategory equals to (customerCategoryId + 1)
        defaultCustomerShouldNotBeFound("customerCategoryId.equals=" + (customerCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByPaymentTermsIsEqualToSomething() throws Exception {
        MasterStaticType paymentTerms;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            paymentTerms = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            paymentTerms = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(paymentTerms);
        em.flush();
        customer.setPaymentTerms(paymentTerms);
        customerRepository.saveAndFlush(customer);
        Long paymentTermsId = paymentTerms.getId();
        // Get all the customerList where paymentTerms equals to paymentTermsId
        defaultCustomerShouldBeFound("paymentTermsId.equals=" + paymentTermsId);

        // Get all the customerList where paymentTerms equals to (paymentTermsId + 1)
        defaultCustomerShouldNotBeFound("paymentTermsId.equals=" + (paymentTermsId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByInvoiceFrequencyIsEqualToSomething() throws Exception {
        MasterStaticType invoiceFrequency;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            invoiceFrequency = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            invoiceFrequency = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(invoiceFrequency);
        em.flush();
        customer.setInvoiceFrequency(invoiceFrequency);
        customerRepository.saveAndFlush(customer);
        Long invoiceFrequencyId = invoiceFrequency.getId();
        // Get all the customerList where invoiceFrequency equals to invoiceFrequencyId
        defaultCustomerShouldBeFound("invoiceFrequencyId.equals=" + invoiceFrequencyId);

        // Get all the customerList where invoiceFrequency equals to (invoiceFrequencyId + 1)
        defaultCustomerShouldNotBeFound("invoiceFrequencyId.equals=" + (invoiceFrequencyId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByGstTreatmentIsEqualToSomething() throws Exception {
        MasterStaticType gstTreatment;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            gstTreatment = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            gstTreatment = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(gstTreatment);
        em.flush();
        customer.setGstTreatment(gstTreatment);
        customerRepository.saveAndFlush(customer);
        Long gstTreatmentId = gstTreatment.getId();
        // Get all the customerList where gstTreatment equals to gstTreatmentId
        defaultCustomerShouldBeFound("gstTreatmentId.equals=" + gstTreatmentId);

        // Get all the customerList where gstTreatment equals to (gstTreatmentId + 1)
        defaultCustomerShouldNotBeFound("gstTreatmentId.equals=" + (gstTreatmentId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByOutstandingPersonIsEqualToSomething() throws Exception {
        User outstandingPerson;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            outstandingPerson = UserResourceIT.createEntity(em);
        } else {
            outstandingPerson = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(outstandingPerson);
        em.flush();
        customer.setOutstandingPerson(outstandingPerson);
        customerRepository.saveAndFlush(customer);
        Long outstandingPersonId = outstandingPerson.getId();
        // Get all the customerList where outstandingPerson equals to outstandingPersonId
        defaultCustomerShouldBeFound("outstandingPersonId.equals=" + outstandingPersonId);

        // Get all the customerList where outstandingPerson equals to (outstandingPersonId + 1)
        defaultCustomerShouldNotBeFound("outstandingPersonId.equals=" + (outstandingPersonId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            department = DepartmentResourceIT.createEntity(em);
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        customer.setDepartment(department);
        customerRepository.saveAndFlush(customer);
        Long departmentId = department.getId();
        // Get all the customerList where department equals to departmentId
        defaultCustomerShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the customerList where department equals to (departmentId + 1)
        defaultCustomerShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByTenatIsEqualToSomething() throws Exception {
        Tenant tenat;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            tenat = TenantResourceIT.createEntity(em);
        } else {
            tenat = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenat);
        em.flush();
        customer.setTenat(tenat);
        customerRepository.saveAndFlush(customer);
        Long tenatId = tenat.getId();
        // Get all the customerList where tenat equals to tenatId
        defaultCustomerShouldBeFound("tenatId.equals=" + tenatId);

        // Get all the customerList where tenat equals to (tenatId + 1)
        defaultCustomerShouldNotBeFound("tenatId.equals=" + (tenatId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByMasterCategoriesIsEqualToSomething() throws Exception {
        MasterCategory masterCategories;
        if (TestUtil.findAll(em, MasterCategory.class).isEmpty()) {
            customerRepository.saveAndFlush(customer);
            masterCategories = MasterCategoryResourceIT.createEntity(em);
        } else {
            masterCategories = TestUtil.findAll(em, MasterCategory.class).get(0);
        }
        em.persist(masterCategories);
        em.flush();
        customer.addMasterCategories(masterCategories);
        customerRepository.saveAndFlush(customer);
        Long masterCategoriesId = masterCategories.getId();
        // Get all the customerList where masterCategories equals to masterCategoriesId
        defaultCustomerShouldBeFound("masterCategoriesId.equals=" + masterCategoriesId);

        // Get all the customerList where masterCategories equals to (masterCategoriesId + 1)
        defaultCustomerShouldNotBeFound("masterCategoriesId.equals=" + (masterCategoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].companyCity").value(hasItem(DEFAULT_COMPANY_CITY)))
            .andExpect(jsonPath("$.[*].companyArea").value(hasItem(DEFAULT_COMPANY_AREA)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].currencyType").value(hasItem(DEFAULT_CURRENCY_TYPE)))
            .andExpect(jsonPath("$.[*].maxInvoiceAmount").value(hasItem(DEFAULT_MAX_INVOICE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].gstNo").value(hasItem(DEFAULT_GST_NO)))
            .andExpect(jsonPath("$.[*].panNo").value(hasItem(DEFAULT_PAN_NO)))
            .andExpect(jsonPath("$.[*].serviceTaxNo").value(hasItem(DEFAULT_SERVICE_TAX_NO)))
            .andExpect(jsonPath("$.[*].tanNo").value(hasItem(DEFAULT_TAN_NO)))
            .andExpect(jsonPath("$.[*].customFieldData").value(hasItem(DEFAULT_CUSTOM_FIELD_DATA.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].gstStateName").value(hasItem(DEFAULT_GST_STATE_NAME)))
            .andExpect(jsonPath("$.[*].gstStateCode").value(hasItem(DEFAULT_GST_STATE_CODE)))
            .andExpect(jsonPath("$.[*].isSubmitSampleWithoutPO").value(hasItem(DEFAULT_IS_SUBMIT_SAMPLE_WITHOUT_PO.booleanValue())))
            .andExpect(jsonPath("$.[*].isBlock").value(hasItem(DEFAULT_IS_BLOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountManagement").value(hasItem(DEFAULT_ACCOUNT_MANAGEMENT.toString())))
            .andExpect(jsonPath("$.[*].revenuePotential").value(hasItem(DEFAULT_REVENUE_POTENTIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].samplePotential").value(hasItem(DEFAULT_SAMPLE_POTENTIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].totalPipeline").value(hasItem(DEFAULT_TOTAL_PIPELINE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyArea(UPDATED_COMPANY_AREA)
            .website(UPDATED_WEBSITE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .currencyType(UPDATED_CURRENCY_TYPE)
            .maxInvoiceAmount(UPDATED_MAX_INVOICE_AMOUNT)
            .gstNo(UPDATED_GST_NO)
            .panNo(UPDATED_PAN_NO)
            .serviceTaxNo(UPDATED_SERVICE_TAX_NO)
            .tanNo(UPDATED_TAN_NO)
            .customFieldData(UPDATED_CUSTOM_FIELD_DATA)
            .correlationId(UPDATED_CORRELATION_ID)
            .accountNo(UPDATED_ACCOUNT_NO)
            .gstStateName(UPDATED_GST_STATE_NAME)
            .gstStateCode(UPDATED_GST_STATE_CODE)
            .isSubmitSampleWithoutPO(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO)
            .isBlock(UPDATED_IS_BLOCK)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountManagement(UPDATED_ACCOUNT_MANAGEMENT)
            .revenuePotential(UPDATED_REVENUE_POTENTIAL)
            .samplePotential(UPDATED_SAMPLE_POTENTIAL)
            .remarks(UPDATED_REMARKS)
            .totalPipeline(UPDATED_TOTAL_PIPELINE)
            .type(UPDATED_TYPE);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomer.getCompanyCity()).isEqualTo(UPDATED_COMPANY_CITY);
        assertThat(testCustomer.getCompanyArea()).isEqualTo(UPDATED_COMPANY_AREA);
        assertThat(testCustomer.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getCurrencyType()).isEqualTo(UPDATED_CURRENCY_TYPE);
        assertThat(testCustomer.getMaxInvoiceAmount()).isEqualTo(UPDATED_MAX_INVOICE_AMOUNT);
        assertThat(testCustomer.getGstNo()).isEqualTo(UPDATED_GST_NO);
        assertThat(testCustomer.getPanNo()).isEqualTo(UPDATED_PAN_NO);
        assertThat(testCustomer.getServiceTaxNo()).isEqualTo(UPDATED_SERVICE_TAX_NO);
        assertThat(testCustomer.getTanNo()).isEqualTo(UPDATED_TAN_NO);
        assertThat(testCustomer.getCustomFieldData()).isEqualTo(UPDATED_CUSTOM_FIELD_DATA);
        assertThat(testCustomer.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
        assertThat(testCustomer.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testCustomer.getGstStateName()).isEqualTo(UPDATED_GST_STATE_NAME);
        assertThat(testCustomer.getGstStateCode()).isEqualTo(UPDATED_GST_STATE_CODE);
        assertThat(testCustomer.getIsSubmitSampleWithoutPO()).isEqualTo(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO);
        assertThat(testCustomer.getIsBlock()).isEqualTo(UPDATED_IS_BLOCK);
        assertThat(testCustomer.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testCustomer.getAccountManagement()).isEqualTo(UPDATED_ACCOUNT_MANAGEMENT);
        assertThat(testCustomer.getRevenuePotential()).isEqualTo(UPDATED_REVENUE_POTENTIAL);
        assertThat(testCustomer.getSamplePotential()).isEqualTo(UPDATED_SAMPLE_POTENTIAL);
        assertThat(testCustomer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testCustomer.getTotalPipeline()).isEqualTo(UPDATED_TOTAL_PIPELINE);
        assertThat(testCustomer.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .name(UPDATED_NAME)
            .currencyType(UPDATED_CURRENCY_TYPE)
            .maxInvoiceAmount(UPDATED_MAX_INVOICE_AMOUNT)
            .gstNo(UPDATED_GST_NO)
            .gstStateCode(UPDATED_GST_STATE_CODE)
            .isSubmitSampleWithoutPO(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO)
            .remarks(UPDATED_REMARKS)
            .type(UPDATED_TYPE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomer.getCompanyCity()).isEqualTo(DEFAULT_COMPANY_CITY);
        assertThat(testCustomer.getCompanyArea()).isEqualTo(DEFAULT_COMPANY_AREA);
        assertThat(testCustomer.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getCurrencyType()).isEqualTo(UPDATED_CURRENCY_TYPE);
        assertThat(testCustomer.getMaxInvoiceAmount()).isEqualTo(UPDATED_MAX_INVOICE_AMOUNT);
        assertThat(testCustomer.getGstNo()).isEqualTo(UPDATED_GST_NO);
        assertThat(testCustomer.getPanNo()).isEqualTo(DEFAULT_PAN_NO);
        assertThat(testCustomer.getServiceTaxNo()).isEqualTo(DEFAULT_SERVICE_TAX_NO);
        assertThat(testCustomer.getTanNo()).isEqualTo(DEFAULT_TAN_NO);
        assertThat(testCustomer.getCustomFieldData()).isEqualTo(DEFAULT_CUSTOM_FIELD_DATA);
        assertThat(testCustomer.getCorrelationId()).isEqualTo(DEFAULT_CORRELATION_ID);
        assertThat(testCustomer.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testCustomer.getGstStateName()).isEqualTo(DEFAULT_GST_STATE_NAME);
        assertThat(testCustomer.getGstStateCode()).isEqualTo(UPDATED_GST_STATE_CODE);
        assertThat(testCustomer.getIsSubmitSampleWithoutPO()).isEqualTo(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO);
        assertThat(testCustomer.getIsBlock()).isEqualTo(DEFAULT_IS_BLOCK);
        assertThat(testCustomer.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testCustomer.getAccountManagement()).isEqualTo(DEFAULT_ACCOUNT_MANAGEMENT);
        assertThat(testCustomer.getRevenuePotential()).isEqualTo(DEFAULT_REVENUE_POTENTIAL);
        assertThat(testCustomer.getSamplePotential()).isEqualTo(DEFAULT_SAMPLE_POTENTIAL);
        assertThat(testCustomer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testCustomer.getTotalPipeline()).isEqualTo(DEFAULT_TOTAL_PIPELINE);
        assertThat(testCustomer.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyCity(UPDATED_COMPANY_CITY)
            .companyArea(UPDATED_COMPANY_AREA)
            .website(UPDATED_WEBSITE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .currencyType(UPDATED_CURRENCY_TYPE)
            .maxInvoiceAmount(UPDATED_MAX_INVOICE_AMOUNT)
            .gstNo(UPDATED_GST_NO)
            .panNo(UPDATED_PAN_NO)
            .serviceTaxNo(UPDATED_SERVICE_TAX_NO)
            .tanNo(UPDATED_TAN_NO)
            .customFieldData(UPDATED_CUSTOM_FIELD_DATA)
            .correlationId(UPDATED_CORRELATION_ID)
            .accountNo(UPDATED_ACCOUNT_NO)
            .gstStateName(UPDATED_GST_STATE_NAME)
            .gstStateCode(UPDATED_GST_STATE_CODE)
            .isSubmitSampleWithoutPO(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO)
            .isBlock(UPDATED_IS_BLOCK)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountManagement(UPDATED_ACCOUNT_MANAGEMENT)
            .revenuePotential(UPDATED_REVENUE_POTENTIAL)
            .samplePotential(UPDATED_SAMPLE_POTENTIAL)
            .remarks(UPDATED_REMARKS)
            .totalPipeline(UPDATED_TOTAL_PIPELINE)
            .type(UPDATED_TYPE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomer.getCompanyCity()).isEqualTo(UPDATED_COMPANY_CITY);
        assertThat(testCustomer.getCompanyArea()).isEqualTo(UPDATED_COMPANY_AREA);
        assertThat(testCustomer.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getCurrencyType()).isEqualTo(UPDATED_CURRENCY_TYPE);
        assertThat(testCustomer.getMaxInvoiceAmount()).isEqualTo(UPDATED_MAX_INVOICE_AMOUNT);
        assertThat(testCustomer.getGstNo()).isEqualTo(UPDATED_GST_NO);
        assertThat(testCustomer.getPanNo()).isEqualTo(UPDATED_PAN_NO);
        assertThat(testCustomer.getServiceTaxNo()).isEqualTo(UPDATED_SERVICE_TAX_NO);
        assertThat(testCustomer.getTanNo()).isEqualTo(UPDATED_TAN_NO);
        assertThat(testCustomer.getCustomFieldData()).isEqualTo(UPDATED_CUSTOM_FIELD_DATA);
        assertThat(testCustomer.getCorrelationId()).isEqualTo(UPDATED_CORRELATION_ID);
        assertThat(testCustomer.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testCustomer.getGstStateName()).isEqualTo(UPDATED_GST_STATE_NAME);
        assertThat(testCustomer.getGstStateCode()).isEqualTo(UPDATED_GST_STATE_CODE);
        assertThat(testCustomer.getIsSubmitSampleWithoutPO()).isEqualTo(UPDATED_IS_SUBMIT_SAMPLE_WITHOUT_PO);
        assertThat(testCustomer.getIsBlock()).isEqualTo(UPDATED_IS_BLOCK);
        assertThat(testCustomer.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testCustomer.getAccountManagement()).isEqualTo(UPDATED_ACCOUNT_MANAGEMENT);
        assertThat(testCustomer.getRevenuePotential()).isEqualTo(UPDATED_REVENUE_POTENTIAL);
        assertThat(testCustomer.getSamplePotential()).isEqualTo(UPDATED_SAMPLE_POTENTIAL);
        assertThat(testCustomer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testCustomer.getTotalPipeline()).isEqualTo(UPDATED_TOTAL_PIPELINE);
        assertThat(testCustomer.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
