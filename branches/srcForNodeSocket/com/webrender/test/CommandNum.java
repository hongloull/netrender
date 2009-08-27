package com.webrender.test;

import java.math.BigDecimal;

public class CommandNum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Start :
		BigDecimal X =  new BigDecimal("0.0") ;
//		End   : 
		BigDecimal Y =  new BigDecimal("100.0") ;
//		ByFrame  ： 
		BigDecimal I = new BigDecimal("1.2") ;
//		FramePerNode : 
		BigDecimal J = new BigDecimal("3") ;
		BigDecimal distance = I.multiply(J);
		BigDecimal total = Y.subtract(X);
		BigDecimal bignum = total.divide(distance,5,BigDecimal.ROUND_HALF_UP);
		
		int num=(int)Math.ceil( bignum.doubleValue() );

		System.out.println(" num: "+num);
		BigDecimal currentStartFrame = X;
		for (int i=0;i<num;i++)
		{
			if (currentStartFrame.compareTo(Y)==1) break;
			System.out.println(i+": "+"-s "+currentStartFrame+" -e  "+(currentStartFrame.add(distance))+" -b " +I);
			currentStartFrame = currentStartFrame.add(distance).add(I);
		}
	}
	
}
