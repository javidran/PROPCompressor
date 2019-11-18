classes = */*.class */*/*.class
classesEspecial1 = */*.class
classesEspecial2 = */*/*.class

all: CtrlEstadisticaDriver CtrlProcesosDriver GestorArchivoDriver JPEGDriver LZ78Driver LZSSDriver LZWDriver MainDriver ProcesoComprimirDriver ProcesoDesomprimirDriver TrieDriver OutputAlgoritmoDriver DatosProcesoDriver

CtrlEstadisticaDriver:
	javac Drivers/CtrlEstadisticaDriver.java -cp ./
	jar -cmvf manifests/CtrlEstadistica/META-INF/MANIFEST.MF ../EXE/CtrlEstadistica/CtrlEstadisticaDriver.jar $(classes)
	rm -f $(classes)

CtrlProcesosDriver:
	javac Drivers/CtrlProcesosDriver.java -cp ./
	jar -cmvf manifests/CtrlProcesos/META-INF/MANIFEST.MF ../EXE/CtrlProcesos/CtrlProcesosDriver.jar $(classes)
	rm -f $(classes)

GestorArchivoDriver:
	javac Drivers/GestorArchivoDriver.java -cp ./
	jar -cmvf manifests/GestorArchivo/META-INF/MANIFEST.MF ../EXE/GestorArchivo/GestorArchivoDriver.jar $(classesEspecial1)
	rm -f $(classesEspecial1)

JPEGDriver:
	javac Drivers/JPEGDriver.java -cp ./
	jar -cmvf manifests/JPEG/META-INF/MANIFEST.MF ../EXE/JPEG/JPEGDriver.jar $(classes)
	rm -f $(classes)

LZ78Driver:
	javac Drivers/LZ78Driver.java -cp ./
	jar -cmvf manifests/LZ78/META-INF/MANIFEST.MF ../EXE/LZ78/LZ78Driver.jar $(classes)
	rm -f $(classes)

LZSSDriver:
	javac Drivers/LZSSDriver.java -cp ./
	jar -cmvf manifests/LZSS/META-INF/MANIFEST.MF ../EXE/LZSS/LZSSDriver.jar $(classes)
	rm -f $(classes)

LZWDriver:
	javac Drivers/LZWDriver.java -cp ./
	jar -cmvf manifests/LZW/META-INF/MANIFEST.MF ../EXE/LZW/LZWDriver.jar $(classes)
	rm -f $(classes)

MainDriver:
	javac Drivers/MainDriver.java -cp ./
	jar -cmvf manifests/Main/META-INF/MANIFEST.MF ../EXE/Main/MainDriver.jar $(classes)
	rm -f $(classes)

ProcesoComprimirDriver:
	javac Drivers/ProcesoComprimirDriver.java -cp ./
	jar -cmvf manifests/ProcesoComprimir/META-INF/MANIFEST.MF ../EXE/ProcesoComprimir/ProcesoComprimirDriver.jar $(classes)
	rm -f $(classes)

ProcesoDesomprimirDriver:
	javac Drivers/ProcesoDescomprimirDriver.java -cp ./
	jar -cmvf manifests/ProcesoDescomprimir/META-INF/MANIFEST.MF ../EXE/ProcesoDescomprimir/ProcesoDescomprimirDriver.jar $(classes)
	rm -f $(classes)

TrieDriver:
	javac DomainLayer/Algoritmos/TrieDriver.java -cp ./
	jar -cmvf manifests/Trie/META-INF/MANIFEST.MF ../EXE/Trie/TrieDriver.jar $(classesEspecial2)
	rm -f $(classesEspecial2)

OutputAlgoritmoDriver:
	javac DomainLayer/Algoritmos/OutputAlgoritmoDriver.java -cp ./
	jar -cmvf manifests/OutputAlgoritmo/META-INF/MANIFEST.MF ../EXE/OutputAlgoritmo/OutputAlgoritmoDriver.jar $(classesEspecial2)
	rm -f $(classesEspecial2)

DatosProcesoDriver:
	javac DomainLayer/Proceso/DatosProcesoDriver.java -cp ./
	jar -cmvf manifests/DatosProceso/META-INF/MANIFEST.MF ../EXE/DatosProceso/DatosProcesoDriver.jar $(classesEspecial2)
	rm -f $(classesEspecial2)

clean:
	rm -f $(classes)
