package at.fhtw.mtcg.service;

import at.fhtw.mtcg.entity.PackageEntity;
import at.fhtw.mtcg.entity.UserEntity;
import at.fhtw.mtcg.exceptions.NoMoneyException;
import at.fhtw.mtcg.exceptions.NoPackagesException;
import at.fhtw.mtcg.repository.CardRepository;
import at.fhtw.mtcg.repository.PackageRepository;
import at.fhtw.mtcg.repository.TokenRepository;
import at.fhtw.mtcg.repository.UserRepository;

public class TransactionService {


    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final TokenRepository tokenRepository;

    public TransactionService() {
        packageRepository = new PackageRepository();
        userRepository = new UserRepository();
        cardRepository = new CardRepository();
        tokenRepository = new TokenRepository();
    }

    public void acquirePackage(String token) {
        //1. Put package(card) in Stack of Buyer
        PackageEntity packageEntity = packageRepository.getPackage();
        if (packageEntity == null) {
            throw new NoPackagesException();
        }

        String username = tokenRepository.getUsernameByToken(token);
        UserEntity userEntity = userRepository.getUserByUsername(username);

        int userCoins = userEntity.getCoins();
        if (userCoins <= 4) {
            throw new NoMoneyException();
        }
        //2. Take package out of PackagesDB
        packageRepository.deletePackage(packageEntity.getId());
        cardRepository.updateUsername(username, packageEntity.getCard1());
        cardRepository.updateUsername(username, packageEntity.getCard2());
        cardRepository.updateUsername(username, packageEntity.getCard3());
        cardRepository.updateUsername(username, packageEntity.getCard4());
        cardRepository.updateUsername(username, packageEntity.getCard5());
        //3. Deduct 5 coins per package from Buyer
        int afterTransactionUserCoins = userCoins - 5;

        userRepository.updateUserCoins(afterTransactionUserCoins, username);

    }
}


