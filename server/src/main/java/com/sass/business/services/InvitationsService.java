package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessInvitationDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.models.User;
import com.sass.business.models.business.Business;
import com.sass.business.models.business.BusinessInvitation;
import com.sass.business.models.business.SharedBusiness;
import com.sass.business.models.pks.UserBusinessPK;
import com.sass.business.others.AuthUtil;
import com.sass.business.others.Mailer;
import com.sass.business.others.UuidConverterUtil;
import com.sass.business.repositories.BusinessInvitationRepository;
import com.sass.business.repositories.BusinessRepository;
import com.sass.business.repositories.SharedBusinessRepository;
import com.sass.business.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationsService {
    // region INJECTED DEPENDENCIES

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final SharedBusinessRepository sharedBusinessRepository;
    private final BusinessInvitationRepository businessInvitationRepository;
    private final AuthUtil authUtil;
    private final UuidConverterUtil uuidConverterUtil;
    private final Mailer mailer;

    public InvitationsService(
            UserRepository userRepository,
            BusinessRepository businessRepository,
            SharedBusinessRepository sharedBusinessRepository,
            BusinessInvitationRepository businessInvitationRepository,
            AuthUtil authUtil,
            UuidConverterUtil uuidConverterUtil,
            Mailer mailer
    ) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.sharedBusinessRepository = sharedBusinessRepository;
        this.businessInvitationRepository = businessInvitationRepository;
        this.authUtil = authUtil;
        this.uuidConverterUtil = uuidConverterUtil;
        this.mailer = mailer;
    }

    // endregion

    // region SERVICE METHODS

    public APIResponse<Void> shareBusiness(UUID uuid, String token, BusinessInvitationDTO businessInvitationDTO) {
        APIResponse<Void> apiResponse;
        Optional<Business> businessById;
        Business business;
        BusinessInvitation businessInvitation;
        User user;
        UUID authUserUUID;
        UUID invitationToken;

        try {
            businessById = businessRepository.findById(uuidConverterUtil.uuidToBytes(uuid));

            if (businessById.isEmpty()) {
                throw new APIResponseException("Business not found", HttpStatus.NOT_FOUND.value());
            }

            business = businessById.get();
            user = business.getUser();
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));

            if (!Arrays.equals(user.getUuid(), uuidConverterUtil.uuidToBytes(authUserUUID))) {
                throw new APIResponseException("Invalid user", HttpStatus.UNAUTHORIZED.value());
            }

            invitationToken = UUID.randomUUID();

            businessInvitation = new BusinessInvitation();
            businessInvitation.setToken(uuidConverterUtil.uuidToBytes(invitationToken));
            businessInvitation.setEmail(businessInvitationDTO.getEmail());
            businessInvitation.setBusiness(business);

            businessInvitationRepository.save(businessInvitation);

            mailer.sendEmail(
                    businessInvitationDTO.getEmail(),
                    String.format("FactuLink - %s quiere que te unas a su negocio %s", user.getName(), business.getName()),
                    "emails/invitation-template",
                    true,
                    Map.of(
                            "business", business.getName(),
                            "token", invitationToken.toString()
                    )
            );

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

    public APIResponse<Void> acceptBusinessInvitation(UUID invitationToken, String token) {
        APIResponse<Void> apiResponse;
        Optional<User> userById;
        Optional<BusinessInvitation> businessInvitationById;
        BusinessInvitation businessInvitation;
        User user;
        Business business;
        SharedBusiness sharedBusiness;
        UserBusinessPK userBusinessPK;
        UUID authUserUUID;

        try {
            authUserUUID = UUID.fromString(authUtil.extractClaim(token, "uuid"));
            userById = userRepository.findById(uuidConverterUtil.uuidToBytes(authUserUUID));
            user = userById.get();

            // Checking if the invitation exists
            businessInvitationById = businessInvitationRepository.findById(uuidConverterUtil.uuidToBytes(invitationToken));
            if (businessInvitationById.isEmpty()) {
                throw new APIResponseException("Business invitation not found", HttpStatus.NOT_FOUND.value());
            }

            businessInvitation = businessInvitationById.get();

            // Checking if the invitation is not expired
            if (LocalDateTime.now().isAfter(businessInvitation.getExpiresAt())) {
                businessInvitationRepository.delete(businessInvitation);
                throw new APIResponseException("Business invitation is expired", HttpStatus.BAD_REQUEST.value());
            }

            // Checking if the user is not the owner of the business
            business = businessInvitation.getBusiness();
            if (Arrays.equals(business.getUser().getUuid(), user.getUuid())) {
                businessInvitationRepository.delete(businessInvitation);
                throw new APIResponseException("This user is the owner of the business", HttpStatus.BAD_REQUEST.value());
            }

            // Checking if the invited user hasn't accepted an invitation for the same shared business
            userBusinessPK = new UserBusinessPK(user, business);
            if (sharedBusinessRepository.findById(userBusinessPK).isPresent()) {
                businessInvitationRepository.delete(businessInvitation);
                throw new APIResponseException("This business has been shared and accepted before for this user", HttpStatus.BAD_REQUEST.value());
            }

            sharedBusiness = new SharedBusiness();
            sharedBusiness.setBusiness(business);
            sharedBusiness.setUser(user);

            sharedBusinessRepository.save(sharedBusiness);
            businessInvitationRepository.delete(businessInvitation);

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
