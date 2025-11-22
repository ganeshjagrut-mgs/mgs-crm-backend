package com.crm.web.rest;

import com.crm.repository.EntityTypeRepository;
import com.crm.service.EntityTypeQueryService;
import com.crm.service.EntityTypeService;
import com.crm.service.criteria.EntityTypeCriteria;
import com.crm.service.dto.EntityTypeDTO;
import com.crm.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.crm.domain.EntityType}.
 */
@RestController
@RequestMapping("/api/entity-types")
public class EntityTypeResource {

    private final Logger log = LoggerFactory.getLogger(EntityTypeResource.class);

    private static final String ENTITY_NAME = "entityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntityTypeService entityTypeService;

    private final EntityTypeRepository entityTypeRepository;

    private final EntityTypeQueryService entityTypeQueryService;

    public EntityTypeResource(
        EntityTypeService entityTypeService,
        EntityTypeRepository entityTypeRepository,
        EntityTypeQueryService entityTypeQueryService
    ) {
        this.entityTypeService = entityTypeService;
        this.entityTypeRepository = entityTypeRepository;
        this.entityTypeQueryService = entityTypeQueryService;
    }

    /**
     * {@code POST  /entity-types} : Create a new entityType.
     *
     * @param entityTypeDTO the entityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityTypeDTO, or with status {@code 400 (Bad Request)} if the entityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EntityTypeDTO> createEntityType(@Valid @RequestBody EntityTypeDTO entityTypeDTO) throws URISyntaxException {
        log.debug("REST request to save EntityType : {}", entityTypeDTO);
        if (entityTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new entityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntityTypeDTO result = entityTypeService.save(entityTypeDTO);
        return ResponseEntity
            .created(new URI("/api/entity-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entity-types/:id} : Updates an existing entityType.
     *
     * @param id the id of the entityTypeDTO to save.
     * @param entityTypeDTO the entityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the entityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityTypeDTO> updateEntityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EntityTypeDTO entityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EntityType : {}, {}", id, entityTypeDTO);
        if (entityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EntityTypeDTO result = entityTypeService.update(entityTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entityTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entity-types/:id} : Partial updates given fields of an existing entityType, field will ignore if it is null
     *
     * @param id the id of the entityTypeDTO to save.
     * @param entityTypeDTO the entityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the entityTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entityTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntityTypeDTO> partialUpdateEntityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EntityTypeDTO entityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EntityType partially : {}, {}", id, entityTypeDTO);
        if (entityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntityTypeDTO> result = entityTypeService.partialUpdate(entityTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entityTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entity-types} : get all the entityTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entityTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EntityTypeDTO>> getAllEntityTypes(
        EntityTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EntityTypes by criteria: {}", criteria);

        Page<EntityTypeDTO> page = entityTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entity-types/count} : count all the entityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEntityTypes(EntityTypeCriteria criteria) {
        log.debug("REST request to count EntityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(entityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entity-types/:id} : get the "id" entityType.
     *
     * @param id the id of the entityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityTypeDTO> getEntityType(@PathVariable("id") Long id) {
        log.debug("REST request to get EntityType : {}", id);
        Optional<EntityTypeDTO> entityTypeDTO = entityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entityTypeDTO);
    }

    /**
     * {@code DELETE  /entity-types/:id} : delete the "id" entityType.
     *
     * @param id the id of the entityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntityType(@PathVariable("id") Long id) {
        log.debug("REST request to delete EntityType : {}", id);
        entityTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
