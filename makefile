JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		House.java \
		Room.java \
		Range.java \
		Creator.java \
		Test.java

run: classes
	java Test

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class