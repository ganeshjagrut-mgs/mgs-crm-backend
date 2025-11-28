package com.mgs.web.rest;

import com.mgs.repository.PermissionModuleRepository;
import com.mgs.service.PermissionModuleQueryService;
import com.mgs.service.PermissionModuleService;
import com.mgs.service.criteria.PermissionModuleCriteria;
import com.mgs.service.dto.PermissionModuleDTO;
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
 * REST controller for managing {@link com.mgs.domain.PermissionModule}.
 */
@RestController
@RequestMapping("/api/permission-modules")
public class PermissionModuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionModuleResource.class);

    private static final String ENTITY_NAME = "permissionModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionModuleService permissionModuleService;

    private final PermissionModuleRepository permissionModuleRepository;

    private final PermissionModuleQueryService permissionModuleQueryService;

    public PermissionModuleResource(
        PermissionModuleService permissionModuleService,
        PermissionModuleRepository permissionModuleRepository,
        PermissionModuleQueryService permissionModuleQueryService
    ) {
        this.permissionModuleService = permissionModuleService;
        this.permissionModuleRepository = permissionModuleRepository;
        this.permissionModuleQueryService = permissionModuleQueryService;
    }

    /**
     * {@code POST  /permission-modules} : Create a new permissionModule.
     *
     * @param permissionModuleDTO the permissionModuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionModuleDTO, or with status {@code 400 (Bad Request)} if the permissionModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PermissionModuleDTO> createPermissionModule(@Valid @RequestBody PermissionModuleDTO permissionModuleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PermissionModule : {}", permissionModuleDTO);
        if (permissionModuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissionModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        permissionModuleDTO = permissionModuleService.save(permissionModuleDTO);
        return ResponseEntity.created(new URI("/api/permission-modules/" + permissionModuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, permissionModuleDTO.getId().toString()))
            .body(permissionModuleDTO);
    }

    /**
     * {@code PUT  /permission-modules/:id} : Updates an existing permissionModule.
     *
     * @param id the id of the permissionModuleDTO to save.
     * @param permissionModuleDTO the permissionModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionModuleDTO,
     * or with status {@code 400 (Bad Request)} if the permissionModuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissionModuleDTO> updatePermissionModule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissionModuleDTO permissionModuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PermissionModule : {}, {}", id, permissionModuleDTO);
        if (permissionModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        permissionModuleDTO = permissionModuleService.update(permissionModuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permissionModuleDTO.getId().toString()))
            .body(permissionModuleDTO);
    }

    /**
     * {@code PATCH  /permission-modules/:id} : Partial updates given fields of an existing permissionModule, field will ignore if it is null
     *
     * @param id the id of the permissionModuleDTO to save.
     * @param permissionModuleDTO the permissionModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionModuleDTO,
     * or with status {@code 400 (Bad Request)} if the permissionModuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionModuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionModuleDTO> partialUpdatePermissionModule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissionModuleDTO permissionModuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PermissionModule partially : {}, {}", id, permissionModuleDTO);
        if (permissionModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionModuleDTO> result = permissionModuleService.partialUpdate(permissionModuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permissionModuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permission-modules} : get all the permissionModules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionModules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PermissionModuleDTO>> getAllPermissionModules(
        PermissionModuleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PermissionModules by criteria: {}", criteria);

        Page<PermissionModuleDTO> page = permissionModuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permission-modules/count} : count all the permissionModules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPermissionModules(PermissionModuleCriteria criteria) {
        LOG.debug("REST request to count PermissionModules by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionModuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permission-modules/:id} : get the "id" permissionModule.
     *
     * @param id the id of the permissionModuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionModuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissionModuleDTO> getPermissionModule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PermissionModule : {}", id);
        Optional<PermissionModuleDTO> permissionModuleDTO = permissionModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionModuleDTO);
    }

    /**
     * {@code DELETE  /permission-modules/:id} : delete the "id" permissionModule.
     *
     * @param id the id of the permissionModuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissionModule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PermissionModule : {}", id);
        permissionModuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
