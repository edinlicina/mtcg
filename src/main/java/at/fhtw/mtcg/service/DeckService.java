package at.fhtw.mtcg.service;

import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.entity.DeckEntity;
import at.fhtw.mtcg.exceptions.NotFourCardsException;
import at.fhtw.mtcg.repository.DeckRepository;
import at.fhtw.mtcg.repository.TokenRepository;

import java.util.ArrayList;
import java.util.List;

public class DeckService {

    private final TokenRepository tokenRepository;
    private final DeckRepository deckRepository;

    public DeckService() {
        tokenRepository = new TokenRepository();
        deckRepository = new DeckRepository();
    }

    public List<CardDto> getDeckForUser(String token) {
        String username = tokenRepository.getUsernameByToken(token);
        DeckEntity deck = deckRepository.getDeckByUsername(username);
        List<CardDto> listOfCardsInDeck = new ArrayList<CardDto>();
        return listOfCardsInDeck;
    }

    public void upsertDeck(List<String> cardList) {
        if (cardList.size() != 4) {
            throw new NotFourCardsException();
        }

    }
}
