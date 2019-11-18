classes = */*.class */*/*.class
classesEspecial = */*/*.class

all: MainDriver LZ78Driver TrieDriver ProcesoComprimirDriver ProcesoDesomprimirDriver

MainDriver:
	javac Drivers/MainDriver.java -cp ./
	jar -cmvf manifests/Main/META-INF/MANIFEST.MF ../EXE/Main/MainDriver.jar $(classes)
	rm -f $(classes)

LZ78Driver:
	javac Drivers/LZ78Driver.java -cp ./
	jar -cmvf manifests/LZ78/META-INF/MANIFEST.MF ../EXE/LZ78/LZ78Driver.jar $(classes)
	rm -f $(classes)

TrieDriver:
	javac DomainLayer/Algoritmos/TrieDriver.java -cp ./
	jar -cmvf manifests/Trie/META-INF/MANIFEST.MF ../EXE/Trie/TrieDriver.jar $(classesEspecial)
	rm -f $(classes)

ProcesoComprimirDriver:
	javac Drivers/ProcesoComprimirDriver.java -cp ./
	jar -cmvf manifests/ProcesoComprimir/META-INF/MANIFEST.MF ../EXE/ProcesoComprimir/ProcesoComprimirDriver.jar $(classes)
	rm -f $(classes)

ProcesoDesomprimirDriver:
	javac Drivers/ProcesoDescomprimirDriver.java -cp ./
	jar -cmvf manifests/ProcesoDescomprimir/META-INF/MANIFEST.MF ../EXE/ProcesoDescomprimir/ProcesoDescomprimirDriver.jar $(classes)
	rm -f $(classes)

clean:
	rm -f $(classes)
