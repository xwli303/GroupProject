package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CovidReader implements ICovidReader{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final char DELIMITER = ',';
    private Logger logger = Logger.getInstance();


    public List<CovidData> readCsvFile(String filename) throws IOException {
        List<CovidData> data = new ArrayList<>();
        BufferedReader reader = null;
            logger.log("Covid Reader opening file: " + filename);
            reader = new BufferedReader(new FileReader(filename));

        String line;
        boolean firstLine = true; // Add a boolean variable to keep track of the first line
        while ((line = reader.readLine()) != null) {
            if (firstLine) { // Skip the first line
                firstLine = false;
                continue;
            }
            List<String> tokens = lex(line);
            String zipCode = tokens.get(0);
            String parseZip = this.parseZip(zipCode);
            if (!tokens.isEmpty() && !parseZip.equals("")) {
                CovidData record = parseRecord(tokens);
                if (record != null) {
                    data.add(record);
                }
            }
        }
        reader.close();

        return data;
    }

    public List<CovidData> readJsonFile(String filename) throws IOException {
        List<CovidData> covidRecords = new ArrayList<>();

            JSONParser parser = new JSONParser();
            logger.log("Covid Reader opening file: " + filename);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) parser.parse(reader);
        }  catch (org.json.simple.parser.ParseException | IOException e) {
            throw new RuntimeException(e);
        }

        for (Object obj : jsonArray) {
                JSONObject jsonData = (JSONObject) obj;
                int zipCode = ((Long)jsonData.get("zip_code")).intValue();
                int negative = jsonData.containsKey("NEG") ? ((Long) jsonData.get("NEG")).intValue() : 0;
                int positive = jsonData.containsKey("POS")? ((Long) jsonData.get("POS")).intValue() : 0;
                int hospitalized = jsonData.containsKey("hospitalized") ? ((Long) jsonData.get("hospitalized")).intValue() : 0;
                int partial = jsonData.containsKey("partially_vaccinated") ? ((Long) jsonData.get("partially_vaccinated")).intValue() : 0;
                int full = jsonData.containsKey("fully_vaccinated") ? ((Long) jsonData.get("fully_vaccinated")).intValue() : 0;
            LocalDate date = null;
            try {
                date = DATE_FORMAT.parse((String) jsonData.get("etl_timestamp")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            CovidData data = new CovidData();
                data.setZipCode(zipCode);
                data.setNegative(negative);
                data.setPositive(positive);
                data.setHospitalization(hospitalized);
                data.setPartial(partial);
                data.setFull(full);
                data.setVaccination(date);

                covidRecords.add(data);
            }
            reader.close();



        return covidRecords;
    }

    private List<String> lex(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean inEscape = false;
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\') {
                inEscape = true;
                builder.append(c);
            } else if (c == '"' && !inEscape) {
                inQuotes = !inQuotes;
                builder.append(c);
            } else if (c == DELIMITER && !inQuotes) {
                tokens.add(builder.toString());
                builder.setLength(0);
            } else {
                builder.append(c);
            }
            inEscape = false;
        }
        tokens.add(builder.toString());
        return tokens;
    }

    private CovidData parseRecord(List<String> tokens) {
        CovidData record = new CovidData();
        try {
            int zipcode = Integer.parseInt(tokens.get(0));
            int neg = tokens.get(1).equals("") ? 0 : Integer.parseInt(tokens.get(1));
            int pos = tokens.get(2).equals("") ? 0 : Integer.parseInt(tokens.get(2));
            int deaths = tokens.get(3).equals("") ? 0: Integer.parseInt(tokens.get(3));
            int hos = tokens.get(4).equals("") ? 0 : Integer.parseInt(tokens.get(4));
            int partial = tokens.get(5).equals("") ? 0 : Integer.parseInt(tokens.get(5));
            int full = tokens.get(6).equals("") ? 0 : Integer.parseInt(tokens.get(6));
            int boost = tokens.get(7).equals("") ? 0 : Integer.parseInt(tokens.get(7));
            String vax = tokens.get(8);

            record.setZipCode(zipcode);
            record.setNegative(neg);
            record.setPositive(pos);
            record.setDeaths(deaths);
            record.setHospitalization(hos);
            record.setPartial(partial);
            record.setFull(full);
            record.setBooster(boost);
            record.setVaccination(DATE_FORMAT.parse(vax.substring(1,vax.length()-1)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            return record;
        } catch (NumberFormatException | ParseException e) {
            System.err.println("Error parsing record: " + e.getMessage());
            return null;
        }
    }

    private String parseZip (String zipCode){
        String newZip = "";
        if(zipCode.length() > 5){
            newZip = zipCode.substring(0, 5);
        }
        else if (zipCode.length() == 5){
            newZip = zipCode;
        }
        else{
            return newZip;
        }
        return newZip;
    }

}


