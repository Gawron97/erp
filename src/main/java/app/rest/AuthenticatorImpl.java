package app.rest;

import app.dto.OperatorLoginCredentialsDto;
import app.dto.OperatorRegisterCredentialsDto;
import app.handler.AuthenticationResultHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class AuthenticatorImpl implements Authenticator{

    private static final String AUTHENTICATION_LOGIN_URL = "http://localhost:8080/verify_operator_login_credentials";
    private static final String AUTHENTICATION_REGISTER_URL = "http://localhost:8080/verify_operator_register_credentials";
    private final RestTemplate restTemplate;

    public AuthenticatorImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void authenticateLogin(OperatorLoginCredentialsDto operatorLoginCredentialsDto, AuthenticationResultHandler authenticationResultHandler) {
        Runnable authenticationTask = () -> {
            processLoginAuthentication(operatorLoginCredentialsDto, authenticationResultHandler);
        };
        Thread authenticationThread = new Thread(authenticationTask);
        authenticationThread.setDaemon(true); // po zamknieciu aplikacji ten watek zostanie zamkniety
        authenticationThread.start();

    }

    private void processLoginAuthentication(OperatorLoginCredentialsDto operatorLoginCredentialsDto, AuthenticationResultHandler handler) {

        ResponseEntity<OperatorLoginCredentialsDto> operatorResponse = restTemplate.postForEntity(AUTHENTICATION_LOGIN_URL,
                operatorLoginCredentialsDto, OperatorLoginCredentialsDto.class);

        handler.handle(operatorResponse.getBody().getAuthenticated());
    }

    @Override
    public void authenticateRegister(OperatorRegisterCredentialsDto dto, AuthenticationResultHandler handler) {
        Thread thread = new Thread(() -> processRegisterAuthentication(dto, handler));
        thread.setDaemon(true);
        thread.start();
    }

    private void processRegisterAuthentication(OperatorRegisterCredentialsDto dto, AuthenticationResultHandler handler) {
        ResponseEntity<OperatorRegisterCredentialsDto> response =
                restTemplate.postForEntity(AUTHENTICATION_REGISTER_URL, dto, OperatorRegisterCredentialsDto.class);

        handler.handle(response.getBody().getAuthenticated());


    }
}
