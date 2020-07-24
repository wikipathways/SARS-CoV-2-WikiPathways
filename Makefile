TARGETS := ${shell cat pathways.txt | sed -e 's/\(.*\)/gpml\/\1.gpml/' }

all: wikipathways-SARS-CoV-2-rdf-authors.zip wikipathways-SARS-CoV-2-rdf-wp.zip

fetch:clean ${TARGETS}

clean:
	@rm ${TARGETS}

gpml/%.gpml:
	@echo "Git fetching $@ ..."
	@echo '$@' | sed -e 's/gpml\/\(.*\)\.gpml/\1/' | xargs bash getPathway.sh

wikipathways-SARS-CoV-2-rdf-authors.zip: authors/*
	@rm -f wikipathways-SARS-CoV-2-rdf-authors.zip
	@zip wikipathways-SARS-CoV-2-rdf-authors.zip authors/*

wikipathways-SARS-CoV-2-rdf-wp.zip: wp/Human/*
	@rm -f wikipathways-SARS-CoV-2-rdf-wp.zip
	@zip wikipathways-SARS-CoV-2-rdf-wp.zip wp/Human/*

wp/Human/%.ttl: gpml/%.gpml src/java/main/org/wikipathways/covid/CreateRDF.class
	cat "$<.rev" | xargs java -cp src/java/main/.:libs/GPML2RDF-3.0.0-SNAPSHOT-jar-with-dependencies.jar:libs/derby-10.5.3.0_1.jar org.wikipathways.covid.CreateRDF $< | grep -v ".bridge" > $@

src/java/main/org/wikipathways/covid/CreateRDF.class: src/java/main/org/wikipathways/covid/CreateRDF.java
	javac -cp libs/GPML2RDF-3.0.0-SNAPSHOT-jar-with-dependencies.jar src/java/main/org/wikipathways/covid/CreateRDF.java

src/java/main/org/wikipathways/covid/CheckRDF.class: src/java/main/org/wikipathways/covid/CheckRDF.java
	javac -cp libs/wikipathways.curator-1-SNAPSHOT-jar-with-dependencies.jar src/java/main/org/wikipathways/covid/CheckRDF.java

check: reports/WP4846.txt

reports/%.txt: wp/Human/%.ttl
	mkdir -p reports
	java -cp libs/jena-arq-3.16.0.jar:src/java/main/:libs/wikipathways.curator-1-SNAPSHOT-jar-with-dependencies.jar org.wikipathways.covid.CheckRDF $< > reports/WP4846.txt
