package at.fhtw.mtcg.service;

import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.exceptions.DuplicatedCardIdException;
import at.fhtw.mtcg.exceptions.NegativeDamageException;
import at.fhtw.mtcg.exceptions.NotFiveCardsException;
import at.fhtw.mtcg.repository.CardRepository;
import at.fhtw.mtcg.repository.PackageRepository;

import java.util.List;

public class PackageService {

    private final PackageRepository packageRepository;
    private final CardRepository cardRepository;

    public PackageService() {
        cardRepository = new CardRepository();
        packageRepository = new PackageRepository();
    }

    public void createPackage(List<CardDto> dto) throws NotFiveCardsException, DuplicatedCardIdException, NegativeDamageException {
        if (dto.size() != 5) {
            throw new NotFiveCardsException();
        }

        List<CardDto> cardsWithNegativeDamage = dto.stream().filter(card -> card.damage < 0).toList();
        if (!cardsWithNegativeDamage.isEmpty()) {
            throw new NegativeDamageException();
        }
        dto.forEach(card -> {
            cardRepository.createCard(card.id, card.name, card.damage);
        });
        packageRepository.createPackage(dto.get(0).id, dto.get(1).id, dto.get(2).id, dto.get(3).id, dto.get(4).id);

    }
}
