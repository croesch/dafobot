#!/usr/bin/env python3

import xml.etree.ElementTree as ET
ns = "{http://www.mediawiki.org/xml/export-0.9/}"
i = 0
for event, elem in ET.iterparse("dewiki-latest-pages-articles.xml", events=('start', 'end')):
  if event == 'end' and elem.tag == ns+'page' :
    revision = elem.find(ns+'revision')
    if not (revision is None):
      textNode = revision.find(ns+'text')
      if textNode is None:
        text = "anc"
      else:
        text = textNode.text
      if (text is None):
        count = 0
      else:
        count = text.count("{{")
      if count > 500:
        print("{:<10} {}".format(count, elem.find(ns+'title').text.encode('utf-8')))
        i += 1
    elem.clear()
print i
