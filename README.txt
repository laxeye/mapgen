mapgen - Mapping file generator for QIIME
Aleksei Korzhenkov (c) 2017-2018


=Description=
mapgen is a short program written in Kotlin.
It generates mapping files for dereplication of Illumina reads using QIIME software.
Please don't disregard file formatting requirements, this may lead you to incorrect results.


=Usage=

java -jar mapgen.jar genmf primers.txt ids.txt > mappingfile.txt

"primers.txt" contains pairs of LGM sample ID and primers
"ids.txt" contains pairs of Atlas ID and LGM ID
Filenames may differ from the example, but the order must be same: 1st primers, 2nd IDs.
If LGM ID from primers.txt isn't presented in ids.txt LGM ID will be used as Sample ID.

All other options are useless by this moment.


=File formatting requirements=

LGM ID and Atlas ID should contain ONLY letters (A-z), numbers (0-9) and dot symbol (.)!
Spaces, cyryllic lettes, -+=:;,#~ etc. are deprecated!
Primers must begin with F or R!
No trailing spaces at the end of line are allowed.
No empty lines between lines are allowed.
No comments in the files are allowed.

*primers.txt must contain two columns separated by tab.
First column is LGM ID, second is pair of primers.
Primers should be separated one from another by ONE SPACE CHARACTER.
Primers must begin with 'F' for forward primers and 'R'for reverse primers.

*ids.txt must contain two columns separated by tab.
Please, check the rules for IDs!

*barcodes.txt must contain two columns separated by tab.
First column is primer code, second is barcode sequence.


=Next steps=
Validate resulted mapping file using QIIME script:
validate_mapping_file.py -m mappingfile.txt -p -o validation-results/


=Possible errors=

*Line N: "<Line content here>" is incorrect! Missing tab!
Check the line for presence of tab. It may be replaced by space

Line N: "8Ne.repl1	F20R8" is incorrect! Missing space between primers!
Check the line for presence of space between forward and reverse primer. It may be replaced by tab

Line N: "<Line content here>" is incorrect! Primers "X Y" are already used!
The primer pairs shoudn't be identical for two and more samples. 
Please check the file and your lab journal. 

"Primer X or Y not found for sample Z!"
Please check the primers in the line in primers.txt for sample Z:
- you have misspelled the primer. Correct the errors and rerun the program.
- you have used primers, not included in the program code. Insert the sample mannually in the mapping file.


=How-to compile=
You should install Kotlin compiller and JDK.
kotlinc-jvm -include-runtime -d mapgen.jar mapgen.kt


=File examples=

==primers.txt begin==
1Ne.repl1	F20 R1
2Ne.repl1	F20 R2
3Ne.repl1	F20 R3
4Ne.repl1	F20 R4
5Ne.repl1	F20 R5
==primers.txt end==

==ids.txt begin==
202.229.614	1Ne
336.090.219	2Ne
784.093.567	3Ne
705.304.898	4Ne
767.335.460	5Ne
==ids.txt end==

You can find examples in example directory.

Have a nice day!
