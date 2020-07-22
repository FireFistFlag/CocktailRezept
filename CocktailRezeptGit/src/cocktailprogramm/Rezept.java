package cocktailprogramm;
import java.io.Serializable;
import java.util.ArrayList;


public class Rezept implements Serializable {
	
	String name;
	ArrayList <Zutaten> zutaten; // Speicherung der Zutaten in dem Rezept
	
	ArrayList<Zutaten> allZutaten;
	
	public Rezept(String nameIn, ArrayList<Zutaten> allZutatenAlc ) {
		name = nameIn;
		allZutaten=allZutatenAlc;
		zutaten= new ArrayList<Zutaten>();
	}
	
	public Rezept (ArrayList<Zutaten> zutatIn, String nameIn) {
		name = nameIn;
		zutaten = zutatIn;
	}
	
	public String AddZutaten(Zutaten a) {
		if (zutaten.contains(a)) {
			return "Diese Zutat wurde bereits eingetragen.";
		}
		if(!zutaten.add(a)) {
			return "Es gab probleme mit den Hinzufügen der Zutat!";	
		}
		else {
			allZutaten.add(a);
			return"";
		}
	}
	
	public boolean LoeschenZutat (String name) {
		for (int a = 0; a<zutaten.size();a++) {
			if (zutaten.get(a).getName().toLowerCase().equals(name.toLowerCase())){
				allZutaten.remove(a);
				zutaten.remove(a);
				return true;
			}
		}
		return false;
	}
	public ArrayList<Zutaten> getListe() {
		return zutaten;
	}
	public String getName() {
		return name;
	}
}
