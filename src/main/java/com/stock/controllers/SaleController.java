package com.stock.controllers;

import com.stock.StockManagementConstants;
import com.stock.api.controller.SaleApi;
import com.stock.model.SaleDTO;
import com.stock.pages.SalePage;
import com.stock.services.ISaleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(StockManagementConstants.API_VERSION)
public class SaleController implements SaleApi {

    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    @Override
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable("id") Integer id) {

        SaleDTO saledto = saleService.getSaleById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saledto);
    }



    @Override
    public ResponseEntity<List<SaleDTO>> getAllSales() {

        List<SaleDTO> listSales = saleService.getAllSales();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listSales);
    }


    @Override
    public ResponseEntity<SaleDTO> addSale(@RequestBody SaleDTO saleDTO) {

            SaleDTO saleDTO1 = saleService.addSale(saleDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(saleDTO1);
         //}catch (IllegalArgumentException ex) {
         //   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
         //}
    }

    @Override
    public ResponseEntity<SaleDTO> editSale(@PathVariable("id") Integer id, @RequestBody SaleDTO saleDTO) {

        SaleDTO saleDTO1 = saleService.editSale(id, saleDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saleDTO1);
    }

    @Override
    public ResponseEntity<String> deleteSale(@PathVariable("id") Integer id) {

        //todo validation
        saleService.deleteSale(id);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Sale is deleted");
    }

    @Override
    public ResponseEntity<List<SaleDTO>> getSalesByName(@RequestParam String description) {

        List<SaleDTO> listSales = saleService.searchSale(description);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listSales);
    }

    @Override
    public ResponseEntity<SalePage> searchForSalesByAnyColumn(@RequestParam String input,
                                                              @RequestParam("sort") String sortField,
                                                              @RequestParam String order,
                                                              @RequestParam("page") Integer pageNum,
                                                              @RequestParam("per_page") Integer limitPerPage
    ) {

        Sort.Direction direction = Objects.equals(order, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (StringUtils.isBlank(sortField)) {
            sortField = "saleDate";
        }
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(pageNum, limitPerPage)
                .withSort(sort);

        SalePage salePage = saleService.searchForSalesByAnyColumn(input, pageable);
        salePage.setPageIndex((long) pageNum);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salePage);
    }


}
