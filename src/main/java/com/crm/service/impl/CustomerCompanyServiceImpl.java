package com.crm.service.impl;

import com.crm.domain.CustomerCompany;
import com.crm.repository.CustomerCompanyRepository;
import com.crm.service.CustomerCompanyService;
import com.crm.service.dto.CustomerCompanyDTO;
import com.crm.service.mapper.CustomerCompanyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.CustomerCompany}.
 */
@Service
@Transactional
public class CustomerCompanyServiceImpl implements CustomerCompanyService {

    private final Logger log = LoggerFactory.getLogger(CustomerCompanyServiceImpl.class);

    private final CustomerCompanyRepository customerCompanyRepository;

    private final CustomerCompanyMapper customerCompanyMapper;

    public CustomerCompanyServiceImpl(CustomerCompanyRepository customerCompanyRepository, CustomerCompanyMapper customerCompanyMapper) {
        this.customerCompanyRepository = customerCompanyRepository;
        this.customerCompanyMapper = customerCompanyMapper;
    }

    @Override
    public CustomerCompanyDTO save(CustomerCompanyDTO customerCompanyDTO) {
        log.debug("Request to save CustomerCompany : {}", customerCompanyDTO);
        CustomerCompany customerCompany = customerCompanyMapper.toEntity(customerCompanyDTO);
        customerCompany = customerCompanyRepository.save(customerCompany);
        return customerCompanyMapper.toDto(customerCompany);
    }

    @Override
    public CustomerCompanyDTO update(CustomerCompanyDTO customerCompanyDTO) {
        log.debug("Request to update CustomerCompany : {}", customerCompanyDTO);
        CustomerCompany customerCompany = customerCompanyMapper.toEntity(customerCompanyDTO);
        customerCompany = customerCompanyRepository.save(customerCompany);
        return customerCompanyMapper.toDto(customerCompany);
    }

    @Override
    public Optional<CustomerCompanyDTO> partialUpdate(CustomerCompanyDTO customerCompanyDTO) {
        log.debug("Request to partially update CustomerCompany : {}", customerCompanyDTO);

        return customerCompanyRepository
            .findById(customerCompanyDTO.getId())
            .map(existingCustomerCompany -> {
                customerCompanyMapper.partialUpdate(existingCustomerCompany, customerCompanyDTO);

                return existingCustomerCompany;
            })
            .map(customerCompanyRepository::save)
            .map(customerCompanyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerCompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerCompanies");
        return customerCompanyRepository.findAll(pageable).map(customerCompanyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerCompanyDTO> findOne(Long id) {
        log.debug("Request to get CustomerCompany : {}", id);
        return customerCompanyRepository.findById(id).map(customerCompanyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerCompany : {}", id);
        customerCompanyRepository.deleteById(id);
    }
}
