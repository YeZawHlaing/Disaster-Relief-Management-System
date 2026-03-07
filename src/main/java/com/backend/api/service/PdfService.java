package com.backend.api.service;

import com.backend.api.entity.DistributionRecord;
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Paragraph title = new Paragraph("Distribution Records", font);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);

        String[] headers = {"ID", "Status", "Distribution Date", "Household NRC",
                "Family Members", "Under Five", "Disabled", "Distributed Items",
                "Beneficiary ID", "Stock ID", "User ID"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        for (DistributionRecord record : records) {
            table.addCell(String.valueOf(record.getId()));
            table.addCell(record.getStatus());
            table.addCell(record.getDistributionDate().toString());
            table.addCell(record.getHouseHoldNrc());
            table.addCell(String.valueOf(record.getFamilyMembers()));
            table.addCell(String.valueOf(record.getUnderFive()));
            table.addCell(String.valueOf(record.getDisabled()));
            table.addCell(record.getDistributedItems());
            table.addCell(String.valueOf(record.getBeneficiary().getId()));
            table.addCell(String.valueOf(record.getStock().getId()));
            table.addCell(String.valueOf(record.getUser().getId()));
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

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        Paragraph title = new Paragraph("Stock Information Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(19);
        document.add(title);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        // Table headers
        String[] headers = {"ID", "Item Name", "Description",
                "Quantity", "Unit", "Storage Location", "Manufactured Date",
                "Expired Date"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        for (StockInfo stock : stockInfos) {
            table.addCell(stock.getId() != null ? stock.getId().toString() : "-");
//            table.addCell((PdfPCell) (stock.getEventType() != null ? stock.getEventType() : "-"));
            table.addCell(stock.getItemName() != null ? stock.getItemName() : "-");
            table.addCell(stock.getItemDescription() != null ? stock.getItemDescription() : "-");
//            table.addCell((PdfPCell) (stock.getType() != null ? stock.getType() : "-"));
            table.addCell(stock.getQuantity() != null ? stock.getQuantity().toString() : "-");
            table.addCell(stock.getUnitOfMeasure() != null ? stock.getUnitOfMeasure() : "-");
            table.addCell(stock.getStorageLocation() != null ? stock.getStorageLocation() : "-");
            table.addCell(stock.getManufacturedDate() != null ? stock.getManufacturedDate().toString() : "-");
            table.addCell(stock.getExpiriedDate() != null ? stock.getExpiriedDate().toString() : "-");
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}