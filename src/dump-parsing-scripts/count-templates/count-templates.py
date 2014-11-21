#!/usr/bin/env python3
import sys
sys.path.insert(0, '../../main/python/')

import xml.etree.ElementTree as ET
import argparse
import mwclient
import javaproperties

def getIt(string):
  return string

parser = argparse.ArgumentParser(description='Counts template usage in dump file and writes result to wiki page.')
parser.add_argument('-c', '--creds', type=argparse.FileType('r'), help='the credentials file', required=True)
parser.add_argument('-f', '--file', type=getIt, help='the dump file', required=True)
parser.add_argument('--max', type=int, default=-1, help='Max number of results.')

args = parser.parse_args()

# parse the dump
ns = "{http://www.mediawiki.org/xml/export-0.9/}"
i = 0
result = []
for event, elem in ET.iterparse(args.file, events=('start', 'end')):
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
        result.append([count, elem.find(ns+'title').text.encode('utf-8')])
        i += 1
        if args.max > 0 and i >= args.max:
          break
    elem.clear()

# sort the result
result.sort(reverse=True)

# Build page text
pagetext = ""
for line in result:
  count = line[0]
  title = line[1]
  titleInLink = title.replace(' ', '_')
  # [[Ewige Tabelle der UEFA Europa League]] <small>([http://de.wikipedia.org/w/index.php?title=Ewige_Tabelle_der_UEFA_Europa_League&action=edit edit])</small>
  pagetext += " {:<10} [[{}]] <small>([http://de.wikipedia.org/w/index.php?title={}&action=edit edit])</small>".format(count, title, titleInLink) + "\n"
pagetext = pagetext[:-1]

# Load credentials
p = javaproperties.Properties()
p.load(args.creds)

# Login
site = mwclient.Site(('https', 'de.wikipedia.org'))
site.login(p['user.name'], p['user.password'])

# Write to page
page = site.Pages['Benutzer:Croesch/Artikel_mit_den_meisten_Vorlageneinbindungen']
page.save(pagetext, summary = 'Bot: Aktualisiere Daten')
