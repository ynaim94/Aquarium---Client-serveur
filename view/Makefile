all : aquarium
	
run:
	cd build/ && java aqua.Aquarium ../affichage.cfg

aquarium:  Config.java PromptThread.java AquaPanel.java Aquarium.java 
	   javac -d build -cp build  $^
clean: 
	rm -r build/aqua



