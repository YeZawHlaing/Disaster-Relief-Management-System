package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.DistributionRecordRequestDto;
import com.backend.api.entity.DistributionRecord;
import com.backend.api.service.DistributionRecordService;
import com.backend.api.service.PdfService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.DocumentException;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import java.io.IOException;

@RestController
@RequestMapping("/api/distribution")
@RequiredArgsConstructor
@CrossOrigin
public class DistributionRecordController {

    private final DistributionRecordService distributionRecordService;
    private final PdfService pdfService;


    @PostMapping("/{userId}")
    public ResponseEntity<DistributionRecord> createDistributionRecord(
            @PathVariable Long userId,
            @RequestBody DistributionRecordRequestDto dto) {

        DistributionRecord savedRecord = distributionRecordService.createDistributionRecord(userId, dto);

        // Return saved entity with 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }
    @GetMapping
    public ApiResponse getAllDistributionRecords() {
        return distributionRecordService.getAllDistributionRecords();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDistributionRecordById(
            @PathVariable Long id,
            @RequestBody DistributionRecordRequestDto dto) {

        ApiResponse response = distributionRecordService.updateDistributionRecordById(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteDistributionRecordById(@PathVariable Long id) {
        return distributionRecordService.deleteDistributionRecordById(id);
    }
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() throws IOException, DocumentException {
        byte[] pdfBytes = pdfService.exportDistributionRecordsToPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"distribution_records.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}