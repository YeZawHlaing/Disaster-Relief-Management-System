package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.StockInfoRequestDto;
import com.backend.api.service.PdfService;
import com.backend.api.service.StockInfoService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin
public class StockInfoController {

    private final StockInfoService stockInfoService;
    private final PdfService pdfService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createStockInfo(
            @PathVariable Long userId,
            @RequestBody StockInfoRequestDto request) {

        ApiResponse response = stockInfoService.createStockInfo(userId, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllStockInfo() {

        ApiResponse response = stockInfoService.getAllStockInfo();
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStockInfo(
            @PathVariable Long id,
            @RequestBody StockInfoRequestDto request) {

        ApiResponse response = stockInfoService.updateStockInfo(id, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStockInfo(@PathVariable Long id) {

        ApiResponse response = stockInfoService.deleteStockInfoById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @GetMapping("/stock-pdf")
    public ResponseEntity<byte[]> downloadStockInfoPdf() throws DocumentException, IOException {
        byte[] pdfBytes = pdfService.exportStockInfoToPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"stock_info.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}