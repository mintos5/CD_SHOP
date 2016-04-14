package sk.stuba.fiit.michal.nikolas.cd_shop.exception;

/**
 * Created by Nikolas on 14.4.2016.
 */
public class ApiException extends Exception {

    private int error_code;

    public ApiException(int error_code) {
        this.error_code = error_code;
    }
}
