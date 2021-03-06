package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		List<EventDefinition> events = new ArrayList();
		List<VariableDefinition> variables = new ArrayList();
		
		int id  = 1;
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				
				if (state.getName() == "") {
					state.setName(""+ id);
					System.out.println("Uj nev: " + state.getName() + "\n");
					id++;
				}
				
				System.out.println(state.getName() + "\n");
				
				if (state.getOutgoingTransitions().size() == 0) {
					System.out.println("Csapda: " + state.getName() + "\n");
				}
				
				for (Transition t : state.getOutgoingTransitions()) {
					State newstate = (State) t.getTarget();
					System.out.println(state.getName() + "->" + newstate.getName() + "\n");
				}
			}
			
			if (content instanceof EventDefinition) {
				EventDefinition state = (EventDefinition) content;
				System.out.println("Event:" + state.getName() + "\n");
				events.add(state);
			}
			
			if (content instanceof VariableDefinition) {
				VariableDefinition state = (VariableDefinition) content;
				System.out.println("Variable: " + state.getName() + "\n");
				variables.add(state);
			}
			
		}
	
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\n" + 
				"\n" + 
				"import java.io.IOException;\n" + 
				"import java.util.Scanner;\n" + 
				"\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\n" + 
				"// import hu.bme.mit.yakindu.analysis.RuntimeService;\n" + 
				"// import hu.bme.mit.yakindu.analysis.TimerService;\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"public class RunStatechart {\n" + 
				"	\n" + 
				"	public static void main(String[] args) throws IOException {\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\n" + 
				"		s.setTimer(new TimerService());\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\n" + 
				"		s.init();\n" + 
				"		s.enter();\n" + 
				"		s.runCycle();\n" + 
				"		\n" + 
				"		Scanner scanner = new Scanner(System.in);\n" + 
				"		print(s);\n" + 
				"        \n" + 
				"        \n" + 
				"        while (scanner.hasNext()) {\n" + 
				"			String string = scanner.nextLine(); \n" + 
				"           System.out.println(\"\\n\");\n" + 
				"\n");
		
		for (int i = 0; i < events.size(); i++) {
			String var = events.get(i).getName();
			char ch = var.charAt(0);
			ch = Character.toUpperCase(ch);
			String string = ch + var.substring(1);
			
			System.out.print("            if (string.equals(\"" + var + "\")) {\n");
			System.out.print("            	s.raise" + string + "();\n");
			
			System.out.print("            	s.runCycle();\n" + 
					"            }\n" + 
					"            \n");
		}
				
				System.out.println( "            if (string.equals(\"exit\")) {\n" + 
				"            	System.exit(0);\n" + 
				"             }\n" + 
				"            \n" + 
				"            print(s);\n" + 
				"         }\n" + 
				"	}\n" + 
				"\n" + 
				"	public static void print(IExampleStatemachine s) {"); 
		
		for (int i = 0; i < variables.size(); i++) {
			String var = variables.get(i).getName();
			char ch = var.charAt(0);
			ch = Character.toUpperCase(ch);
			String string = ch + var.substring(1);
			System.out.println("		System.out.println(\"" + ch + " = \" + s.getSCInterface().get" + string + "());");
		}
		
				System.out.println("	}\n" + 
				"}\n" + 
				"");
	}
}
