package com.crm.web.rest;

import com.crm.repository.EncryptionRepository;
import com.crm.service.EncryptionQueryService;
import com.crm.service.EncryptionService;
import com.crm.service.criteria.EncryptionCriteria;
import com.crm.service.dto.EncryptionDTO;
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
 * REST controller for managing {@link com.crm.domain.Encryption}.
 */
@RestController
@RequestMapping("/api/encryptions")
public class EncryptionResource {

    private final Logger log = LoggerFactory.getLogger(EncryptionResource.class);

    private static final String ENTITY_NAME = "encryption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EncryptionService encryptionService;

    private final EncryptionRepository encryptionRepository;

    private final EncryptionQueryService encryptionQueryService;

    public EncryptionResource(
        EncryptionService encryptionService,
        EncryptionRepository encryptionRepository,
        EncryptionQueryService encryptionQueryService
    ) {
        this.encryptionService = encryptionService;
        this.encryptionRepository = encryptionRepository;
        this.encryptionQueryService = encryptionQueryService;
    }

    /**
     * {@code POST  /encryptions} : Create a new encryption.
     *
     * @param encryptionDTO the encryptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new encryptionDTO, or with status {@code 400 (Bad Request)} if the encryption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EncryptionDTO> createEncryption(@Valid @RequestBody EncryptionDTO encryptionDTO) throws URISyntaxException {
        log.debug("REST request to save Encryption : {}", encryptionDTO);
        if (encryptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new encryption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EncryptionDTO result = encryptionService.save(encryptionDTO);
        return ResponseEntity
            .created(new URI("/api/encryptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /encryptions/:id} : Updates an existing encryption.
     *
     * @param id the id of the encryptionDTO to save.
     * @param encryptionDTO the encryptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encryptionDTO,
     * or with status {@code 400 (Bad Request)} if the encryptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the encryptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EncryptionDTO> updateEncryption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EncryptionDTO encryptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Encryption : {}, {}", id, encryptionDTO);
        if (encryptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encryptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encryptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EncryptionDTO result = encryptionService.update(encryptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encryptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /encryptions/:id} : Partial updates given fields of an existing encryption, field will ignore if it is null
     *
     * @param id the id of the encryptionDTO to save.
     * @param encryptionDTO the encryptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encryptionDTO,
     * or with status {@code 400 (Bad Request)} if the encryptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the encryptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the encryptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EncryptionDTO> partialUpdateEncryption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EncryptionDTO encryptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Encryption partially : {}, {}", id, encryptionDTO);
        if (encryptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encryptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encryptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EncryptionDTO> result = encryptionService.partialUpdate(encryptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encryptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /encryptions} : get all the encryptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of encryptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EncryptionDTO>> getAllEncryptions(
        EncryptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Encryptions by criteria: {}", criteria);

        Page<EncryptionDTO> page = encryptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /encryptions/count} : count all the encryptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEncryptions(EncryptionCriteria criteria) {
        log.debug("REST request to count Encryptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(encryptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /encryptions/:id} : get the "id" encryption.
     *
     * @param id the id of the encryptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the encryptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EncryptionDTO> getEncryption(@PathVariable("id") Long id) {
        log.debug("REST request to get Encryption : {}", id);
        Optional<EncryptionDTO> encryptionDTO = encryptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(encryptionDTO);
    }

    /**
     * {@code DELETE  /encryptions/:id} : delete the "id" encryption.
     *
     * @param id the id of the encryptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncryption(@PathVariable("id") Long id) {
        log.debug("REST request to delete Encryption : {}", id);
        encryptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
