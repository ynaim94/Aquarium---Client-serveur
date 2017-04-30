all : aquarium

run:
	cd build/ && java aqua.Aquarium ../affichage.cfg

aquarium:  Fish.java Config.java AquaConnection.java AquaPanel.java ClientLog.java Aquarium.java Parser.java GFCThread.java
	   javac -d build -cp build  $^
clean: 
	rm -r build/aqua



