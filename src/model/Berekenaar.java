package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tanja on 31-8-2017.
 */
public class Berekenaar {
    public double startBedrag;
    public double aflossing;
    public List<Verstrekker> verstrekkers = new ArrayList<Verstrekker>();
    public double totaleRente = 0;
    public double vasteExtraAflossing = 0;

    public double[] extraAflossing = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0
    };

    public void printBedragen(Verstrekker verstrekker) {
        double restBedrag = startBedrag;
        totaleRente = 0;

        System.out.println(verstrekker.name.toUpperCase());
        System.out.format("%-10s%-15s%-15s%-15s%-15s%-15s\n", new String[]{"Jaar", "Restbedrag", "Aflossing", "Rente", "Bruto", "Extra"});

        for (int i = 0; restBedrag >= 0; i++) {
            double maandRentePercentage = verstrekker.berekenRente(restBedrag / startBedrag) / 100 / 12;
            double maandRenteBedrag = maandRentePercentage * restBedrag;
            totaleRente += maandRenteBedrag;
            aflossing = verstrekker.berekenAflossing(maandRenteBedrag, startBedrag);
            double extraMaandAflossing = vasteExtraAflossing > 0 ? vasteExtraAflossing : extraAflossing[i / 12];

            if (i % 12 == 0) {
                String[] row = new String[]{(i/12 + 1) + "", String.format("€ %,6.0f", restBedrag),
                        String.format("€ %,4.0f", aflossing), String.format("€ %,3.0f", maandRenteBedrag),
                        String.format("€ %,3.0f", aflossing + maandRenteBedrag), String.format("€ %,6.0f", extraMaandAflossing)};
                System.out.format("%-10s%-15s%-15s%-15s%-15s%-15s\n", row);
            }

            restBedrag -= aflossing + extraMaandAflossing / 12;
        }

        System.out.format("%-10s%-15s%-15s%-15s%-15s%-15s\n", new String[]{"----------", "---------------", "---------------", "---------------", "---------------", "---------"});
        System.out.format("%-10s%-15s%-15s%-15s\n", new String[]{"", "", "", String.format("€ %,3.0f", totaleRente)});
    }

    public Berekenaar(double startBedrag) {
        Map<Double, Double> rabobankRentes = new HashMap<Double, Double>();
        rabobankRentes.put(0.9, 2.40);
        rabobankRentes.put(0.675, 2.1);
        rabobankRentes.put(0.0, 1.7);
        Verstrekker rabobankAnnuitair = new AnnuitaireVerstrekker("Rabobank", rabobankRentes);

       /* Map<Double, Double> rabobankPlusRentes = new HashMap<Double, Double>();
        rabobankPlusRentes.put(0.9, 2.7);
        rabobankPlusRentes.put(0.675, 2.4);
        rabobankPlusRentes.put(0.0, 2.0);
        Verstrekker rabobankPlusAnnuitair = new AnnuitaireVerstrekker("Rabobank", rabobankPlusRentes);

        Map<Double, Double> rabobankRentesVast = new HashMap<Double, Double>();
        rabobankRentesVast.put(0.0, 2.45);
        Verstrekker rabobankVastAnnuitair = new AnnuitaireVerstrekker("Rabobank vast", rabobankRentesVast);
*/
        Map<Double, Double> centraalBeheerRentes = new HashMap<>();
        centraalBeheerRentes.put(1.00, 2.34);
        centraalBeheerRentes.put(0.95, 2.32);
        centraalBeheerRentes.put(0.9, 2.24);
        centraalBeheerRentes.put(0.85, 2.2);
        centraalBeheerRentes.put(0.8, 2.19);
        centraalBeheerRentes.put(0.7, 2.17);
        centraalBeheerRentes.put(0.6, 2.15);
        centraalBeheerRentes.put(0.0, 2.1);
        Verstrekker centraalBeheerAnnuitair = new AnnuitaireVerstrekker("Centraal Beheer", centraalBeheerRentes);

        Map<Double, Double> snsRentes = new HashMap<>();
        snsRentes.put(1.06, 2.8);
        snsRentes.put(1.01, 2.7);
        snsRentes.put(0.95, 2.2);
        snsRentes.put(0.9, 2.15);
        snsRentes.put(0.8, 2.05);
        snsRentes.put(0.7, 1.95);
        snsRentes.put(0.6, 1.9);
        snsRentes.put(0.5, 1.85);
        snsRentes.put(0.4, 1.80);
        snsRentes.put(0.0, 1.75);
        Verstrekker snsAnnuitair = new AnnuitaireVerstrekker("SNS", snsRentes);

        Map<Double, Double> snsVerlengdRentes = new HashMap<>();
        snsVerlengdRentes.put(1.06, 2.9);
        snsVerlengdRentes.put(1.01, 2.8);
        snsVerlengdRentes.put(0.95, 2.3);
        snsVerlengdRentes.put(0.9, 2.25);
        snsVerlengdRentes.put(0.8, 2.15);
        snsVerlengdRentes.put(0.7, 2.05);
        snsVerlengdRentes.put(0.6, 2.0);
        snsVerlengdRentes.put(0.5, 1.95);
        snsVerlengdRentes.put(0.4, 1.9);
        snsVerlengdRentes.put(0.0, 1.85);
        Verstrekker snsVerlengdAnnuitair = new AnnuitaireVerstrekker("SNS Verlengd", snsVerlengdRentes);

        Map<Double, Double> ingRentes = new HashMap<>();
        ingRentes.put(1.01, 2.94);
        ingRentes.put(0.95, 2.29);
        ingRentes.put(0.9, 2.09);
        ingRentes.put(0.85, 2.04);
        ingRentes.put(0.8, 1.94);
        ingRentes.put(0.75, 1.89);
        ingRentes.put(0.7, 1.84);
        ingRentes.put(0.65, 1.79);
        ingRentes.put(0.6, 1.74);
        ingRentes.put(0.55, 1.69);
        ingRentes.put(0.0, 1.64);
        Verstrekker ingAnnuitair = new AnnuitaireVerstrekker("ING", ingRentes);

        Map<Double, Double> ingLoyaliteitsRentes = new HashMap<>();
        ingLoyaliteitsRentes.put(1.01, 2.79);
        ingLoyaliteitsRentes.put(0.95, 2.14);
        ingLoyaliteitsRentes.put(0.9, 1.94);
        ingLoyaliteitsRentes.put(0.85, 1.89);
        ingLoyaliteitsRentes.put(0.8, 1.79);
        ingLoyaliteitsRentes.put(0.75, 1.74);
        ingLoyaliteitsRentes.put(0.7, 1.69);
        ingLoyaliteitsRentes.put(0.65, 1.64);
        ingLoyaliteitsRentes.put(0.6, 1.59);
        ingLoyaliteitsRentes.put(0.55, 1.54);
        ingLoyaliteitsRentes.put(0.0, 1.49);
        Verstrekker ingLoyaliteitsAnnuitair = new AnnuitaireVerstrekker("ING Loyaliteit", ingLoyaliteitsRentes);

        Map<Double, Double> abnAmroRentes = new HashMap<Double, Double>();
        abnAmroRentes.put(0.85, 2.38);
        abnAmroRentes.put(0.65, 1.98);
        abnAmroRentes.put(0.0, 1.78);
        Verstrekker abnAmroAnnuitair = new AnnuitaireVerstrekker("ABN AMRO", abnAmroRentes);

        Map<Double, Double> woonfondsRentes = new HashMap<>();
        woonfondsRentes.put(0.0, 2.07);
        woonfondsRentes.put(0.6, 2.11);
        woonfondsRentes.put(0.8, 2.13);
        woonfondsRentes.put(1.0, 2.21);
        woonfondsRentes.put(1.1, 2.49);
        Verstrekker woonfondsAnnuitair = new AnnuitaireVerstrekker("Woonfonds", woonfondsRentes);

        verstrekkers.add(rabobankAnnuitair);
        verstrekkers.add(centraalBeheerAnnuitair);
        verstrekkers.add(snsAnnuitair);
        verstrekkers.add(snsVerlengdAnnuitair);
        verstrekkers.add(ingAnnuitair);
        verstrekkers.add(abnAmroAnnuitair);
        verstrekkers.add(woonfondsAnnuitair);
        verstrekkers.add(ingLoyaliteitsAnnuitair);

        this.startBedrag = startBedrag;

        for (Verstrekker verstrekker : verstrekkers) {
     //       verstrekker.berekenBedragen(startBedrag, 30000);
       //     printBedragen(verstrekker);
        }
//        new Berekenaar(rabobankPlusAnnuitair, 40000, 330000).verstrekker.berekenBedragen(startBedrag, 0);

    }

    public void addVerstrekker(boolean annuitair, String name, Map<Double, Double> renteGroepen) {
        if (annuitair) {
            verstrekkers.add(new AnnuitaireVerstrekker(name, renteGroepen));
        } else {
            verstrekkers.add(new LineaireVerstrekker(name, renteGroepen));
        }
    }
}
