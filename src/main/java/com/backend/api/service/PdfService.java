package com.backend.api.service;

import com.backend.api.entity.Beneficiary;
import com.backend.api.entity.DistributionRecord;
import com.backend.api.entity.Location;
import com.backend.api.entity.StockInfo;
import com.backend.api.repository.DistributionRecordRepository;
import com.backend.api.repository.StockInfoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final DistributionRecordRepository distributionRecordRepository;
    private final StockInfoRepository stockInfoRepository;


    // Generate PDF as byte[]
    public byte[] exportDistributionRecordsToPdf() throws IOException, DocumentException {
        List<DistributionRecord> records = distributionRecordRepository.findAll();
        Beneficiary beneficiary=new Beneficiary();
        Location location=new Location();
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Paragraph title = new Paragraph("Distribution Records", font);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);

        String[] headers = {"ID","Beneficiary Name", "Household NRC",
                "Family Members", "Under Five", "Disabled", "Distributed Items",
                 "Distribution Date", "Staff email","Status","Location name"};


        BaseColor headerColor = new BaseColor(72, 114, 183); // #4872b7
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, BaseColor.WHITE); // adjust size 6
        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 5, BaseColor.BLACK); // adjust size 5


        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setPadding(5f);
            table.addCell(cell);
        }

        for (DistributionRecord record : records) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getId()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(record.getBeneficiary().getBeneficName(), rowFont)));
            table.addCell(new PdfPCell(new Phrase(record.getHouseHoldNrc(), rowFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getFamilyMembers()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getUnderFive()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getDisabled()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(record.getDistributedItems(), rowFont)));
//    table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getStock().getId()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(record.getDistributionDate().toString(), rowFont)));
            table.addCell(new PdfPCell(new Phrase(record.getUser().getEmail(), rowFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(record.getStatus()), rowFont)));
            table.addCell(new PdfPCell(new Phrase(
                    record.getBeneficiary().getLocation() != null
                            ? record.getBeneficiary().getLocation().getLocationName()
                            : "",
                    rowFont
            )));
        }

        document.add(table);
        document.close();

        // Return PDF as byte array directly
        return baos.toByteArray();
    }
    // Generate PDF for StockInfo
    @Transactional
    public byte[] exportStockInfoToPdf() throws DocumentException, IOException {
        List<StockInfo> stockInfos = stockInfoRepository.findAll();
        System.out.println("stock size: " + stockInfos.size());

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Paragraph title = new Paragraph("Stock Information Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // Table
        PdfPTable table = new PdfPTable(11); // 10 columns
        table.setWidthPercentage(100);

        // Headers
        String[] headers = {"ID","Event Type", "Item Name", "Description","Type",
                "Quantity", "Unit", "Storage Location", "Created Date","Expired Date","logistics"};

        BaseColor headerColor = new BaseColor(72, 114, 183); // #4872b7
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, BaseColor.WHITE);
        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 5, BaseColor.BLACK);

        // Add headers
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setPadding(5F);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Add rows
        for (StockInfo stock : stockInfos) {
            table.addCell(new PdfPCell(new Phrase(stock.getId() != null ? stock.getId().toString() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getEventType() != null ? String.valueOf(stock.getEventType()) : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getItemName() != null ? stock.getItemName() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getItemDescription() != null ? stock.getItemDescription() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getType() != null ? String.valueOf(stock.getType()) : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getQuantity() != null ? stock.getQuantity().toString() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getUnitOfMeasure() != null ? stock.getUnitOfMeasure() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getStorageLocation() != null ? stock.getStorageLocation() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getCreatedDate() != null ? stock.getCreatedDate().toString() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(stock.getExpiriedDate() != null ? stock.getExpiriedDate().toString() : "-", rowFont)));
            table.addCell(new PdfPCell(new Phrase(
                    stock.getUser().getEmail() != null
                            ? stock.getUser().getEmail()
                            : "",
                    rowFont
            )));
        }
        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}