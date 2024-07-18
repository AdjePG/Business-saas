package com.sass.business.controllers;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessInvitationDTO;
import com.sass.business.services.InvitationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
public class InvitationsController {
    // region INJECTED DEPENDENCIES

    private final InvitationsService invitationsService;

    @Autowired
    public InvitationsController(
            InvitationsService invitationsService
    ) {
        this.invitationsService = invitationsService;
    }

    // endregion

    // region REQUEST METHODS

    // region POST - SHAREBUSINESS
    @Operation(summary = "Share business to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @PostMapping("/share-business/{uuid}")
    public ResponseEntity<APIResponse<Void>> shareBusiness(
            @PathVariable UUID uuid,
            @RequestHeader("Authorization") String authorization,
            @RequestBody BusinessInvitationDTO businessInvitationDTO
    ) {
        APIResponse<Void> apiResponse = invitationsService.shareBusiness(
                uuid,
                authorization.substring(7),
                businessInvitationDTO
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - ACCEPTBUSINESSINVITATION
    @Operation(summary = "Accept invitation to a business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @PostMapping("/business-invit-accept/{invitationToken}")
    public ResponseEntity<APIResponse<Void>> acceptBusinessInvitation(
            @PathVariable UUID invitationToken,
            @RequestHeader("Authorization") String authorization
    ) {
        APIResponse<Void> apiResponse = invitationsService.acceptBusinessInvitation(
                invitationToken,
                authorization.substring(7)
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // endregion
}
