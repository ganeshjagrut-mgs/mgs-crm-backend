package com.mgs.web.rest;

import static com.mgs.domain.EventAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Event;
import com.mgs.domain.Tenant;
import com.mgs.repository.EventRepository;
import com.mgs.service.dto.EventDTO;
import com.mgs.service.mapper.EventMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final UUID DEFAULT_EVENT_KEY = UUID.randomUUID();
    private static final UUID UPDATED_EVENT_KEY = UUID.randomUUID();

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    private Event insertedEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .eventType(DEFAULT_EVENT_TYPE)
            .eventKey(DEFAULT_EVENT_KEY)
            .description(DEFAULT_DESCRIPTION)
            .eventDate(DEFAULT_EVENT_DATE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        event.setTenant(tenant);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event updatedEvent = new Event()
            .eventType(UPDATED_EVENT_TYPE)
            .eventKey(UPDATED_EVENT_KEY)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE);
        // Add required entity
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createUpdatedEntity();
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        updatedEvent.setTenant(tenant);
        return updatedEvent;
    }

    @BeforeEach
    void initTest() {
        event = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEvent != null) {
            eventRepository.delete(insertedEvent);
            insertedEvent = null;
        }
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        var returnedEventDTO = om.readValue(
            restEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventDTO.class
        );

        // Validate the Event in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvent = eventMapper.toEntity(returnedEventDTO);
        assertEventUpdatableFieldsEquals(returnedEvent, getPersistedEvent(returnedEvent));

        insertedEvent = returnedEvent;
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventType(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventKey(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventDate(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventKey").value(hasItem(DEFAULT_EVENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.eventKey").value(DEFAULT_EVENT_KEY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType equals to
        defaultEventFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType in
        defaultEventFiltering("eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE, "eventType.in=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType is not null
        defaultEventFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType contains
        defaultEventFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType does not contain
        defaultEventFiltering("eventType.doesNotContain=" + UPDATED_EVENT_TYPE, "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventKey equals to
        defaultEventFiltering("eventKey.equals=" + DEFAULT_EVENT_KEY, "eventKey.equals=" + UPDATED_EVENT_KEY);
    }

    @Test
    @Transactional
    void getAllEventsByEventKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventKey in
        defaultEventFiltering("eventKey.in=" + DEFAULT_EVENT_KEY + "," + UPDATED_EVENT_KEY, "eventKey.in=" + UPDATED_EVENT_KEY);
    }

    @Test
    @Transactional
    void getAllEventsByEventKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventKey is not null
        defaultEventFiltering("eventKey.specified=true", "eventKey.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where description equals to
        defaultEventFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where description in
        defaultEventFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where description is not null
        defaultEventFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where description contains
        defaultEventFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where description does not contain
        defaultEventFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate equals to
        defaultEventFiltering("eventDate.equals=" + DEFAULT_EVENT_DATE, "eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate in
        defaultEventFiltering("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE, "eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is not null
        defaultEventFiltering("eventDate.specified=true", "eventDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByTenantIsEqualToSomething() throws Exception {
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            eventRepository.saveAndFlush(event);
            tenant = TenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        event.setTenant(tenant);
        eventRepository.saveAndFlush(event);
        Long tenantId = tenant.getId();
        // Get all the eventList where tenant equals to tenantId
        defaultEventShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the eventList where tenant equals to (tenantId + 1)
        defaultEventShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            eventRepository.saveAndFlush(event);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        event.setCustomer(customer);
        eventRepository.saveAndFlush(event);
        Long customerId = customer.getId();
        // Get all the eventList where customer equals to customerId
        defaultEventShouldBeFound("customerId.equals=" + customerId);

        // Get all the eventList where customer equals to (customerId + 1)
        defaultEventShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            eventRepository.saveAndFlush(event);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        event.setContact(contact);
        eventRepository.saveAndFlush(event);
        Long contactId = contact.getId();
        // Get all the eventList where contact equals to contactId
        defaultEventShouldBeFound("contactId.equals=" + contactId);

        // Get all the eventList where contact equals to (contactId + 1)
        defaultEventShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    private void defaultEventFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventShouldBeFound(shouldBeFound);
        defaultEventShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventKey").value(hasItem(DEFAULT_EVENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .eventType(UPDATED_EVENT_TYPE)
            .eventKey(UPDATED_EVENT_KEY)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventToMatchAllProperties(updatedEvent);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent.eventKey(UPDATED_EVENT_KEY);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEvent, event), getPersistedEvent(event));
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventType(UPDATED_EVENT_TYPE)
            .eventKey(UPDATED_EVENT_KEY)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(partialUpdatedEvent, getPersistedEvent(partialUpdatedEvent));
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventRepository.count();
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

    protected Event getPersistedEvent(Event event) {
        return eventRepository.findById(event.getId()).orElseThrow();
    }

    protected void assertPersistedEventToMatchAllProperties(Event expectedEvent) {
        assertEventAllPropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }

    protected void assertPersistedEventToMatchUpdatableProperties(Event expectedEvent) {
        assertEventAllUpdatablePropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }
}
