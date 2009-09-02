package com.webrender.tool;

public class StrOperate {
	private   static   String   StrReplace(String   rStr,   String   rFix,   String   rRep)   
    {   
        int   l   =   0;   
        String   gRtnStr   =   rStr;   
        do   
        {   
            l   =   rStr.indexOf(rFix,l);   
            if(l   ==   -1)   break;   
            gRtnStr   =   rStr.substring(0,l)   +   rRep   +   rStr.substring(l   +   rFix.length());   
            l   +=   rRep.length();   
            rStr   =   gRtnStr;   
        }while(true);   
        return   gRtnStr.substring(0,   gRtnStr.length());   
    }
	
	public static String pathReplace(String   rStr,   String   rFix,   String   rRep)
	{
		if ( rFix.endsWith("\\") || rFix.endsWith("/") )
		{
			rFix = rFix.substring(0,rFix.length()-1);
		}
		if ( rRep.endsWith("\\") || rRep.endsWith("/") )
		{
			rRep = rRep.substring(0,rRep.length()-1);
		}
		String result = StrReplace(rStr,rFix,rRep);
		return toUnixPath(result);
	}
	private  static String toUnixPath(String filePath) {
	    return filePath.replace('\\', '/');
	}
	public static void main(String args[])
	{
		System.out.println( StrOperate.pathReplace("/mnt/production/project/a.mb", "/mnt/production","D:\\") );
	}
}