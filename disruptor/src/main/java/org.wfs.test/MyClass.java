package org.wfs.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyClass {
	String key =null;
	public static void main(String[] args) throws IOException {
		Map<String, Double> map =new HashMap<>();
		BufferedReader  fileInputStream1 =new BufferedReader(new FileReader(new File("F:\\miraecompany.txt")));
		BufferedReader  fileInputStream2 =new BufferedReader(new FileReader(new File("F:\\miraeshare.txt")));
		String key ;
		while((key =fileInputStream1.readLine())!=null) {
			map.put(key.trim(), new Double(fileInputStream2.readLine().trim()));
		}
		System.out.println(map);
		
		Map<String, Double> map1 =new HashMap<>();
		BufferedReader  fileInputStream3 =new BufferedReader(new FileReader(new File("F:\\canaracomany.txt")));
		BufferedReader  fileInputStream4 =new BufferedReader(new FileReader(new File("F:\\canarashare.txt")));
		
		while((key =fileInputStream3.readLine())!=null) {
			map1.put(key.trim(), new Double(fileInputStream4.readLine().trim()));
		}
		System.out.println(map1);
		System.out.println();
		compare(map, map1);
		
	}
	
	private static void compare(Map<String, Double> map  ,Map<String, Double> map1 ) {
		for(Map.Entry<String, Double> entry :map.entrySet()) {
			if(map1.containsKey(entry.getKey())) {
				System.out.println(entry.getKey() +"\t"+ "\t"+ "Mirae Share ->"+entry.getValue()+"\t"+ "Canara Share "+map1.get(entry.getKey()).doubleValue());
			}
		}
	}
	
}
