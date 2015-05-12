/**
 * Created by glapul on 12.05.15.
 */
public class IBAN {

    private final String IBAN;

    IBAN(String iban) {
        if(!validate(iban))
            throw new IllegalArgumentException();

        this.IBAN = iban;
    }

    private boolean validate(String iban) {
        return true;
    }

    public String getIBAN() {
        return IBAN;
    }
}
