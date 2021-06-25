# SARS-CoV-2-WikiPathways

Temporary repository of RDF of selected pathways from WikiPathways, supporting the Wikidata bot.
If you like this work and want to cite it, for now, please cite
[this paper](https://www.nature.com/articles/s41597-020-0477-8). If you use specific pathways,
please cite that pathway according to
[the guidelines](https://www.wikipathways.org/index.php/How_to_cite_WikiPathways).

The following steps can be taken to update the content of this repository.

## Step 1: Update the GPML files

```shell
make fetch
```

## Step 2: Update the author information

This is currently a manual step. Check all pathways and see if the authors are updated,
and update or create the appropriate files in the `authors/` folder. Even the order of
the authors can have changed.

## Step 3: Make sure to have the BridgeDb ID mapping databases

To download the BridgeDb identifier mapping files, download them from
[here](https://bridgedb.github.io/data/gene_database/)
and save them in the `/path/to/where/the/bridge/files/are` folder, mathching what
you entered in the `config.properties` file above with the `bridgefiles=` parameter.
You also want to download the identifier mapping database for coronavirus
genes and proteins.

## Step 4: Create the RDF (Turtle)

The Turtle in the `wp/Human` folder is created with WPRDF from the
[SARS-CoV-2-WikiPathways](https://github.com/wikipathways/GPML2RDF/tree/SARS-CoV-2-WikiPathways)
branch. The RDF is generated with the following command:

```shell
make
```

## Step 5: Update the SBML

```shell
make sbml
make svg
```

## Step 6: Run the validation and create the reports

```shell
make check
```

## Step 7: Update the JSON/SVG for Wikidata (automatic)

The JSON/SVG used on Scholia needs updating. A hook on this
repository has been put in place and each time a commit is made,
the JSON and SVG needed by Scholia are automatically updated.
Thanks to Anders!

## Step 8: Summary statistics (optional)

If you have the Debian/Ubuntu `rasqal-utils` package installed, you can run
the following queries to get some statistics:

```shell
cat wp/Human/WP*.ttl > all.ttl
echo "## Reactions"
roqet sparql/reactions.rq -D all.ttl > /dev/null
echo "## Publications"
roqet sparql/publications.rq -D all.ttl > /dev/null
echo "## Genes"
roqet sparql/genes.rq -D all.ttl > /dev/null
echo "## Proteins"
roqet sparql/proteins.rq -D all.ttl > /dev/null
echo "## Metabolites"
roqet sparql/metabolites.rq -D all.ttl > /dev/null
echo "## DataNodes"
roqet sparql/datanodes.rq -D all.ttl > /dev/null
rm all.ttl
```

## Step 9: run the Wikidata bot (optional)

Before you attempt to run the bot, make sure all publications are in Wikidata
and with statements to list their PubMed identifiers.

The bot is an adaption of the WikiPathways bot in the
[github.com/SuLab/scheduled-bots/](https://github.com/SuLab/scheduled-bots.git)
repository. You need, however, the `sars-cov-2-wikipathways-2`
branch:

```shell
git clone https://github.com/SuLab/scheduled-bots.git
cd scheduled-bots
git checkout -b sars-cov-2-wikipathways-2
```

In the `scheduled_bots/wikipathways` folder in that repository you
will find the bot:

```shell
python3 bot.py
```

