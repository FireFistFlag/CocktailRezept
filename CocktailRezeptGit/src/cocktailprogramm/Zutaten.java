package cocktailprogramm;
import java.io.Serializable;
import java.text.*;

public class Zutaten implements Serializable{
	String name;
	double menge;
	String mengeneinheit;
	
	public Zutaten (String nameIn, double mengeIn, String mengeneinheitIn)
	{
		name = nameIn;
		menge = mengeIn;
		mengeneinheit = mengeneinheitIn;
	}
	public String getName() {return name;}
	public double getMenge() {return menge;}
	public String  getMengeneinheit () {return mengeneinheit;}
	public void setMenge(double qIn) {menge = qIn;}
	@Override
	public String toString() {
		String tempMenge = ""+menge;
		if (tempMenge.endsWith(".0")) {
		NumberFormat formatter = new DecimalFormat("#0");
		return name + ": "+ formatter.format(menge)+ " "+ mengeneinheit;
		}
		else {return name + ": " + menge +" "+ mengeneinheit;}
	}
	@Override
	public boolean equals(Object o)
	{
		Zutaten i = (Zutaten) o;
		if(name.toLowerCase().equals(i.getName().toLowerCase())) {return true;}
		else {return false;}
	
	}
}
// GIT HUB IST FÜR WICHSER