package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.CardDto;
import org.example.dataprotal.model.page.Card;
import org.example.dataprotal.repository.page.CardRepository;
import org.example.dataprotal.service.CardService;
import org.example.dataprotal.service.FileService;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final FileService fileService;

    @GetMapping
    @Operation(description = "Səhifə adı ilə cardları almaq üçün endpoint")
    public ResponseEntity<List<Card>>getCardsByPageName(@RequestParam String pageName) {
        return ResponseEntity.ok(cardService.getCardsByPageName(pageName));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @GetMapping("/{id}/export-word")
    public ResponseEntity<byte[]> getWord(@PathVariable Long id) {
        Card card = cardRepository.findById(id).orElseThrow(()->new RuntimeException("Card not found"));
        byte [] docBytes = fileService.generateWordFile(card);
        HttpHeaders headers = new HttpHeaders();
        String safeFileName=card.getTitle().replaceAll("[\\\\/:*?\"<>|]", "")
                .replace("–", "-")
                .replaceAll("\\s+", "_")
                .trim();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(safeFileName +".docx").build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(docBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Card> likeCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.likeCard(id));
    }

    @PostMapping
    @Operation(description = "Yeni cardlar əlavə etmək üçün endpoint")
    public ResponseEntity<Card>createCard(@RequestBody CardDto cardDto) {
        final var createdCard = cardService.createCard(cardDto);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{Id}").build(createdCard.getId());
        return ResponseEntity.created(location).body(createdCard);
    }

    @PutMapping("/{id}")
    @Operation(description = "Cardlar güncəlləmək üçün endpoint")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody CardDto cardDto) {
        final var card = cardService.updateCard(id,cardDto);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{Id}").build(card.getId());
        return ResponseEntity.created(location).body(card);
    }

}
