package app.rest;

import app.dto.OperatorCredentialsDto;
import app.handler.AuthenticationResultHandler;

public interface Authenticator {

    void authenticate(OperatorCredentialsDto operatorCredentialsDto, AuthenticationResultHandler authenticationResultHandler);

}
