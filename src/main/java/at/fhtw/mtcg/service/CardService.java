package at.fhtw.mtcg.service;

import at.fhtw.mtcg.entity.CardEntity;
import at.fhtw.mtcg.repository.CardRepository;
import at.fhtw.mtcg.repository.TokenRepository;

import java.util.List;

public class CardService {
    private final CardRepository cardRepository;

    private final TokenRepository tokenRepository;

    public CardService() {
        cardRepository = new CardRepository();
        tokenRepository = new TokenRepository();

    }

    public List<CardEntity> getCardsForUser(String token) {
        String username = tokenRepository.getUsernameByToken(token);
        return cardRepository.getCardsForUser(username);
    }
}
