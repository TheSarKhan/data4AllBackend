//package org.example.dataprotal.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.dataprotal.model.analytics.Card;
//import org.example.dataprotal.model.analytics.SubContent;
//import org.example.dataprotal.repository.page.CardRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CardService {
//    private final CardRepository cardRepository;
//    private final CardMapper cardMapper;
//
//    public Card createCard(CardDto cardDto) {
//        Card card = cardMapper.dtoToEntity(cardDto);
//        return cardRepository.save(card);
//    }
//
//    public Card updateCard(Long id, CardDto cardDto) {
//        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
//        Card saveCard = cardMapper.updateCardFromDto(cardDto, card);
//        return cardRepository.save(saveCard);
//    }
//
//    public List<Card>getCardsByPageName(String pageName) {
//        return cardRepository.findByPageName(pageName);
//    }
//
//    public Card getCardById(Long id) {
//       Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
//       card.setViews(card.getViews() + 1);
//       cardRepository.save(card);
//        return card;
//    }
//
//    public Card addSubContent(Long id, SubContent subContent) {
//        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
//        card.getSubContents().add(subContent);
//        return cardRepository.save(card);
//    }
//
//    public Card likeCard(Long id) {
//        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
//        card.setLikes(card.getLikes() + 1);
//        cardRepository.save(card);
//        return card;
//    }
//}
