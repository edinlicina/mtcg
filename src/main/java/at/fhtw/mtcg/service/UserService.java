package at.fhtw.mtcg.service;

import at.fhtw.mtcg.dto.LoginUserDto;
import at.fhtw.mtcg.entity.UserEntity;
import at.fhtw.mtcg.repository.TokenRepository;
import at.fhtw.mtcg.repository.UserRepository;

import java.security.NoSuchAlgorithmException;

public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public UserService() {
        userRepository = new UserRepository();
        tokenRepository = new TokenRepository();
    }


    public String loginUser(LoginUserDto dto) throws NoSuchAlgorithmException {

        UserEntity userEntity = userRepository.getUserByUsername(dto.username);
        if (dto.password.equals(userEntity.getPassword())) {
            String token = tokenRepository.getTokenByUsername(dto.username);
            if (token == null) {

                token = tokenRepository.createToken( dto.username);
            }
            return token;
        }

        return null;
    }


}

