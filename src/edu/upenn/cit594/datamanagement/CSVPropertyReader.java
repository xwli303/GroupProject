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

public class CSVPropertyReader implements PropertyReader {

    private File file;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private CSVPropertyReader.STATES state;
    enum STATES {START, ESC, NES};
    private Stack<Integer> stack;
    private int mvIndex;
    private int tlaIndex;
    private int zcIndex;
    private Logger logger = Logger.getInstance();

    public CSVPropertyReader(String filename) throws IOException {
        logger.log("CSVProperty Reader opening file: " + filename);
        file = new File(filename);
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        setStateToSTART();
        stack = new Stack<Integer>();
    }

    public Map<String, List<PropertyData>> readPropertyFile() {
        //StringBuilder sb = new StringBuilder();
        //List<PropertyData> pdAL = new ArrayList<PropertyData>();
        Map<String, List<PropertyData>> propMap = new HashMap<String, List<PropertyData>>();
        try {
            mvIndex = -1;
            tlaIndex = -1;
            zcIndex = -1;
            int tokenCtr = 0;
            String token = bufferedReader.readLine();
            //System.out.println(token);
            String[] arr = token.split(",");
            for(int i = 0; i < arr.length; i++) {
                switch(arr[i]) {
                    case "market_value":
                        mvIndex = i;
                        break;
                    case "total_livable_area":
                        tlaIndex = i;
                        //System.out.println("tla index is " + tlaIndex);
                        break;
                    case "zip_code":
                        zcIndex = i;
                        break;
                    default:
                        break;
                }
            }

            //System.out.println("market_value is at index " + mvIndex);
            //System.out.println("zip_code is at index " + tlaIndex);

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
                                //System.out.println(stack.toString());
                                //System.out.println("start of escaped sequence " + c);
                                break;
                            case 10:
                                token = "";
                                setAttribute(token, tokenCtr, pd);
                                //System.out.println("new line character in start state " + c);
                                tokenCtr = 0;
                                //pdAL.add(pd);
                                addProperty(pd, propMap);
                                pd = new PropertyData();
                                break;
                            case 13:
                                // do nothing
                                //System.out.println("carriage line character in start state" + c);
                                break;
                            case 44:
                                setAttribute(token, tokenCtr, pd);
                                tokenCtr++;
                                token = "";
                                break;
                            default:
                                setStateToNES();
                                //add char c to string
                                //System.out.println("start of non-escaped sequence " + c);
                                //sb.append(c);
                                //System.out.println("sb is " + sb);
                                ch = (char)c;
                                token += ch;
                                //System.out.println("token is " + token);
                                break;
                        }
                        break;
                    case ESC:
                        switch(c) {
                            case 44:
                                //System.out.println("comma possibly in escaped sequence " + c);
                                // check stack-- it must be empty
                                if(stack.empty()) {
                                    setAttribute(token, tokenCtr, pd);
                                    //System.out.println("market value : " + pd.getMarketValue());
                                    // send token to parser
                                    // clear token
                                    token = "";
                                    //System.out.println("comma ends escaped sequence \n state reset to start");
                                    setStateToSTART();
                                    tokenCtr++;
                                } else {
                                    if (previous == 34) {
                                        ch = (char)previous;
                                        token += ch;
                                    }
                                    //add char c to string
                                    //System.out.println("comma is in escaped sequence");
                                    ch = (char)c;
                                    token += ch;
                                }
                                break;
                            case 34:
                                //System.out.println("double quote possibly in escaped sequence " + c);
                                //check stack
                                if(!stack.empty()) {
                                    stack.pop();
                                    //System.out.println(stack.toString());
                                    //System.out.println("might be end of escaped sequence");
                                    previous = c;
                                } else {
                                    stack.push(c);
                                    //System.out.println(stack.toString());
                                    //System.out.println("double quote in escaped sequence");
                                    ch = (char)c;
                                    token += ch;
                                }
                                break;
                            case 10:
                                //System.out.println("new line character in escaped sequence " + c);
                                // send token to parser
                                // clear token
                                // create new prop dat obj

                                //System.out.println("set state to start");
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                //pdAL.add(pd);
                                addProperty(pd, propMap);
                                pd = new PropertyData();
                                token = "";
                                tokenCtr = 0;
                                break;
                            case 13:
                                //System.out.println("carriage return character in escaped sequence " + c);
                                //do nothing
                                break;
                            default:
                                //System.out.println("character in escaped sequence " + c);
                                // add char c to string
                                break;
                        }
                        break;
                    case NES:
                        switch(c) {
                            case 44:
                                //System.out.println("comma ends non-escaped sequence " + c);
                                //System.out.println("state reset to start");
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                //System.out.println("tla is " + pd.getTotalLivableArea());
                                //send token to parser
                                token = "";
                                tokenCtr++;
                                break;
                            case 10:
                                //System.out.println("new line character in non-escaped sequence " + c);
                                //System.out.println("state reset to start");
                                setStateToSTART();
                                setAttribute(token, tokenCtr, pd);
                                //pdAL.add(pd);
                                addProperty(pd, propMap);
                                //System.out.println("tla is " + pd.getTotalLivableArea());
                                //send token to parser
                                token = ""; //clear token
                                //create new prop data obj
                                tokenCtr = 0;
                                pd = new PropertyData();
                            case 13:
                                //System.out.println("carriage return character in non-escaped sequence " + c);
                                // do nothing
                                break;
                            default:
                                //System.out.println("character in non-escaped sequence " +c);
                                //add char c to string
                                ch = (char)c;
                                token += ch;
                                break;

                        }
                        break;

                }
                c = bufferedReader.read();
                //System.out.println("token is " + token);
                //System.out.println("tokenCtr is " + tokenCtr);
                //System.out.println("size of array list is " + pdAL.size());
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
            //System.out.println("key : " + key + " the size of val before adding pd : " + map.get(key).size());
            map.get(key).add(pd);
            //System.out.println("key : " + key + " the size of val after adding pd : " + map.get(key).size());
            //System.out.println();
        } else {
            List<PropertyData> val = new ArrayList<PropertyData>();
            val.add(pd);
            //System.out.println("new key is : " + key);
            //System.out.println();
            map.put(key, val);
        }
    }

}
