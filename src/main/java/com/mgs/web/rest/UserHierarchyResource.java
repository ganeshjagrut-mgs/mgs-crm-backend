package com.mgs.web.rest;

import com.mgs.repository.UserHierarchyRepository;
import com.mgs.service.UserHierarchyQueryService;
import com.mgs.service.UserHierarchyService;
import com.mgs.service.criteria.UserHierarchyCriteria;
import com.mgs.service.dto.UserHierarchyDTO;
import com.mgs.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mgs.domain.UserHierarchy}.
 */
@RestController
@RequestMapping("/api/user-hierarchies")
public class UserHierarchyResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserHierarchyResource.class);

    private static final String ENTITY_NAME = "userHierarchy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserHierarchyService userHierarchyService;

    private final UserHierarchyRepository userHierarchyRepository;

    private final UserHierarchyQueryService userHierarchyQueryService;

    public UserHierarchyResource(
        UserHierarchyService userHierarchyService,
        UserHierarchyRepository userHierarchyRepository,
        UserHierarchyQueryService userHierarchyQueryService
    ) {
        this.userHierarchyService = userHierarchyService;
        this.userHierarchyRepository = userHierarchyRepository;
        this.userHierarchyQueryService = userHierarchyQueryService;
    }

    /**
     * {@code POST  /user-hierarchies} : Create a new userHierarchy.
     *
     * @param userHierarchyDTO the userHierarchyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userHierarchyDTO, or with status {@code 400 (Bad Request)} if the userHierarchy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserHierarchyDTO> createUserHierarchy(@Valid @RequestBody UserHierarchyDTO userHierarchyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserHierarchy : {}", userHierarchyDTO);
        if (userHierarchyDTO.getId() != null) {
            throw new BadRequestAlertException("A new userHierarchy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userHierarchyDTO = userHierarchyService.save(userHierarchyDTO);
        return ResponseEntity.created(new URI("/api/user-hierarchies/" + userHierarchyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userHierarchyDTO.getId().toString()))
            .body(userHierarchyDTO);
    }

    /**
     * {@code PUT  /user-hierarchies/:id} : Updates an existing userHierarchy.
     *
     * @param id the id of the userHierarchyDTO to save.
     * @param userHierarchyDTO the userHierarchyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHierarchyDTO,
     * or with status {@code 400 (Bad Request)} if the userHierarchyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userHierarchyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserHierarchyDTO> updateUserHierarchy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserHierarchyDTO userHierarchyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserHierarchy : {}, {}", id, userHierarchyDTO);
        if (userHierarchyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHierarchyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHierarchyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userHierarchyDTO = userHierarchyService.update(userHierarchyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userHierarchyDTO.getId().toString()))
            .body(userHierarchyDTO);
    }

    /**
     * {@code PATCH  /user-hierarchies/:id} : Partial updates given fields of an existing userHierarchy, field will ignore if it is null
     *
     * @param id the id of the userHierarchyDTO to save.
     * @param userHierarchyDTO the userHierarchyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHierarchyDTO,
     * or with status {@code 400 (Bad Request)} if the userHierarchyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userHierarchyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userHierarchyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserHierarchyDTO> partialUpdateUserHierarchy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserHierarchyDTO userHierarchyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserHierarchy partially : {}, {}", id, userHierarchyDTO);
        if (userHierarchyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHierarchyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHierarchyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserHierarchyDTO> result = userHierarchyService.partialUpdate(userHierarchyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userHierarchyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-hierarchies} : get all the userHierarchies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userHierarchies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserHierarchyDTO>> getAllUserHierarchies(
        UserHierarchyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserHierarchies by criteria: {}", criteria);

        Page<UserHierarchyDTO> page = userHierarchyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-hierarchies/count} : count all the userHierarchies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserHierarchies(UserHierarchyCriteria criteria) {
        LOG.debug("REST request to count UserHierarchies by criteria: {}", criteria);
        return ResponseEntity.ok().body(userHierarchyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-hierarchies/:id} : get the "id" userHierarchy.
     *
     * @param id the id of the userHierarchyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userHierarchyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserHierarchyDTO> getUserHierarchy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserHierarchy : {}", id);
        Optional<UserHierarchyDTO> userHierarchyDTO = userHierarchyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userHierarchyDTO);
    }

    /**
     * {@code DELETE  /user-hierarchies/:id} : delete the "id" userHierarchy.
     *
     * @param id the id of the userHierarchyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserHierarchy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserHierarchy : {}", id);
        userHierarchyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
