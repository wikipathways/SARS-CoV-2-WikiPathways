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
