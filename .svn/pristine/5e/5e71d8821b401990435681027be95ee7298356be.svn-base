package com.mmec.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvParser{
    // Saved input CSV file pathname
    private String inputCsvFile;
    
    // Space mark , ; : etc.
    private String spaceMark=",";
    
    /**
     * Contructor
     * @param inputCsvFile
     */
    public CsvParser(String inputCsvFile,String spaceMark){
        this.inputCsvFile=inputCsvFile;
        this.spaceMark=spaceMark;
    }
    
    /**
     * Contructor
     * @param inputCsvFile
     */
    public CsvParser(String inputCsvFile){
        this.inputCsvFile=inputCsvFile;
        this.spaceMark=",";
    }
    
    /**
     * Get parsed array from CSV file
     * @return
     */
    public Object[] getParsedArray() throws Exception{
        List<List<String>> retval=new ArrayList<List<String>>();
        
        String regExp = getRegExp();
        BufferedReader in = new BufferedReader(new FileReader(this.inputCsvFile));
        String strLine;
        String str = "";
        
        while ((strLine = in.readLine()) != null) {
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(strLine);
            List<String> listTemp = new ArrayList<String>();
            while (matcher.find())
            {
                str = matcher.group();
                str = str.trim();
                
                if (str.endsWith(spaceMark))
                {
                    str = str.substring(0, str.length() - 1);
                    str = str.trim();
                }
                
                if (str.startsWith("\"") && str.endsWith("\""))
                {
                    str = str.substring(1, str.length() - 1);
                    if (CsvParser.isExisted("\"\"", str))
                    {
                        str = str.replaceAll("\"\"", "\"");
                    }
                }
                
                if (!"".equals(str))
                {
                    listTemp.add(str);
                }
            }
            
            // Add to retval
            retval.add(listTemp);     
        }
        in.close();
        
        return retval.toArray();
    }
    
    /**
     * Regular Expression for CSV parse
     * @return
     */
    private String getRegExp()
    {
        final String SPECIAL_CHAR_A = "[^\",\\n 　]";
        final String SPECIAL_CHAR_B = "[^\""+spaceMark+"\\n]";
        
        StringBuffer strRegExps = new StringBuffer();
        strRegExps.append("\"((");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*["+spaceMark+"\\n 　])*(");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"{2})*)*");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"[ 　]*"+spaceMark+"[ 　]*");
        strRegExps.append("|");
        strRegExps.append(SPECIAL_CHAR_B);
        strRegExps.append("*[ 　]*"+spaceMark+"[ 　]*");
        strRegExps.append("|\"((");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*["+spaceMark+"\\n 　])*(");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"{2})*)*");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"[ 　]*");
        strRegExps.append("|");
        strRegExps.append(SPECIAL_CHAR_B);
        strRegExps.append("*[ 　]*");
        return strRegExps.toString();
    }
    
    /**
     * If argChar is exist in argStr
     * @param argChar
     * @param argStr
     * @return
     */
    private static boolean isExisted(String argChar, String argStr)
    {
        
        boolean blnReturnValue = false;
        if ((argStr.indexOf(argChar) >= 0)
                && (argStr.indexOf(argChar) <= argStr.length()))
        {
            blnReturnValue = true;
        }
        return blnReturnValue;
    }

    /**
     * Test
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)  throws Exception{
        CsvParser parser=new CsvParser("D:/liantiao.csv");
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_not quoted_2.csv");
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_quoted.csv");
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_quoted_2.csv");
        
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_1.csv",";");
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_2.csv",":");
        
        Object[] arr=parser.getParsedArray();
        System.out.println(arr.length);
        //System.out.println(arr);
//        System.out.println(arr[0]);
//        String s = (String)arr[0];
        String s = "";
        for(Object obj:arr){
            //System.out.print("[");
            
            List<String> ls=(List<String>)obj;
            
            for(String item:ls){
            	s = item;
//                System.out.println(item+",");
            }
            
            //System.out.println("],");
        }
//        System.out.println(s);
        
        String s_arr [] = s.split("\t");
//        System.out.println(s_arr.length);
        for(int i=0;i<s_arr.length;i++)
        {
        	System.out.println(s_arr[i]);
        }
    }
}
