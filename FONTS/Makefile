classes = */*.class */*/*.class

all: MainDriver LZ78Driver

MainDriver: clean
	javac Drivers/MainDriver.java -cp ./
	jar -cmvf manifests/Main/META-INF/MANIFEST.MF ../EXE/Main/MainDriver.jar $(classes)
	rm -f $(classes)

LZ78Driver: clean
	javac Drivers/LZ78Driver.java -cp ./
	jar -cmvf manifests/LZ78/META-INF/MANIFEST.MF ../EXE/LZ78/LZ78Driver.jar $(classes)
	rm -f $(classes)

clean:
	rm -f $(classes)
