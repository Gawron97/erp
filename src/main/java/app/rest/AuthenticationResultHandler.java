package app.rest;

import app.dto.OperatorAuthenticationResultDto;

@FunctionalInterface
public interface AuthenticationResultHandler {

    void handle(OperatorAuthenticationResultDto operatorAuthenticationResultDto);

}
