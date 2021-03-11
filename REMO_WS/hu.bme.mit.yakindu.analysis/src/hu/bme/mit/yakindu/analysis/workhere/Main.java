package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

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
			
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
