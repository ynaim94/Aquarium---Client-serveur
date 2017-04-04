package aqua;
import java.util.Arrays;


public class Fish {
    String name;
    int init_position[];
    int mobility_time;
    int dimensions[];

    public Fish(String name, int init_position[], int mobility_time, int dimensions[]){
	this.name=name;
	this.init_position=Arrays.copyOf(init_position, 2);
	this.dimensions=Arrays.copyOf(dimensions, 2);
	this.mobility_time=mobility_time;
    }

    
}
    
