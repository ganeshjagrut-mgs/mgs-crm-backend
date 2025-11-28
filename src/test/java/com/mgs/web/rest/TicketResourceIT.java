package com.mgs.web.rest;

import static com.mgs.domain.TicketAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Tenant;
import com.mgs.domain.Ticket;
import com.mgs.domain.User;
import com.mgs.domain.enumeration.TicketPriority;
import com.mgs.domain.enumeration.TicketStatus;
import com.mgs.repository.TicketRepository;
import com.mgs.service.dto.TicketDTO;
import com.mgs.service.mapper.TicketMapper;
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
 * Integration tests for the {@link TicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketResourceIT {

    private static final String DEFAULT_TICKET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TicketPriority DEFAULT_PRIORITY = TicketPriority.LOW;
    private static final TicketPriority UPDATED_PRIORITY = TicketPriority.MEDIUM;

    private static final TicketStatus DEFAULT_STATUS = TicketStatus.OPEN;
    private static final TicketStatus UPDATED_STATUS = TicketStatus.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    private Ticket insertedTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .ticketNumber(DEFAULT_TICKET_NUMBER)
            .subject(DEFAULT_SUBJECT)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .status(DEFAULT_STATUS);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        ticket.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        ticket.setCustomer(customer);
        return ticket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket updatedTicket = new Ticket()
            .ticketNumber(UPDATED_TICKET_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedTicket.setTenant(tenant);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        updatedTicket.setCustomer(customer);
        return updatedTicket;
    }

    @BeforeEach
    void initTest() {
        ticket = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTicket != null) {
            ticketRepository.delete(insertedTicket);
            insertedTicket = null;
        }
    }

    @Test
    @Transactional
    void createTicket() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);
        var returnedTicketDTO = om.readValue(
            restTicketMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TicketDTO.class
        );

        // Validate the Ticket in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTicket = ticketMapper.toEntity(returnedTicketDTO);
        assertTicketUpdatableFieldsEquals(returnedTicket, getPersistedTicket(returnedTicket));

        insertedTicket = returnedTicket;
    }

    @Test
    @Transactional
    void createTicketWithExistingId() throws Exception {
        // Create the Ticket with an existing ID
        ticket.setId(1L);
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTicketNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ticket.setTicketNumber(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ticket.setSubject(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ticket.setPriority(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ticket.setStatus(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickets() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketNumber").value(hasItem(DEFAULT_TICKET_NUMBER)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTicket() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.ticketNumber").value(DEFAULT_TICKET_NUMBER))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTicketFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTicketFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketsByTicketNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketNumber equals to
        defaultTicketFiltering("ticketNumber.equals=" + DEFAULT_TICKET_NUMBER, "ticketNumber.equals=" + UPDATED_TICKET_NUMBER);
    }

    @Test
    @Transactional
    void getAllTicketsByTicketNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketNumber in
        defaultTicketFiltering(
            "ticketNumber.in=" + DEFAULT_TICKET_NUMBER + "," + UPDATED_TICKET_NUMBER,
            "ticketNumber.in=" + UPDATED_TICKET_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllTicketsByTicketNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketNumber is not null
        defaultTicketFiltering("ticketNumber.specified=true", "ticketNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByTicketNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketNumber contains
        defaultTicketFiltering("ticketNumber.contains=" + DEFAULT_TICKET_NUMBER, "ticketNumber.contains=" + UPDATED_TICKET_NUMBER);
    }

    @Test
    @Transactional
    void getAllTicketsByTicketNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketNumber does not contain
        defaultTicketFiltering(
            "ticketNumber.doesNotContain=" + UPDATED_TICKET_NUMBER,
            "ticketNumber.doesNotContain=" + DEFAULT_TICKET_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllTicketsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where subject equals to
        defaultTicketFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllTicketsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where subject in
        defaultTicketFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllTicketsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where subject is not null
        defaultTicketFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where subject contains
        defaultTicketFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllTicketsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where subject does not contain
        defaultTicketFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description equals to
        defaultTicketFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description in
        defaultTicketFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description is not null
        defaultTicketFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description contains
        defaultTicketFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description does not contain
        defaultTicketFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority equals to
        defaultTicketFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority in
        defaultTicketFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority is not null
        defaultTicketFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status equals to
        defaultTicketFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status in
        defaultTicketFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status is not null
        defaultTicketFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            ticketRepository.saveAndFlush(ticket);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        ticket.setTenant(tenant);
        ticketRepository.saveAndFlush(ticket);
        Long tenantId = tenant.getId();
        // Get all the ticketList where tenant equals to tenantId
        defaultTicketShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the ticketList where tenant equals to (tenantId + 1)
        defaultTicketShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            ticketRepository.saveAndFlush(ticket);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        ticket.setCustomer(customer);
        ticketRepository.saveAndFlush(ticket);
        Long customerId = customer.getId();
        // Get all the ticketList where customer equals to customerId
        defaultTicketShouldBeFound("customerId.equals=" + customerId);

        // Get all the ticketList where customer equals to (customerId + 1)
        defaultTicketShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            ticketRepository.saveAndFlush(ticket);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        ticket.setContact(contact);
        ticketRepository.saveAndFlush(ticket);
        Long contactId = contact.getId();
        // Get all the ticketList where contact equals to contactId
        defaultTicketShouldBeFound("contactId.equals=" + contactId);

        // Get all the ticketList where contact equals to (contactId + 1)
        defaultTicketShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByAssignedToIsEqualToSomething() throws Exception {
        User assignedTo;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            ticketRepository.saveAndFlush(ticket);
            assignedTo = UserResourceIT.createEntity(em);
        } else {
            assignedTo = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedTo);
        em.flush();
        ticket.setAssignedTo(assignedTo);
        ticketRepository.saveAndFlush(ticket);
        Long assignedToId = assignedTo.getId();
        // Get all the ticketList where assignedTo equals to assignedToId
        defaultTicketShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the ticketList where assignedTo equals to (assignedToId + 1)
        defaultTicketShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    private void defaultTicketFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTicketShouldBeFound(shouldBeFound);
        defaultTicketShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketNumber").value(hasItem(DEFAULT_TICKET_NUMBER)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicket() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket
            .ticketNumber(UPDATED_TICKET_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS);
        TicketDTO ticketDTO = ticketMapper.toDto(updatedTicket);

        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTicketToMatchAllProperties(updatedTicket);
    }

    @Test
    @Transactional
    void putNonExistingTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ticketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket.subject(UPDATED_SUBJECT).priority(UPDATED_PRIORITY);

        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTicketUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTicket, ticket), getPersistedTicket(ticket));
    }

    @Test
    @Transactional
    void fullUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket
            .ticketNumber(UPDATED_TICKET_NUMBER)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS);

        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTicketUpdatableFieldsEquals(partialUpdatedTicket, getPersistedTicket(partialUpdatedTicket));
    }

    @Test
    @Transactional
    void patchNonExistingTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ticketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ticketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ticket.setId(longCount.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ticketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicket() throws Exception {
        // Initialize the database
        insertedTicket = ticketRepository.saveAndFlush(ticket);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ticket
        restTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ticketRepository.count();
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

    protected Ticket getPersistedTicket(Ticket ticket) {
        return ticketRepository.findById(ticket.getId()).orElseThrow();
    }

    protected void assertPersistedTicketToMatchAllProperties(Ticket expectedTicket) {
        assertTicketAllPropertiesEquals(expectedTicket, getPersistedTicket(expectedTicket));
    }

    protected void assertPersistedTicketToMatchUpdatableProperties(Ticket expectedTicket) {
        assertTicketAllUpdatablePropertiesEquals(expectedTicket, getPersistedTicket(expectedTicket));
    }
}
