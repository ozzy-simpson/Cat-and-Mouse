JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Simulator.java \
	# Cat.java \
	# City.java \
	# Creature.java \
	# GridPoint.java \
	# Mouse.java \
	# PoisonMouse.java \
	# ZombieCat.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class