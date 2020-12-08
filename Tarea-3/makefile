.PHONY: tarea-3

all: tarea-3

tarea-3:
	javac -cp ".:json-simple-1.1.1.jar" GUI_Tools.java Horario.java
	jar -cmvf MANIFEST.MF tarea-3.jar *.class

clear:
	rm *.class