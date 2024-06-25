package com.sass.business.controllers;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.services.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
//@Api(value = "Sistema de Gestió de Negocis", description = "Operacions relatives als negocis en el Sistema de Gestió de Negocis")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @Operation(summary = "Veure una llista de negocis disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<BusinessDTO>>> getAllBusinesses() {

        APIResponse<List<BusinessDTO>> apiResponse = businessService.getAllBusinesses();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }

    //@ApiOperation(value = "Obtenir un negoci per Id")
    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getBusinessById(
            //@ApiParam(value = "Id del negoci del qual es recuperarà l'objecte negoci", required = true)
            @PathVariable Long id) {
        BusinessDTO businessDTO = businessService.getBusinessById(id);
        if (businessDTO != null) {
            return ResponseEntity.ok(businessDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@ApiOperation(value = "Afegir un negoci")
    @PostMapping
    public BusinessDTO createBusiness(
            //@ApiParam(value = "Objecte negoci que s'emmagatzemarà a la taula de la base de dades", required = true)
            @Valid @RequestBody BusinessDTO businessDTO) {
        return businessService.createBusiness(businessDTO);
    }

    //@ApiOperation(value = "Actualitzar un negoci")
    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO> updateBusiness(
            //@ApiParam(value = "Id del negoci per actualitzar l'objecte negoci", required = true)
            @PathVariable Long id,
            //@ApiParam(value = "Actualitzar objecte negoci", required = true)
            @Valid @RequestBody BusinessDTO businessDTO) {
        BusinessDTO updatedBusinessDTO = businessService.updateBusiness(id, businessDTO);
        if (updatedBusinessDTO != null) {
            return ResponseEntity.ok(updatedBusinessDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@ApiOperation(value = "Eliminar un negoci")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(
            //@ApiParam(value = "Id del negoci del qual s'eliminarà l'objecte negoci de la taula de la base de dades", required = true)
            @PathVariable Long id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}