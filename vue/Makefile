all : client

run:
	cd build/ && java -ea Client ../affichage.cfg	

client: Config.java Client.java
	javac -d build -cp build $^


clean: 
	rm build/*



