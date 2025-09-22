package org.example.dataprotal.service.integration;

import org.example.dataprotal.dto.response.integration.*;

import java.util.List;

public interface AnalyticService {

    List<SectorResponse> getSectors();

    List<ModelResponse> getModels(String sectorSlug);

    List<CategoryResponse> getCategories(String sectorSlug, String modelSlug);

    CategoryReportResponse getCategoryReportInfo(String sectorSlug, String modelSlug, String categorySlug);

    AnalyzedDataResponse getData(String sectorSlug, String modelSlug, String categorySlug,
                                 Integer startYear, Integer endYear);

    ModelReportResponse getModelReportInfo(String sectorSlug, String modelSlug);

    byte[] downloadModelReport(String sectorSlug, String modelSlug);

    byte[] downloadCategoryReport(String sectorSlug, String modelSlug, String categorySlug);
}
