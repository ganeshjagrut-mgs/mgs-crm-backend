package com.mgs.web.rest;

import static com.mgs.domain.TaskCommentAsserts.*;
import static com.mgs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.IntegrationTest;
import com.mgs.domain.Task;
import com.mgs.domain.TaskComment;
import com.mgs.domain.User;
import com.mgs.repository.TaskCommentRepository;
import com.mgs.service.dto.TaskCommentDTO;
import com.mgs.service.mapper.TaskCommentMapper;
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
 * Integration tests for the {@link TaskCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskCommentResourceIT {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/task-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskCommentRepository taskCommentRepository;

    @Autowired
    private TaskCommentMapper taskCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskCommentMockMvc;

    private TaskComment taskComment;

    private TaskComment insertedTaskComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskComment createEntity(EntityManager em) {
        TaskComment taskComment = new TaskComment().comment(DEFAULT_COMMENT);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        taskComment.setTask(task);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        taskComment.setCreatedByUser(user);
        return taskComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskComment createUpdatedEntity(EntityManager em) {
        TaskComment updatedTaskComment = new TaskComment().comment(UPDATED_COMMENT);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createUpdatedEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        updatedTaskComment.setTask(task);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        updatedTaskComment.setCreatedByUser(user);
        return updatedTaskComment;
    }

    @BeforeEach
    void initTest() {
        taskComment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTaskComment != null) {
            taskCommentRepository.delete(insertedTaskComment);
            insertedTaskComment = null;
        }
    }

    @Test
    @Transactional
    void createTaskComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);
        var returnedTaskCommentDTO = om.readValue(
            restTaskCommentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskCommentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskCommentDTO.class
        );

        // Validate the TaskComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTaskComment = taskCommentMapper.toEntity(returnedTaskCommentDTO);
        assertTaskCommentUpdatableFieldsEquals(returnedTaskComment, getPersistedTaskComment(returnedTaskComment));

        insertedTaskComment = returnedTaskComment;
    }

    @Test
    @Transactional
    void createTaskCommentWithExistingId() throws Exception {
        // Create the TaskComment with an existing ID
        taskComment.setId(1L);
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taskComment.setComment(null);

        // Create the TaskComment, which fails.
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        restTaskCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaskComments() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getTaskComment() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get the taskComment
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, taskComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskComment.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getTaskCommentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        Long id = taskComment.getId();

        defaultTaskCommentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaskCommentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaskCommentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList where comment equals to
        defaultTaskCommentFiltering("comment.equals=" + DEFAULT_COMMENT, "comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList where comment in
        defaultTaskCommentFiltering("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT, "comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList where comment is not null
        defaultTaskCommentFiltering("comment.specified=true", "comment.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList where comment contains
        defaultTaskCommentFiltering("comment.contains=" + DEFAULT_COMMENT, "comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskCommentList where comment does not contain
        defaultTaskCommentFiltering("comment.doesNotContain=" + UPDATED_COMMENT, "comment.doesNotContain=" + DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void getAllTaskCommentsByTaskIsEqualToSomething() throws Exception {
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            taskCommentRepository.saveAndFlush(taskComment);
            task = TaskResourceIT.createEntity(em);
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        em.persist(task);
        em.flush();
        taskComment.setTask(task);
        taskCommentRepository.saveAndFlush(taskComment);
        Long taskId = task.getId();
        // Get all the taskCommentList where task equals to taskId
        defaultTaskCommentShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskCommentList where task equals to (taskId + 1)
        defaultTaskCommentShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllTaskCommentsByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            taskCommentRepository.saveAndFlush(taskComment);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        taskComment.setCreatedByUser(createdByUser);
        taskCommentRepository.saveAndFlush(taskComment);
        Long createdByUserId = createdByUser.getId();
        // Get all the taskCommentList where createdByUser equals to createdByUserId
        defaultTaskCommentShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the taskCommentList where createdByUser equals to (createdByUserId + 1)
        defaultTaskCommentShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    private void defaultTaskCommentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaskCommentShouldBeFound(shouldBeFound);
        defaultTaskCommentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskCommentShouldBeFound(String filter) throws Exception {
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskCommentShouldNotBeFound(String filter) throws Exception {
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaskComment() throws Exception {
        // Get the taskComment
        restTaskCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaskComment() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskComment
        TaskComment updatedTaskComment = taskCommentRepository.findById(taskComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaskComment are not directly saved in db
        em.detach(updatedTaskComment);
        updatedTaskComment.comment(UPDATED_COMMENT);
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(updatedTaskComment);

        restTaskCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskCommentToMatchAllProperties(updatedTaskComment);
    }

    @Test
    @Transactional
    void putNonExistingTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskCommentWithPatch() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskComment using partial update
        TaskComment partialUpdatedTaskComment = new TaskComment();
        partialUpdatedTaskComment.setId(taskComment.getId());

        partialUpdatedTaskComment.comment(UPDATED_COMMENT);

        restTaskCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskComment))
            )
            .andExpect(status().isOk());

        // Validate the TaskComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskCommentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaskComment, taskComment),
            getPersistedTaskComment(taskComment)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaskCommentWithPatch() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskComment using partial update
        TaskComment partialUpdatedTaskComment = new TaskComment();
        partialUpdatedTaskComment.setId(taskComment.getId());

        partialUpdatedTaskComment.comment(UPDATED_COMMENT);

        restTaskCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskComment))
            )
            .andExpect(status().isOk());

        // Validate the TaskComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskCommentUpdatableFieldsEquals(partialUpdatedTaskComment, getPersistedTaskComment(partialUpdatedTaskComment));
    }

    @Test
    @Transactional
    void patchNonExistingTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskComment.setId(longCount.incrementAndGet());

        // Create the TaskComment
        TaskCommentDTO taskCommentDTO = taskCommentMapper.toDto(taskComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCommentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskComment() throws Exception {
        // Initialize the database
        insertedTaskComment = taskCommentRepository.saveAndFlush(taskComment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taskComment
        restTaskCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskCommentRepository.count();
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

    protected TaskComment getPersistedTaskComment(TaskComment taskComment) {
        return taskCommentRepository.findById(taskComment.getId()).orElseThrow();
    }

    protected void assertPersistedTaskCommentToMatchAllProperties(TaskComment expectedTaskComment) {
        assertTaskCommentAllPropertiesEquals(expectedTaskComment, getPersistedTaskComment(expectedTaskComment));
    }

    protected void assertPersistedTaskCommentToMatchUpdatableProperties(TaskComment expectedTaskComment) {
        assertTaskCommentAllUpdatablePropertiesEquals(expectedTaskComment, getPersistedTaskComment(expectedTaskComment));
    }
}
