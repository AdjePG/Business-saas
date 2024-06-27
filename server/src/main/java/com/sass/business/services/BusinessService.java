package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.mappers.BusinessMapper;
import com.sass.business.models.Business;
import com.sass.business.models.User;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public APIResponse<List<BusinessDTO>> getAllBusinesses(Optional<Long> userId) {

        APIResponse<List<BusinessDTO>> apiResponse;
        List<BusinessDTO> businessList;

        try {
            // Filtrar por userId si está presente, de lo contrario, obtener todos los negocios
            Stream<Business> businessStream = userId.isPresent() ?
                    businessRepository.findByUserUuid(userId.get()).stream() :
                    businessRepository.findAll().stream();

            businessList = businessStream
                    .map(businessMapper::toDto)
                    .collect(Collectors.toList());

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    businessList
            );
        } catch (APIResponseException exception) {
            apiResponse = new APIResponse<>(
                    exception.getHttpStatus(),
                    exception.getMessage()
            );
        } catch (Exception exception) {
            apiResponse = new APIResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "There's a server error"
            );
        }

        return apiResponse;
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
