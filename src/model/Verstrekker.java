package model;

import java.util.*;

/**
 * Created by Tanja on 31-8-2017.
 */
public abstract class Verstrekker {

    public String name;
    public Map<Double, Double> rente;

    public List<Double> restBedragen = new ArrayList<Double>();
    public List<Double> aflossingen = new ArrayList<Double>();
    public List<Double> rentes = new ArrayList<Double>();
    public List<Double> brutos = new ArrayList<Double>();

    public Map<String, List<Double>> data = new HashMap<String, List<Double>>();
    public double totaleRente = 0;
    private double aflossingsPercentage;

    // >0.9 -> 2.45
    // >67.5 -> 2.15
    // >0 -> 1.75

    public Verstrekker(String name, Map<Double, Double> renteGroepen) {
        this.name = name;
        this.rente = renteGroepen;
        this.data.put("Restbedrag", restBedragen);
        this.data.put("Aflossing", aflossingen);
        this.data.put("Rente", rentes);
        this.data.put("Bruto", brutos);
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

    public abstract double berekenAflossing(double renteBedrag, double startBedrag);

    public void berekenBedragen(double startBedrag, double extraAflossing) {
        double[] extraAflossingArray = new double[30];
        for (int i=0; i<30; i++) {
            extraAflossingArray[i] = extraAflossing;
        }
        berekenBedragen(startBedrag, extraAflossingArray);
    }

    public void berekenBedragen(double startBedrag, double[] extraAflossing) {
        double restBedrag = startBedrag;

        for (int i = 0; restBedrag >= 0; i++) {
            double maandRentePercentage = berekenRente(restBedrag / startBedrag) / 100 / 12;
            double maandRenteBedrag = maandRentePercentage * restBedrag;
            totaleRente += maandRenteBedrag;
            double aflossing = berekenAflossing(maandRenteBedrag, startBedrag);
            double extraMaandAflossing = extraAflossing[0];//i / 12];

            restBedragen.add(restBedrag);
            aflossingen.add(aflossing);
            rentes.add(maandRenteBedrag);
            brutos.add(aflossing + maandRenteBedrag);

            restBedrag -= aflossing + extraMaandAflossing / 12;
        }
    }
}
