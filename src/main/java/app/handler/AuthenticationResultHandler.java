package app.handler;

import app.dto.OperatorAuthenticationResultDto;

@FunctionalInterface
public interface AuthenticationResultHandler {

    void handle(OperatorAuthenticationResultDto operatorAuthenticationResultDto);

}
