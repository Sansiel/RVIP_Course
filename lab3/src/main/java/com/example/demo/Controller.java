package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lab")
public class Controller {
	
	@Autowired
	InLineElementsSum inLineElementsSum;

	@Autowired
	Generator generator;
	
	@GetMapping
	public String res() throws InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();

		int x = 1000;
		int y = 1000;
		int[][] arr = generator.generateArray(x, y);

		int ans = 0;
		List<Future<Integer>> futureElements = new ArrayList<Future<Integer>>();

		for (int i = 0; i < x; i++) {
			futureElements.add(inLineElementsSum.calculate(arr[i]));
		}

		for (int i = 0; i < x; i++) {
			ans += futureElements.get(i).get();
		}

		long stop = System.currentTimeMillis();
		
		return( "Answer = " + ans + " Time = "+(stop - start));
	}
}
