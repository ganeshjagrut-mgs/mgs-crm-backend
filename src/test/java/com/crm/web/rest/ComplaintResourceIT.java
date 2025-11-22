package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.Complaint;
import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.User;
import com.crm.domain.enumeration.ComplaintStatus;
import com.crm.repository.ComplaintRepository;
import com.crm.service.ComplaintService;
import com.crm.service.dto.ComplaintDTO;
import com.crm.service.mapper.ComplaintMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link ComplaintResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ComplaintResourceIT {

    private static final String DEFAULT_COMPLAINT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPLAINT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLAINT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RECORD_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_NUMBERS = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPECTED_CLOSURE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_CLOSURE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ROOT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_ROOT_CAUSE = "BBBBBBBBBB";

    private static final ComplaintStatus DEFAULT_COMPLAINT_STATUS = ComplaintStatus.OPEN;
    private static final ComplaintStatus UPDATED_COMPLAINT_STATUS = ComplaintStatus.IN_PROGRESS;

    private static final String DEFAULT_CORRECTIVE_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_CORRECTIVE_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_PREVENTIVE_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_PREVENTIVE_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPLAINT_CLOSURE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLAINT_CLOSURE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/complaints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComplaintRepository complaintRepository;

    @Mock
    private ComplaintRepository complaintRepositoryMock;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Mock
    private ComplaintService complaintServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplaintMockMvc;

    private Complaint complaint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .complaintNumber(DEFAULT_COMPLAINT_NUMBER)
            .complaintDate(DEFAULT_COMPLAINT_DATE)
            .recordNumbers(DEFAULT_RECORD_NUMBERS)
            .customerContactNumber(DEFAULT_CUSTOMER_CONTACT_NUMBER)
            .customerContactEmail(DEFAULT_CUSTOMER_CONTACT_EMAIL)
            .complaintDescription(DEFAULT_COMPLAINT_DESCRIPTION)
            .expectedClosureDate(DEFAULT_EXPECTED_CLOSURE_DATE)
            .rootCause(DEFAULT_ROOT_CAUSE)
            .complaintStatus(DEFAULT_COMPLAINT_STATUS)
            .correctiveAction(DEFAULT_CORRECTIVE_ACTION)
            .preventiveAction(DEFAULT_PREVENTIVE_ACTION)
            .complaintClosureDate(DEFAULT_COMPLAINT_CLOSURE_DATE);
        return complaint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createUpdatedEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .complaintDate(UPDATED_COMPLAINT_DATE)
            .recordNumbers(UPDATED_RECORD_NUMBERS)
            .customerContactNumber(UPDATED_CUSTOMER_CONTACT_NUMBER)
            .customerContactEmail(UPDATED_CUSTOMER_CONTACT_EMAIL)
            .complaintDescription(UPDATED_COMPLAINT_DESCRIPTION)
            .expectedClosureDate(UPDATED_EXPECTED_CLOSURE_DATE)
            .rootCause(UPDATED_ROOT_CAUSE)
            .complaintStatus(UPDATED_COMPLAINT_STATUS)
            .correctiveAction(UPDATED_CORRECTIVE_ACTION)
            .preventiveAction(UPDATED_PREVENTIVE_ACTION)
            .complaintClosureDate(UPDATED_COMPLAINT_CLOSURE_DATE);
        return complaint;
    }

    @BeforeEach
    public void initTest() {
        complaint = createEntity(em);
    }

    @Test
    @Transactional
    void createComplaint() throws Exception {
        int databaseSizeBeforeCreate = complaintRepository.findAll().size();
        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isCreated());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate + 1);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getComplaintNumber()).isEqualTo(DEFAULT_COMPLAINT_NUMBER);
        assertThat(testComplaint.getComplaintDate()).isEqualTo(DEFAULT_COMPLAINT_DATE);
        assertThat(testComplaint.getRecordNumbers()).isEqualTo(DEFAULT_RECORD_NUMBERS);
        assertThat(testComplaint.getCustomerContactNumber()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_NUMBER);
        assertThat(testComplaint.getCustomerContactEmail()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_EMAIL);
        assertThat(testComplaint.getComplaintDescription()).isEqualTo(DEFAULT_COMPLAINT_DESCRIPTION);
        assertThat(testComplaint.getExpectedClosureDate()).isEqualTo(DEFAULT_EXPECTED_CLOSURE_DATE);
        assertThat(testComplaint.getRootCause()).isEqualTo(DEFAULT_ROOT_CAUSE);
        assertThat(testComplaint.getComplaintStatus()).isEqualTo(DEFAULT_COMPLAINT_STATUS);
        assertThat(testComplaint.getCorrectiveAction()).isEqualTo(DEFAULT_CORRECTIVE_ACTION);
        assertThat(testComplaint.getPreventiveAction()).isEqualTo(DEFAULT_PREVENTIVE_ACTION);
        assertThat(testComplaint.getComplaintClosureDate()).isEqualTo(DEFAULT_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void createComplaintWithExistingId() throws Exception {
        // Create the Complaint with an existing ID
        complaint.setId(1L);
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        int databaseSizeBeforeCreate = complaintRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComplaintNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setComplaintNumber(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComplaintDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setComplaintDate(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComplaints() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintNumber").value(hasItem(DEFAULT_COMPLAINT_NUMBER)))
            .andExpect(jsonPath("$.[*].complaintDate").value(hasItem(DEFAULT_COMPLAINT_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordNumbers").value(hasItem(DEFAULT_RECORD_NUMBERS)))
            .andExpect(jsonPath("$.[*].customerContactNumber").value(hasItem(DEFAULT_CUSTOMER_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].customerContactEmail").value(hasItem(DEFAULT_CUSTOMER_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].complaintDescription").value(hasItem(DEFAULT_COMPLAINT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].expectedClosureDate").value(hasItem(DEFAULT_EXPECTED_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].rootCause").value(hasItem(DEFAULT_ROOT_CAUSE.toString())))
            .andExpect(jsonPath("$.[*].complaintStatus").value(hasItem(DEFAULT_COMPLAINT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].correctiveAction").value(hasItem(DEFAULT_CORRECTIVE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].preventiveAction").value(hasItem(DEFAULT_PREVENTIVE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].complaintClosureDate").value(hasItem(DEFAULT_COMPLAINT_CLOSURE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComplaintsWithEagerRelationshipsIsEnabled() throws Exception {
        when(complaintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplaintMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(complaintServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComplaintsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(complaintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplaintMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(complaintRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get the complaint
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL_ID, complaint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complaint.getId().intValue()))
            .andExpect(jsonPath("$.complaintNumber").value(DEFAULT_COMPLAINT_NUMBER))
            .andExpect(jsonPath("$.complaintDate").value(DEFAULT_COMPLAINT_DATE.toString()))
            .andExpect(jsonPath("$.recordNumbers").value(DEFAULT_RECORD_NUMBERS))
            .andExpect(jsonPath("$.customerContactNumber").value(DEFAULT_CUSTOMER_CONTACT_NUMBER))
            .andExpect(jsonPath("$.customerContactEmail").value(DEFAULT_CUSTOMER_CONTACT_EMAIL))
            .andExpect(jsonPath("$.complaintDescription").value(DEFAULT_COMPLAINT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.expectedClosureDate").value(DEFAULT_EXPECTED_CLOSURE_DATE.toString()))
            .andExpect(jsonPath("$.rootCause").value(DEFAULT_ROOT_CAUSE.toString()))
            .andExpect(jsonPath("$.complaintStatus").value(DEFAULT_COMPLAINT_STATUS.toString()))
            .andExpect(jsonPath("$.correctiveAction").value(DEFAULT_CORRECTIVE_ACTION.toString()))
            .andExpect(jsonPath("$.preventiveAction").value(DEFAULT_PREVENTIVE_ACTION.toString()))
            .andExpect(jsonPath("$.complaintClosureDate").value(DEFAULT_COMPLAINT_CLOSURE_DATE.toString()));
    }

    @Test
    @Transactional
    void getComplaintsByIdFiltering() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        Long id = complaint.getId();

        defaultComplaintShouldBeFound("id.equals=" + id);
        defaultComplaintShouldNotBeFound("id.notEquals=" + id);

        defaultComplaintShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComplaintShouldNotBeFound("id.greaterThan=" + id);

        defaultComplaintShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComplaintShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber equals to DEFAULT_COMPLAINT_NUMBER
        defaultComplaintShouldBeFound("complaintNumber.equals=" + DEFAULT_COMPLAINT_NUMBER);

        // Get all the complaintList where complaintNumber equals to UPDATED_COMPLAINT_NUMBER
        defaultComplaintShouldNotBeFound("complaintNumber.equals=" + UPDATED_COMPLAINT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber in DEFAULT_COMPLAINT_NUMBER or UPDATED_COMPLAINT_NUMBER
        defaultComplaintShouldBeFound("complaintNumber.in=" + DEFAULT_COMPLAINT_NUMBER + "," + UPDATED_COMPLAINT_NUMBER);

        // Get all the complaintList where complaintNumber equals to UPDATED_COMPLAINT_NUMBER
        defaultComplaintShouldNotBeFound("complaintNumber.in=" + UPDATED_COMPLAINT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber is not null
        defaultComplaintShouldBeFound("complaintNumber.specified=true");

        // Get all the complaintList where complaintNumber is null
        defaultComplaintShouldNotBeFound("complaintNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber contains DEFAULT_COMPLAINT_NUMBER
        defaultComplaintShouldBeFound("complaintNumber.contains=" + DEFAULT_COMPLAINT_NUMBER);

        // Get all the complaintList where complaintNumber contains UPDATED_COMPLAINT_NUMBER
        defaultComplaintShouldNotBeFound("complaintNumber.contains=" + UPDATED_COMPLAINT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberNotContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber does not contain DEFAULT_COMPLAINT_NUMBER
        defaultComplaintShouldNotBeFound("complaintNumber.doesNotContain=" + DEFAULT_COMPLAINT_NUMBER);

        // Get all the complaintList where complaintNumber does not contain UPDATED_COMPLAINT_NUMBER
        defaultComplaintShouldBeFound("complaintNumber.doesNotContain=" + UPDATED_COMPLAINT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintDateIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintDate equals to DEFAULT_COMPLAINT_DATE
        defaultComplaintShouldBeFound("complaintDate.equals=" + DEFAULT_COMPLAINT_DATE);

        // Get all the complaintList where complaintDate equals to UPDATED_COMPLAINT_DATE
        defaultComplaintShouldNotBeFound("complaintDate.equals=" + UPDATED_COMPLAINT_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintDateIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintDate in DEFAULT_COMPLAINT_DATE or UPDATED_COMPLAINT_DATE
        defaultComplaintShouldBeFound("complaintDate.in=" + DEFAULT_COMPLAINT_DATE + "," + UPDATED_COMPLAINT_DATE);

        // Get all the complaintList where complaintDate equals to UPDATED_COMPLAINT_DATE
        defaultComplaintShouldNotBeFound("complaintDate.in=" + UPDATED_COMPLAINT_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintDate is not null
        defaultComplaintShouldBeFound("complaintDate.specified=true");

        // Get all the complaintList where complaintDate is null
        defaultComplaintShouldNotBeFound("complaintDate.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByRecordNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where recordNumbers equals to DEFAULT_RECORD_NUMBERS
        defaultComplaintShouldBeFound("recordNumbers.equals=" + DEFAULT_RECORD_NUMBERS);

        // Get all the complaintList where recordNumbers equals to UPDATED_RECORD_NUMBERS
        defaultComplaintShouldNotBeFound("recordNumbers.equals=" + UPDATED_RECORD_NUMBERS);
    }

    @Test
    @Transactional
    void getAllComplaintsByRecordNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where recordNumbers in DEFAULT_RECORD_NUMBERS or UPDATED_RECORD_NUMBERS
        defaultComplaintShouldBeFound("recordNumbers.in=" + DEFAULT_RECORD_NUMBERS + "," + UPDATED_RECORD_NUMBERS);

        // Get all the complaintList where recordNumbers equals to UPDATED_RECORD_NUMBERS
        defaultComplaintShouldNotBeFound("recordNumbers.in=" + UPDATED_RECORD_NUMBERS);
    }

    @Test
    @Transactional
    void getAllComplaintsByRecordNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where recordNumbers is not null
        defaultComplaintShouldBeFound("recordNumbers.specified=true");

        // Get all the complaintList where recordNumbers is null
        defaultComplaintShouldNotBeFound("recordNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByRecordNumbersContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where recordNumbers contains DEFAULT_RECORD_NUMBERS
        defaultComplaintShouldBeFound("recordNumbers.contains=" + DEFAULT_RECORD_NUMBERS);

        // Get all the complaintList where recordNumbers contains UPDATED_RECORD_NUMBERS
        defaultComplaintShouldNotBeFound("recordNumbers.contains=" + UPDATED_RECORD_NUMBERS);
    }

    @Test
    @Transactional
    void getAllComplaintsByRecordNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where recordNumbers does not contain DEFAULT_RECORD_NUMBERS
        defaultComplaintShouldNotBeFound("recordNumbers.doesNotContain=" + DEFAULT_RECORD_NUMBERS);

        // Get all the complaintList where recordNumbers does not contain UPDATED_RECORD_NUMBERS
        defaultComplaintShouldBeFound("recordNumbers.doesNotContain=" + UPDATED_RECORD_NUMBERS);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactNumber equals to DEFAULT_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldBeFound("customerContactNumber.equals=" + DEFAULT_CUSTOMER_CONTACT_NUMBER);

        // Get all the complaintList where customerContactNumber equals to UPDATED_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldNotBeFound("customerContactNumber.equals=" + UPDATED_CUSTOMER_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactNumber in DEFAULT_CUSTOMER_CONTACT_NUMBER or UPDATED_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldBeFound(
            "customerContactNumber.in=" + DEFAULT_CUSTOMER_CONTACT_NUMBER + "," + UPDATED_CUSTOMER_CONTACT_NUMBER
        );

        // Get all the complaintList where customerContactNumber equals to UPDATED_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldNotBeFound("customerContactNumber.in=" + UPDATED_CUSTOMER_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactNumber is not null
        defaultComplaintShouldBeFound("customerContactNumber.specified=true");

        // Get all the complaintList where customerContactNumber is null
        defaultComplaintShouldNotBeFound("customerContactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactNumberContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactNumber contains DEFAULT_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldBeFound("customerContactNumber.contains=" + DEFAULT_CUSTOMER_CONTACT_NUMBER);

        // Get all the complaintList where customerContactNumber contains UPDATED_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldNotBeFound("customerContactNumber.contains=" + UPDATED_CUSTOMER_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactNumber does not contain DEFAULT_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldNotBeFound("customerContactNumber.doesNotContain=" + DEFAULT_CUSTOMER_CONTACT_NUMBER);

        // Get all the complaintList where customerContactNumber does not contain UPDATED_CUSTOMER_CONTACT_NUMBER
        defaultComplaintShouldBeFound("customerContactNumber.doesNotContain=" + UPDATED_CUSTOMER_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactEmail equals to DEFAULT_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldBeFound("customerContactEmail.equals=" + DEFAULT_CUSTOMER_CONTACT_EMAIL);

        // Get all the complaintList where customerContactEmail equals to UPDATED_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldNotBeFound("customerContactEmail.equals=" + UPDATED_CUSTOMER_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactEmail in DEFAULT_CUSTOMER_CONTACT_EMAIL or UPDATED_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldBeFound("customerContactEmail.in=" + DEFAULT_CUSTOMER_CONTACT_EMAIL + "," + UPDATED_CUSTOMER_CONTACT_EMAIL);

        // Get all the complaintList where customerContactEmail equals to UPDATED_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldNotBeFound("customerContactEmail.in=" + UPDATED_CUSTOMER_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactEmail is not null
        defaultComplaintShouldBeFound("customerContactEmail.specified=true");

        // Get all the complaintList where customerContactEmail is null
        defaultComplaintShouldNotBeFound("customerContactEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactEmailContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactEmail contains DEFAULT_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldBeFound("customerContactEmail.contains=" + DEFAULT_CUSTOMER_CONTACT_EMAIL);

        // Get all the complaintList where customerContactEmail contains UPDATED_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldNotBeFound("customerContactEmail.contains=" + UPDATED_CUSTOMER_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerContactEmailNotContainsSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where customerContactEmail does not contain DEFAULT_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldNotBeFound("customerContactEmail.doesNotContain=" + DEFAULT_CUSTOMER_CONTACT_EMAIL);

        // Get all the complaintList where customerContactEmail does not contain UPDATED_CUSTOMER_CONTACT_EMAIL
        defaultComplaintShouldBeFound("customerContactEmail.doesNotContain=" + UPDATED_CUSTOMER_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllComplaintsByExpectedClosureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where expectedClosureDate equals to DEFAULT_EXPECTED_CLOSURE_DATE
        defaultComplaintShouldBeFound("expectedClosureDate.equals=" + DEFAULT_EXPECTED_CLOSURE_DATE);

        // Get all the complaintList where expectedClosureDate equals to UPDATED_EXPECTED_CLOSURE_DATE
        defaultComplaintShouldNotBeFound("expectedClosureDate.equals=" + UPDATED_EXPECTED_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByExpectedClosureDateIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where expectedClosureDate in DEFAULT_EXPECTED_CLOSURE_DATE or UPDATED_EXPECTED_CLOSURE_DATE
        defaultComplaintShouldBeFound("expectedClosureDate.in=" + DEFAULT_EXPECTED_CLOSURE_DATE + "," + UPDATED_EXPECTED_CLOSURE_DATE);

        // Get all the complaintList where expectedClosureDate equals to UPDATED_EXPECTED_CLOSURE_DATE
        defaultComplaintShouldNotBeFound("expectedClosureDate.in=" + UPDATED_EXPECTED_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByExpectedClosureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where expectedClosureDate is not null
        defaultComplaintShouldBeFound("expectedClosureDate.specified=true");

        // Get all the complaintList where expectedClosureDate is null
        defaultComplaintShouldNotBeFound("expectedClosureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintStatus equals to DEFAULT_COMPLAINT_STATUS
        defaultComplaintShouldBeFound("complaintStatus.equals=" + DEFAULT_COMPLAINT_STATUS);

        // Get all the complaintList where complaintStatus equals to UPDATED_COMPLAINT_STATUS
        defaultComplaintShouldNotBeFound("complaintStatus.equals=" + UPDATED_COMPLAINT_STATUS);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintStatusIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintStatus in DEFAULT_COMPLAINT_STATUS or UPDATED_COMPLAINT_STATUS
        defaultComplaintShouldBeFound("complaintStatus.in=" + DEFAULT_COMPLAINT_STATUS + "," + UPDATED_COMPLAINT_STATUS);

        // Get all the complaintList where complaintStatus equals to UPDATED_COMPLAINT_STATUS
        defaultComplaintShouldNotBeFound("complaintStatus.in=" + UPDATED_COMPLAINT_STATUS);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintStatus is not null
        defaultComplaintShouldBeFound("complaintStatus.specified=true");

        // Get all the complaintList where complaintStatus is null
        defaultComplaintShouldNotBeFound("complaintStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintClosureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintClosureDate equals to DEFAULT_COMPLAINT_CLOSURE_DATE
        defaultComplaintShouldBeFound("complaintClosureDate.equals=" + DEFAULT_COMPLAINT_CLOSURE_DATE);

        // Get all the complaintList where complaintClosureDate equals to UPDATED_COMPLAINT_CLOSURE_DATE
        defaultComplaintShouldNotBeFound("complaintClosureDate.equals=" + UPDATED_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintClosureDateIsInShouldWork() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintClosureDate in DEFAULT_COMPLAINT_CLOSURE_DATE or UPDATED_COMPLAINT_CLOSURE_DATE
        defaultComplaintShouldBeFound("complaintClosureDate.in=" + DEFAULT_COMPLAINT_CLOSURE_DATE + "," + UPDATED_COMPLAINT_CLOSURE_DATE);

        // Get all the complaintList where complaintClosureDate equals to UPDATED_COMPLAINT_CLOSURE_DATE
        defaultComplaintShouldNotBeFound("complaintClosureDate.in=" + UPDATED_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintClosureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintClosureDate is not null
        defaultComplaintShouldBeFound("complaintClosureDate.specified=true");

        // Get all the complaintList where complaintClosureDate is null
        defaultComplaintShouldNotBeFound("complaintClosureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerNameIsEqualToSomething() throws Exception {
        Customer customerName;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            customerName = CustomerResourceIT.createEntity(em);
        } else {
            customerName = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customerName);
        em.flush();
        complaint.setCustomerName(customerName);
        complaintRepository.saveAndFlush(complaint);
        Long customerNameId = customerName.getId();
        // Get all the complaintList where customerName equals to customerNameId
        defaultComplaintShouldBeFound("customerNameId.equals=" + customerNameId);

        // Get all the complaintList where customerName equals to (customerNameId + 1)
        defaultComplaintShouldNotBeFound("customerNameId.equals=" + (customerNameId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintRelatedToIsEqualToSomething() throws Exception {
        MasterStaticType complaintRelatedTo;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            complaintRelatedTo = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            complaintRelatedTo = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(complaintRelatedTo);
        em.flush();
        complaint.setComplaintRelatedTo(complaintRelatedTo);
        complaintRepository.saveAndFlush(complaint);
        Long complaintRelatedToId = complaintRelatedTo.getId();
        // Get all the complaintList where complaintRelatedTo equals to complaintRelatedToId
        defaultComplaintShouldBeFound("complaintRelatedToId.equals=" + complaintRelatedToId);

        // Get all the complaintList where complaintRelatedTo equals to (complaintRelatedToId + 1)
        defaultComplaintShouldNotBeFound("complaintRelatedToId.equals=" + (complaintRelatedToId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByTypeOfComplaintIsEqualToSomething() throws Exception {
        MasterStaticType typeOfComplaint;
        if (TestUtil.findAll(em, MasterStaticType.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            typeOfComplaint = MasterStaticTypeResourceIT.createEntity(em);
        } else {
            typeOfComplaint = TestUtil.findAll(em, MasterStaticType.class).get(0);
        }
        em.persist(typeOfComplaint);
        em.flush();
        complaint.setTypeOfComplaint(typeOfComplaint);
        complaintRepository.saveAndFlush(complaint);
        Long typeOfComplaintId = typeOfComplaint.getId();
        // Get all the complaintList where typeOfComplaint equals to typeOfComplaintId
        defaultComplaintShouldBeFound("typeOfComplaintId.equals=" + typeOfComplaintId);

        // Get all the complaintList where typeOfComplaint equals to (typeOfComplaintId + 1)
        defaultComplaintShouldNotBeFound("typeOfComplaintId.equals=" + (typeOfComplaintId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintRelatedPersonsIsEqualToSomething() throws Exception {
        User complaintRelatedPersons;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            complaintRelatedPersons = UserResourceIT.createEntity(em);
        } else {
            complaintRelatedPersons = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(complaintRelatedPersons);
        em.flush();
        complaint.addComplaintRelatedPersons(complaintRelatedPersons);
        complaintRepository.saveAndFlush(complaint);
        Long complaintRelatedPersonsId = complaintRelatedPersons.getId();
        // Get all the complaintList where complaintRelatedPersons equals to complaintRelatedPersonsId
        defaultComplaintShouldBeFound("complaintRelatedPersonsId.equals=" + complaintRelatedPersonsId);

        // Get all the complaintList where complaintRelatedPersons equals to (complaintRelatedPersonsId + 1)
        defaultComplaintShouldNotBeFound("complaintRelatedPersonsId.equals=" + (complaintRelatedPersonsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComplaintShouldBeFound(String filter) throws Exception {
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintNumber").value(hasItem(DEFAULT_COMPLAINT_NUMBER)))
            .andExpect(jsonPath("$.[*].complaintDate").value(hasItem(DEFAULT_COMPLAINT_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordNumbers").value(hasItem(DEFAULT_RECORD_NUMBERS)))
            .andExpect(jsonPath("$.[*].customerContactNumber").value(hasItem(DEFAULT_CUSTOMER_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].customerContactEmail").value(hasItem(DEFAULT_CUSTOMER_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].complaintDescription").value(hasItem(DEFAULT_COMPLAINT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].expectedClosureDate").value(hasItem(DEFAULT_EXPECTED_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].rootCause").value(hasItem(DEFAULT_ROOT_CAUSE.toString())))
            .andExpect(jsonPath("$.[*].complaintStatus").value(hasItem(DEFAULT_COMPLAINT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].correctiveAction").value(hasItem(DEFAULT_CORRECTIVE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].preventiveAction").value(hasItem(DEFAULT_PREVENTIVE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].complaintClosureDate").value(hasItem(DEFAULT_COMPLAINT_CLOSURE_DATE.toString())));

        // Check, that the count call also returns 1
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComplaintShouldNotBeFound(String filter) throws Exception {
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComplaint() throws Exception {
        // Get the complaint
        restComplaintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Update the complaint
        Complaint updatedComplaint = complaintRepository.findById(complaint.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedComplaint are not directly saved in db
        em.detach(updatedComplaint);
        updatedComplaint
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .complaintDate(UPDATED_COMPLAINT_DATE)
            .recordNumbers(UPDATED_RECORD_NUMBERS)
            .customerContactNumber(UPDATED_CUSTOMER_CONTACT_NUMBER)
            .customerContactEmail(UPDATED_CUSTOMER_CONTACT_EMAIL)
            .complaintDescription(UPDATED_COMPLAINT_DESCRIPTION)
            .expectedClosureDate(UPDATED_EXPECTED_CLOSURE_DATE)
            .rootCause(UPDATED_ROOT_CAUSE)
            .complaintStatus(UPDATED_COMPLAINT_STATUS)
            .correctiveAction(UPDATED_CORRECTIVE_ACTION)
            .preventiveAction(UPDATED_PREVENTIVE_ACTION)
            .complaintClosureDate(UPDATED_COMPLAINT_CLOSURE_DATE);
        ComplaintDTO complaintDTO = complaintMapper.toDto(updatedComplaint);

        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getComplaintNumber()).isEqualTo(UPDATED_COMPLAINT_NUMBER);
        assertThat(testComplaint.getComplaintDate()).isEqualTo(UPDATED_COMPLAINT_DATE);
        assertThat(testComplaint.getRecordNumbers()).isEqualTo(UPDATED_RECORD_NUMBERS);
        assertThat(testComplaint.getCustomerContactNumber()).isEqualTo(UPDATED_CUSTOMER_CONTACT_NUMBER);
        assertThat(testComplaint.getCustomerContactEmail()).isEqualTo(UPDATED_CUSTOMER_CONTACT_EMAIL);
        assertThat(testComplaint.getComplaintDescription()).isEqualTo(UPDATED_COMPLAINT_DESCRIPTION);
        assertThat(testComplaint.getExpectedClosureDate()).isEqualTo(UPDATED_EXPECTED_CLOSURE_DATE);
        assertThat(testComplaint.getRootCause()).isEqualTo(UPDATED_ROOT_CAUSE);
        assertThat(testComplaint.getComplaintStatus()).isEqualTo(UPDATED_COMPLAINT_STATUS);
        assertThat(testComplaint.getCorrectiveAction()).isEqualTo(UPDATED_CORRECTIVE_ACTION);
        assertThat(testComplaint.getPreventiveAction()).isEqualTo(UPDATED_PREVENTIVE_ACTION);
        assertThat(testComplaint.getComplaintClosureDate()).isEqualTo(UPDATED_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplaintWithPatch() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Update the complaint using partial update
        Complaint partialUpdatedComplaint = new Complaint();
        partialUpdatedComplaint.setId(complaint.getId());

        partialUpdatedComplaint
            .recordNumbers(UPDATED_RECORD_NUMBERS)
            .customerContactNumber(UPDATED_CUSTOMER_CONTACT_NUMBER)
            .complaintDescription(UPDATED_COMPLAINT_DESCRIPTION)
            .expectedClosureDate(UPDATED_EXPECTED_CLOSURE_DATE)
            .preventiveAction(UPDATED_PREVENTIVE_ACTION)
            .complaintClosureDate(UPDATED_COMPLAINT_CLOSURE_DATE);

        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplaint))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getComplaintNumber()).isEqualTo(DEFAULT_COMPLAINT_NUMBER);
        assertThat(testComplaint.getComplaintDate()).isEqualTo(DEFAULT_COMPLAINT_DATE);
        assertThat(testComplaint.getRecordNumbers()).isEqualTo(UPDATED_RECORD_NUMBERS);
        assertThat(testComplaint.getCustomerContactNumber()).isEqualTo(UPDATED_CUSTOMER_CONTACT_NUMBER);
        assertThat(testComplaint.getCustomerContactEmail()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_EMAIL);
        assertThat(testComplaint.getComplaintDescription()).isEqualTo(UPDATED_COMPLAINT_DESCRIPTION);
        assertThat(testComplaint.getExpectedClosureDate()).isEqualTo(UPDATED_EXPECTED_CLOSURE_DATE);
        assertThat(testComplaint.getRootCause()).isEqualTo(DEFAULT_ROOT_CAUSE);
        assertThat(testComplaint.getComplaintStatus()).isEqualTo(DEFAULT_COMPLAINT_STATUS);
        assertThat(testComplaint.getCorrectiveAction()).isEqualTo(DEFAULT_CORRECTIVE_ACTION);
        assertThat(testComplaint.getPreventiveAction()).isEqualTo(UPDATED_PREVENTIVE_ACTION);
        assertThat(testComplaint.getComplaintClosureDate()).isEqualTo(UPDATED_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateComplaintWithPatch() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Update the complaint using partial update
        Complaint partialUpdatedComplaint = new Complaint();
        partialUpdatedComplaint.setId(complaint.getId());

        partialUpdatedComplaint
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .complaintDate(UPDATED_COMPLAINT_DATE)
            .recordNumbers(UPDATED_RECORD_NUMBERS)
            .customerContactNumber(UPDATED_CUSTOMER_CONTACT_NUMBER)
            .customerContactEmail(UPDATED_CUSTOMER_CONTACT_EMAIL)
            .complaintDescription(UPDATED_COMPLAINT_DESCRIPTION)
            .expectedClosureDate(UPDATED_EXPECTED_CLOSURE_DATE)
            .rootCause(UPDATED_ROOT_CAUSE)
            .complaintStatus(UPDATED_COMPLAINT_STATUS)
            .correctiveAction(UPDATED_CORRECTIVE_ACTION)
            .preventiveAction(UPDATED_PREVENTIVE_ACTION)
            .complaintClosureDate(UPDATED_COMPLAINT_CLOSURE_DATE);

        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplaint))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getComplaintNumber()).isEqualTo(UPDATED_COMPLAINT_NUMBER);
        assertThat(testComplaint.getComplaintDate()).isEqualTo(UPDATED_COMPLAINT_DATE);
        assertThat(testComplaint.getRecordNumbers()).isEqualTo(UPDATED_RECORD_NUMBERS);
        assertThat(testComplaint.getCustomerContactNumber()).isEqualTo(UPDATED_CUSTOMER_CONTACT_NUMBER);
        assertThat(testComplaint.getCustomerContactEmail()).isEqualTo(UPDATED_CUSTOMER_CONTACT_EMAIL);
        assertThat(testComplaint.getComplaintDescription()).isEqualTo(UPDATED_COMPLAINT_DESCRIPTION);
        assertThat(testComplaint.getExpectedClosureDate()).isEqualTo(UPDATED_EXPECTED_CLOSURE_DATE);
        assertThat(testComplaint.getRootCause()).isEqualTo(UPDATED_ROOT_CAUSE);
        assertThat(testComplaint.getComplaintStatus()).isEqualTo(UPDATED_COMPLAINT_STATUS);
        assertThat(testComplaint.getCorrectiveAction()).isEqualTo(UPDATED_CORRECTIVE_ACTION);
        assertThat(testComplaint.getPreventiveAction()).isEqualTo(UPDATED_PREVENTIVE_ACTION);
        assertThat(testComplaint.getComplaintClosureDate()).isEqualTo(UPDATED_COMPLAINT_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(complaintDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        int databaseSizeBeforeDelete = complaintRepository.findAll().size();

        // Delete the complaint
        restComplaintMockMvc
            .perform(delete(ENTITY_API_URL_ID, complaint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
