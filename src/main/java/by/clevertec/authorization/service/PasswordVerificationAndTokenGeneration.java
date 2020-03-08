package by.clevertec.authorization.service;

import by.clevertec.authorization.config.JwtTokenUtil;
import by.clevertec.authorization.repository.UserRepository;
import by.clevertec.authorization.dto.AuthorizationResponseDto;
import by.clevertec.authorization.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class PasswordVerificationAndTokenGeneration {

    private String jwtToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    @Autowired
    public PasswordVerificationAndTokenGeneration(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    public ResponseEntity<AuthorizationResponseDto> checkLogAndPass (String authorization){
        //Remove the first part of the line
        int startNumLogin = 6;
        authorization = authorization.substring(startNumLogin);
        //Decode the second part of a string into an array of character codes
        byte [] barr = Base64.getDecoder().decode(authorization);
        //Convert an array of character codes to a string
        String log_pas = new String(barr);
        //Break the line into two parts: before ":" and after
        String[] login = log_pas.split(":");

        //Search encoded password for the login in DB
        List<UserApp> userApps = userRepository.findByLogin(login[0]);
        String passwordEncInDb = userApps.get(0).getPassword();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //Return greeting with login and token to client

        if (encoder.matches(login[1],passwordEncInDb)) {
            jwtToken = jwtTokenUtil.generateToken(login[0]);
            //Add token in DB
            UserApp userApp = userRepository.findById(login[0]).orElseThrow(IllegalArgumentException::new);
            userApp.setToken(jwtToken);
            userRepository.save(userApp);
            return ResponseEntity.ok().body(new AuthorizationResponseDto(login[0], jwtToken));
        }
        else {
           // return "password are wrong";
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
