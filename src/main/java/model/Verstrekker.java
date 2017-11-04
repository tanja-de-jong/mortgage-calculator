package model;

import org.json.JSONObject;

import java.util.*;

/**
 * Created by Tanja on 31-8-2017.
 */
public class Verstrekker {

    public static String[] vergelijkingsOnderwerpen = new String[]{"Restbedrag", "Aflossing", "Rente", "Bruto"};

    private String naam;
    private String laatstBijgewerkt;
    private Map<Double, Double> renteGroepen;
    private String actueleRenteURL;
    private Offerte offerte;
    private Double extraKorting;
    private Double maximaalAflosbaar;
    private Map<String, Double> maandelijkseKortingen;
    private List<String> opmerkingen;

    private List<Double> restBedragen = new ArrayList<Double>();
    private List<Double> aflossingen = new ArrayList<Double>();
    private List<Double> rentes = new ArrayList<Double>();
    private List<Double> brutos = new ArrayList<Double>();

    private Map<String, List<Double>> data = new HashMap<String, List<Double>>();
    public Double totaleRente = null;
    public Double totaleProvisie = 0.0;

    public Verstrekker() {
        this.data.put("Restbedrag", restBedragen);
        this.data.put("Aflossing", aflossingen);
        this.data.put("Rente", rentes);
        this.data.put("Bruto", brutos);
    }

    public String getNaam() {
        return naam;
    }

    public Offerte getOfferte() {
        return offerte;
    }

    public Double getTotaleRente() {
        return totaleRente;
    }

    public Double berekenRente(double marktWaarde) {
        double result = -1;

        Set<Double> marktWaardeRente = renteGroepen.keySet();
        List<Double> marktWaardeRenteGesorteerd = new ArrayList<Double>(marktWaardeRente);
        Collections.sort(marktWaardeRenteGesorteerd);

        for(int i = marktWaardeRenteGesorteerd.size() - 1; i >= 0 && result == -1; i--) {
            double huidigeMarktWaardeRente = marktWaardeRenteGesorteerd.get(i);
            if (huidigeMarktWaardeRente < marktWaarde) result = renteGroepen.get(huidigeMarktWaardeRente);
        }

        return result;
    }

    public double berekenMaandlast(double startBedrag) {
        double maandrente = berekenRente(100) / 100 / 12;
        double nrPeriodes = 360;
        return (maandrente / (1 - (Math.pow((1 + maandrente), -nrPeriodes)))) * startBedrag;
    }

    public double berekenAflossing(boolean annuitair, double renteBedrag, double startBedrag) {
        return annuitair ? berekenMaandlast(startBedrag) - renteBedrag : startBedrag / 360;
    }

    public void berekenBedragen(boolean annuitair, Double woningWaarde, Double startBedrag, Double extraAflossing, Date hypotheekDatum, Date notarisDatum) {
        reset();

        if (startBedrag != null) {
            Double[] extraAflossingArray = new Double[30];
            for (int i = 0; i < 30; i++) {
                extraAflossingArray[i] = extraAflossing == null ? 0 : extraAflossing;
            }
            berekenBedragen(annuitair, woningWaarde, startBedrag, extraAflossingArray, hypotheekDatum, notarisDatum);
        }
    }

    public void berekenBedragen(boolean annuitair, Double woningWaarde, Double startBedrag, Double[] extraAflossing, Date hypotheekDatum, Date notarisDatum) {
        reset();

        if (startBedrag != null) {
            Double restBedrag = startBedrag;
            Double prevMaandRentePercentage = 0.0;

            List<Double> maandRentePercentages = new ArrayList<>();

            if (naam.equals("ABN AMRO Won. Duurz.")) {
                System.out.println(naam);
                System.out.println("Jaar; Maand; Rente; Aflossing; RestBedrag; Rentepercentage; Waarde");
            }

            for (int i = 0; restBedrag >= 0; i++) {
                Double maandRentePercentage = berekenRente(restBedrag / woningWaarde * 100) / 100 / 12;
                /*if (naam.equals("ING") && !maandRentePercentage.equals(prevMaandRentePercentage)) {

                    System.out.println(jaren + "  jaren en " + maanden + " maanden (totaal " + (i+1) + " maanden) voor " + restBedrag);
                }*/
                maandRentePercentages.add(maandRentePercentage * 100 * 12);
                if (offerte != null && offerte.isVerlengingMogelijk(hypotheekDatum, notarisDatum) && offerte.getBenodigdeVerlenging(hypotheekDatum, notarisDatum) > 0 && offerte.isVasteRente()) {
                    //maandRentePercentage += offerte.getProvisie();
                    totaleProvisie += offerte.getProvisie() / 12 * restBedrag;
                }
                Double maandRenteBedrag = maandRentePercentage * restBedrag;
                totaleRente = totaleRente == null ? maandRenteBedrag : totaleRente + maandRenteBedrag;
                Double aflossing = berekenAflossing(annuitair, maandRenteBedrag, startBedrag);
                Double extraMaandAflossing = extraAflossing[0];//i / 12];

                restBedragen.add(restBedrag);
                aflossingen.add(aflossing);
                rentes.add(maandRenteBedrag);
                brutos.add(aflossing + maandRenteBedrag);

                restBedrag -= aflossing + extraMaandAflossing / 12;
                prevMaandRentePercentage = maandRentePercentage;
                if (naam.equals("ABN AMRO Won. Duurz.")) {
                    int maanden = (i % 12) + 1;
                    int jaren = ((i + 1) - maanden) / 12;
                    System.out.println(jaren + "; " + maanden + "; " + maandRenteBedrag + "; " + aflossing + "; " + restBedrag + "; " + (maandRentePercentage * 100 * 12) + "; " + (restBedrag / woningWaarde * 100));
                }
            }
            totaleRente -= extraKorting;
            if (naam.equals("ABN AMRO Won. Duurz.")) {
                System.out.println();
                System.out.println();
            }
        }
    }

    public void berekenProvisie(Double restBedrag) {
        if (offerte.isVasteRente()) {
            totaleProvisie = offerte.getProvisie() * restBedrag;
        }
    }

    public void reset() {
        totaleRente = null;
        restBedragen.clear();
        aflossingen.clear();
        rentes.clear();
        brutos.clear();
        totaleProvisie = 0.0;
    }

    public void setOfferte(Offerte offerte) {
        this.offerte = offerte;
    }

    public static String[] getVergelijkingsOnderwerpen() {
        return vergelijkingsOnderwerpen;
    }

    public Map<Double, Double> getRenteGroepen() {
        return renteGroepen;
    }

    public String getActueleRenteURL() {
        return actueleRenteURL;
    }

    public Double getExtraKorting() {
        return extraKorting;
    }

    public Double getMaximaalAflosbaar() {
        return maximaalAflosbaar;
    }

    public Map<String, Double> getMaandelijkseKortingen() {
        return maandelijkseKortingen;
    }

    public List<String> getOpmerkingen() {
        return opmerkingen;
    }

    public List<Double> getRestBedragen() {
        return restBedragen;
    }

    public List<Double> getAflossingen() {
        return aflossingen;
    }

    public List<Double> getRentes() {
        return rentes;
    }

    public List<Double> getBrutos() {
        return brutos;
    }

    public Map<String, List<Double>> getData() {
        return data;
    }

    public Double getTotaleProvisie() {
        return totaleProvisie;
    }

    public static void setVergelijkingsOnderwerpen(String[] vergelijkingsOnderwerpen) {
        Verstrekker.vergelijkingsOnderwerpen = vergelijkingsOnderwerpen;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setRenteGroepen(Map<Double, Double> renteGroepen) {
        this.renteGroepen = renteGroepen;
    }

    public void setActueleRenteURL(String actueleRenteURL) {
        this.actueleRenteURL = actueleRenteURL;
    }

    public void setExtraKorting(Double extraKorting) {
        this.extraKorting = extraKorting;
    }

    public void setMaximaalAflosbaar(Double maximaalAflosbaar) {
        this.maximaalAflosbaar = maximaalAflosbaar;
    }

    public void setMaandelijkseKortingen(Map<String, Double> maandelijkseKortingen) {
        this.maandelijkseKortingen = maandelijkseKortingen;
    }

    public void setOpmerkingen(List<String> opmerkingen) {
        this.opmerkingen = opmerkingen;
    }

    public void setRestBedragen(List<Double> restBedragen) {
        this.restBedragen = restBedragen;
    }

    public void setAflossingen(List<Double> aflossingen) {
        this.aflossingen = aflossingen;
    }

    public void setRentes(List<Double> rentes) {
        this.rentes = rentes;
    }

    public void setBrutos(List<Double> brutos) {
        this.brutos = brutos;
    }

    public void setData(Map<String, List<Double>> data) {
        this.data = data;
    }

    public void setTotaleRente(Double totaleRente) {
        this.totaleRente = totaleRente;
    }

    public void setTotaleProvisie(Double totaleProvisie) {
        this.totaleProvisie = totaleProvisie;
    }

    public String getLaatstBijgewerkt() {
        return laatstBijgewerkt;
    }

    public void setLaatstBijgewerkt(String laatst_bijgewerkt) {
        this.laatstBijgewerkt = laatst_bijgewerkt;
    }

    public JSONObject toJSON() {
        JSONObject verstrekkerJSON = new JSONObject();
        verstrekkerJSON.put("naam", naam);
        verstrekkerJSON.putOpt("laatstBijgewerkt", laatstBijgewerkt);
        verstrekkerJSON.putOpt("rente_groepen", renteGroepen);
        verstrekkerJSON.putOpt("actuele_rente_url", actueleRenteURL);
        JSONObject offerteJSON = null;
        if (offerte != null) {
            offerteJSON = new JSONObject();
            offerteJSON.putOpt("standaard_looptijd", offerte.getStandaardLooptijd());
            offerteJSON.putOpt("mogelijke_verlenging", offerte.getMogelijkeVerlenging());
            offerteJSON.putOpt("tussentijdse_daling", offerte.isTussentijdseDalingMogelijk());
            offerteJSON.putOpt("provisie", offerte.getProvisie());
            offerteJSON.putOpt("vaste_rente", offerte.isVasteRente());
            offerteJSON.putOpt("kosten_bij_rentestijging", offerte.isKostenBijRenteStijging());
        }
        verstrekkerJSON.putOpt("offerte", offerteJSON);
        verstrekkerJSON.putOpt("extra_korting", extraKorting);
        verstrekkerJSON.putOpt("maximaal_aflosbaar", maximaalAflosbaar);
        verstrekkerJSON.putOpt("maandelijkse_kortingen", maandelijkseKortingen);
        verstrekkerJSON.putOpt("opmerkingen", opmerkingen);

        return verstrekkerJSON;
    }

}
