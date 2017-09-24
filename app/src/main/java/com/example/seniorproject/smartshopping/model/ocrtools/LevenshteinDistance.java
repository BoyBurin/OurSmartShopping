package com.example.seniorproject.smartshopping.model.ocrtools;

import com.example.seniorproject.smartshopping.model.dao.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.ItemInventoryManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevenshteinDistance {

	public ArrayList<ItemInventoryMap> doLevenshteinDistance(ArrayList<String> data){

		ArrayList<ItemInventoryMap> itemInventoryMaps = new ArrayList<ItemInventoryMap>();
		for(int i = 0 ; i < ItemInventoryManager.getInstance().getSize() ; i++){
			itemInventoryMaps.add(ItemInventoryManager.getInstance().getItemInventory(i));

		}

		int minScore = Integer.MAX_VALUE;
		ArrayList<ItemInventoryMap> result = new ArrayList<ItemInventoryMap>();
		int index = 0;
		while(index < data.size()){
			String str1 = data.get(index);
			ItemInventoryMap top = itemInventoryMaps.get(0);
			//result.add(index, str1);
			for(int i = 0 ; i < itemInventoryMaps.size() ; i++){
				int temp = editDistance(str1, itemInventoryMaps.get(i).getItemInventory().getName());
				if(temp < minScore){
					minScore = temp;
					top = itemInventoryMaps.get(i);
				}
			}
			result.add(index, top);
			minScore = Integer.MAX_VALUE;
			index++;
		}

		return result;

	}
	
	public static String findWord(String data) throws FileNotFoundException{
		File f = new File("/Users/boyburin/CS_KMUTT/SeniorProject/myTrainData/Test/mytest2text3.txt");
		Scanner sc = new Scanner(f);
		String[] myword = new String[10];
		for(int i = 0 ; i <10 ; i++){
			myword[i] = sc.nextLine();
		}
		int min = Integer.MAX_VALUE;
		String result = "";
		for(int i = 0 ; i < myword.length; i++){
			int temp = editDistance(data, myword[i]);
			if(temp < min){
				min = temp;
				result = myword[i];
			}
		}
		return result;
	}
	
	public static int editDistance(String str1, String str2){
		str1 = " " + str1.toLowerCase();
		str2 = " " + str2.toLowerCase();
		int[][] table = new int[str2.length()][str1.length()];
		for(int i = 0 ; i < table[0].length ; i++){
			table[0][i] = i;
		}
		for(int i = 0 ; i < table.length ; i++){
			table[i][0] = i;
		}

		for(int i = 1 ; i < table.length ; i++){
			for(int j = 1 ; j < table[0].length ; j++){
				if(str1.charAt(j) == str2.charAt(i)) table[i][j] = table[i-1][j-1];
				else{
					table[i][j] = Math.min(table[i-1][j-1], Math.min(table[i-1][j], table[i][j-1])) + 1;
				}
			}
		}
		
		int edit = table[str2.length() - 1][str1.length() - 1];
		//System.out.println(str2);
		/*for(int i = 0 ; i < table.length ; i++){
			for(int j = 0 ; j < table[0].length ; j++){
				System.out.print(table[i][j]);
			}
			System.out.println("");
		}*/
		//System.out.println("Edit = " + edit);
		return edit;
	}

}
