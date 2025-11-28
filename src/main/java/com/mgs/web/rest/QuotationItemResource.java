package com.mgs.web.rest;

import com.mgs.repository.QuotationItemRepository;
import com.mgs.service.QuotationItemQueryService;
import com.mgs.service.QuotationItemService;
import com.mgs.service.criteria.QuotationItemCriteria;
import com.mgs.service.dto.QuotationItemDTO;
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
 * REST controller for managing {@link com.mgs.domain.QuotationItem}.
 */
@RestController
@RequestMapping("/api/quotation-items")
public class QuotationItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationItemResource.class);

    private static final String ENTITY_NAME = "quotationItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationItemService quotationItemService;

    private final QuotationItemRepository quotationItemRepository;

    private final QuotationItemQueryService quotationItemQueryService;

    public QuotationItemResource(
        QuotationItemService quotationItemService,
        QuotationItemRepository quotationItemRepository,
        QuotationItemQueryService quotationItemQueryService
    ) {
        this.quotationItemService = quotationItemService;
        this.quotationItemRepository = quotationItemRepository;
        this.quotationItemQueryService = quotationItemQueryService;
    }

    /**
     * {@code POST  /quotation-items} : Create a new quotationItem.
     *
     * @param quotationItemDTO the quotationItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationItemDTO, or with status {@code 400 (Bad Request)} if the quotationItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuotationItemDTO> createQuotationItem(@Valid @RequestBody QuotationItemDTO quotationItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuotationItem : {}", quotationItemDTO);
        if (quotationItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotationItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotationItemDTO = quotationItemService.save(quotationItemDTO);
        return ResponseEntity.created(new URI("/api/quotation-items/" + quotationItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, quotationItemDTO.getId().toString()))
            .body(quotationItemDTO);
    }

    /**
     * {@code PUT  /quotation-items/:id} : Updates an existing quotationItem.
     *
     * @param id the id of the quotationItemDTO to save.
     * @param quotationItemDTO the quotationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationItemDTO,
     * or with status {@code 400 (Bad Request)} if the quotationItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuotationItemDTO> updateQuotationItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuotationItemDTO quotationItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuotationItem : {}, {}", id, quotationItemDTO);
        if (quotationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quotationItemDTO = quotationItemService.update(quotationItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationItemDTO.getId().toString()))
            .body(quotationItemDTO);
    }

    /**
     * {@code PATCH  /quotation-items/:id} : Partial updates given fields of an existing quotationItem, field will ignore if it is null
     *
     * @param id the id of the quotationItemDTO to save.
     * @param quotationItemDTO the quotationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationItemDTO,
     * or with status {@code 400 (Bad Request)} if the quotationItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotationItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuotationItemDTO> partialUpdateQuotationItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuotationItemDTO quotationItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuotationItem partially : {}, {}", id, quotationItemDTO);
        if (quotationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuotationItemDTO> result = quotationItemService.partialUpdate(quotationItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quotation-items} : get all the quotationItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotationItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuotationItemDTO>> getAllQuotationItems(
        QuotationItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuotationItems by criteria: {}", criteria);

        Page<QuotationItemDTO> page = quotationItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quotation-items/count} : count all the quotationItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuotationItems(QuotationItemCriteria criteria) {
        LOG.debug("REST request to count QuotationItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(quotationItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quotation-items/:id} : get the "id" quotationItem.
     *
     * @param id the id of the quotationItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuotationItemDTO> getQuotationItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuotationItem : {}", id);
        Optional<QuotationItemDTO> quotationItemDTO = quotationItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotationItemDTO);
    }

    /**
     * {@code DELETE  /quotation-items/:id} : delete the "id" quotationItem.
     *
     * @param id the id of the quotationItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotationItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuotationItem : {}", id);
        quotationItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
