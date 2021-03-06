# Deprecated

#### This plugin is no longer used in Pelias!!!

If you are following instructions to install this plugin, they are probably old, please open an issue to alert someone to fix them.

This repository will stay around for a few more releases just in case anyone is running an older version of the codebase.

If not already done so by Sept 2015, this repo can be permanetly deleted.

##Pelias ElasticSearch Plugin

Elasticsearch comes with a wide variety of analyzers that can be used just by specifying them in the Elasticsearch settings file. For our geocoder, however, we need broader control over how search queries and string fields are treated. This means extending the functionality of ElasticSearch by subclassing parts of the library it's based on, Lucene.

###On analysis
We use analysis to make searches more flexible, and more relevant. In Elasticsearch, we analyze both specified fields on each document on indexation, and also searches made to those documents.

Our current analysis stack goes like this:

1.- **Tokenization**: Lucene's ```whitespace``` [tokenizer](http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/analysis-tokenizers.html) divides a string into basic tokens when separated by a white space.

> ```"Old St Station" => ["Old", "St", "Station"]```

2.- **Lowercase filtering**: by applying a lowercase filter, we make sure that the user gets the same results regardless of the casing used in the document.

> ```["Old", "St", "Station"] => ["old", "st", "station"]```

3.- **ASCII folding**: All diacritics and special characters are either reduced to its ASCII form or removed.

> ```["a", "coruña"] => ["a", "coruna"]```
> ```["yorckstraße"] => ["yorckstrasse"]```
> ```["málaga"] => ["malaga"]```

4.- **Ampersand filtering**

> ```["highbury", "&", "islington"] => ["highbury", "and", "islington"]```
> ```["johnson&johnson"] => ["johnson and johnson"]``` *(notice we need to retokenize after this)*

5.- **Synonym mapping**: Read more about it [here](https://github.com/pelias/synonyms)

> ```["old", "st", "station"] => ["old", "street", "station"]```
> ```["st", "michaels"] => ["saint", "michaels"]```

6.- **Retokenizing**: The previous steps may have added spaces or punctuation, which means we need to re-separate them, like in our previous example:

> ```"Johnson&Johnson" => ["johnson and johnson"] => ["johnson", "and", "johnson"]```

###Requirements
There needs to be an ElasticSearch instance running on your server, so we'll assume you have Java installed as well. To package and install the plugin from source, you need [Maven](http://maven.apache.org/).

###Installation

**Easy installation**
In your ES directory:

```shell
bin/plugin -url https://github.com/pelias/elasticsearch-plugin/blob/master/pelias-analysis.zip?raw=true -install pelias-plugin
```

**From source**

Clone the repo and from the repo directory, run:

```shell
mvn clean package
path/to/elasticsearch/bin/plugin -url file://path/to/repo/pelias-analysis.zip -install pelias-plugin
```

###Usage
Note: Previously after installing, you would need to follow these additional setup steps. This is not required anymore if using the latest from master but kept here as reference.
- Once the plugin is installed, you should restart ES and add the analyzer to your settings mapping, as shown [in the ES site](http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/analysis-custom-analyzer.html).

```json
    "analysis": {
        "analyzer": {
	    "pelias": {
	        "type": "pelias-analysis"
	    }
        }
    }
```

And try it with Elasticsearch's Analyze API

```
http://localhost:9200/pelias/_analyze?text=whatever&analyzer=pelias
```
