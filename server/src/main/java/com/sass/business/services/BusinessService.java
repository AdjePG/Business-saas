package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.mappers.BusinessMapper;
import com.sass.business.models.Business;
import com.sass.business.models.User;
import com.sass.business.others.AuthUtil;
import com.sass.business.others.UuidConverterUtil;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BusinessService {
    // region INJECTED DEPENDENCIES

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessMapper businessMapper;
    private final AuthUtil authUtil;
    private final UuidConverterUtil uuidConverterUtil;

    public BusinessService(
            BusinessRepository businessRepository,
            UserRepository userRepository,
            BusinessMapper businessMapper,
            AuthUtil authUtil,
            UuidConverterUtil uuidConverterUtil
    ) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.businessMapper = businessMapper;
        this.authUtil = authUtil;
        this.uuidConverterUtil = uuidConverterUtil;
    }

    // endregion

    // region SERVICE METHODS

    public APIResponse<List<BusinessDTO>> getBusinesses(Optional<UUID> userId) {
        APIResponse<List<BusinessDTO>> apiResponse;
        List<BusinessDTO> businessesDTO;

        try {
            // Filtrar por userId si está presente, de lo contrario, obtener todos los negocios
            Stream<Business> businessStream = userId.isPresent() ?
                    businessRepository.findByUserUuid(uuidConverterUtil.uuidToBytes(userId.get())).stream() :
                    businessRepository.findAll().stream();

            businessesDTO = businessStream
                    .map(businessMapper::toDto)
                    .collect(Collectors.toList());

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    businessesDTO
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

    public APIResponse<BusinessDTO> getBusinessById(UUID uuid) {
        Optional<BusinessDTO> businessDTO;
        APIResponse<BusinessDTO> apiResponse;

        try {
            businessDTO = businessRepository.findById(uuidConverterUtil.uuidToBytes(uuid))
                    .map(businessMapper::toDto);

            if (businessDTO.isEmpty()) {
                throw new APIResponseException("Business not found", HttpStatus.NOT_FOUND.value());
            }

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    businessDTO.get()
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

    public APIResponse<BusinessDTO> createBusiness(String token, BusinessDTO businessDTO) {
        APIResponse<BusinessDTO> apiResponse;
        UUID authUserUUID;
        Optional<User> userById;
        User user;
        Business business;

        try {
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));
            userById = userRepository.findById(uuidConverterUtil.uuidToBytes(authUserUUID));

            if (userById.isEmpty()) {
                throw new APIResponseException("User not found", HttpStatus.NOT_FOUND.value());
            }

            user = userById.get();

            business = businessMapper.toModel(businessDTO, user);
            business = businessRepository.save(business);

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    businessMapper.toDto(business)
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

    public APIResponse<BusinessDTO> updateBusiness(UUID uuid, String token, BusinessDTO businessDTO) {
        APIResponse<BusinessDTO> apiResponse;
        Optional<Business> businessById;
        Business business;
        User user;
        UUID authUserUUID;

        try {
            businessById = businessRepository.findById(uuidConverterUtil.uuidToBytes(uuid));

            if (businessById.isEmpty()) {
                throw new APIResponseException("Business not found", HttpStatus.NOT_FOUND.value());
            }

            user = businessById.get().getUser();
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));

            if (!Arrays.equals(user.getUuid(), uuidConverterUtil.uuidToBytes(authUserUUID))) {
                throw new APIResponseException("Invalid user", HttpStatus.UNAUTHORIZED.value());
            }

            business = businessMapper.toModel(businessDTO, user);
            business.setUuid(businessById.get().getUuid());
            business = businessRepository.save(business);

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    businessMapper.toDto(business)
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

    public APIResponse<Void> deleteBusiness(UUID uuid, String token) {
        APIResponse<Void> apiResponse;
        Optional<Business> businessById;
        User user;
        UUID authUserUUID;

        try {
            businessById = businessRepository.findById(uuidConverterUtil.uuidToBytes(uuid));

            if (businessById.isEmpty()) {
                throw new APIResponseException("Business not found", HttpStatus.NOT_FOUND.value());
            }

            user = businessById.get().getUser();
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));

            if (!Arrays.equals(user.getUuid(), uuidConverterUtil.uuidToBytes(authUserUUID))) {
                throw new APIResponseException("Invalid user", HttpStatus.UNAUTHORIZED.value());
            }

            businessRepository.deleteById(uuidConverterUtil.uuidToBytes(uuid));

            apiResponse = new APIResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "Success!"
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

    // endregion
}
