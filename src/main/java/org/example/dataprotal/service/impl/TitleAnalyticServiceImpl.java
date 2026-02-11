package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dataprotal.dto.request.analytic.AnalyticRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticRequest;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;
import org.example.dataprotal.exception.NotActiveException;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.analytic.AnalyticMapper;
import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.EmbedLink;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.repository.analytics.AnalyticRepository;
import org.example.dataprotal.repository.analytics.SubTitleRepository;
import org.example.dataprotal.service.FileService;
import org.example.dataprotal.service.TitleAnalyticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TitleAnalyticServiceImpl implements TitleAnalyticService {
    private final AnalyticRepository analyticRepository;
    private final FileService fileService;
    private final SubTitleRepository subTitleRepository;

    @Override
    public List<AnalyticResponse> getAll() {
        log.info("Get all analytics");
        return analyticRepository.findAll().stream().map(AnalyticMapper::toResponse).toList();
    }

    @Override
    public AnalyticResponse getById(Long id) {
        log.info("Get analytic by id : {}", id);
        return AnalyticMapper.toResponse(analyticRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Analytic not found")));
    }

    @Override
    public AnalyticResponse save(AnalyticRequest analyticRequest, MultipartFile image) throws IOException {
        Analytic analytic = AnalyticMapper.toEntity(analyticRequest);
        String coverImageUrl = fileService.uploadFile(image);
        analytic.setCoverImage(coverImageUrl);

        SubTitle subTitle = subTitleRepository.findById(analyticRequest.subTitleId())
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));

        analytic.setSubTitle(subTitle);

        log.info("Save analytic : {}", analytic);
        return AnalyticMapper.toResponse(analyticRepository.save(analytic));
    }

    @Override
    public AnalyticResponse update(Long id, UpdatedAnalyticRequest analyticRequest) throws IOException {
        log.info("Update analytic by id : {}", id);

        Analytic analytic = analyticRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Analytic not found"));

        analytic.getEmbedLinks().size();
        Iterator<EmbedLink> iterator = analytic.getEmbedLinks().iterator();
        while (iterator.hasNext()) {
            EmbedLink e = iterator.next();
            boolean existsInRequest = analyticRequest.embedLinks().stream()
                    .anyMatch(r -> r.id() != null && r.id().equals(e.getId()));
            if (!existsInRequest) {
                iterator.remove();
            }
        }

        if (analyticRequest.coverImage() != null && !analyticRequest.coverImage().isEmpty()) {
            String coverImageUrl = fileService.uploadFile(analyticRequest.coverImage());
            analytic.setCoverImage(coverImageUrl);
        }

        AnalyticMapper.updateAnalytic(analytic, analyticRequest);

        SubTitle subTitle = subTitleRepository.findById(analyticRequest.subTitleId())
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));

        analytic.setSubTitle(subTitle);

        analyticRepository.save(analytic);

        return AnalyticMapper.toResponse(analytic);
    }


    @Override
    public List<AnalyticResponse> getBySubTitleId(Long subTitleId) {

        List<Analytic> analytics = analyticRepository.findBySubTitleId(subTitleId);
        return analytics.stream().map(AnalyticMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Analytic analytic = analyticRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Analytic not found"));

        SubTitle subTitle = analytic.getSubTitle();

        subTitle.getAnalytics().remove(analytic);
        analytic.setSubTitle(null);

        log.info("Delete analytic by id : {}", id);
        analyticRepository.delete(analytic);
    }

    @Override
    public ByteArrayInputStream exportToExcel() throws IOException {
        List<Analytic> analytics = analyticRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Analytics");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Cover Image", "SubTitle", "Embed Links"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Analytic analytic : analytics) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(analytic.getId());
                row.createCell(1).setCellValue(analytic.getName());
                row.createCell(2).setCellValue(analytic.getCoverImage());
                row.createCell(3).setCellValue(
                        analytic.getSubTitle() != null ? analytic.getSubTitle().getName() : ""
                );

                String links = analytic.getEmbedLinks().stream()
                        .map(EmbedLink::getEmbedLink)
                        .collect(Collectors.joining(", "));
                row.createCell(4).setCellValue(links);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
