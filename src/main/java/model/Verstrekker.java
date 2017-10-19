package model;

import java.util.*;

/**
 * Created by Tanja on 31-8-2017.
 */
public class Verstrekker {

    public static String[] vergelijkingsOnderwerpen = new String[]{"Restbedrag", "Aflossing", "Rente", "Bruto"};

    public String name;
    public Map<Double, Double> rente;
    private Offerte offerte;
    private Double extraKorting = null;

    public List<Double> restBedragen = new ArrayList<Double>();
    public List<Double> aflossingen = new ArrayList<Double>();
    public List<Double> rentes = new ArrayList<Double>();
    public List<Double> brutos = new ArrayList<Double>();

    public Map<String, List<Double>> data = new HashMap<String, List<Double>>();
    public Double totaleRente = null;
    public Double totaleProvisie = 0.0;

    public Verstrekker(String name, Map<Double, Double> renteGroepen, Double extraKorting) {
        this.name = name;
        this.rente = renteGroepen;
        this.data.put("Restbedrag", restBedragen);
        this.data.put("Aflossing", aflossingen);
        this.data.put("Rente", rentes);
        this.data.put("Bruto", brutos);
        this.extraKorting = extraKorting;
    }

    public String getName() {
        return name;
    }

    public Offerte getOfferte() {
        return offerte;
    }

    public Double getTotaleRente() {
        return totaleRente;
    }

    public Double berekenRente(double marktWaarde) {
        double result = -1;

        Set<Double> marktWaardeRente = rente.keySet();
        List<Double> marktWaardeRenteGesorteerd = new ArrayList<Double>(marktWaardeRente);
        Collections.sort(marktWaardeRenteGesorteerd);

        for(int i = marktWaardeRenteGesorteerd.size() - 1; i >= 0 && result == -1; i--) {
            double huidigeMarktWaardeRente = marktWaardeRenteGesorteerd.get(i);
            if (huidigeMarktWaardeRente < marktWaarde) result = rente.get(huidigeMarktWaardeRente);
        }

        return result;
    }

    public double berekenMaandlast(double startBedrag) {
        double maandrente = berekenRente(1) / 100 / 12;
        double nrPeriodes = 360;
        return (maandrente / (1 - (Math.pow((1 + maandrente), -nrPeriodes)))) * startBedrag;
    }

    public double berekenAflossing(boolean annuitair, double renteBedrag, double startBedrag) {
        return annuitair ? berekenMaandlast(startBedrag) - renteBedrag : startBedrag / 360;
    }

    public void berekenBedragen(boolean annuitair, Double startBedrag, Double extraAflossing) {
        reset();

        if (startBedrag != null) {
            Double[] extraAflossingArray = new Double[30];
            for (int i = 0; i < 30; i++) {
                extraAflossingArray[i] = extraAflossing == null ? 0 : extraAflossing;
            }
            berekenBedragen(annuitair, startBedrag, extraAflossingArray);
        }
    }

    public void berekenBedragen(boolean annuitair, Double startBedrag, Double[] extraAflossing) {
        reset();

        if (startBedrag != null) {
            Double restBedrag = startBedrag;

            for (int i = 0; restBedrag >= 0; i++) {
                Double maandRentePercentage = berekenRente(restBedrag / startBedrag) / 100 / 12;
                if (offerte != null && offerte.isVerlengingMogelijk() && offerte.getVerlenging() > 0 && offerte.isVasteRente()) {
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
            }

            totaleRente -= extraKorting;
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
    }

    public void setOfferte(Offerte offerte) {
        this.offerte = offerte;
    }
}
