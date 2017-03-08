package isebase.cognito.tourpilot.Utils;

public class NCryptor {
	
    private char[] ncode;
    private char[] ncodeReverse;

    public NCryptor(){
        ncode = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        ncodeReverse = new char[256];
        InitNcodeReverse();
    }

    public String LToNcode(long x)
    {
        char[] zz = new char[9];
        int i;
        int r;
        for (i = 0; i < 9; i++)
            zz[i] = (char)0;
        i = 8;
        while (x >= 0 && i >= 0)
        {
            r = (int) (x % 62);
            zz[i] = ncode[r];
            x /= 62;
            i--;
        }
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(zz);
        return strBuff.toString();
    }
    
    public long NcodeToL(String str)
    {
        char[] ncodeStr = str.toCharArray();
        long x = 0;
        for (int i = 0; i < ncodeStr.length; i++)
        {
            x += ncodeReverse[ncodeStr[i]];
            x *= 62;
        }
        x /= 62;
        return x;
    }    

    private void InitNcodeReverse()
    {
        for (int i = 0; i < 256; i++) 
        	ncodeReverse[i] = (char)(0);
        for (char i = '0'; i <= '9'; i++) 
        	ncodeReverse[i] = (char)(i - '0');
        for (char i = 'a'; i <= 'z'; i++) 
        	ncodeReverse[i] = (char)(10 + i - 'a');
        for (char i = 'A'; i <= 'Z'; i++) 
        	ncodeReverse[i] = (char)(36 + i - 'A');
    }
}
