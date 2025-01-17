package at.fhtw.mtcg.service;

import at.fhtw.mtcg.dto.CreateUserDto;
import at.fhtw.mtcg.dto.LoginUserDto;
import at.fhtw.mtcg.dto.UpdateUserDto;
import at.fhtw.mtcg.entity.UserEntity;
import at.fhtw.mtcg.exceptions.InvalidPasswordException;
import at.fhtw.mtcg.exceptions.NotUniqueException;
import at.fhtw.mtcg.exceptions.UserNotAuthorizedException;
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


    public String loginUser(LoginUserDto dto) throws InvalidPasswordException, NoSuchAlgorithmException {
        UserEntity userEntity = userRepository.getUserByUsername(dto.username);
        boolean isPasswordCorrect = dto.password.equals(userEntity.getPassword());
        if (isPasswordCorrect) {
            String token = tokenRepository.getTokenByUsername(dto.username);
            if (token == null) {
                token = tokenRepository.createToken(dto.username);
            }
            return token;
        } else {
            throw new InvalidPasswordException();
        }
    }

    public void registerUser(CreateUserDto dto) throws NotUniqueException {
        userRepository.createUser(dto.username, dto.password);
    }

    public boolean isValidToken(String token) {
        return tokenRepository.isValidToken(token);
    }

    public void updateUser(UpdateUserDto dto, String userToUpdate, String token) {
        String currentUser = tokenRepository.getUsernameByToken(token);
        if (!currentUser.equals(userToUpdate)) {
            throw new UserNotAuthorizedException();
        }
        userRepository.updateUser(dto.name, dto.image, dto.bio, currentUser);
    }

    public UserEntity getDataFromUser(String token, String userToGet) {
        String username = tokenRepository.getUsernameByToken(token);
        if (!username.equals(userToGet)) {
            throw new UserNotAuthorizedException();
        }
        return userRepository.getUserdata(username);
    }
}

