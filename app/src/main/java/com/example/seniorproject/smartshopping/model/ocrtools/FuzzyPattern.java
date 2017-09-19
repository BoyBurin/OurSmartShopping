package com.example.seniorproject.smartshopping.model.ocrtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FuzzyPattern {

	/*public static void main(String[] args) throws FileNotFoundException {
		File f = new File("/Users/boyburin/CS_KMUTT/SeniorProject/myTrainData/Test/mytest2text3.txt");
		Scanner sc = new Scanner(f);
		String[] product = new String[9];
		String[] patternproduct = new String[9];
		for(int i = 0 ; i < 9 ; i++){
			product[i] = sc.nextLine();
			patternproduct[i] = product[i] + " %d.%d";
			//System.out.println(data[i]);
		}
		String pattern1 = "รวมเงิน %d.%d";
		String pattern2 = "%s %d.%d";
		String pattern3 = "%d x %d.%d";
		f = new File("/Users/boyburin/CS_KMUTT/SeniorProject/myTrainData/Test/mytest2text2.txt");
		sc = new Scanner(f);
		String[] data = new String[14];
		for(int i = 0 ; i <14 ; i++){
			data[i] = sc.nextLine();
			System.out.println(data[i]);
		}
		String pattern[] = new String[14];
		int min = Integer.MAX_VALUE;
		for(int i = 0 ; i < 14 ; i++){
			int temp = editPattern(pattern1, data[i]);
			if(temp < min){
				min = temp;
				pattern[i] = pattern1;
			}
			temp = editPattern(pattern3, data[i]);
			if(temp < min){
				min = temp;
				pattern[i] = pattern3;
			}
			for(int j = 0 ; j < 9 ; j++){
				temp = editPattern(patternproduct[j], data[i]);
				if(temp < min){
					min = temp;
					pattern[i] = "%s %d.%d";
				}
			}
			min = Integer.MAX_VALUE;
		}
		/*for(int i = 0 ; i < 14 ; i++){
			System.out.println(data[i] + " ----> " + pattern[i]);
		}
		
		for(int i = 0 ; i < 14 ; i++){
			String[] ans = new String[0];
			if(pattern[i].equals(pattern2)){
				ans = productPattern(data[i]);
				if(i+1 >= 14) break;
				if(pattern[i+1].equals(pattern3)){
					int temp = getAmont(data[i+1]);
					ans[1] = "" + temp;
					i++;
				}
				System.out.printf("%-30s	%-10s	%-10s\n",ans[0],ans[1],ans[2]);
			}
			else if(pattern[i].equals(pattern1)){
				ans = getTotal(data[i]);
				System.out.printf("%-40s		%-10s\n",ans[0],ans[1]);
			}
			
		}
	}*/
	
	public static int editPattern(String p, String s){
		String[] pattern = p.split("\\s+");
		String[] data = s.split("\\s+");
		//System.out.println(Arrays.toString(pattern));
		int sum = 0;
		if(pattern.length != data.length) return Integer.MAX_VALUE;
		for(int i = 0 ; i < pattern.length ; i++){
			String str1 = pattern[i];
			String str2 = data[i];
			//str1 = str1.replaceAll("%d", "");
			//str1 = str1.replaceAll("%s", "");
			System.out.println("Why  =  " + str1);
			if(str1.length() == 0) continue;
			sum += LevenshteinDistance.editDistance(str1, str2);
			
		}
		System.out.println(Arrays.toString(pattern) + "  :  " + sum);
		return sum;
	}

	public static String[] productPattern(String data) throws FileNotFoundException{
		String[] mydata = data.split("\\s+");
		String[] result = new String[3];
		result[0] = LevenshteinDistance.findWord(mydata[0]);
		result[1] = "1";
		result[2] = mydata[1];
		return result;
	}
	
	public static int getAmont(String data){
		String[] mydata = data.split("\\s+");
		return Integer.parseInt(mydata[0]);
	}
	
	public static String[] getTotal(String data) throws FileNotFoundException{
		String[] mydata = data.split("\\s+");
		String[] result = new String[2];
		result[0] = LevenshteinDistance.findWord(mydata[0]);
		result[1] = mydata[1];
		return result;
	}
}
