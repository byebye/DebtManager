JAVAC = javac
JAR   = jar
PRG_JAVA = $(wildcard */*.java)
EXE_JAVA = $(patsubst %.java,%.class,$(PRG_JAVA))
EXE     = $(EXE_JAVA)


server: $(EXE)
	$(JAR) cvf common.jar common/*.class
	cp common.jar ~/public_html/classes/common.jar

%.class: %.java
	$(JVC) $^

runServer: server
	./startServer.sh

