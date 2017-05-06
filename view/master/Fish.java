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
    int started;
    int imageIndex;
    double actualPosition[];
    
    

    public Fish(String name, ArrayList<Integer> initPosition, int mobilityTime, int dimensions[],int started, double actualPosition[]){
	this.name=name;
	this.initPosition=new ArrayList<>(initPosition);
	this.dimensions=Arrays.copyOf(dimensions, 2);
	this.mobilityTime=mobilityTime;
	this.started = started;
	this.actualPosition=actualPosition;
    }

    /** @brief  clone
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     Object
     *
     * @details    Create a clone of the instance super
     *
     */

    @Override
    public Object clone() throws CloneNotSupportedException{
	return super.clone();
    }

    /** @brief  equals
     * @param[in]  Object o
     *
     * @param[out] NONE
     *
     *
     * @return     boolean
     *
     * @details    Test if 2 fishes have the same name
     *
     */

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


    /** @brief  hashCode
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     int
     *
     * @details    return a hash code
     *
     */

    
    @Override
    public int hashCode(){
	return Objects.hash(name);
    }


    /** @brief  start
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details  start a fish
     *
     */

    public void start(){
	started = 1;
    }

};
    
