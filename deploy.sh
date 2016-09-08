#! /bin/sh

mkdir ~/edm-html-generator
mkdir ~/edm-html-generator/logs
mkdir ~/edm-html-generator/templates
mkdir ~/edm-html-generator/sql
mkdir ~/edm-html-generator/data

cp src/main/resources/config.properties ~/edm-html-generator
cp src/main/resources/email-template.ftl ~/edm-html-generator/templates
cp src/main/resources/*.sql ~/edm-html-generator/sql

mkdir ~/.aws
cp src/main/resources/credentials ~/.aws

native2ascii src/main/resources/config.properties ~/edm-html-generator/config.properties
