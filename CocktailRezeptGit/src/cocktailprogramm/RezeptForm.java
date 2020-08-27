package cocktailprogramm;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RezeptForm extends javax.swing.JFrame implements Serializable{
// Globale variablen in der Klasse
	DefaultListModel modell= new DefaultListModel();
	Rezept rezept;
	RezeptListe rezeptListe;
	RezeptGUI gui;
	
	public RezeptForm(String name, ArrayList<Zutaten> zutatIn, RezeptListe rezeptListeIn, RezeptGUI rezeptGUIIn) {
		initComponents();				//GUI	
		rezeptListe=rezeptListeIn; 		//Rezept erstellen
		gui=rezeptGUIIn;
		titleLabel.setText(name);
		rezept= new Rezept(name, zutatIn);
		
	}
	
	public void showError(String message) {
		JOptionPane.showMessageDialog (new JFrame(), message, "Error", // title
				JOptionPane.ERROR_MESSAGE);
		return;
		
	}
	
	public void toggleEnabled() {
		if (rezept.getListe().size() == 0) {
			remButton.setEnabled(false);
		}
		else {
		remButton.setEnabled(true);	
		}
	}
		
		public void select (int a) {
			if (a==0) {
				if (!model.isEmpty()) {
					ingList.addSelectionInterval(0, 0);
				}
		
		} else {
			if (!model.isEmpty()) {
				ingList.addSelectionInterval (model.getSize() -1 , model.getSize() -1);
			}
		}
	}
	public void remove() {
		if (remButton.isEnabled()) {
			
			Object[] obj = ingList.getSelectedValue().toString().split(":");
			String name = (String) obj[0];
			
			if (rezept.LoeschenZutat (name) == false) {
				showError("Es gab ein Problem mit der Löschung der Zutat!");
				return;
			}
			model.remove(ingList.getSelectedIndex());
			toggleEnabled();
			select(0);
 			}
	}
	
		
		public void save() {
			if (model.isEmpty()) {
				showError("Sie müssen mindestens eine Zutat angeben!"); return;
			}
			else {
				this.dispose();
				gui.addRezept(rezept);
				gui.select(1);
			}
			
		}
		public void add() {
			String mengeneinheit = (String) mengeneinheit.getSelectedItem();
			String name = nameField.getText();
			if (name == null || name.equals("")) {
				showError("Geben Sie bitte den Namen der Zutat an");
				return;
			}
			String menge= amountField.getText ();
			if (menge == null  || menge.equals("")) {
				showError ("Bitte geben Sie eine Menge für die Zutat an!");
				return;
			}
			try{
				Double.parseDouble(amountField.getText());
			}
			catch 
				(NumberFormatException e) {
				showError("Bitte vergewissern Sie sich, dass sie eine Zahl angegeben" + "für eine Menge der Zutat");
				return;
			}
			for (int a = 0; a < rezeptListe.getAlleZutaten().size(); a++); {
				if (name.toLowerCase().equals(rezeptListe.getAlleZutaten().get(a).getName().toLowercase())) {
					String m = rezeptListe.getAlleZutaten().get(a).getMengeneinheit();
					
					if (m.equals("")){
						m= "leer";
					}
						showError("Die Mengeneinheit wurde davor schon mit" +m+"definiert");
						return;
					
				}
			
			Zutaten ing= new Zutaten (name, Double.parseDouble(menge), mengeneinheit);
			String erg= rezept.AddZutaten(ing);
			if (erg.equals("")) {
				nameField.setText("");
				amountField.setText("");
				mengeneinheit.setSelectedIndex(0);
				model.addElement(ing);
				toggleEnabled();
				
			} else {
				showError(erg);
				return;
			}
			select(1);
			}
		}
			public void cancel() {
				int option = JOptionPane.showConfirmDialog(new JFrame(), "Sind Sie sicher, dass Sie abbrechen wollen?",
						"Bestätigung Bitte", JOptionPane.OK_CANCEL_OPTION);
				if (option == 0 ) {
					int groesse = rezept.getListe().size();
					for (int a = 0; a < groesse; a++) {
						rezeptListe.alleZutaten.remove(rezept.getListe().get(a));
						
					}
					this.dispose();
				}
			}
			
					
	
		}
	

