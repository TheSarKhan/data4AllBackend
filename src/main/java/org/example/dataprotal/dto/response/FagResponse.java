package org.example.dataprotal.dto.response;

import java.util.Map;

public record FagResponse(String faqHeader, Map<String, Map<String, String>> headersSubHeadersAndTheirContentMap) {
}
