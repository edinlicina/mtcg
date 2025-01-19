package at.fhtw.mtcg.service;

import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.entity.CardEntity;
import at.fhtw.mtcg.entity.DeckEntity;
import at.fhtw.mtcg.exceptions.CardNotFoundException;
import at.fhtw.mtcg.exceptions.NotFourCardsException;
import at.fhtw.mtcg.exceptions.UserNotAuthorizedException;
import at.fhtw.mtcg.repository.CardRepository;
import at.fhtw.mtcg.repository.DeckRepository;
import at.fhtw.mtcg.repository.TokenRepository;

import java.util.ArrayList;
import java.util.List;

public class DeckService {

    private final TokenRepository tokenRepository;
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;

    public DeckService() {
        tokenRepository = new TokenRepository();
        deckRepository = new DeckRepository();
        cardRepository = new CardRepository();
    }

    public List<CardDto> getDeckForUser(String token) {
        String username = tokenRepository.getUsernameByToken(token);
        DeckEntity deck = deckRepository.getDeckByUsername(username);
        List<CardDto> listOfCardsInDeck = new ArrayList<CardDto>();
        return listOfCardsInDeck;
    }

    public void upsertDeck(List<String> cardList, String token) {
        if (cardList.size() != 4) {
            throw new NotFourCardsException();
        }
        String username = tokenRepository.getUsernameByToken(token);
        cardList.forEach(card -> {
            CardEntity cardEntity = cardRepository.getCardById(card);
            if (cardEntity == null) {
                throw new CardNotFoundException(card);
            }
            if (!username.equals(cardEntity.getUsername())) {
                throw new UserNotAuthorizedException();
            }
        });
        DeckEntity deckEntity = deckRepository.getDeckByUsername(username);
        if (deckEntity == null) {
            deckRepository.createDeck(cardList.get(0), cardList.get(1), cardList.get(2), cardList.get(3), username);
        } else {
            deckRepository.updateDeck(cardList.get(0), cardList.get(1), cardList.get(2), cardList.get(3), username);
        }
    }

}
