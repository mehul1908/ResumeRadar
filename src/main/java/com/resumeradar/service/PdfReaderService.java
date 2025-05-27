package com.resumeradar.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfReaderService {

    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
        	PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to extract text from PDF.";
        }
    }
}
