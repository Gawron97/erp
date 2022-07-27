package app.rest;

import app.dto.UserCredentialsDto;

public interface Authenticator {

    void authenticate(UserCredentialsDto userCredentialsDto, AuthenticationResultHandler authenticationResultHandler);

}
