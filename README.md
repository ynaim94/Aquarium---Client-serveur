#How to use fish tank simulation

#controller

#The "controller.cfg" file contain the default values for the configuration variables.
#You can change these variables by changing them directly on the file.
#The "aquarium.info" file contain the default fish tank configuration.
#You can change it directly on the file or changing after launching the controller in the user prompt.
#To build the controller : Go in the controller folder and compile the sources as below
#                          cd /controller/
#                          make
#To lauch the controller : In the controller folder launch the controller
#                          ./serveur
#after launching the controller, a log file will be genereted and communication between the user , the clients and the controller
#will be saved.
#after launching the controller, load the fish tank configuration file on the user prompt using the command:
#                          load aquarium.info
#you can also load any alternative fish tank by using load command.
#if the fish tank configuration is not loaded, no client can be accepted.
#to show the fish tank configuration use the command:
#                           show aquarium
#to add a view to the fish tank:
#                           add view  Nx view_x x view_y + view_width + view_height
#to delete a view from the fish tank:   
#                           del view Nx
# Nx where x is a view number.
#to save the fish tank configuration into a file:
#                           save file_name
