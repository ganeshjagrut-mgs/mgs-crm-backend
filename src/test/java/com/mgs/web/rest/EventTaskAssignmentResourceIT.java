package com.mgs.web.rest;

import static com.mgs.domain.EventTaskAssignmentAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Event;
import com.mgs.domain.EventTaskAssignment;
import com.mgs.domain.Task;
import com.mgs.domain.User;
import com.mgs.repository.EventTaskAssignmentRepository;
import com.mgs.service.dto.EventTaskAssignmentDTO;
import com.mgs.service.mapper.EventTaskAssignmentMapper;
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
 * Integration tests for the {@link EventTaskAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTaskAssignmentResourceIT {

    private static final String ENTITY_API_URL = "/api/event-task-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventTaskAssignmentRepository eventTaskAssignmentRepository;

    @Autowired
    private EventTaskAssignmentMapper eventTaskAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTaskAssignmentMockMvc;

    private EventTaskAssignment eventTaskAssignment;

    private EventTaskAssignment insertedEventTaskAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTaskAssignment createEntity(EntityManager em) {
        EventTaskAssignment eventTaskAssignment = new EventTaskAssignment();
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventTaskAssignment.setEvent(event);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        eventTaskAssignment.setTask(task);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eventTaskAssignment.setAssignedTo(user);
        return eventTaskAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTaskAssignment createUpdatedEntity(EntityManager em) {
        EventTaskAssignment updatedEventTaskAssignment = new EventTaskAssignment();
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        updatedEventTaskAssignment.setEvent(event);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createUpdatedEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        updatedEventTaskAssignment.setTask(task);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedEventTaskAssignment.setAssignedTo(user);
        return updatedEventTaskAssignment;
    }

    @BeforeEach
    void initTest() {
        eventTaskAssignment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEventTaskAssignment != null) {
            eventTaskAssignmentRepository.delete(insertedEventTaskAssignment);
            insertedEventTaskAssignment = null;
        }
    }

    @Test
    @Transactional
    void createEventTaskAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);
        var returnedEventTaskAssignmentDTO = om.readValue(
            restEventTaskAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventTaskAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventTaskAssignmentDTO.class
        );

        // Validate the EventTaskAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventTaskAssignment = eventTaskAssignmentMapper.toEntity(returnedEventTaskAssignmentDTO);
        assertEventTaskAssignmentUpdatableFieldsEquals(
            returnedEventTaskAssignment,
            getPersistedEventTaskAssignment(returnedEventTaskAssignment)
        );

        insertedEventTaskAssignment = returnedEventTaskAssignment;
    }

    @Test
    @Transactional
    void createEventTaskAssignmentWithExistingId() throws Exception {
        // Create the EventTaskAssignment with an existing ID
        eventTaskAssignment.setId(1L);
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTaskAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventTaskAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventTaskAssignments() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        // Get all the eventTaskAssignmentList
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTaskAssignment.getId().intValue())));
    }

    @Test
    @Transactional
    void getEventTaskAssignment() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        // Get the eventTaskAssignment
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, eventTaskAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventTaskAssignment.getId().intValue()));
    }

    @Test
    @Transactional
    void getEventTaskAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        Long id = eventTaskAssignment.getId();

        defaultEventTaskAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventTaskAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventTaskAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTaskAssignmentsByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventTaskAssignment.setEvent(event);
        eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
        Long eventId = event.getId();
        // Get all the eventTaskAssignmentList where event equals to eventId
        defaultEventTaskAssignmentShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventTaskAssignmentList where event equals to (eventId + 1)
        defaultEventTaskAssignmentShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllEventTaskAssignmentsByTaskIsEqualToSomething() throws Exception {
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
            task = TaskResourceIT.createEntity(em);
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        em.persist(task);
        em.flush();
        eventTaskAssignment.setTask(task);
        eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
        Long taskId = task.getId();
        // Get all the eventTaskAssignmentList where task equals to taskId
        defaultEventTaskAssignmentShouldBeFound("taskId.equals=" + taskId);

        // Get all the eventTaskAssignmentList where task equals to (taskId + 1)
        defaultEventTaskAssignmentShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllEventTaskAssignmentsByAssignedToIsEqualToSomething() throws Exception {
        User assignedTo;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
            assignedTo = UserResourceIT.createEntity(em);
        } else {
            assignedTo = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedTo);
        em.flush();
        eventTaskAssignment.setAssignedTo(assignedTo);
        eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);
        Long assignedToId = assignedTo.getId();
        // Get all the eventTaskAssignmentList where assignedTo equals to assignedToId
        defaultEventTaskAssignmentShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the eventTaskAssignmentList where assignedTo equals to (assignedToId + 1)
        defaultEventTaskAssignmentShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    private void defaultEventTaskAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventTaskAssignmentShouldBeFound(shouldBeFound);
        defaultEventTaskAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTaskAssignmentShouldBeFound(String filter) throws Exception {
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTaskAssignment.getId().intValue())));

        // Check, that the count call also returns 1
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTaskAssignmentShouldNotBeFound(String filter) throws Exception {
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTaskAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventTaskAssignment() throws Exception {
        // Get the eventTaskAssignment
        restEventTaskAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventTaskAssignment() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventTaskAssignment
        EventTaskAssignment updatedEventTaskAssignment = eventTaskAssignmentRepository.findById(eventTaskAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventTaskAssignment are not directly saved in db
        em.detach(updatedEventTaskAssignment);
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(updatedEventTaskAssignment);

        restEventTaskAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTaskAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventTaskAssignmentToMatchAllProperties(updatedEventTaskAssignment);
    }

    @Test
    @Transactional
    void putNonExistingEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTaskAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventTaskAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTaskAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventTaskAssignment using partial update
        EventTaskAssignment partialUpdatedEventTaskAssignment = new EventTaskAssignment();
        partialUpdatedEventTaskAssignment.setId(eventTaskAssignment.getId());

        restEventTaskAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTaskAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventTaskAssignment))
            )
            .andExpect(status().isOk());

        // Validate the EventTaskAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventTaskAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventTaskAssignment, eventTaskAssignment),
            getPersistedEventTaskAssignment(eventTaskAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventTaskAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventTaskAssignment using partial update
        EventTaskAssignment partialUpdatedEventTaskAssignment = new EventTaskAssignment();
        partialUpdatedEventTaskAssignment.setId(eventTaskAssignment.getId());

        restEventTaskAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTaskAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventTaskAssignment))
            )
            .andExpect(status().isOk());

        // Validate the EventTaskAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventTaskAssignmentUpdatableFieldsEquals(
            partialUpdatedEventTaskAssignment,
            getPersistedEventTaskAssignment(partialUpdatedEventTaskAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTaskAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventTaskAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventTaskAssignment.setId(longCount.incrementAndGet());

        // Create the EventTaskAssignment
        EventTaskAssignmentDTO eventTaskAssignmentDTO = eventTaskAssignmentMapper.toDto(eventTaskAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTaskAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventTaskAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTaskAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventTaskAssignment() throws Exception {
        // Initialize the database
        insertedEventTaskAssignment = eventTaskAssignmentRepository.saveAndFlush(eventTaskAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventTaskAssignment
        restEventTaskAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventTaskAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventTaskAssignmentRepository.count();
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

    protected EventTaskAssignment getPersistedEventTaskAssignment(EventTaskAssignment eventTaskAssignment) {
        return eventTaskAssignmentRepository.findById(eventTaskAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedEventTaskAssignmentToMatchAllProperties(EventTaskAssignment expectedEventTaskAssignment) {
        assertEventTaskAssignmentAllPropertiesEquals(
            expectedEventTaskAssignment,
            getPersistedEventTaskAssignment(expectedEventTaskAssignment)
        );
    }

    protected void assertPersistedEventTaskAssignmentToMatchUpdatableProperties(EventTaskAssignment expectedEventTaskAssignment) {
        assertEventTaskAssignmentAllUpdatablePropertiesEquals(
            expectedEventTaskAssignment,
            getPersistedEventTaskAssignment(expectedEventTaskAssignment)
        );
    }
}
