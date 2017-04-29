package aqua;
import java.util.Arrays;
import java.lang.*;
import java.util.Objects;
import java.util.ArrayList;

public class Fish implements Cloneable{
    String name;
    ArrayList<Integer> initPosition;
    int mobilityTime;
    int dimensions[];
    String imageName;

    public Fish(String name, ArrayList<Integer> initPosition, int mobilityTime, int dimensions[], String imageName){
	this.name=name;
	this.initPosition=new ArrayList<>(initPosition);
	this.dimensions=Arrays.copyOf(dimensions, 2);
	this.mobilityTime=mobilityTime;
	this.imageName=imageName;
    }

	
    @Override
    public Object clone() throws CloneNotSupportedException{
	return super.clone();
    }

    @Override
    public boolean equals(Object o){
	if (o instanceof Fish) {
	    Fish fish = (Fish) o;
	    if (fish.name==this.name){
		return true;
	    }
	}
	return false;
    }


    @Override
    public int hashCode(){
	return Objects.hash(name);
    }
};
    
