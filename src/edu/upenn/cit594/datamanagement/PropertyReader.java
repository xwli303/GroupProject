package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyData;

public class PropertyReader implements IPropertyReader {

    private File file;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private PropertyReader.STATES state;
    enum STATES {START, ESC, NES};
    private Stack<Integer> stack;
    private int mvIndex;
    private int tlaIndex;
    private int zcIndex;
    private Logger logger = Logger.getInstance();

    public PropertyReader(String filename) throws IOException {
        logger.log("CSVProperty Reader opening file: " + filename);
        file = new File(filename);
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        setStateToSTART();
        stack = new Stack<Integer>();
    }

    public Map<String, List<PropertyData>> readPropertyFile() {
        Map<String, List<PropertyData>> propMap = new HashMap<String, List<PropertyData>>();
        try {
            mvIndex = -1;
            tlaIndex = -1;
            zcIndex = -1;
            int tokenCtr = 0;
            String token = bufferedReader.readLine();
            String[] arr = token.split(",");
            for(int i = 0; i < arr.length; i++) {
                switch(arr[i]) {
                    case "market_value":
                        mvIndex = i;
                        break;
                    case "total_livable_area":
                        tlaIndex = i;
                        break;
                    case "zip_code":
                        zcIndex = i;
                        break;
                    default:
                        break;
                }
            }

            int previous = 0;
            char ch;
            token = "";
            int c = bufferedReader.read();
            PropertyData pd = new PropertyData();
            while (c != -1) {
                switch(state) {
                    case START:
                        switch(c) {
                            case 34:
                                setStateToESC();
                                stack.push(c);
                                break;
                            case 10:
                                token = "";
                                setAttribute(token, tokenCtr, pd);
                                tokenCtr = 0;
                                addProperty(pd, propMap);
                                pd = new PropertyData();
                                break;
                            case 13:
                                // do nothing
                                break;
                            case 44:
                                setAttribute(token, tokenCtr, pd);
                                tokenCtr++;
                                token = "";
                                break;
                            default:
                                setStateToNES();
                                ch = (char)c;
                                token += ch;
                                break;
                        }
                        break;
                    case ESC:
                        switch(c) {
                            case 44:
                                if(stack.empty()) {
                                    setAttribute(token, tokenCtr, pd);
                                    token = "";
                                    setStateToSTART();
                                    tokenCtr++;
                                } else {
                                    if (previous == 34) {
                                        ch = (char)previous;
                                        token += ch;
                                    }
                                    ch = (char)c;
                                    token += ch;
                                }
                                break;
                            case 34:
                                if(!stack.empty()) {
                                    stack.pop();
                                    previous = c;
                                } else {
                                    stack.push(c);
                                    ch = (char)c;
                                    token += ch;
                                }
                                break;
                            case 10:
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                addProperty(pd, propMap);
                                pd = new PropertyData();
                                token = "";
                                tokenCtr = 0;
                                break;
                            case 13:
                                break;
                            default:
                                break;
                        }
                        break;
                    case NES:
                        switch(c) {
                            case 44:
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                token = "";
                                tokenCtr++;
                                break;
                            case 10:
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                addProperty(pd, propMap);
                                token = ""; //clear token
                                tokenCtr = 0;
                                pd = new PropertyData();
                            case 13:
                                break;
                            default:
                                ch = (char)c;
                                token += ch;
                                break;

                        }
                        break;

                }
                c = bufferedReader.read();

            }
            setAttribute(token, tokenCtr, pd);
            addProperty(pd, propMap);

            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {

        }


        return propMap;
    }

    private void setStateToSTART() {
        state = STATES.START;
    }

    private void setStateToESC() {
        state = STATES.ESC;
    }

    private void setStateToNES() {
        state = STATES.NES;
    }

    private void setAttribute(String att, int ctr, PropertyData pd) {
        if (ctr == mvIndex) {
            pd.setMarketValue(att);
        } else if (ctr == tlaIndex) {
            pd.setTotalLivableArea(att);
        } else if (ctr == zcIndex) {
            pd.setZipCode(att);
        }
    }

    private void addProperty(PropertyData pd, Map<String, List<PropertyData>> map) {
        String key = pd.getZipCode();
        if (map.containsKey(key)) {
            map.get(key).add(pd);
        } else {
            List<PropertyData> val = new ArrayList<PropertyData>();
            val.add(pd);
            map.put(key, val);
        }
    }

}
