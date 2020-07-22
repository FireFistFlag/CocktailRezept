package cocktailprogramm;

import java.awt.event.WindowEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class RezeptGUI extends JFrame implements Serializable
{
	
	DefaultListModel model = new DefaultListModel();
	RezeptListe rezeptListe;
	File e;
	Object nameEnter;
	
	public static void main (String args[])
	{
		if (args.length==0) {
			JOptionPane.showMessageDialog (
				new JFrame(), "Sie haben kein Argument angegeben, deshalb wurde eine Datei " +" namens rezepte.dat erstellt.",
				"Information",
				JOptionPane.INFORMATION_MESSAGE);
			System.out.println("Sie haben kein Argument angegeben, deshalb wurde eine Datei " +" namens rezepte.dat erstellt.");
			new RezeptGUI("rezepte.dat") .setVisible(true);	
		}
		else if (args.length > 1) {
			System.out.println ("Geben Sie ein Argument ein");
			System.exit(0);
		} else {
			if (args[0].endsWith(".dat")) {
				new RezeptGUI (args[0]).setVisible(true);
			}
			else if (args[0].endsWith(".csv")) {
				new RezeptGUI (args[0]).setVisible(true);
			}
			else {
				System.out.println("Bitte geben Sie eine .csv Datei oder eine .dat Datei ein");
				System.exit(0);
			}
		}
	}
	public RezeptGUI(String args)
	{
		e = new File (args);
		rezeptListe = new RezeptListe ();
		if (args.endsWith(".dat")) {
			if (e.exists())
			{
				ObjectInputStream in = null;
				try
				{
					in = new ObjectInputStream (new FileInputStream (e));
					rezeptListe = (RezeptListe) in.readObject();
					
				} catch (IOException ex)
				{
					System.out.println ("Die .dat Datei konnte nicht geladen werden, " +" bitte versuchen Sie es erenut.");
					Logger.getLogger (RezeptGUI.class.getName()).log (
							Level.SEVERE, null, ex);
				}
				catch (ClassNotFoundException ex) {
					System.out.println("Die .dat Datei konnte nicht geladen werden, " +" bitte versuchen Sie es erenut.");
					Logger.getLogger(RezeptGUI.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				finally {
					try {in.close();}
					catch (IOException ex) {
						System.out.println("Es gab einen Fehler beim schließen der Datei.");
						Logger.getLogger(RezeptGUI.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
		}
		else if (args.endsWith(". csv"))
		{
			if (e.exists()) {
				try {
					readCSV(e);
				} catch (FileNotFoundException ex)
				{
					System.out.println ("Die .csv Datei konnte nicht geladen werden, " +" bitte versuchen Sie es erenut.");
					Logger.getLogger(RezeptGUI.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (IOException ex)
				{
					Logger.getLogger(RezeptGUI.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}
		try
		{
			UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			Logger.getLogger(RezeptGUI.class.getName())
				.log(Level.SEVERE, null, ex);
		}
		initComponents();
		showList();
		toggleEnabled();
		select(0);
		addAlleZutaten();
	}
	public void readCSV (File e) throws FileNotFoundException, IOException
	{
		String thisLine;
		BufferedReader reader = new BufferedReader (new InputStreamReader (
				new FileInputStream (e)));
		String nameField = null;
		Rezept r = null;
		while ((thisLine = reader.readLine()) !=null)
		{
			StringTokenizer st = new StringTokenizer (thisLine, ",");
			String temp = st.nextToken();
				if(!st.hasMoreTokens()) {
					if (!temp.equals("---------")) {
						nameField = temp;
						r=new Rezept(nameField, rezeptListe.getAlleZutaten());
					}
					else {rezeptListe.addRezept (r);}
				}
				while (st.hasMoreTokens()){
					String quant = st.nextToken(); 
					double quants = Double.parseDouble(quant); 
					String meas = st.nextToken();
					r.AddZutaten(new Zutaten(temp, quants, meas));
				}
		}
	}
	public void addRezept(Rezept r){ 
		String res = rezeptListe.addRezept(r); 
		if (res.equals("")){ 
			showList(); 
			select(0); 
			toggleEnabled();  
			addAlleZutaten();
		} 
		else{ 
			showError(res);
		}
	}
	public void addAlleZutaten()
	{
		rezeptListe.getAlleZutaten().clear();
		for (int a = 0; a < rezeptListe.getAlleRezepte().size(); a++){ 
			for (int j = 0; j < rezeptListe.getRezept(a).getListe().size(); j++){
				rezeptListe.getAlleZutaten().add(
						rezeptListe.getRezept(a).getListe().get(j));	
			}
		}
	}
	public void showList()
	{ 
		model.removeAllElements();
		for (int i = 0; i < rezeptListe.getAlleRezepte().size(); i++){ 
			model.add(i, rezeptListe.getRezept(i).getName());
		}
	}
	public void toggleEnabled()
	{ 
		
		if (rezeptListe.getAlleRezepte().size() == 0){
			saveButton.setEnabled(false); 
			ViewSummary.setEnabled(false); 
			deleteButton.setEnabled(false); 
			MENUsave.setEnabled(false); 
		} else 
		{ 
			saveButton.setEnabled(true); 
			ViewSummary.setEnabled(true); 
			deleteButton.setEnabled(true); 
			MENUsave.setEnabled(true); 
		}
	}
	public void save()
	{ 
		if (saveButton.isEnabled()){
			ObjectOutputStream out; 
			PrintWriter pw = null; 
			try 
			{ 
				out = new ObjectOutputStream(new FileOutputStream("rezepte.dat")); 
				out.writeObject(rezeptListe); 
				out.close(); 
				JOptionPane.showMessageDialog( 
						new JFrame(), "Die Rezeptlsite wurde gespeichert.", "Speichern",  
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) 
			{ 
				showError("Die Liste konnte nicht gespeichert werden, da die Datei noch offen ist");
				Logger.getLogger(RezeptGUI.class.getName()).log(Level.SEVERE,null,ex); 
				return;
			} 
			try
			{
				pw = new PrintWriter(new FileOutputStream("rezepte.csv"));
				for (int i = 0; i < rezeptListe.getAlleRezepte().size(); i++) {
					pw.printf(rezeptListe.getRezept(i).getName());
					for(int j=0; j < rezeptListe.getRezept(i).zutaten.size();j++) 
					{
						pw.printf("n" +
								rezeptListe.getRezept(i).zutaten.get(j).getName() + "," + 
								rezeptListe.getRezept(i).zutaten.get(j).getMenge() + ",");
						 if (rezeptListe.getRezept(i).zutaten.get(j).getMengeneinheit(). equals(""))
						 {
							 pw.printf(" ");
						 } else
						 {
							 pw.printf(rezeptListe.getRezept(i).zutaten.get(j). getMengeneinheit());
						 }
					}
					pw.printf("n---------n");
					pw.close();
				}
				pw.flush();
			} catch (Exception e)
			{
				showError("Es gab ein Problem beim speichern der .csv Datei"); 
				System.out.println(e);
			}
			}	
	}
	public void showError(String message)
	{
		JOptionPane.showMessageDialog( 
				new JFrame(), message, 
				"Fehler",
				JOptionPane.ERROR_MESSAGE);
		return;
	}
	public void select(int i)
	{
		if (i == 0){
			if (!model.isEmpty()){displayList.addSelectionInterval(0, 0);} 
			} else{
				if (!model.isEmpty()){ 
				displayList.addSelectionInterval(model.getSize()-1,model.getSize()-1);
			}
		}
	}
	public void loadRezeptForm(String name, ArrayList<Zutaten> zutatIn)
	{
		RezeptForm r = new RezeptForm(name, zutatIn, rezeptListe, this);
		r.setVisible(true);
	}
	public void exit(){ 
	int option = JOptionPane.showConfirmDialog(new JFrame(),
		"Sind Sie sicher, dass sie das Programm verlassen wollen?",
		"Beenden Bestätigen", JOptionPane.OK_CANCEL_OPTION);
	if (option == 0){
	processEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	}
	public void add(java.awt.event.ActionEvent evt){
	nameEnter = JOptionPane.showInputDialog(
			new JFrame(), 
			"Bitte geben Sie einen Rezeptnamen ein:",
			"Neues Rezept hinzufügen", 
			JOptionPane.INFORMATION_MESSAGE,
			null, null, null);
	if (nameEnter == null){return;}
	if (nameEnter.toString().trim().equals("")) {
		showError("Bitte geben Sie einen Rezeptnamen ein."); 
		AddRecipeButtonActionPerformed(evt);
	} else {
		for (int i = 0; i < rezeptListe.getAlleRezepte().size(); i++){
			if (rezeptListe.getRezept(i).getName().toLowerCase().equals( 
					nameEnter.toString().toLowerCase())){
				showError("Der Rezeptname \"" 
						+nameEnter+"\" ist schon in der Liste.");
				return;
			}
		}
		loadRezeptForm(nameEnter.toString(), rezeptListe.getAlleZutaten());
	}
	select(1);
	addAlleZutaten();
	}
	public void viewSummary(){
		if (ViewSummary.isEnabled())	
	{
	Rezept rec = rezeptListe.getRezept(displayList.getSelectedIndex());
	String message = "";
	for (int i = 0; i < rec.getListe().size(); i++)
	{
		message += rec.getListe().get(i).getName() + ": ";
		String s = "" + rec.getListe().get(i).getMenge();
		if (s.endsWith(".0")) {
			NumberFormat formatter = new DecimalFormat("#0");
			message += formatter.format(rec.getListe().get(i).getMenge())+"";
		} else {
			message += rec.getListe().get(i).getMenge() + "";
		}
		message += rec.getListe().get(i).getMengeneinheit();
		message += "n"; 
	}
	JOptionPane.showMessageDialog(
			new JFrame(), message, 
			"Summary of " + rec.getName(),
			JOptionPane.INFORMATION_MESSAGE);
	}
	}
	public void delete(){
		 if (deleteButton.isEnabled())
	{
	int option = JOptionPane.showConfirmDialog(new JFrame(), 
				"Sind Sie sicher, dass Sie das Rezept löschen wollen?", 
				"Löschen Bestätigen", JOptionPane.OK_CANCEL_OPTION);
	if (option == 0) { 
		Rezept rec = rezeptListe.getRecipe(displayList.getSelectedIndex());
		if(rezeptListe.removeRezept(rec.getName()))
		{
			showList();
			toggleEnabled(); 
			select(0); 
			addAlleZutaten();
		}
		else{showError("Es gab ein Problem beim Löschen des Rezepts"); }
	}
	}
	}
	public void about(){
		JOptionPane.showMessageDialog(
		new JFrame(),
		"Programmieren II Projekt SS 2020" +
		" \n " +
		"Kim Niklas Neuhäusler und Toni Zubac" +
		" \n " +
		"Matr. Nr.: 80045 und Matr. Nr.: 79441",
		  JOptionPane.INFORMATION_MESSAGE, null);
	}
}
