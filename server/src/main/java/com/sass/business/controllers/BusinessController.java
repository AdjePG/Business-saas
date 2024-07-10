package com.sass.business.controllers;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.services.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
//@Api(value = "Sistema de Gestió de Negocis", description = "Operacions relatives als negocis en el Sistema de Gestió de Negocis")
public class BusinessController {
    // region INJECTED DEPENDENCIES

    private final BusinessService businessService;

    @Autowired
    public BusinessController(
            BusinessService businessService
    ) {
        this.businessService = businessService;
    }

    // endregion

    // region REQUEST METHODS

    // region GET - GETBUSINESSES
    @Operation(summary = "Get list of businesses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @GetMapping("/")
    public ResponseEntity<APIResponse<List<BusinessDTO>>> getBusinesses(
            @RequestParam(required = false) UUID userId
    ) {
        APIResponse<List<BusinessDTO>> apiResponse = businessService.getBusinesses(userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region GET - GETBUSINESSBYID
    @Operation(summary = "Get business by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<APIResponse<BusinessDTO>> getBusinessById(
            //@ApiParam(value = "Id del negoci del qual es recuperarà l'objecte negoci", required = true)
            @PathVariable UUID uuid
    ) {
        APIResponse<BusinessDTO> apiResponse = businessService.getBusinessById(uuid);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - CREATEBUSINESS
    @Operation(summary = "Create business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created status")
    })
    @PostMapping("/")
    public ResponseEntity<APIResponse<BusinessDTO>> createBusiness(
            //@ApiParam(value = "Objecte negoci que s'emmagatzemarà a la taula de la base de dades", required = true)
            @RequestHeader("Authorization") String authorization,
            @RequestBody BusinessDTO businessDTO
    ) {
        APIResponse<BusinessDTO> apiResponse = businessService.createBusiness(authorization.substring(7), businessDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region PUT - UPDATEBUSINESS
    @Operation(summary = "Update business details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<APIResponse<BusinessDTO>> updateBusiness(
            //@ApiParam(value = "Actualitzar objecte negoci", required = true)
            @PathVariable UUID uuid,
            @RequestHeader("Authorization") String authorization,
            @RequestBody BusinessDTO businessDTO
    ) {
        APIResponse<BusinessDTO> apiResponse = businessService.updateBusiness(
                uuid,
                authorization.substring(7),
                businessDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region DELETE - DELETEBUSINESS
    @Operation(summary = "Delete business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content status")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<APIResponse<Void>> deleteBusiness(
            //@ApiParam(value = "Id del negoci del qual s'eliminarà l'objecte negoci de la taula de la base de dades", required = true)
            @PathVariable UUID uuid,
            @RequestHeader("Authorization") String authorization
    ) {
        APIResponse<Void> apiResponse = businessService.deleteBusiness(uuid, authorization.substring(7));
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // endregion
}