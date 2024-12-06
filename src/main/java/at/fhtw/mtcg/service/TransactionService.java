package at.fhtw.mtcg.service;

import at.fhtw.mtcg.entity.PackageEntity;
import at.fhtw.mtcg.entity.UserEntity;
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
        //TODO test if possible without packages
        String username = tokenRepository.getUsernameByToken(token);
        cardRepository.updateUsername(username, packageEntity.getCard1());
        cardRepository.updateUsername(username, packageEntity.getCard2());
        cardRepository.updateUsername(username, packageEntity.getCard3());
        cardRepository.updateUsername(username, packageEntity.getCard4());
        cardRepository.updateUsername(username, packageEntity.getCard5());
        //2. Take package out of PackagesDB
        packageRepository.deletePackage(packageEntity.getId());
        //3. Deduct 5 coins per package from Buyer
        UserEntity userEntity = userRepository.getUserByUsername(username);
        //TODO Test for negative account
        int userCoins = userEntity.getCoins();
        int afterTransactionUserCoins = userCoins - 5;
        userRepository.updateUserCoins(afterTransactionUserCoins, username);

    }
}


