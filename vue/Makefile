all : client aquarium

run:
	cd build/ && java -ea Client ../affichage.cfg	

client: Config.java Client.java
	javac -d build -cp build $^


aqua:
	cd build/ && java aqua.Aquarium  ../../images/fish1.png ../../images/fish2.png ../../images/fish2.png ../../images/fish3.png ../../images/fish4.png ../../images/fish5.png ../../images/fish6.png ../../images/fish7.png 

aquarium:  AquaPanel.java Aquarium.java 
	   javac -d build -cp build  $^

clean: 
	rm -r build/client build/aqua



