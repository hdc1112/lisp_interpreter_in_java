# makefile for the lisp interpreter project
# typical use: make (or make compile, compile is the first target, so it's the default target) ; 
# then make run (or make run-io, if input&output mode is what you want)
# other use:   make jar     ; make clean
# the eclipse project folder structure is kept on purpose
#
# Copyright (c) 2012 Dachuan Huang
#
# Author: Dachuan Huang
# huangda@cse.ohio-state.edu

.PHONY: compile jar run-io run clean

BIN = ./bin/
SRC = ./src/

MAINCLASS = LispInterpreter


JAVAC = javac
JAVAFLAGS = -d ../$(BIN)/

JAR = jar
JARFLAGS = cvfm
JARNAME = $(MAINCLASS).jar
MANIFEST = manifest.txt

JAVA = java
INPUTOUTPUTMODE = inputoutput

compile: 
	cd $(SRC); $(JAVAC) $(JAVAFLAGS) *.java 

jar:
	cd $(BIN); $(JAR) $(JARFLAGS) $(JARNAME) ../$(MANIFEST) *.class; mv $(JARNAME) ../

run-io:
	cd $(BIN); $(JAVA) $(MAINCLASS) $(INPUTOUTPUTMODE)

run: 
	cd $(BIN); $(JAVA) $(MAINCLASS)

clean:
	rm -rf $(BIN)/*; rm -rf $(JARNAME); rm -rf *~
