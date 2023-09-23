all:
	mkdir -p out
	javac -d out src/*.java src/expression/*.java src/parser/*.java src/util/*.java


run:
	java -cp out Main

pack:
	zip hw0.zip -r Makefile src lib

clean:
	rm -rf out
	rm -f src/parser/*.java

out/%.class: src/%.java out
	javac -cp src $< -d out

out:
	mkdir -p out
