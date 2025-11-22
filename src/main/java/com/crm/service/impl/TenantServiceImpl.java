package com.crm.service.impl;

import com.crm.domain.Encryption;
import com.crm.domain.Tenant;
import com.crm.repository.EncryptionRepository;
import com.crm.repository.TenantRepository;
import com.crm.service.TenantService;
import com.crm.service.dto.*;
import com.crm.service.dto.TenantDTO;
import com.crm.service.mapper.TenantMapper;
import com.crm.service.UserService;
import com.crm.repository.UserRepository;
import com.crm.service.AddressService;
import com.crm.domain.User;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.crm.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.Tenant}.
 */
@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    private final UserService userService;

    private final AddressService addressService;

    private final EncryptionRepository encryptionRepository;

    private final UserRepository userRepository;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantMapper tenantMapper, UserService userService,
            AddressService addressService, EncryptionRepository encryptionRepository, UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
        this.userService = userService;
        this.addressService = addressService;
        this.encryptionRepository = encryptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TenantDTO save(TenantDTO tenantDTO) {
        log.debug("Request to save Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    @Override
    public TenantDTO update(TenantDTO tenantDTO) {
        log.debug("Request to update Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    @Override
    public Optional<TenantDTO> partialUpdate(TenantDTO tenantDTO) {
        log.debug("Request to partially update Tenant : {}", tenantDTO);

        return tenantRepository
                .findById(tenantDTO.getId())
                .map(existingTenant -> {
                    tenantMapper.partialUpdate(existingTenant, tenantDTO);

                    return existingTenant;
                })
                .map(tenantRepository::save)
                .map(tenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tenants");
        return tenantRepository.findAll(pageable).map(tenantMapper::toDto);
    }

    /**
     * Get all the tenants where Encryption is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findAllWhereEncryptionIsNull() {
        log.debug("Request to get all tenants where Encryption is null");
        return StreamSupport
                .stream(tenantRepository.findAll().spliterator(), false)
                .filter(tenant -> tenant.getEncryption() == null)
                .map(tenantMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id).map(tenantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tenant : {}", id);
        tenantRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TenantDTO registerTenant(TenantRegistrationDTO tenantRegistrationDTO) {
        log.debug("Request to register Tenant : {}", tenantRegistrationDTO);

        // 1. Create and Save User
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setLogin(tenantRegistrationDTO.getLogin());
        userDTO.setFirstName(tenantRegistrationDTO.getFirstName());
        userDTO.setLastName(tenantRegistrationDTO.getLastName());
        userDTO.setEmail(tenantRegistrationDTO.getEmail());
        userDTO.setLangKey(tenantRegistrationDTO.getLangKey());
        userDTO.setActivated(true);

        User user = userService.registerUser(userDTO, tenantRegistrationDTO.getPassword());
        user.setActivated(true);

        // 2. Create and Save Tenant
        TenantDTO tenantDTO = new TenantDTO();
        tenantDTO.setCompanyName(tenantRegistrationDTO.getCompanyName());
        tenantDTO.setContactPerson(tenantRegistrationDTO.getContactPerson());
        tenantDTO.setLogo(tenantRegistrationDTO.getLogo());
        tenantDTO.setWebsite(tenantRegistrationDTO.getWebsite());
        tenantDTO.setRegistrationNumber(tenantRegistrationDTO.getRegistrationNumber());

        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);

        // Link User to Tenant
        tenant.addUsers(user);
        tenantRepository.save(tenant);
        userRepository.save(user);

        Encryption encryption = new Encryption();
        encryption.setKey(EncryptionUtil.generateKey());
        encryption.setPin(tenantRegistrationDTO.getSecurityPin());
        encryption.setTenant(tenant);

        encryptionRepository.save(encryption);

        // Attach encryption to tenant
        tenant.setEncryption(encryption);
        tenantRepository.save(tenant);

        // Convert to DTO AFTER encryption assigned
        TenantDTO savedTenantDTO = tenantMapper.toDto(tenant);

        // 4. Create and Save Address
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressLine1(tenantRegistrationDTO.getAddressLine1());
        addressDTO.setAddressLine2(tenantRegistrationDTO.getAddressLine2());
        addressDTO.setPincode(tenantRegistrationDTO.getPincode());

        if (tenantRegistrationDTO.getCityId() != null) {
            CityDTO cityDTO = new CityDTO();
            cityDTO.setId(tenantRegistrationDTO.getCityId());
            addressDTO.setCity(cityDTO);
        }
        if (tenantRegistrationDTO.getStateId() != null) {
            StateDTO stateDTO = new StateDTO();
            stateDTO.setId(tenantRegistrationDTO.getStateId());
            addressDTO.setState(stateDTO);
        }
        if (tenantRegistrationDTO.getCountryId() != null) {
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setId(tenantRegistrationDTO.getCountryId());
            addressDTO.setCountry(countryDTO);
        }

        addressDTO.setTenant(savedTenantDTO);
        addressService.save(addressDTO);

        return savedTenantDTO;
    }

}
