package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Tanja on 31-8-2017.
 */
public class Berekenaar {

    public static final int CREATE = 0;
    public static final int DELETE = 1;

    public List<Verstrekker> verstrekkers = new ArrayList();
    JSONArray verstrekkersJson;

    public Double woningWaarde = null;
    public Double startBedrag = null;
    public Double extraAflossing = null;
    public Date hypotheekDatum = null;
    public Date notarisDatum = null;

    private File verstrekkersFile = new File("verstrekkers.json");

    public Berekenaar() {
        verwerkVerstrekkers();
    }

    public void verwerkVerstrekkers() {
        verstrekkers.clear();
        // Read verstrekkers info from json file
        try {
            verstrekkersJson = new JSONArray(FileUtils.readFileToString(verstrekkersFile, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse individual verstrekker info
        for (int i = 0; i < verstrekkersJson.length(); i++) {
            JSONObject verstrekkerJson = verstrekkersJson.getJSONObject(i);

            // Naam
            String naam = verstrekkerJson.getString("naam");

            String laatstBijgewerkt = verstrekkerJson.optString("laatst_bijgewerkt");

            String url = verstrekkerJson.optString("actuele_rente_url");

            // Rentegroepen
            Map<String, Double> rentesJson = new HashMap<>();
            try {
                JSONObject rentesObject = verstrekkerJson.optJSONObject("rente_groepen");
                if (rentesObject != null) {
                    rentesJson = new ObjectMapper().readValue(rentesObject.toString(), HashMap.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<Double, Double> rentesTemp = rentesJson.entrySet().stream()
                    .collect(Collectors.toMap(entry -> Double.parseDouble(entry.getKey()), entry -> entry.getValue()));
            TreeMap<Double,Double> rentes = new TreeMap<>();
            rentes.putAll(rentesTemp);

            // Extra korting
            Double extraKorting = verstrekkerJson.optDouble("extra_korting", 0.0);

            // Opmerkingen
            JSONArray opmerkingenJSON = verstrekkerJson.optJSONArray("opmerkingen");
            List<String> opmerkingen = new ArrayList<>();
            if (opmerkingenJSON != null) {
                for (int j = 0; j < opmerkingenJSON.length(); j++) {
                    opmerkingen.add(opmerkingenJSON.get(j).toString());
                }
            }

            Verstrekker verstrekker = new Verstrekker();
            verstrekker.setNaam(naam);
            verstrekker.setLaatstBijgewerkt(laatstBijgewerkt);
            verstrekker.setActueleRenteURL(url);
            verstrekker.setRenteGroepen(rentes);
            verstrekker.setExtraKorting(extraKorting);
            verstrekker.setOpmerkingen(opmerkingen);

            // Offerte
            JSONObject offerteJson = verstrekkerJson.optJSONObject("offerte");
            if (offerteJson != null) {
                int looptijd = offerteJson.optInt("standaard_looptijd");
                int verlengingInt = offerteJson.optInt("mogelijke_verlenging", -1);
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

    public void update(Double woningWaarde, Double startBedrag, Date hypotheekDatum, Date notarisDatum, String vasteExtraAflossing) {
        this.woningWaarde = woningWaarde;
        this.startBedrag = startBedrag;
        this.hypotheekDatum = hypotheekDatum;
        this.notarisDatum = notarisDatum;

        if (vasteExtraAflossing == null) {
            extraAflossing = null;
        } else {
            extraAflossing = Double.parseDouble(vasteExtraAflossing);
        }

        for (Verstrekker verstrekker : verstrekkers) {
            verstrekker.berekenBedragen(true, woningWaarde, startBedrag, extraAflossing, hypotheekDatum, notarisDatum);
        }
    }

    public List<Verstrekker> getVerstrekkers() {
        return verstrekkers;
    }

    public Verstrekker getVerstrekker(String name) {
        for (Verstrekker verstrekker : getVerstrekkers()) {
            if (verstrekker.getNaam().equals(name)) return verstrekker;
        }
        return null;
    }


    public void updateJSONVerstrekkers() {
        verstrekkersJson = new JSONArray();

        for (Verstrekker v : verstrekkers) verstrekkersJson.put(v.toJSON());

        try {
            FileUtils.writeStringToFile(verstrekkersFile, verstrekkersJson.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateVerstrekkers(Verstrekker verstrekker, int action) {
        if (action == CREATE) {
            if (!verstrekkers.contains(verstrekker)) verstrekkers.add(verstrekker);
        } else if (action == DELETE) {
            verstrekkers.remove(verstrekker);
        }

        updateJSONVerstrekkers();
    }
}
