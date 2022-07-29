package app.rest;

import app.dto.OperatorCredentialsDto;

public interface Authenticator {

    void authenticate(OperatorCredentialsDto operatorCredentialsDto, AuthenticationResultHandler authenticationResultHandler);

}
