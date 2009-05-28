package namnam.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

/**
 * represents a price
 * @author fake
 */
public class Preis {

    private Integer cents;
    private Currency currency = Currency.getInstance(Locale.GERMANY);

    public Preis(Integer value) {
        this.cents = value;
    }

    public Preis(Integer value, Currency currency) {
        this.cents = value;
        this.currency = currency;
    }

    public Integer getCents() {
        return cents;
    }

    public void setCents(Integer cents) {
        this.cents = cents;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        DecimalFormat nf = new DecimalFormat();
        DecimalFormatSymbols decf = new DecimalFormatSymbols(Locale.GERMAN);
        nf.setDecimalFormatSymbols(decf);
        nf.setMinimumFractionDigits(currency.getDefaultFractionDigits());
        return nf.format(cents/100.0);
    }


}
