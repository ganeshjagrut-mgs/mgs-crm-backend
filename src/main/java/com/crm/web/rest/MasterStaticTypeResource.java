package com.crm.web.rest;

import com.crm.repository.MasterStaticTypeRepository;
import com.crm.service.MasterStaticTypeQueryService;
import com.crm.service.MasterStaticTypeService;
import com.crm.service.criteria.MasterStaticTypeCriteria;
import com.crm.service.dto.MasterStaticTypeDTO;
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
 * REST controller for managing {@link com.crm.domain.MasterStaticType}.
 */
@RestController
@RequestMapping("/api/master-static-types")
public class MasterStaticTypeResource {

    private final Logger log = LoggerFactory.getLogger(MasterStaticTypeResource.class);

    private static final String ENTITY_NAME = "masterStaticType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterStaticTypeService masterStaticTypeService;

    private final MasterStaticTypeRepository masterStaticTypeRepository;

    private final MasterStaticTypeQueryService masterStaticTypeQueryService;

    public MasterStaticTypeResource(
        MasterStaticTypeService masterStaticTypeService,
        MasterStaticTypeRepository masterStaticTypeRepository,
        MasterStaticTypeQueryService masterStaticTypeQueryService
    ) {
        this.masterStaticTypeService = masterStaticTypeService;
        this.masterStaticTypeRepository = masterStaticTypeRepository;
        this.masterStaticTypeQueryService = masterStaticTypeQueryService;
    }

    /**
     * {@code POST  /master-static-types} : Create a new masterStaticType.
     *
     * @param masterStaticTypeDTO the masterStaticTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterStaticTypeDTO, or with status {@code 400 (Bad Request)} if the masterStaticType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterStaticTypeDTO> createMasterStaticType(@Valid @RequestBody MasterStaticTypeDTO masterStaticTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MasterStaticType : {}", masterStaticTypeDTO);
        if (masterStaticTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new masterStaticType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterStaticTypeDTO result = masterStaticTypeService.save(masterStaticTypeDTO);
        return ResponseEntity
            .created(new URI("/api/master-static-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /master-static-types/:id} : Updates an existing masterStaticType.
     *
     * @param id the id of the masterStaticTypeDTO to save.
     * @param masterStaticTypeDTO the masterStaticTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterStaticTypeDTO,
     * or with status {@code 400 (Bad Request)} if the masterStaticTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterStaticTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterStaticTypeDTO> updateMasterStaticType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterStaticTypeDTO masterStaticTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MasterStaticType : {}, {}", id, masterStaticTypeDTO);
        if (masterStaticTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterStaticTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterStaticTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MasterStaticTypeDTO result = masterStaticTypeService.update(masterStaticTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterStaticTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /master-static-types/:id} : Partial updates given fields of an existing masterStaticType, field will ignore if it is null
     *
     * @param id the id of the masterStaticTypeDTO to save.
     * @param masterStaticTypeDTO the masterStaticTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterStaticTypeDTO,
     * or with status {@code 400 (Bad Request)} if the masterStaticTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the masterStaticTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterStaticTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterStaticTypeDTO> partialUpdateMasterStaticType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterStaticTypeDTO masterStaticTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MasterStaticType partially : {}, {}", id, masterStaticTypeDTO);
        if (masterStaticTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterStaticTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterStaticTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterStaticTypeDTO> result = masterStaticTypeService.partialUpdate(masterStaticTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterStaticTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /master-static-types} : get all the masterStaticTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterStaticTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MasterStaticTypeDTO>> getAllMasterStaticTypes(
        MasterStaticTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MasterStaticTypes by criteria: {}", criteria);

        Page<MasterStaticTypeDTO> page = masterStaticTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-static-types/count} : count all the masterStaticTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMasterStaticTypes(MasterStaticTypeCriteria criteria) {
        log.debug("REST request to count MasterStaticTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterStaticTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-static-types/:id} : get the "id" masterStaticType.
     *
     * @param id the id of the masterStaticTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterStaticTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterStaticTypeDTO> getMasterStaticType(@PathVariable("id") Long id) {
        log.debug("REST request to get MasterStaticType : {}", id);
        Optional<MasterStaticTypeDTO> masterStaticTypeDTO = masterStaticTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterStaticTypeDTO);
    }

    /**
     * {@code DELETE  /master-static-types/:id} : delete the "id" masterStaticType.
     *
     * @param id the id of the masterStaticTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterStaticType(@PathVariable("id") Long id) {
        log.debug("REST request to delete MasterStaticType : {}", id);
        masterStaticTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
