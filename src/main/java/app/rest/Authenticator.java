package app.rest;

import app.dto.OperatorLoginCredentialsDto;
import app.dto.OperatorRegisterCredentialsDto;
import app.handler.AuthenticationResultHandler;

public interface Authenticator {

    void authenticateLogin(OperatorLoginCredentialsDto operatorLoginCredentialsDto, AuthenticationResultHandler authenticationResultHandler);


    void authenticateRegister(OperatorRegisterCredentialsDto dto, AuthenticationResultHandler authenticationResultHandler);

}
