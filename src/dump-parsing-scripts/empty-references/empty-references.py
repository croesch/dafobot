#!/usr/bin/env python3
# coding: utf8
import re
import xml.etree.ElementTree as ET
ns = "{http://www.mediawiki.org/xml/export-0.9/}"
for event, elem in ET.iterparse("dewiki-latest-pages-articles.xml", events=('start', 'end')):
  if event == 'end' and elem.tag == ns+'page' :
    revision = elem.find(ns+'revision')
    if not (revision is None):
      textNode = revision.find(ns+'text')
      if textNode is None:
        text = "anc"
      else:
        text = textNode.text
      if not (text is None):
        text = re.sub('<!--.*?-->', '', text, flags=re.DOTALL)
#        print text
        if "<references" in text and "<ref " not in text and "<ref>" not in text and "{{Infobox" not in text:
#        if "<references" in text and "<ref " not in text and "<ref>" not in text and "{{Infobox Ort in Tschechien" not in text and "{{Infobox Gemeinde in Italien" not in text and "{{Infobox Ort in der Türkei".decode('utf-8') not in text and "{{Infobox Chemikalie" not in text and "{{BS-header-Beleg" not in text and "{{Infobox Kreditinstitut" not in text and "{{Infobox Fluss" not in text and "{{Infobox Berg" not in text:
          print(elem.find(ns+'title').text)
#      else:
#        print("\t\t"+elem.find(ns+'title').text)
    elem.clear()
