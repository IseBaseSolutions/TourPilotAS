package isebase.cognito.tourpilot_apk.Utils;

final public class StringParser {
	
    private String fldInitString;
    private int pos1 = 0;
    private int pos2 = 0;

    public StringParser(String initString)
    {
        fldInitString = initString;
    }

    public String next(String token)
    {
        pos2 = fldInitString.indexOf(token, pos1);
        if (pos2 == -1)
            return "";
        String subString = fldInitString.substring(pos1, pos2);
        pos1 = ++pos2;
        return subString;
    }

    public String next()
    {
        return fldInitString.substring(pos1);
    }
    
    public boolean contains(String string) {
    	return fldInitString.contains(string);
    }

}
