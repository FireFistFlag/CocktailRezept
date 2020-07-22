package cocktailprogramm;
import	java.awt.event.WindowEvent;
import	java.io.*;
import	java.text.DecimalFormat;
import	java.text.NumberFormat;
import  java.util.*;
import 	java.util.logging.Level;
import	java.util.logging.Logger;
import	javax.swing.*;
import  java.awt.event.ActionListener;

public class ListeGUI extends javax.swing.JFrame implements Serializable {

	//Globale Variablen initialisieren für die Shopping liste
	
	DefaultListModel model = new DefaultListModel();
	
	DefaultListModel modelRezepte = new DefaultListModel();
	File e;
	RezeptListe rezeptListe;
	RezeptListe gewählteRezeptListe;
	String num;
	
	public static void main(String args[]) {
		if (args.length >1) {
			System.out.println("Bitte Argument eingeben");
			System.exit(0);
		}
		else if (args.length == 0) {   //kein argument
			Object keinArg = JOptionPane.showInputDialog(new JFrame(), "Bitte geben Sie einen Namen für die Datei an, welche geladen werden soll", "Lade Rezeptemacher",
					JOptionPane.INFORMATION_MESSAGE, //art der Meldung
					null,null,null);
			
			if (keinArg == null) {
				System.exit(0);
			}
			else {
				if (keinArg.toString().endsWith(".dat") || keinArg.toString().endsWith(".csv")) {
					File e = new File (keinArg.toString());
					if (e.exists()) {
						new ListeGUI(keinArg.toString()).setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(), "Bitte öffnen Sie eine existierende Datei", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.out.println("Bitte öffnen sie eine existierende File");
						System.exit(0);
					}
				}
				}
			}
		 else {
			if (args[0].endsWith(".dat")) {
				new ListeGUI(args[0]).setVisible(true);
			}
			else if (args[0].endsWith(".csv")){
				new ListeGUI(args[0]).setVisible(true);
			}
			else {
				System.out.println("Geben sie eine csv oder dat Datei an");
				System.exit(0);
			}
			}
		}
	
		public ListeGUI (String args) {
			e= new File (args);
			rezeptListe = new RezeptListe();
			if  (args.endsWith(".dat")) {
				if (e.exists()) {
					ObjectInputStream in = null;  
				try {
					in= new ObjectInputStream(new FileInputStream(e));
					
					rezeptListe = (RezeptListe)in.readObject();
				}
				catch (IOException ex) {
					System.out.println("Die .dat Datei konnte nicht geladen werden bitte versuchen Sie es erneut");
					Logger.getLogger(ListeGUI.class.getName()).log(Level.SEVERE, null,ex);
				}
				catch (ClassNotFoundException ex) {
					System.out.println("Die .dat konnte nicht geladen werden, bitte versuchen Sie es erneut");
					Logger.getLogger(ListeGUI.class.getName()).log(Level.SEVERE, null, ex);
				}
				finally {
					try {
						in.close(); //schließet IO
					}
					catch (IOException ex) {
				
			System.out.println("Es gab ein Problem mit dem schließen der Datei");
			Logger.getLogger(ListeGUI.class.getName()).log(Level.SEVERE,null, ex);
		}
		}
		}
		}
		else if(args.endsWith(".csv")) 
		{
			if (e.exists()) {
				try {
					readCSV (e);
				} 
				catch (FileNotFoundException ex) {
					System.out.println("Die csv Datei konnte nicht geladen werden, bitte versuchen Sie es erneut");
					Logger.getLogger(ListeGUI.class.getName()).log(Level.SEVERE,null, ex);
				}
				catch (IOException ex) {
					System.out.println("Die .csv datei konne nicht geladen werden bitte versuchen Sie es erneut");
					Logger.getLogger(ListeGUI.class.getName()).log(Level.SEVERE,null,ex);
				}
			}
		}
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				
			}
			catch (Exception ex) {
				Logger.getLogger(RezeptGUI.class.getName()).log(Level.SEVERE,null,ex);
			}
			initComponents();
			showList();
			toggleEnabled();
			select(0);
			
		}
		public void readCSV(File e) throws FileNotFoundException, IOException{
			
			String thisLine;
			
			BufferedReader reader = new BufferedReader (new InputStreamReader(new FileInputStream(e)));
			String nameField = null;
			Rezept r = null;
			
			while((thisLine = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(thisLine, ",");
				
				String temp = st.nextToken();
				
				if(!st.hasMoreTokens()) {
					
					if (!temp.contentEquals("---------")) {
					nameField = temp;
					
					r= new Rezept (nameField, rezeptListe.getAlleZutaten());
					}
					
					else { 
						rezeptListe.addRezept(r); 	
					}
				}
				while (st.hasMoreTokens()) {
					String quant = st.nextToken();
					double quants = Double.parseDouble (quant);
					String meas = st.nextToken();
					
					r.AddZutaten(new Zutaten(temp,quants,meas));
					
				}
			}
			
		}
		
		public void showError (String message) {
			JOptionPane.showMessageDialog(new JFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		public void showList() {
			modelRezepte.removeAllElements();
			
			for(int a = 0; a< rezeptListe.getAlleRezepte().size(); a++) {
				modelRezepte.add(a, rezeptListe.getRezept(a).getName());
			}
			
		}
		
		public void select(int a) {
			if(a == 0) {
				if(!modelRezepte.isEmpty()) {
					rezeptDisplay.addSelectionInterval(0, 0);
				}
				if (!model.isEmpty()) {
					displayList.addSelectionInterval (0, 0);
					
				}
				else {
					if (!modelRezepte.isEmpty()) {
						rezeptDisplay.addSelectionInterval(0,0);
					}
					if (!model.isEmpty()) {
						displayList.addSelectionInterval(model.getSize() - 1, model.getSize() - 1);
					}
				}
			}
		}
			
			
		//ändert die enabled funktion für verschiedene Buttons, z.b um auswählen zu verhindern wenn es kein Rezept in der Liste gibt.
			public void toggleEnabled() {
				if (rezeptListe.getAlleRezepte().size()== 0) {
					listButton.setEnabled(false);
					selectButton.setEnabled(false);
					removeButton.setEnabled(false);
				} else {
					selectButton.setEnabled(true);
					if (!model.isEmpty()) {
						listButton.setEnabled(true);
						removeButton.setEnabled(true);
					} else {
						listButton.setEnabled(false);
						removeButton.setEnabled(false);
					}
				}
			}
		public void exit () {
			int option = JOptionPane.showConfirmDialog(new JFrame(), "Sind Sie sicher, dass Sie das Programm beenden wollen?","Beenden bestätigen", JOptionPane.OK_CANCEL_OPTION);
			
			if (option == 0) {
				processEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
				
			}
		}
		
		public void ueber() {
			JOptionPane.showMessageDialog(new JFrame(), "Geschrieben von Toni Zubac 79441 und Kim Niklas Neuhäusler 80045", num, JOptionPane.INFORMATION_MESSAGE, null);
			
		}
		public void select(){
			Object c= JOptionPane.showInputDialog(new JFrame(), "Wie viele Gläser:", "Zum menü hinzufügen",JOptionPane.INFORMATION_MESSAGE,null,null,2);
			int a= 0;
			if ( c== null ) {
				return;
			}else {
				num = (String) c;
			}
			if (num.trim().contentEquals("")) {
				showError("Geben Sie eine Nummer an bitte");
				return;
			}
			
			int numInt = 0;
			try {
				numInt = Integer.parseInt(num);
			} catch (NumberFormatException e) {
				showError("Bitte geben Sie eine Zahl ein.");
				return;
			}
			if (numInt<1) {
				showError("Bitte geben Sie eine Zahl größer 1 an.");
				return;
			}
			String name = "";
			boolean inlist = false;
			Object[] obj = null;
			int pos = 0;
			String person = "Mensch";
			
			if (numInt == 1) {
				person = "person";
			}
			if (!model.isEmpty()) {
				for ( a= 0; a< model.getSize(); a++);{
					obj = model.get(a).toString().split(":");
					name = (String) obj[0];
					
					if (rezeptDisplay.getSelectedCalue().toString().toLowerCase().equals(name.toLowerCase())) {
						inlist = true;
						pos=a;
					}
				}
				if (inlist){
					String amount = (String) obj [1];
					Object [] obj2 = amount.toString().split("\\s");
					String amount2 = (String) obj2[2];
					
					int numOfPeople = Integer.parseInt(amount2);
					int numVar = Integer.parseInt(num) + numOfPeople;
					
					model.set(pos,rezeptDisplay.getSelectedValue().toString() + ": " +numVar +person);
					
				}
				else {
					model.addElement(rezeptDisplay.getSelectedValue().toString() + ": " + num +person);
				}
			}
			else {
				model.addElement(rezeptDisplay.getSelectedValue().toString() + ": "+num + person);
				
			}
			select (1);
			toggleEnabled();
		}
		public void remove () {
			if (removeButton.isEnabled()) {
				model.remove(displayListe.getSelectedIndex());
				toggleEnabled();
				select(0);
			}
		}
		
		public void createList() {
			if (listButton.isEnabled()) {
				String message = "";
				ArrayList<Zutaten> FullList = new ArrayList <Zutaten>();
				
				for (int a = 0; a< model.getSize(); a++) {
					Object [] obj = model.get(a).toString().split(":");
					String name = (String) obj[0];
					Object [] obj2 = obj[1].toString().split("\\s");
					String amount = (String) obj2[1];
					
					int numOfPeople = Integer.parseInt(amount);
					
					ArrayList<Zutaten> ingList = rezeptListe.getRezept(name).getListe();
					
					for (int j = 0; j< ingList.size();j++) {
						String IngName = ingList.get(j).getName();
						double d = ingList.get(j).getMenge();
						d= d * numOfPeople;
						String mengeneinheit = ingList.get(j).getMengeneinheit();	
						Zutaten newZutat = new Zutaten (IngName, d, mengeneinheit);
						
						if (FullList.contains(newZutat)) {
							int pos = 0;
							for (int k = 0; k< FullList.size(); k++) {
								if (FullList.get(k).equals(newZutat)) {
									pos = k;
								}
							}
							double tempQ= d+ FullList.get(pos).getMenge();
							FullList.set(pos,new Zutaten(IngName,tempQ, mengeneinheit));
							
						} else {
							FullList.add(newZutat);
						}
					}
				}
				
				for ( int z= 0; z<FullList.size();z++) {
					message += FullList.get(z).getName() + ": ";
					String s = "" + FullList.get(z).getMenge();
					
					if(s.endsWith(".0")) {
						NumberFormat formatter = new DecimalFormat ("#0");
						message += formatter.format(FullList.get(z).getMenge()) + "";
					} else {
						message += FullList.get(z).getMenge() + "";
					}
					message+= FullList.get(z).getMengeneinheit();
					message +="\n";
				}
				
				JOptionPane.showMessageDialog(new JFrame (), message, "Zutatenliste", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
}

