package model;

import java.util.Calendar;
import java.util.Date;

public class Offerte {

    public static double NIET_VERLENGD = 0.0;
    public static double DATUM_NIET_INGEVULD = -1.0;
    public static double VERLENGING_NIET_MOGELIJK = -2.0;
    public static double PROVISIE_ONBEKEND = -3.0;
    public static double VERLENGING_ONBEKEND = -4.0;

    public Verstrekker verstrekker;

    private int looptijd;
    private Integer verlenging;
    private boolean tussentijdseDaling;
    private Double provisie;
    private boolean vasteRente;
    private boolean kostenBijRenteStijging;

    public Offerte(Verstrekker verstrekker, int looptijd, Integer verlenging, boolean tussentijdseDaling, Double provisie, boolean vasteRente, boolean kostenBijRenteStijging) {
        this.verstrekker = verstrekker;
        this.looptijd = looptijd;
        this.verlenging = verlenging;
        this.tussentijdseDaling = tussentijdseDaling;
        this.provisie = provisie;
        this.vasteRente = vasteRente;
        this.kostenBijRenteStijging = kostenBijRenteStijging;
    }

    public int getLooptijd() {
        return looptijd;
    }

    public Integer getVerlenging() {
        return verlenging;
    }

    public boolean isTussentijdseDaling() {
        return tussentijdseDaling;
    }

    public Double getProvisie() {
        return provisie;
    }

    public boolean isVasteRente() {
        return vasteRente;
    }

    public boolean isKostenBijRenteStijging() {
        return kostenBijRenteStijging;
    }

    public boolean isVerlengingMogelijk() {
        return verlenging == null ? false : getVerlenging() <= verlenging;
    }

    public double getVerlenging(Date hypotheekDatum, Date notarisDatum) {
        double aantalMaanden = (double) (notarisDatum.getTime() - hypotheekDatum.getTime()) / (24 * 60 * 60 * 1000) / 30;
        return aantalMaanden - looptijd;
    }

    public double berekenKostenVerlenging(Date hypotheekDatum, Date notarisDatum, Double bedrag) {
        double result = NIET_VERLENGD;

        if (hypotheekDatum == null || notarisDatum == null) {
            result = DATUM_NIET_INGEVULD;
        } else if (verlenging == null) {
            result = VERLENGING_ONBEKEND;
        } else {
            double maandenTeLang = getVerlenging(hypotheekDatum, notarisDatum);

            if (maandenTeLang <= 0) { return result; }

            if (maandenTeLang > verlenging) {
                result = VERLENGING_NIET_MOGELIJK;
            } else if (provisie == null) { // TODO: 14/10/2017
                result = PROVISIE_ONBEKEND;
            } else if (maandenTeLang > 0) {
                if (isVasteRente()) {
                    result = verstrekker.totaleProvisie;
                } else {
                    double prijsPerMaand = getProvisie() * bedrag;
                    result = prijsPerMaand * maandenTeLang;
                }
            }

        }

        return result;
    }
}
