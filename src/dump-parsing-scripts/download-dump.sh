#!/bin/bash

rsync --progress --copy-links rsync://ftpmirror.your.org/wikimedia-dumps/dewiki/latest/dewiki-latest-pages-articles.xml.bz2 .
