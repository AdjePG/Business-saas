package com.sass.business.services;

import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.mappers.BusinessMapper;
import com.sass.business.models.Business;
import com.sass.business.models.User;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessMapper businessMapper;

    @Autowired
    public BusinessService(BusinessRepository businessRepository, UserRepository userRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.businessMapper = businessMapper;
    }

    public List<BusinessDTO> getAllBusinesses() {
        return businessRepository.findAll().stream()
                .map(businessMapper::toDto)
                .collect(Collectors.toList());
    }

    public BusinessDTO getBusinessById(Long uuid) {
        return businessRepository.findById(uuid)
                .map(businessMapper::toDto)
                .orElse(null);
    }

    public BusinessDTO createBusiness(BusinessDTO businessDTO) {
        User user = userRepository.findById(businessDTO.getUuidUser())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user UUID"));
        Business business = businessMapper.toModal(businessDTO, user);
        Business savedBusiness = businessRepository.save(business);
        return businessMapper.toDto(savedBusiness);
    }

    public BusinessDTO updateBusiness(Long uuid, BusinessDTO businessDTO) {
        return businessRepository.findById(uuid).map(existingBusiness -> {
            User user = userRepository.findById(businessDTO.getUuidUser())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user UUID"));
            Business business = businessMapper.toModal(businessDTO, user);
            business.setUuid(existingBusiness.getUuid()); // Preserva el UUID existente
            Business updatedBusiness = businessRepository.save(business);
            return businessMapper.toDto(updatedBusiness);
        }).orElse(null);
    }

    public void deleteBusiness(Long uuid) {
        businessRepository.deleteById(uuid);
    }
}
