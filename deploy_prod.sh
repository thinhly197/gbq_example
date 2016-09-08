#!/usr/bin/env bash

mkdir ~/edm-html-generator/logs
mkdir ~/edm-html-generator/templates
mkdir ~/edm-html-generator/sql
mkdir ~/edm-html-generator/data

cp resources/config.properties ~/edm-html-generator
cp resources/email-template.ftl ~/edm-html-generator/templates
cp resources/*.sql ~/edm-html-generator/sql