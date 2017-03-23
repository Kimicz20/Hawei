package com.lab603.jun;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.cacheserverdeploy.deploy.Deploy;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.util.TransStringToModule;

public class TestCase {
	private static Net net;
	public static void main(String args[]) throws IOException {
		FileReader fr = new FileReader("case0.txt");
		BufferedReader br = new BufferedReader(fr);
        String s;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> strings = new ArrayList<>();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            strings.add(s);
        }

	    br.close();
	    
	    String[] input = new String[strings.size()];
	    for(int i = 0; i < input.length; i++) {
	    	input[i] = strings.get(i);
	    }
	  
	    
	    
		String[] out = Deploy.deployServer(input);///
		
		for (String string : out) {
			System.out.println(string);
		}
	}
	
}
