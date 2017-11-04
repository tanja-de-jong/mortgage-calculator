package model;

import java.util.Date;

public class Offerte {

    public static double NIET_VERLENGD = 0.0;
    public static double DATUM_NIET_INGEVULD = -1.0;
    public static double VERLENGING_NIET_MOGELIJK = -2.0;
    public static double PROVISIE_ONBEKEND = -3.0;
    public static double VERLENGING_ONBEKEND = -4.0;

    public Verstrekker verstrekker;

    private int standaardLooptijd;
    private Integer mogelijkeVerlenging;
    private boolean tussentijdseDalingMogelijk;
    private Double provisie;
    private boolean vasteRente;
    private boolean kostenBijRenteStijging;

    public Offerte() {}

    public Offerte(Verstrekker verstrekker, int standaardLooptijd, Integer mogelijkeVerlenging, boolean tussentijdseDalingMogelijk, Double provisie, boolean vasteRente, boolean kostenBijRenteStijging) {
        this.verstrekker = verstrekker;
        this.standaardLooptijd = standaardLooptijd;
        this.mogelijkeVerlenging = mogelijkeVerlenging;
        this.tussentijdseDalingMogelijk = tussentijdseDalingMogelijk;
        this.provisie = provisie;
        this.vasteRente = vasteRente;
        this.kostenBijRenteStijging = kostenBijRenteStijging;
    }

    public int getStandaardLooptijd() {
        return standaardLooptijd;
    }

    public Integer getMogelijkeVerlenging() {
        return mogelijkeVerlenging;
    }

    public boolean isTussentijdseDalingMogelijk() {
        return tussentijdseDalingMogelijk;
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

    public boolean isVerlengingMogelijk(Date hypotheekDatum, Date notarisDatum) {
        return mogelijkeVerlenging == null ? false : getBenodigdeVerlenging(hypotheekDatum, notarisDatum) <= mogelijkeVerlenging;
    }

    public double getBenodigdeVerlenging(Date hypotheekDatum, Date notarisDatum) {
        if (hypotheekDatum == null || notarisDatum == null) return 0;
        double aantalMaanden = (double) (notarisDatum.getTime() - hypotheekDatum.getTime()) / (24 * 60 * 60 * 1000) / 30;
        return aantalMaanden - standaardLooptijd;
    }

    public void setVerstrekker(Verstrekker verstrekker) {
        this.verstrekker = verstrekker;
    }

    public void setStandaardLooptijd(int standaardLooptijd) {
        this.standaardLooptijd = standaardLooptijd;
    }

    public void setMogelijkeVerlenging(Integer mogelijkeVerlenging) {
        this.mogelijkeVerlenging = mogelijkeVerlenging;
    }

    public void setTussentijdseDalingMogelijk(boolean tussentijdseDalingMogelijk) {
        this.tussentijdseDalingMogelijk = tussentijdseDalingMogelijk;
    }

    public void setProvisie(Double provisie) {
        this.provisie = provisie;
    }

    public void setVasteRente(boolean vasteRente) {
        this.vasteRente = vasteRente;
    }

    public void setKostenBijRenteStijging(boolean kostenBijRenteStijging) {
        this.kostenBijRenteStijging = kostenBijRenteStijging;
    }

    public double berekenKostenVerlenging(Date hypotheekDatum, Date notarisDatum, Double bedrag) {
        double result = NIET_VERLENGD;

        if (hypotheekDatum == null || notarisDatum == null) {
            result = DATUM_NIET_INGEVULD;
        } else if (mogelijkeVerlenging == null) {
            result = VERLENGING_ONBEKEND;
        } else {
            double maandenTeLang = getBenodigdeVerlenging(hypotheekDatum, notarisDatum);

            if (maandenTeLang <= 0) { return result; }

            if (maandenTeLang > mogelijkeVerlenging) {
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
