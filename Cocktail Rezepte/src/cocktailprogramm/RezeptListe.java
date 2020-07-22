package cocktailprogramm;
import java.io.Serializable;
import java.util.ArrayList;
public class RezeptListe implements Serializable {
	
	 ArrayList<Rezept> rezeptListe;
	 ArrayList<Zutaten> alleZutaten;
	 
	 public RezeptListe(){ 
		 rezeptListe = new ArrayList<Rezept>();
		 alleZutaten = new ArrayList<Zutaten>();
	 }
	 public String addRezept(Rezept r) { 
		 if(!rezeptListe.add(r)){return "Es gab ein Problem beim hinzufügen des Rezepts.";} else{return "";} 
	 }
	 public boolean removeRezept(String name) 
	 {
		 for (int a = 0; a<rezeptListe.size();a++) 
		 { 
			 if (rezeptListe.get(a).getName().toLowerCase().equals( 
					 name.toLowerCase())) 
			 { 
				 rezeptListe.remove(a);
				 return true; 
			 }
		 } 
		 return false; 
	 }
	 public Rezept getRezept (int a){return rezeptListe.get(a);}
	 public Rezept getRezept (String name) 
	 { 
		 for (int i=0;i<rezeptListe.size();i++){ 
			 if (name.toLowerCase().equals( 
					 rezeptListe.get(i).getName().toLowerCase())){ 
				 return rezeptListe.get(i); 
			 } 
		 } 
		 return null; 
	 }
	 public ArrayList<Rezept> getAlleRezepte(){return rezeptListe;}
	 public ArrayList<Zutaten> getAlleZutaten(){return alleZutaten;}
}
