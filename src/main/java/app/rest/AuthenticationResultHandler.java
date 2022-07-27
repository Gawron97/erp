package app.rest;

@FunctionalInterface
public interface AuthenticationResultHandler {

    void handle(boolean authenticationResult);

}
