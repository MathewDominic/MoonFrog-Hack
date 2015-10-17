package com.myappstack.gball;

import com.myappstack.gball.actors.Food;

public class PowerupsManager {
	//private Food.FoodType foodType;
	private int nextPowerupAfter;
	
	public PowerupsManager(){
		//this.foodType = Food.FoodType.NORMAL;
		this.nextPowerupAfter = 5;
	}
	
	public boolean powerup(){
		boolean ret;
		nextPowerupAfter--;
		if(nextPowerupAfter <= 0){
			ret = true;
			//make random
			nextPowerupAfter = 5;
		}
		else{
			ret = false;
		}
		return ret;
	}
}
