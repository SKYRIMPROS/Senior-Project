#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      Cramit
#
# Created:     07/02/2012
# Copyright:   (c) Cramit 2012
# Licence:     <your licence>
#-------------------------------------------------------------------------------
#!/usr/bin/env python

def unformatTextFile(file):
    f = open(file, 'r')
    textLines = f.readlines()
    f.close()

    text = ""

    for l in textLines:
        print l
        if l == "\n":
            text += "\n"
        elif l[-1] == "\n":
            text += l[:-1] + " "
        else:
            text += l

    f = open("formatted_"+file, 'w')
    f.write(text)
    f.close()

def main():
    unformatTextFile("alice_in_wonderland")

if __name__ == '__main__':
    main()
