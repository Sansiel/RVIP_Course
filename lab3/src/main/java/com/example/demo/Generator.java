package com.example.demo;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class Generator {

	public int[][] generateArray(int x, int y) {
		int[][] arr = new int[x][y];
		Random rnd = new Random();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = rnd.nextInt(10);
			}
		}
		return arr;
	}

}
