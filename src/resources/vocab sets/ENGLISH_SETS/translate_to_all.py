#!/usr/bin/env python2
# encoding: utf-8

import sys
import urllib2

#### TO USE FROM COMMAND LINE:
####           python translate_to_all.py filename
#### 
#### EXAMPLE:  python translate_to_all.py English_animals.txt
####     (this will take the animals vocab set in English and translate it to all languages listed below)
####     languages in google translate format: en, es, fr... see bottom of file.


languages = ["en", "fr", "de", "it", "pt", "es"]
#languages = ["fa", "en"]



## Read in file
filename = sys.argv[1]
## Start language
#strt_lan = sys.argv[2]
strt_lan = "en";
## Translate to:
i = languages.index(strt_lan)
languages.pop( i );




agent = {'User-Agent':"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"}


def translate(to_translate, to_langage="auto", langage="auto"):
    """
    Return the translation using google translate
    you must shortcut the langage you define (French = fr, English = en, Spanish = es, etc...)
    if you don't define anything it will detect it or use english by default
    Example:
    print(translate("salut tu vas bien?", "en"))
    hello you alright?
    """
    before_trans = 'class="t0">'
    link = "http://translate.google.com/m?hl=%s&sl=%s&q=%s" % (to_langage, langage, to_translate.replace(" ", "+"))
    request = urllib2.Request(link, headers=agent)
    page = urllib2.urlopen(request).read()
    result = page[page.find(before_trans)+len(before_trans):]
    result = result.split("<")[0]
    return result




if __name__ == '__main__':



    for language in languages:

        fin = open(filename, 'r')
        # Filenames must be of form:  English_vocabSet.txt
        outname = strt_lan+"_"+language+"_"+filename.split('_')[1].split('.')[0]+".txt"
        fout = open(outname, 'w')

        for line in fin:

            translation = translate( line.strip() , language, strt_lan)
            #fout.write( line.strip().replace(" ","+") + " " + spantran.replace(" ", "+") + "\n")
            fout.write( line.strip() + "\t" + translation + "\n")

        fout.close()
        fin.close()




""""
Google language codes:

ar-sa —– Arabic Saudi Arabia
eu —– Basque
bg —– Bulgarian
ca —– Catalan
zh-cn —– Chinese PRC
zh-tw —– Chinese Taiwan
hr —– Croatian
cs —– Czech
da —– Danish
nl —– Dutch Standard
en —– English
en-ca —– English Canada
en-gb —– English United Kingdom
en-us —– English United States
et —– Estonian
fa —– Farsi
fi —– Finnish
fr-ca —– French Canada
fr —– French Standard
mk —– FYRO Macedonian
ms —– Malaysian
de-at —– German Austria
de —– German Standard
el —– Greek
he —– Hebrew
hu —– Hungarian
it —– Italian Standard
ja —– Japanese
ko —– Korean
lt —– Lithuanian
no —– Norwegian Bokmal
pl —– Polish
pt-br —– Portuguese Brazil
pt —– Portuguese Portugal
ru —– Russian
sk —– Slovak
sl —– Slovenian
es —– Spanish
es-ar —– Spanish Argentina
es —– Spanish Spain Traditional
sv —– Swedish
th —– Thai
tr —– Turkish
"""

