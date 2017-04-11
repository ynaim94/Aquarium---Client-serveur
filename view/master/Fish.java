package aqua;
import java.util.Arrays;
import java.lang.*;
import java.util.Objects;

public class Fish implements Cloneable{
    String name;
    int initPosition[];
    int mobilityTime;
    int dimensions[];

    public Fish(String name, int initPosition[], int mobilityTime, int dimensions[]){
	this.name=name;
	this.initPosition=Arrays.copyOf(initPosition, 2);
	this.dimensions=Arrays.copyOf(dimensions, 2);
	this.mobilityTime=mobilityTime;
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
    
