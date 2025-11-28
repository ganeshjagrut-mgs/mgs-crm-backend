package com.mgs.web.rest;

import static com.mgs.domain.EventNotificationAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Event;
import com.mgs.domain.EventNotification;
import com.mgs.domain.User;
import com.mgs.repository.EventNotificationRepository;
import com.mgs.service.dto.EventNotificationDTO;
import com.mgs.service.mapper.EventNotificationMapper;
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
 * Integration tests for the {@link EventNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventNotificationResourceIT {

    private static final String DEFAULT_NOTIFICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/event-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventNotificationRepository eventNotificationRepository;

    @Autowired
    private EventNotificationMapper eventNotificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventNotificationMockMvc;

    private EventNotification eventNotification;

    private EventNotification insertedEventNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventNotification createEntity(EntityManager em) {
        EventNotification eventNotification = new EventNotification().notificationType(DEFAULT_NOTIFICATION_TYPE).message(DEFAULT_MESSAGE);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventNotification.setEvent(event);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eventNotification.setUser(user);
        return eventNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventNotification createUpdatedEntity(EntityManager em) {
        EventNotification updatedEventNotification = new EventNotification()
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .message(UPDATED_MESSAGE);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        updatedEventNotification.setEvent(event);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedEventNotification.setUser(user);
        return updatedEventNotification;
    }

    @BeforeEach
    void initTest() {
        eventNotification = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEventNotification != null) {
            eventNotificationRepository.delete(insertedEventNotification);
            insertedEventNotification = null;
        }
    }

    @Test
    @Transactional
    void createEventNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);
        var returnedEventNotificationDTO = om.readValue(
            restEventNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventNotificationDTO.class
        );

        // Validate the EventNotification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventNotification = eventNotificationMapper.toEntity(returnedEventNotificationDTO);
        assertEventNotificationUpdatableFieldsEquals(returnedEventNotification, getPersistedEventNotification(returnedEventNotification));

        insertedEventNotification = returnedEventNotification;
    }

    @Test
    @Transactional
    void createEventNotificationWithExistingId() throws Exception {
        // Create the EventNotification with an existing ID
        eventNotification.setId(1L);
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNotificationTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventNotification.setNotificationType(null);

        // Create the EventNotification, which fails.
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventNotification.setMessage(null);

        // Create the EventNotification, which fails.
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventNotifications() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @Test
    @Transactional
    void getEventNotification() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get the eventNotification
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, eventNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventNotification.getId().intValue()))
            .andExpect(jsonPath("$.notificationType").value(DEFAULT_NOTIFICATION_TYPE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getEventNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        Long id = eventNotification.getId();

        defaultEventNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where notificationType equals to
        defaultEventNotificationFiltering(
            "notificationType.equals=" + DEFAULT_NOTIFICATION_TYPE,
            "notificationType.equals=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where notificationType in
        defaultEventNotificationFiltering(
            "notificationType.in=" + DEFAULT_NOTIFICATION_TYPE + "," + UPDATED_NOTIFICATION_TYPE,
            "notificationType.in=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where notificationType is not null
        defaultEventNotificationFiltering("notificationType.specified=true", "notificationType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where notificationType contains
        defaultEventNotificationFiltering(
            "notificationType.contains=" + DEFAULT_NOTIFICATION_TYPE,
            "notificationType.contains=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where notificationType does not contain
        defaultEventNotificationFiltering(
            "notificationType.doesNotContain=" + UPDATED_NOTIFICATION_TYPE,
            "notificationType.doesNotContain=" + DEFAULT_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEventNotificationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where message equals to
        defaultEventNotificationFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where message in
        defaultEventNotificationFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where message is not null
        defaultEventNotificationFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllEventNotificationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where message contains
        defaultEventNotificationFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where message does not contain
        defaultEventNotificationFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            eventNotificationRepository.saveAndFlush(eventNotification);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventNotification.setEvent(event);
        eventNotificationRepository.saveAndFlush(eventNotification);
        Long eventId = event.getId();
        // Get all the eventNotificationList where event equals to eventId
        defaultEventNotificationShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventNotificationList where event equals to (eventId + 1)
        defaultEventNotificationShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllEventNotificationsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            eventNotificationRepository.saveAndFlush(eventNotification);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        eventNotification.setUser(user);
        eventNotificationRepository.saveAndFlush(eventNotification);
        Long userId = user.getId();
        // Get all the eventNotificationList where user equals to userId
        defaultEventNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the eventNotificationList where user equals to (userId + 1)
        defaultEventNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultEventNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventNotificationShouldBeFound(shouldBeFound);
        defaultEventNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventNotificationShouldBeFound(String filter) throws Exception {
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));

        // Check, that the count call also returns 1
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventNotificationShouldNotBeFound(String filter) throws Exception {
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventNotification() throws Exception {
        // Get the eventNotification
        restEventNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventNotification() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification
        EventNotification updatedEventNotification = eventNotificationRepository.findById(eventNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventNotification are not directly saved in db
        em.detach(updatedEventNotification);
        updatedEventNotification.notificationType(UPDATED_NOTIFICATION_TYPE).message(UPDATED_MESSAGE);
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(updatedEventNotification);

        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventNotificationToMatchAllProperties(updatedEventNotification);
    }

    @Test
    @Transactional
    void putNonExistingEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification using partial update
        EventNotification partialUpdatedEventNotification = new EventNotification();
        partialUpdatedEventNotification.setId(eventNotification.getId());

        partialUpdatedEventNotification.notificationType(UPDATED_NOTIFICATION_TYPE).message(UPDATED_MESSAGE);

        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventNotification))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventNotification, eventNotification),
            getPersistedEventNotification(eventNotification)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification using partial update
        EventNotification partialUpdatedEventNotification = new EventNotification();
        partialUpdatedEventNotification.setId(eventNotification.getId());

        partialUpdatedEventNotification.notificationType(UPDATED_NOTIFICATION_TYPE).message(UPDATED_MESSAGE);

        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventNotification))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventNotificationUpdatableFieldsEquals(
            partialUpdatedEventNotification,
            getPersistedEventNotification(partialUpdatedEventNotification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(longCount.incrementAndGet());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventNotification() throws Exception {
        // Initialize the database
        insertedEventNotification = eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventNotification
        restEventNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventNotificationRepository.count();
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

    protected EventNotification getPersistedEventNotification(EventNotification eventNotification) {
        return eventNotificationRepository.findById(eventNotification.getId()).orElseThrow();
    }

    protected void assertPersistedEventNotificationToMatchAllProperties(EventNotification expectedEventNotification) {
        assertEventNotificationAllPropertiesEquals(expectedEventNotification, getPersistedEventNotification(expectedEventNotification));
    }

    protected void assertPersistedEventNotificationToMatchUpdatableProperties(EventNotification expectedEventNotification) {
        assertEventNotificationAllUpdatablePropertiesEquals(
            expectedEventNotification,
            getPersistedEventNotification(expectedEventNotification)
        );
    }
}
