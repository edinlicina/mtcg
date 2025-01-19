package at.fhtw.mtcg.service;

import at.fhtw.mtcg.entity.CardEntity;
import at.fhtw.mtcg.entity.DeckEntity;
import at.fhtw.mtcg.exceptions.*;
import at.fhtw.mtcg.repository.CardRepository;
import at.fhtw.mtcg.repository.DeckRepository;
import at.fhtw.mtcg.repository.TokenRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeckService {

    private final TokenRepository tokenRepository;
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;

    public DeckService() {
        tokenRepository = new TokenRepository();
        deckRepository = new DeckRepository();
        cardRepository = new CardRepository();
    }

    public List<String> getDeckForUser(String token) {
        String username = tokenRepository.getUsernameByToken(token);
        DeckEntity deck = deckRepository.getDeckByUsername(username);
        List<String> listOfCardsInDeck = new ArrayList<String>();
        if (deck == null) {
            throw new DeckNotFoundException();
        }
        listOfCardsInDeck.add(deck.getCard1());
        listOfCardsInDeck.add(deck.getCard2());
        listOfCardsInDeck.add(deck.getCard3());
        listOfCardsInDeck.add(deck.getCard4());
        return listOfCardsInDeck;
    }

    public void upsertDeck(List<String> cardList, String token) {
        if (cardList.size() != 4) {
            throw new NotFourCardsException();
        }
        String username = tokenRepository.getUsernameByToken(token);
        DeckEntity deckEntity = deckRepository.getDeckByUsername(username);
        List<String> cardsInDeck = new ArrayList<>();
        if (deckEntity != null) {
            cardsInDeck.add(deckEntity.getCard1());
            cardsInDeck.add(deckEntity.getCard2());
            cardsInDeck.add(deckEntity.getCard3());
            cardsInDeck.add(deckEntity.getCard4());
        }
        cardList.forEach(card -> {
            CardEntity cardEntity = cardRepository.getCardById(card);
            if (cardEntity == null) {
                throw new CardNotFoundException(card);
            }
            if (!username.equals(cardEntity.getUsername())) {
                throw new UserNotAuthorizedException(cardsInDeck.toString());
            }
        });
        Set<String> uniqueCardList = new HashSet<String>(cardList);
        if (uniqueCardList.size() < cardList.size()) {
            throw new DuplicatedCardIdException();
        }
        if (deckEntity == null) {
            deckRepository.createDeck(cardList.get(0), cardList.get(1), cardList.get(2), cardList.get(3), username);
        } else {
            deckRepository.updateDeck(cardList.get(0), cardList.get(1), cardList.get(2), cardList.get(3), username);
        }

    }

}
