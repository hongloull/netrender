package com.webrender.tool;

import java.util.TreeSet;

import org.junit.Test;

import com.webrender.tool.FramesOperate;

public class TestFramesOperate {
	@Test
	public void testFramesOperate(){
		FramesOperate framesOperate = new FramesOperate();
		String frames = "1.11";
		TreeSet<Double> setFrames = new TreeSet<Double>();
		framesOperate.framesOperate(frames, 1.0, setFrames);
		for(double frame : setFrames){
			System.out.print(frame+" ");
		}
		System.out.println();
	}
}
