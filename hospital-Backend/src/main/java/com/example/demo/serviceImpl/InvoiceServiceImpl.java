package com.example.demo.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.InvoiceService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public ByteArrayInputStream generateInvoice(int billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        Bill bill = optionalBill.get();
        Patient patient = bill.getPatient();
        Doctor doctor = bill.getPrescription().getAppointment().getDoctor();

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 💙 HEADER (Blue Bar)
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell(new Phrase("CityCare Hospital",
                    new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.WHITE)));
            headerCell.setBackgroundColor(new BaseColor(52, 152, 219)); // nice blue
            headerCell.setPadding(15);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(headerCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // 🟦 SECTION TITLE
            Paragraph sectionTitle = new Paragraph("Invoice Summary",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));
            sectionTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(sectionTitle);

            document.add(new Paragraph("\n"));

            // 🧍 Patient + Doctor details table
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setSpacingBefore(10);

            // Patient Block
            PdfPCell patientCell = new PdfPCell();
            patientCell.setPadding(10);
            patientCell.setBackgroundColor(new BaseColor(236, 240, 241)); // light grey
            patientCell.addElement(new Phrase("Patient Details:", bold(12)));
            patientCell.addElement(new Phrase("Name: " + patient.getFullName(), normal(11)));
            patientCell.addElement(new Phrase("Age: " + patient.getAge(), normal(11)));
            patientCell.addElement(new Phrase("Phone: " + patient.getPhone(), normal(11)));
            detailsTable.addCell(patientCell);

            // Doctor Block
            PdfPCell doctorCell = new PdfPCell();
            doctorCell.setPadding(10);
            doctorCell.setBackgroundColor(new BaseColor(236, 240, 241));
            doctorCell.addElement(new Phrase("Doctor Details:", bold(12)));
            doctorCell.addElement(new Phrase("Name: " + doctor.getDoctorName(), normal(11)));
            doctorCell.addElement(new Phrase("Specialization: " + doctor.getSpecialization(), normal(11)));
            detailsTable.addCell(doctorCell);

            document.add(detailsTable);
            document.add(new Paragraph("\n"));

            // 📘 Invoice Breakdown Table (Colorful)
            PdfPTable billTable = new PdfPTable(2);
            billTable.setWidthPercentage(100);
            billTable.setSpacingBefore(10);

            // Table header style
            PdfPCell th1 = coloredHeader("Description");
            PdfPCell th2 = coloredHeader("Amount (₹)");
            billTable.addCell(th1);
            billTable.addCell(th2);

            // Rows
            billTable.addCell(lightCell("Consultation Fee"));
            billTable.addCell(lightCell(String.format("%.2f", bill.getConsultationFee())));

            billTable.addCell(lightCell("Medicine Charges"));
            billTable.addCell(lightCell(String.format("%.2f", bill.getMedicineCharges())));

            // Highlighted Total Row
            PdfPCell totalLabel = new PdfPCell(new Phrase("Total Amount", bold(12)));
            totalLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalLabel.setPadding(10);
            totalLabel.setBackgroundColor(new BaseColor(255, 234, 167)); // yellow soft
            billTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase("₹" + String.format("%.2f", bill.getTotalAmount()),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setPadding(10);
            totalValue.setBackgroundColor(new BaseColor(255, 234, 167));
            billTable.addCell(totalValue);

            document.add(billTable);

            document.add(new Paragraph("\n\n"));

            // 🩵 Footer Message
            Paragraph footer = new Paragraph(
                    "Thank you for choosing CityCare Hospital",
                    new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, BaseColor.GRAY)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Helper: Bold font
    private Font bold(int size) {
        return new Font(Font.FontFamily.HELVETICA, size, Font.BOLD, BaseColor.BLACK);
    }

    // Helper: Normal font
    private Font normal(int size) {
        return new Font(Font.FontFamily.HELVETICA, size, Font.NORMAL, BaseColor.DARK_GRAY);
    }

    // Header cell style
    private PdfPCell coloredHeader(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, bold(12)));
        cell.setBackgroundColor(new BaseColor(41, 128, 185)); // blue
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        return cell;
    }

    // Light grey cells
    private PdfPCell lightCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, normal(11)));
        cell.setBackgroundColor(new BaseColor(245, 245, 245));
        cell.setPadding(10);
        return cell;
    }
}
