package com.mgs.web.rest;

import com.mgs.repository.TenantEncryptionKeyRepository;
import com.mgs.service.TenantEncryptionKeyQueryService;
import com.mgs.service.TenantEncryptionKeyService;
import com.mgs.service.criteria.TenantEncryptionKeyCriteria;
import com.mgs.service.dto.TenantEncryptionKeyDTO;
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
 * REST controller for managing {@link com.mgs.domain.TenantEncryptionKey}.
 */
@RestController
@RequestMapping("/api/tenant-encryption-keys")
public class TenantEncryptionKeyResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantEncryptionKeyResource.class);

    private static final String ENTITY_NAME = "tenantEncryptionKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantEncryptionKeyService tenantEncryptionKeyService;

    private final TenantEncryptionKeyRepository tenantEncryptionKeyRepository;

    private final TenantEncryptionKeyQueryService tenantEncryptionKeyQueryService;

    public TenantEncryptionKeyResource(
        TenantEncryptionKeyService tenantEncryptionKeyService,
        TenantEncryptionKeyRepository tenantEncryptionKeyRepository,
        TenantEncryptionKeyQueryService tenantEncryptionKeyQueryService
    ) {
        this.tenantEncryptionKeyService = tenantEncryptionKeyService;
        this.tenantEncryptionKeyRepository = tenantEncryptionKeyRepository;
        this.tenantEncryptionKeyQueryService = tenantEncryptionKeyQueryService;
    }

    /**
     * {@code POST  /tenant-encryption-keys} : Create a new tenantEncryptionKey.
     *
     * @param tenantEncryptionKeyDTO the tenantEncryptionKeyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantEncryptionKeyDTO, or with status {@code 400 (Bad Request)} if the tenantEncryptionKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantEncryptionKeyDTO> createTenantEncryptionKey(
        @Valid @RequestBody TenantEncryptionKeyDTO tenantEncryptionKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TenantEncryptionKey : {}", tenantEncryptionKeyDTO);
        if (tenantEncryptionKeyDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantEncryptionKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tenantEncryptionKeyDTO = tenantEncryptionKeyService.save(tenantEncryptionKeyDTO);
        return ResponseEntity.created(new URI("/api/tenant-encryption-keys/" + tenantEncryptionKeyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tenantEncryptionKeyDTO.getId().toString()))
            .body(tenantEncryptionKeyDTO);
    }

    /**
     * {@code PUT  /tenant-encryption-keys/:id} : Updates an existing tenantEncryptionKey.
     *
     * @param id the id of the tenantEncryptionKeyDTO to save.
     * @param tenantEncryptionKeyDTO the tenantEncryptionKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantEncryptionKeyDTO,
     * or with status {@code 400 (Bad Request)} if the tenantEncryptionKeyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantEncryptionKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantEncryptionKeyDTO> updateTenantEncryptionKey(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantEncryptionKeyDTO tenantEncryptionKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TenantEncryptionKey : {}, {}", id, tenantEncryptionKeyDTO);
        if (tenantEncryptionKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantEncryptionKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantEncryptionKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tenantEncryptionKeyDTO = tenantEncryptionKeyService.update(tenantEncryptionKeyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantEncryptionKeyDTO.getId().toString()))
            .body(tenantEncryptionKeyDTO);
    }

    /**
     * {@code PATCH  /tenant-encryption-keys/:id} : Partial updates given fields of an existing tenantEncryptionKey, field will ignore if it is null
     *
     * @param id the id of the tenantEncryptionKeyDTO to save.
     * @param tenantEncryptionKeyDTO the tenantEncryptionKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantEncryptionKeyDTO,
     * or with status {@code 400 (Bad Request)} if the tenantEncryptionKeyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantEncryptionKeyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantEncryptionKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantEncryptionKeyDTO> partialUpdateTenantEncryptionKey(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantEncryptionKeyDTO tenantEncryptionKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TenantEncryptionKey partially : {}, {}", id, tenantEncryptionKeyDTO);
        if (tenantEncryptionKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantEncryptionKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantEncryptionKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantEncryptionKeyDTO> result = tenantEncryptionKeyService.partialUpdate(tenantEncryptionKeyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantEncryptionKeyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-encryption-keys} : get all the tenantEncryptionKeys.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantEncryptionKeys in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantEncryptionKeyDTO>> getAllTenantEncryptionKeys(
        TenantEncryptionKeyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantEncryptionKeys by criteria: {}", criteria);

        Page<TenantEncryptionKeyDTO> page = tenantEncryptionKeyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-encryption-keys/count} : count all the tenantEncryptionKeys.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantEncryptionKeys(TenantEncryptionKeyCriteria criteria) {
        LOG.debug("REST request to count TenantEncryptionKeys by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantEncryptionKeyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-encryption-keys/:id} : get the "id" tenantEncryptionKey.
     *
     * @param id the id of the tenantEncryptionKeyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantEncryptionKeyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantEncryptionKeyDTO> getTenantEncryptionKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantEncryptionKey : {}", id);
        Optional<TenantEncryptionKeyDTO> tenantEncryptionKeyDTO = tenantEncryptionKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantEncryptionKeyDTO);
    }

    /**
     * {@code DELETE  /tenant-encryption-keys/:id} : delete the "id" tenantEncryptionKey.
     *
     * @param id the id of the tenantEncryptionKeyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantEncryptionKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TenantEncryptionKey : {}", id);
        tenantEncryptionKeyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
