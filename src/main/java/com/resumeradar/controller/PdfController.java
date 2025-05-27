package com.resumeradar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resumeradar.service.PdfReaderService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfReaderService pdfReaderService;

    @PostMapping("/extract")
    public String extractTextFromPdf(@RequestParam("file") MultipartFile file) {
        return pdfReaderService.extractText(file);
    }
}

