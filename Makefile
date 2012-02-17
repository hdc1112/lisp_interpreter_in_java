# makefile for the lisp interpreter project
# typical use: make (or make compile, compile is the first target, so it's the default target) ; 
# then make run (or make run-evaloff, if input&output mode is what you want,
# or even make evaloff-listprintoff, if you want to forbid list printing)
# other use:   make jar     ; make clean
# the eclipse project folder structure is kept on purpose
#
# Copyright (c) 2012 Dachuan Huang
#
# Author: Dachuan Huang
# huangda@cse.ohio-state.edu

.PHONY: compile jar run-evaloff-listprintoff run-evaloff \
	run-listprintoff run clean pre clearscreen help run-1stpart
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
EVALOFF = evaloff
LISTPRINTOFF = listprintoff

# want to add some extra arguments to the tail of make?
# do this:
# make run XARGS="foo bar"
# you can add "lowercaseoff" or "lowercasesameoff"
# to the XARGS, see README for details
XARGS =

# print how many lines of manual from README?
MAN=45

compile: pre
	cd $(SRC); $(JAVAC) $(JAVAFLAGS) *.java $(XARGS); head -$(MAN) ../README

jar: 
	cd $(BIN); $(JAR) $(JARFLAGS) $(JARNAME) ../$(MANIFEST) *.class $(XARGS); mv $(JARNAME) ../

run-evaloff-listprintoff:
	cd $(BIN); $(JAVA) $(MAINCLASS) $(EVALOFF) $(LISTPRINTOFF) $(XARGS)

run-evaloff:
	cd $(BIN); $(JAVA) $(MAINCLASS) $(EVALOFF) $(XARGS)

# not commonly used
run-listprintoff:
	cd $(BIN); $(JAVA) $(MAINCLASS) $(LISTPRINTOFF) $(XARGS)

run:
	cd $(BIN); $(JAVA) $(MAINCLASS) $(XARGS)

clean:
	rm -rf $(BIN)/*; rm -rf $(JARNAME); rm -rf *~

pre: clearscreen
	if test -d $(BIN); then true; else mkdir $(BIN); fi 

clearscreen:
	clear

help: clearscreen
	head -$(MAN) README

# just because it's too boring to type run-evaloff-listprintoff
# to ***grader***, run this to test the first part of this project!
run-1stpart: run-evaloff-listprintoff
