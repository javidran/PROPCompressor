classes = *.class */*.class */*/*.class

all: PROPresor

PROPresor:
	javac PROPresor.java -cp ./
	jar -cmvf manifests/META-INF/MANIFEST.MF ../EXE/PROPresor.jar $(classes)
	rm -f $(classes)

clean:
	rm -f $(classes)
