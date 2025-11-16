package org.example.dataprotal.service;

import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.dataprotal.model.page.Card;
import org.example.dataprotal.model.page.SubContent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {
    private final Path storageDirectory;
    private final Path root = Paths.get("data");

    public FileService() throws IOException {
        this.storageDirectory = Paths.get("data");
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Boş fayl  yüklənə bilməz.");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Fayl uzantısı tapılmadı.");
        }

        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueName = timestamp + "_" + UUID.randomUUID() + fileExtension;

        Path destination = this.storageDirectory.resolve(uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;
    }

    public Resource getFile(String storedName) throws MalformedURLException {
        Path filePath = root.resolve(storedName);
        return new UrlResource(filePath.toUri());
    }

    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.storageDirectory.resolve(fileName);
            boolean deleted = Files.deleteIfExists(filePath);
            System.out.println("Silindi mi? " + fileName + ": " + deleted);
            return deleted;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SneakyThrows
    public byte[] generateWordFile(Card card){
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph title = doc.createParagraph();
        title.setSpacingAfter(250);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(switchToSafeText(card.getTitle()));
        titleRun.setFontFamily("Poppins SemiBold");
        titleRun.setFontSize(20);
        XWPFParagraph mainParagraph = doc.createParagraph();
        mainParagraph.setSpacingAfter(250);
        XWPFRun runMainParagraph = mainParagraph.createRun();
        runMainParagraph.setText(switchToSafeText(card.getText()));
        runMainParagraph.setFontFamily("Poppins Regular");
        runMainParagraph.setFontSize(12);
        for(SubContent subContent : card.getSubContents()){
            XWPFParagraph subTitle = doc.createParagraph();
            XWPFRun subTitleRun = subTitle.createRun();
            subTitleRun.setText(switchToSafeText(subContent.getTitle()));
            subTitleRun.setFontFamily("Poppins");
            subTitleRun.setFontSize(16);
            subTitleRun.setBold(true);
            XWPFParagraph subParagraph = doc.createParagraph();
            XWPFRun subParagraphRun = subParagraph.createRun();
            subParagraphRun.setText(switchToSafeText(subContent.getText()));
            subParagraphRun.setFontFamily("Poppins Regular");
            subParagraphRun.setFontSize(10);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        doc.close();
        return out.toByteArray();
    }

    private String switchToSafeText(String text){
       return text.replace("–", "-")
                .replace("—", "--")
                .replace("…", "...");
    }



}