package app.rest;

import app.dto.UserCredentialsDto;

public class AuthenticatorImpl implements Authenticator{

    private static final String LOGIN = "user";
    private static final String PASSWORD = "1234";

    //normalnie dane powinny pochodzic np. z bazy danych(mozna podpiac repozytorium implementujace metody do bazy danych)

    @Override
    public void authenticate(UserCredentialsDto userCredentialsDto, AuthenticationResultHandler authenticationResultHandler) {
        Runnable authenticationTask = createAuthenticationTask(userCredentialsDto, authenticationResultHandler);
        Thread authenticationThread = new Thread(authenticationTask);
        authenticationThread.setDaemon(true); // po zamknieciu aplikacji ten watek zostanie zamkniety
        authenticationThread.start();


    }

    private Runnable createAuthenticationTask(UserCredentialsDto userCredentialsDto, AuthenticationResultHandler authenticationResultHandler) {
        return () -> {
            try {
                Thread.sleep(2000);
                boolean bool = userCredentialsDto.getLogin().equals(LOGIN) && userCredentialsDto.getPassword().equals(PASSWORD);
                authenticationResultHandler.handle(bool);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}
