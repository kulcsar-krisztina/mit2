package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
// import hu.bme.mit.yakindu.analysis.RuntimeService;
// import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		
		Scanner scanner = new Scanner(System.in);
        print(s);
        
        
        while (scanner.hasNext()) {
        	String string = scanner.nextLine(); 
            System.out.println("\n");

            if (string.equals("start")) {
            	s.raiseStart();
            	s.runCycle();
            }
            
            if (string.equals("white")) {
            	s.raiseWhite();
        		s.runCycle();
             }
            
            if (string.equals("black")) {
            	s.raiseBlack();
        		s.runCycle();
             }
            
            if (string.equals("exit")) {
            	System.exit(0);
             }
            
            print(s);
         }
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
