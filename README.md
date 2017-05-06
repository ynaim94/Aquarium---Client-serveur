#How to use fish tank simulation

##The controller

The file "controller.cfg" contains the default values for the configuration variables. You can change these variables by changing them directly on the file.
The file "aquarium.info" contains the default fish tank configuration. You can change it directly on the file or changing after launching the controller in the user prompt.

**To build the controller** : Go in the controller folder and compile the sources as below 
     
      > cd /controller/
      > make

**To launch the controller** : In the controller folder launch the controller
	
	  > ./serveur
       
After launching the controller, load the fish tank configuration file on the user prompt using the command:
        
      > load aquarium.info
        
You can also load any alternative fish tank by using load command. If the fish tank configuration is not loaded, no client can be accepted.

**To show the fish tank configuration use the command**:
        
      > show aquarium
        
**To add a view to the fish tank**:
        
      > add view  Nx view_x x view_y + view_width + view_height
     
  _x is a view number_
      
**To delete a view from the fish tank**:   
        
      > del view Nx
         
  _x is a view number_
         
**To save the fish tank configuration into a file**:

	  > save file_name

##The client

The file "affichage.cfg" contains the default values for the configuration variables.
The source files are in /view/src/. 
**To build the client** : 

	> cd /view/
	> make

**To launch the client** : 
        
    > make run 
    
  _in the /view/ folder_

**Fish names** : happyFish madFish smilingFish bubbleFish lostFish oldFish sneakingFish

**In the folder /view/build/** :
     aqua/ contains the executables
     fishes/ contains the images of the fishes
     a log file generated with the execution