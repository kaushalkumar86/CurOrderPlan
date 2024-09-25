package com.kaushal.cutorderplan.markercalculation;

import java.util.ArrayList;

public class MarkerResult {
	public static MarkerResult LAST_CALCULATED_RESULT;
	
	public int count;
	public int contigency;
	public int maxPlies;
	public int maxGarment;
	
	private boolean check;
	private boolean stopper;
	
	public String styleName;
	public ArrayList<String> name;
	public ArrayList<String> layBreak;
	public ArrayList<Integer> quant;
	public ArrayList<Integer> temp_quant;
	public ArrayList<Integer> position;
	public ArrayList<Integer> soluNum;
	public ArrayList<Integer> layPlies;
	
	public ArrayList<Row> Result;
	
	
	public void beginCalc(){
		
		Result = new ArrayList<Row>();
		layPlies = new ArrayList<Integer>();
		layBreak = new ArrayList<String>();
		
		check = false;
		stopper = false;
		
		int minPosi = getMinValue(quant);
		count = quant.size();
		temp_quant = new ArrayList<Integer>(quant);
		//int test = getGCD(1200, 1000);
		//Log.v("Testing", test + "");
		findFracRatio(minPosi, 1);
		findLayBreakUp();
		
	}
	
	private void findLayBreakUp(){
		int resulNum = layPlies.size();
		for(int i = 0; i < resulNum; i++){
			String lybreak;
			int mainLay = layPlies.get(i)/maxPlies;
			if(mainLay > 0){
				lybreak = "(" + mainLay + " X " + maxPlies + ")";
				int remenbrk = layPlies.get(i) % maxPlies;
				if(remenbrk > 0){
					lybreak = lybreak + " (1 X " + remenbrk + ")";
				}
			}
			else{
				lybreak = "(1 X " + layPlies.get(i) + ")";
			}
			layBreak.add(lybreak);
			//Log.v("Testing", "LayBreak "+ layBreak.get(i));
		}
	}
	
	
	
	private void findFracRatio(int minPosi, int ratioMultiplier){
		if(stopper == false){
			check = false;
			for(int i = 0; i < count; i++){
				if ((i != minPosi) && (temp_quant.get(i) != 0)){
					int a = temp_quant.get(i);
					int b = temp_quant.get(minPosi);
					int gcd = getGCD(a, b);
					if(gcd > 1){
						int sRatio = (temp_quant.get(i) / gcd) + (temp_quant.get(minPosi) / gcd);
						if (sRatio == maxGarment){
							Row rowadd = new Row(count);
							rowadd.cells[i] = (temp_quant.get(i) / gcd);
							rowadd.cells[minPosi] = (temp_quant.get(minPosi) / gcd);
							Result.add(rowadd);
							layPlies.add(gcd);
							temp_quant.set(i, 0);
							temp_quant.set(minPosi, 0);
							//Log.v("Testing", gcd + "");
							check = true;
							break;
						}
					}
				}
			}
			if (check){
				findFracRatio(getMinValue(temp_quant), ratioMultiplier);
			}
			else{
				conditionTwo(ratioMultiplier);
			}
		}
		else{
			return;
		}
	}
	
	
	private void conditionTwo(int ratioMultiplier){
		if(stopper == false){
			check = false;
			boolean funcCheck = false;
			sortAscend();
			int[] tRatio = new int[count];
			int sRatio = 0;
			for(int x = 0; x < count; x++){
				sRatio = 0;
				for(int k = 0; k < count; k++){
					tRatio[k] = 0;
				}
				//Log.v("Testing", "Reload");
				if ((funcCheck == false) && (temp_quant.get(position.get(x)) > 0)){
					
					for(int y = 0; y < count; y++){
						
						tRatio[y] = temp_quant.get(position.get(y)) / temp_quant.get(position.get(x));
						tRatio[y] = tRatio[y] * ratioMultiplier;
						sRatio += tRatio[y];
						
						
						if(sRatio >= maxGarment){
							Row rowadd = new Row(count);
							
							int remen = temp_quant.get(position.get(x)) % ratioMultiplier;
							if (remen > 0){
								remen = (temp_quant.get(position.get(x)) / ratioMultiplier) + 1;
							}
							else{
								remen = (temp_quant.get(position.get(x)) / ratioMultiplier);
							}
							int garmentNum = remen;
							layPlies.add(garmentNum);
							//Log.v("Testing", "garment "+ garmentNum);
							
							int tempMaxGarm = maxGarment;
							for (int z = 0; z < count; z++){
								int ratio = tRatio[z] < tempMaxGarm ? tRatio[z] : tempMaxGarm;
								int newQuan = (temp_quant.get(position.get(z)) - (garmentNum * ratio)) > 0 ?(temp_quant.get(position.get(z)) - (garmentNum * ratio)) : 0;
								temp_quant.set(position.get(z), newQuan);
								rowadd.cells[position.get(z)] = ratio;
								tempMaxGarm = tempMaxGarm - ratio;
								
							}
							Result.add(rowadd);
							check = true;
							funcCheck = true;
							break;
							
						}
					}
				}else if(funcCheck == true){
					break;
				}
			}
			if(check){
				findFracRatio(getMinValue(temp_quant), ratioMultiplier);
			}else{
				conditionTwo(ratioMultiplier + 1);
			}
		}
		else{
			return;
		}
		
	}
	

	
	
	
	
	private void sortAscend(){
		for(int x = 0; x < count; x++){
			for(int y = 0; y < (count - 1); y++){
				if(temp_quant.get(position.get(y)) > temp_quant.get(position.get(y+1)) ){
					int temp = position.get(y+1);
					position.set(y+1, position.get(y));
					position.set(y, temp);
				}
			}
			
		}
	}
	
	private int getGCD(int a, int b){
		int x;
		int y;
		while(a%b != 0){
			x = b;
			y = a%b;
			a = x;
			b = y;
		}
		return b;
		/*if(a == b){
			return a;
		}
		if(a > b){
			return getGCD((a-b) , b);
		}else{
			return getGCD(a, (b-a));
		}*/
	}
	
	private int getMinValue(ArrayList<Integer> numbers){
		int minValue = numbers.get(0);
		int minposi = 0;
		int j = 0;
		while (minValue <= 0){
			if(j >= count){
				stopper = true;
				break;
			}else{
				minValue = numbers.get(j);
				minposi = j;
				j++;
			}
		}
		
		for(int i=1;i<numbers.size();i++){  
			if ((numbers.get(i) < minValue) && (numbers.get(i) != 0 )){  
				minValue = numbers.get(i);
				minposi = i;
		    }  
		}  
		return minposi;  
	}
	
	 
	
}
