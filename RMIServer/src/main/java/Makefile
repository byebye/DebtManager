PRG_JAVA = $(wildcard */*.java)
EXE_JAVA = $(patsubst %.java,%.class,$(PRG_JAVA))
EXE     = $(EXE_JAVA)


server: $(EXE)
	jar cvf common.jar common/*.class
	cp common.jar ~/public_html/classes/common.jar

%.class: %.java
	javac $^

runServer: server
	./startServer.sh

