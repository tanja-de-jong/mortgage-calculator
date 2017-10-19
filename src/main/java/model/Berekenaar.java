package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Tanja on 31-8-2017.
 */
public class Berekenaar {
    public List<Verstrekker> verstrekkers = new ArrayList();

    public Double startBedrag = null;
    public Double extraAflossing = null;
    public Date hypotheekDatum = null;
    public Date notarisDatum = null;

    public double[] extraAflossingen;

    /*public void printBedragen(boolean annuitair, Verstrekker verstrekker) {
        double restBedrag = startBedrag;
        totaleRente = 0;

        System.out.println(verstrekker.name.toUpperCase());
        System.out.format("%-10s%-15s%-15s%-15s%-15s%-15s\n", new String[]{"Jaar", "Restbedrag", "Aflossing", "Rente", "Bruto", "Extra"});

        for (int i = 0; restBedrag >= 0; i++) {
            double maandRentePercentage = verstrekker.berekenRente(restBedrag / startBedrag) / 100 / 12;
            double maandRenteBedrag = maandRentePercentage * restBedrag;
            totaleRente += maandRenteBedrag;
            aflossing = verstrekker.berekenAflossing(annuitair, maandRenteBedrag, startBedrag);
            double extraMaandAflossing = extraAflossing > 0 ? extraAflossing : extraAflossingen[i / 12];

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
    }*/

    public Berekenaar() {
        verwerkVerstrekkers();
    }

    public void verwerkVerstrekkers() {
        // Read verstrekkers info from json file
        JSONArray verstrekkersJson = null;
        try {
            verstrekkersJson = new JSONArray(FileUtils.readFileToString(new File("verstrekkers.json"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse individual verstrekker info
        for (int i=0; i<verstrekkersJson.length(); i++) {
            JSONObject verstrekkerJson = verstrekkersJson.getJSONObject(i);

            // Naam
            String naam = verstrekkerJson.getString("naam");

            // Rentegroepen
            Map<String, Double> rentesJson = null;
            try {
                rentesJson = new ObjectMapper().readValue(verstrekkerJson.getJSONObject("rentes").toString(), HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<Double, Double> rentes = rentesJson.entrySet().stream()
                    .collect(Collectors.toMap(entry -> Double.parseDouble(entry.getKey()), entry -> entry.getValue()));

            // Extra korting
            Double extraKorting = verstrekkerJson.optDouble("extra_korting", 0.0);

            Verstrekker verstrekker = new Verstrekker(naam, rentes, extraKorting);

            // Offerte
            Offerte offerte = null;
            JSONObject offerteJson = verstrekkerJson.optJSONObject("offerte");
            if (offerteJson != null) {
                int looptijd = offerteJson.getInt("looptijd");
                int verlengingInt = offerteJson.optInt("verlenging", -1);
                Integer verlenging = verlengingInt == -1 ? null : verlengingInt;
                boolean tussentijdseDaling = offerteJson.optBoolean("tussentijdse_daling");
                double provisieDouble = offerteJson.optDouble("provisie", -1.0);
                Double provisie = provisieDouble == -1.0 ? null : provisieDouble;
                boolean vasteRente = offerteJson.optBoolean("vaste_rente");
                boolean alleenKostenBijRenteStijging = offerteJson.optBoolean("alleen_kosten_bij_rentestijging");
                verstrekker.setOfferte(new Offerte(verstrekker, looptijd, verlenging, tussentijdseDaling, provisie, vasteRente, alleenKostenBijRenteStijging));
            }

            verstrekkers.add(verstrekker);
        }
    }

    public void update(Double startBedrag, Date hypotheekDatum, Date notarisDatum, String vasteExtraAflossing) {
        this.startBedrag = startBedrag;
        this.hypotheekDatum = hypotheekDatum;
        this.notarisDatum = notarisDatum;

        if (vasteExtraAflossing == null) {
            extraAflossing = null;
        } else {
            extraAflossing = Double.parseDouble(vasteExtraAflossing);
        }

        for (Verstrekker verstrekker : verstrekkers) {
            verstrekker.berekenBedragen(true, startBedrag, extraAflossing);
        }
    }

    public List<Verstrekker> getVerstrekkers() {
        return verstrekkers;
    }
}
