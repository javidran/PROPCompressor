classes = $(wildcard */*.class) $(wildcard */*/*.class)

all: MainDriver

MainDriver: clean
	javac Drivers/MainDriver.java -cp ./
	jar -cmvf manifests/Main/META-INF/MANIFEST.MF ../EXE/Main/MainDriver.jar $(classes)
	rm -f $(classes)

clean:
	rm -f $(classes)
