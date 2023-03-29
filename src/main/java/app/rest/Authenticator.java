package app.rest;

import app.dto.OperatorLoginCredentialsDto;
import app.dto.OperatorRegisterCredentialsDto;
import app.handler.AuthenticationResultHandler;
import app.handler.ProcessFinishedHandler;

public interface Authenticator {

    void authenticateLogin(OperatorLoginCredentialsDto operatorLoginCredentialsDto, ProcessFinishedHandler authenticationResultHandler);


    void authenticateRegister(OperatorRegisterCredentialsDto dto, ProcessFinishedHandler authenticationResultHandler);

}
