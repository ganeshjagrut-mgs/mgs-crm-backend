package com.mgs.web.rest;

import static com.mgs.domain.ComplaintAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Complaint;
import com.mgs.domain.ComplaintCategory;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Department;
import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.enumeration.ComplaintPriority;
import com.mgs.domain.enumeration.ComplaintSource;
import com.mgs.domain.enumeration.ComplaintStatus;
import com.mgs.repository.ComplaintRepository;
import com.mgs.service.dto.ComplaintDTO;
import com.mgs.service.mapper.ComplaintMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ComplaintResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComplaintResourceIT {

    private static final String DEFAULT_COMPLAINT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ComplaintPriority DEFAULT_PRIORITY = ComplaintPriority.LOW;
    private static final ComplaintPriority UPDATED_PRIORITY = ComplaintPriority.MEDIUM;

    private static final ComplaintStatus DEFAULT_STATUS = ComplaintStatus.OPEN;
    private static final ComplaintStatus UPDATED_STATUS = ComplaintStatus.IN_PROGRESS;

    private static final ComplaintSource DEFAULT_SOURCE = ComplaintSource.EMAIL;
    private static final ComplaintSource UPDATED_SOURCE = ComplaintSource.PHONE;

    private static final String ENTITY_API_URL = "/api/complaints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplaintMockMvc;

    private Complaint complaint;

    private Complaint insertedComplaint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .complaintNumber(DEFAULT_COMPLAINT_NUMBER)
            .subject(DEFAULT_SUBJECT)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .status(DEFAULT_STATUS)
            .source(DEFAULT_SOURCE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        complaint.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        complaint.setCustomer(customer);
        return complaint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complaint createUpdatedEntity(EntityManager em) {
        Complaint updatedComplaint = new Complaint()
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .source(UPDATED_SOURCE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedComplaint.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        updatedComplaint.setCustomer(customer);
        return updatedComplaint;
    }

    @BeforeEach
    void initTest() {
        complaint = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedComplaint != null) {
            complaintRepository.delete(insertedComplaint);
            insertedComplaint = null;
        }
    }

    @Test
    @Transactional
    void createComplaint() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
        var returnedComplaintDTO = om.readValue(
            restComplaintMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ComplaintDTO.class
        );

        // Validate the Complaint in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedComplaint = complaintMapper.toEntity(returnedComplaintDTO);
        assertComplaintUpdatableFieldsEquals(returnedComplaint, getPersistedComplaint(returnedComplaint));

        insertedComplaint = returnedComplaint;
    }

    @Test
    @Transactional
    void createComplaintWithExistingId() throws Exception {
        // Create the Complaint with an existing ID
        complaint.setId(1L);
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComplaintNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        complaint.setComplaintNumber(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        complaint.setSubject(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        complaint.setPriority(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        complaint.setStatus(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComplaints() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintNumber").value(hasItem(DEFAULT_COMPLAINT_NUMBER)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    void getComplaint() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get the complaint
        restComplaintMockMvc
            .perform(get(ENTITY_API_URL_ID, complaint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complaint.getId().intValue()))
            .andExpect(jsonPath("$.complaintNumber").value(DEFAULT_COMPLAINT_NUMBER))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    void getComplaintsByIdFiltering() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        Long id = complaint.getId();

        defaultComplaintFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultComplaintFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultComplaintFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber equals to
        defaultComplaintFiltering(
            "complaintNumber.equals=" + DEFAULT_COMPLAINT_NUMBER,
            "complaintNumber.equals=" + UPDATED_COMPLAINT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber in
        defaultComplaintFiltering(
            "complaintNumber.in=" + DEFAULT_COMPLAINT_NUMBER + "," + UPDATED_COMPLAINT_NUMBER,
            "complaintNumber.in=" + UPDATED_COMPLAINT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber is not null
        defaultComplaintFiltering("complaintNumber.specified=true", "complaintNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber contains
        defaultComplaintFiltering(
            "complaintNumber.contains=" + DEFAULT_COMPLAINT_NUMBER,
            "complaintNumber.contains=" + UPDATED_COMPLAINT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllComplaintsByComplaintNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where complaintNumber does not contain
        defaultComplaintFiltering(
            "complaintNumber.doesNotContain=" + UPDATED_COMPLAINT_NUMBER,
            "complaintNumber.doesNotContain=" + DEFAULT_COMPLAINT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllComplaintsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where subject equals to
        defaultComplaintFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllComplaintsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where subject in
        defaultComplaintFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllComplaintsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where subject is not null
        defaultComplaintFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where subject contains
        defaultComplaintFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllComplaintsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where subject does not contain
        defaultComplaintFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllComplaintsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where description equals to
        defaultComplaintFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllComplaintsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where description in
        defaultComplaintFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllComplaintsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where description is not null
        defaultComplaintFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where description contains
        defaultComplaintFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllComplaintsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where description does not contain
        defaultComplaintFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllComplaintsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where priority equals to
        defaultComplaintFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllComplaintsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where priority in
        defaultComplaintFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllComplaintsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where priority is not null
        defaultComplaintFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where status equals to
        defaultComplaintFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllComplaintsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where status in
        defaultComplaintFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllComplaintsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where status is not null
        defaultComplaintFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where source equals to
        defaultComplaintFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllComplaintsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where source in
        defaultComplaintFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllComplaintsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList where source is not null
        defaultComplaintFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllComplaintsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        complaint.setTenant(tenant);
        complaintRepository.saveAndFlush(complaint);
        Long tenantId = tenant.getId();
        // Get all the complaintList where tenant equals to tenantId
        defaultComplaintShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the complaintList where tenant equals to (tenantId + 1)
        defaultComplaintShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        complaint.setCustomer(customer);
        complaintRepository.saveAndFlush(complaint);
        Long customerId = customer.getId();
        // Get all the complaintList where customer equals to customerId
        defaultComplaintShouldBeFound("customerId.equals=" + customerId);

        // Get all the complaintList where customer equals to (customerId + 1)
        defaultComplaintShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        complaint.setContact(contact);
        complaintRepository.saveAndFlush(complaint);
        Long contactId = contact.getId();
        // Get all the complaintList where contact equals to contactId
        defaultComplaintShouldBeFound("contactId.equals=" + contactId);

        // Get all the complaintList where contact equals to (contactId + 1)
        defaultComplaintShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByCategoryIsEqualToSomething() throws Exception {
        ComplaintCategory category;
        if (TestUtil.findAll(em, ComplaintCategory.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            category = ComplaintCategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, ComplaintCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        complaint.setCategory(category);
        complaintRepository.saveAndFlush(complaint);
        Long categoryId = category.getId();
        // Get all the complaintList where category equals to categoryId
        defaultComplaintShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the complaintList where category equals to (categoryId + 1)
        defaultComplaintShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByPipelineIsEqualToSomething() throws Exception {
        Pipeline pipeline;
        if (TestUtil.findAll(em, Pipeline.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            pipeline = PipelineResourceIT.createEntity(em);
        } else {
            pipeline = TestUtil.findAll(em, Pipeline.class).get(0);
        }
        em.persist(pipeline);
        em.flush();
        complaint.setPipeline(pipeline);
        complaintRepository.saveAndFlush(complaint);
        Long pipelineId = pipeline.getId();
        // Get all the complaintList where pipeline equals to pipelineId
        defaultComplaintShouldBeFound("pipelineId.equals=" + pipelineId);

        // Get all the complaintList where pipeline equals to (pipelineId + 1)
        defaultComplaintShouldNotBeFound("pipelineId.equals=" + (pipelineId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByStageIsEqualToSomething() throws Exception {
        SubPipeline stage;
        if (TestUtil.findAll(em, SubPipeline.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            stage = SubPipelineResourceIT.createEntity(em);
        } else {
            stage = TestUtil.findAll(em, SubPipeline.class).get(0);
        }
        em.persist(stage);
        em.flush();
        complaint.setStage(stage);
        complaintRepository.saveAndFlush(complaint);
        Long stageId = stage.getId();
        // Get all the complaintList where stage equals to stageId
        defaultComplaintShouldBeFound("stageId.equals=" + stageId);

        // Get all the complaintList where stage equals to (stageId + 1)
        defaultComplaintShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByAssignedDepartmentIsEqualToSomething() throws Exception {
        Department assignedDepartment;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            assignedDepartment = DepartmentResourceIT.createEntity(em);
        } else {
            assignedDepartment = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(assignedDepartment);
        em.flush();
        complaint.setAssignedDepartment(assignedDepartment);
        complaintRepository.saveAndFlush(complaint);
        Long assignedDepartmentId = assignedDepartment.getId();
        // Get all the complaintList where assignedDepartment equals to assignedDepartmentId
        defaultComplaintShouldBeFound("assignedDepartmentId.equals=" + assignedDepartmentId);

        // Get all the complaintList where assignedDepartment equals to (assignedDepartmentId + 1)
        defaultComplaintShouldNotBeFound("assignedDepartmentId.equals=" + (assignedDepartmentId + 1));
    }

    @Test
    @Transactional
    void getAllComplaintsByAssignedUserIsEqualToSomething() throws Exception {
        User assignedUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            complaintRepository.saveAndFlush(complaint);
            assignedUser = UserResourceIT.createEntity(em);
        } else {
            assignedUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedUser);
        em.flush();
        complaint.setAssignedUser(assignedUser);
        complaintRepository.saveAndFlush(complaint);
        Long assignedUserId = assignedUser.getId();
        // Get all the complaintList where assignedUser equals to assignedUserId
        defaultComplaintShouldBeFound("assignedUserId.equals=" + assignedUserId);

        // Get all the complaintList where assignedUser equals to (assignedUserId + 1)
        defaultComplaintShouldNotBeFound("assignedUserId.equals=" + (assignedUserId + 1));
    }

    private void defaultComplaintFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultComplaintShouldBeFound(shouldBeFound);
        defaultComplaintShouldNotBeFound(shouldNotBeFound);
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
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));

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
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaint
        Complaint updatedComplaint = complaintRepository.findById(complaint.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedComplaint are not directly saved in db
        em.detach(updatedComplaint);
        updatedComplaint
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .source(UPDATED_SOURCE);
        ComplaintDTO complaintDTO = complaintMapper.toDto(updatedComplaint);

        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintDTO))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedComplaintToMatchAllProperties(updatedComplaint);
    }

    @Test
    @Transactional
    void putNonExistingComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplaintWithPatch() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaint using partial update
        Complaint partialUpdatedComplaint = new Complaint();
        partialUpdatedComplaint.setId(complaint.getId());

        partialUpdatedComplaint.subject(UPDATED_SUBJECT).priority(UPDATED_PRIORITY).status(UPDATED_STATUS);

        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComplaint))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComplaintUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedComplaint, complaint),
            getPersistedComplaint(complaint)
        );
    }

    @Test
    @Transactional
    void fullUpdateComplaintWithPatch() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the complaint using partial update
        Complaint partialUpdatedComplaint = new Complaint();
        partialUpdatedComplaint.setId(complaint.getId());

        partialUpdatedComplaint
            .complaintNumber(UPDATED_COMPLAINT_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .source(UPDATED_SOURCE);

        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplaint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComplaint))
            )
            .andExpect(status().isOk());

        // Validate the Complaint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComplaintUpdatableFieldsEquals(partialUpdatedComplaint, getPersistedComplaint(partialUpdatedComplaint));
    }

    @Test
    @Transactional
    void patchNonExistingComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complaintDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(complaintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplaint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        complaint.setId(longCount.incrementAndGet());

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplaintMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(complaintDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complaint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplaint() throws Exception {
        // Initialize the database
        insertedComplaint = complaintRepository.saveAndFlush(complaint);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the complaint
        restComplaintMockMvc
            .perform(delete(ENTITY_API_URL_ID, complaint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return complaintRepository.count();
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

    protected Complaint getPersistedComplaint(Complaint complaint) {
        return complaintRepository.findById(complaint.getId()).orElseThrow();
    }

    protected void assertPersistedComplaintToMatchAllProperties(Complaint expectedComplaint) {
        assertComplaintAllPropertiesEquals(expectedComplaint, getPersistedComplaint(expectedComplaint));
    }

    protected void assertPersistedComplaintToMatchUpdatableProperties(Complaint expectedComplaint) {
        assertComplaintAllUpdatablePropertiesEquals(expectedComplaint, getPersistedComplaint(expectedComplaint));
    }
}
