package com.crm.web.rest;

import com.crm.repository.MasterStaticGroupRepository;
import com.crm.service.MasterStaticGroupQueryService;
import com.crm.service.MasterStaticGroupService;
import com.crm.service.criteria.MasterStaticGroupCriteria;
import com.crm.service.dto.MasterStaticGroupDTO;
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
 * REST controller for managing {@link com.crm.domain.MasterStaticGroup}.
 */
@RestController
@RequestMapping("/api/master-static-groups")
public class MasterStaticGroupResource {

    private final Logger log = LoggerFactory.getLogger(MasterStaticGroupResource.class);

    private static final String ENTITY_NAME = "masterStaticGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterStaticGroupService masterStaticGroupService;

    private final MasterStaticGroupRepository masterStaticGroupRepository;

    private final MasterStaticGroupQueryService masterStaticGroupQueryService;

    public MasterStaticGroupResource(
        MasterStaticGroupService masterStaticGroupService,
        MasterStaticGroupRepository masterStaticGroupRepository,
        MasterStaticGroupQueryService masterStaticGroupQueryService
    ) {
        this.masterStaticGroupService = masterStaticGroupService;
        this.masterStaticGroupRepository = masterStaticGroupRepository;
        this.masterStaticGroupQueryService = masterStaticGroupQueryService;
    }

    /**
     * {@code POST  /master-static-groups} : Create a new masterStaticGroup.
     *
     * @param masterStaticGroupDTO the masterStaticGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterStaticGroupDTO, or with status {@code 400 (Bad Request)} if the masterStaticGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterStaticGroupDTO> createMasterStaticGroup(@Valid @RequestBody MasterStaticGroupDTO masterStaticGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save MasterStaticGroup : {}", masterStaticGroupDTO);
        if (masterStaticGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new masterStaticGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterStaticGroupDTO result = masterStaticGroupService.save(masterStaticGroupDTO);
        return ResponseEntity
            .created(new URI("/api/master-static-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /master-static-groups/:id} : Updates an existing masterStaticGroup.
     *
     * @param id the id of the masterStaticGroupDTO to save.
     * @param masterStaticGroupDTO the masterStaticGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterStaticGroupDTO,
     * or with status {@code 400 (Bad Request)} if the masterStaticGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterStaticGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterStaticGroupDTO> updateMasterStaticGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterStaticGroupDTO masterStaticGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MasterStaticGroup : {}, {}", id, masterStaticGroupDTO);
        if (masterStaticGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterStaticGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterStaticGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MasterStaticGroupDTO result = masterStaticGroupService.update(masterStaticGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterStaticGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /master-static-groups/:id} : Partial updates given fields of an existing masterStaticGroup, field will ignore if it is null
     *
     * @param id the id of the masterStaticGroupDTO to save.
     * @param masterStaticGroupDTO the masterStaticGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterStaticGroupDTO,
     * or with status {@code 400 (Bad Request)} if the masterStaticGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the masterStaticGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterStaticGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterStaticGroupDTO> partialUpdateMasterStaticGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterStaticGroupDTO masterStaticGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MasterStaticGroup partially : {}, {}", id, masterStaticGroupDTO);
        if (masterStaticGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterStaticGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterStaticGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterStaticGroupDTO> result = masterStaticGroupService.partialUpdate(masterStaticGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterStaticGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /master-static-groups} : get all the masterStaticGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterStaticGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MasterStaticGroupDTO>> getAllMasterStaticGroups(
        MasterStaticGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MasterStaticGroups by criteria: {}", criteria);

        Page<MasterStaticGroupDTO> page = masterStaticGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-static-groups/count} : count all the masterStaticGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMasterStaticGroups(MasterStaticGroupCriteria criteria) {
        log.debug("REST request to count MasterStaticGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterStaticGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-static-groups/:id} : get the "id" masterStaticGroup.
     *
     * @param id the id of the masterStaticGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterStaticGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterStaticGroupDTO> getMasterStaticGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get MasterStaticGroup : {}", id);
        Optional<MasterStaticGroupDTO> masterStaticGroupDTO = masterStaticGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterStaticGroupDTO);
    }

    /**
     * {@code DELETE  /master-static-groups/:id} : delete the "id" masterStaticGroup.
     *
     * @param id the id of the masterStaticGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterStaticGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete MasterStaticGroup : {}", id);
        masterStaticGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
