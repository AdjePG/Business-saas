package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.mappers.BusinessMapper;
import com.sass.business.models.business.Business;
import com.sass.business.models.business.SharedBusiness;
import com.sass.business.models.User;
import com.sass.business.models.pks.UserBusinessPK;
import com.sass.business.others.AuthUtil;
import com.sass.business.others.UuidConverterUtil;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.SharedBusinessRepository;
import com.sass.business.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BusinessService {
    // region INJECTED DEPENDENCIES

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final SharedBusinessRepository sharedBusinessRepository;
    private final BusinessMapper businessMapper;
    private final AuthUtil authUtil;
    private final UuidConverterUtil uuidConverterUtil;

    public BusinessService(
            BusinessRepository businessRepository,
            UserRepository userRepository,
            SharedBusinessRepository sharedBusinessRepository,
            BusinessMapper businessMapper,
            AuthUtil authUtil,
            UuidConverterUtil uuidConverterUtil
    ) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.sharedBusinessRepository = sharedBusinessRepository;
        this.businessMapper = businessMapper;
        this.authUtil = authUtil;
        this.uuidConverterUtil = uuidConverterUtil;
    }

    // endregion

    // region SERVICE METHODS

    public APIResponse<List<BusinessDTO>> getBusinesses(UUID userUuid, boolean shared) {
        APIResponse<List<BusinessDTO>> apiResponse;
        List<BusinessDTO> businessesDTO;
        Stream<Business> businessesStream;

        try {
            if (userUuid == null) {
                businessesStream = businessRepository.findAll().stream();
            } else {
                if (shared) {
                    businessesStream = sharedBusinessRepository.findByUserUuid(uuidConverterUtil.uuidToBytes(userUuid)).stream()
                            .map(SharedBusiness::getBusiness);
                } else {
                    businessesStream = businessRepository.findByUserUuid(uuidConverterUtil.uuidToBytes(userUuid)).stream();
                }
            }

            businessesDTO = businessesStream
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
            user = userById.get();

            business = businessMapper.toModel(businessDTO, user);
            business = businessRepository.save(business);

            apiResponse = new APIResponse<>(
                    HttpStatus.CREATED.value(),
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

            business = businessMapper.toModel(businessById.get(), businessDTO, user);
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

    public APIResponse<Void> deleteBusiness(UUID uuid, boolean shared, String token) {
        APIResponse<Void> apiResponse;
        Optional<Business> businessById;
        Optional<SharedBusiness> sharedBusinessById;
        Business business;
        User user;
        UserBusinessPK userBusinessPK;
        UUID authUserUUID;

        try {
            businessById = businessRepository.findById(uuidConverterUtil.uuidToBytes(uuid));

            if (businessById.isEmpty()) {
                throw new APIResponseException("Business not found", HttpStatus.NOT_FOUND.value());
            }

            business = businessById.get();
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));

            if (shared) {
                user = userRepository.findById(uuidConverterUtil.uuidToBytes(authUserUUID)).get();

                userBusinessPK = new UserBusinessPK(user, business);
                sharedBusinessById = sharedBusinessRepository.findById(userBusinessPK);
                if (sharedBusinessById.isEmpty()) {
                    throw new APIResponseException("This business wasn't shared for this user", HttpStatus.NOT_FOUND.value());
                }

                sharedBusinessRepository.deleteById(userBusinessPK);
            } else {
                user = business.getUser();

                if (!Arrays.equals(user.getUuid(), uuidConverterUtil.uuidToBytes(authUserUUID))) {
                    throw new APIResponseException("Invalid user", HttpStatus.UNAUTHORIZED.value());
                }

                businessRepository.deleteById(uuidConverterUtil.uuidToBytes(uuid));
            }

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
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
