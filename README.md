mapgen - Mapping file generator for QIIME
Aleksei Korzhenkov (c) 2017-2018


## Description
mapgen is a short program written in Kotlin.
It generates mapping files for dereplication of Illumina reads with QIIME software.
mapgen could replace inner IDs to outer IDs when such file is presented, it could be great when You use short ID in Your lab and should present Your results whith more meaninful IDs.
Please don't disregard file formatting requirements, this may lead You to incorrect results.


## Usage

### Generating mapping file
The script uses "barcodes.txt" (the filename is hardcoded by now).

java -jar mapgen.jar genmf examples/primers.txt [examples/ids.txt] > mappingfile.txt

"primers.txt" contains pairs of (inner) ID and primers.
"ids.txt" contains pairs of outer ID and inner ID, it could be omitted.
Filenames may differ from the example, but the order must be same: 1st file for primers, 2nd for IDs.
If inner ID from primers.txt isn't represented in ids.txt, inner ID will be used as Sample ID.

### Checking primers
"check" option performs only barcode check for duplication.
java -jar mapgen.jar check examples/primers.txt

All other options are useless by this moment.


## File formatting requirements

Inner ID and outer ID should contain ONLY letters (A-z), numbers (0-9) and dot symbol (.)!
Spaces, non-latin lettes, -+=:;,#~ etc. are deprecated!
Primers must begin with F or R, which stands for forward and reverse strand!
No trailing spaces at the end of line are allowed.
No empty lines between lines are allowed.
No comments in the files are allowed.

**barcodes.txt** must contain two columns separated by tab.
First column is primer code, second is barcode sequence.
You need to fill the file with Your actual barcodes.

**primers.txt** must contain two columns separated by tab.
First column is LGM ID, second is pair of primers.
Primers should be separated one from another by ONE SPACE CHARACTER.
Primers must begin with 'F' for forward primers and 'R'for reverse primers.

**ids.txt** must contain two columns separated by tab.
Please, check the rules for IDs!


## Next steps
Validate resulted mapping file using QIIME script:
validate_mapping_file.py -m mappingfile.txt -p -o validation-results/


## Possible errors

*Line N: "<Line content here>" is incorrect! Missing tab!*

Check the line for presence of tab. It may be replaced by space

*Line N: "8Ne.repl1	F20R8" is incorrect! Missing space between primers!*

Check the line for presence of space between forward and reverse primer. It may be replaced by tab

*Line N: "<Line content here>" is incorrect! Primers "X Y" are already used!*

The primer pairs shoudn't be identical for two and more samples. 
Please check the file and Your lab journal. 

*"Primer X or Y not found for sample Z!"*

Please check the primers in the line in primers.txt for sample Z:
- You have misspelled the primer. Correct the errors and rerun the program.
- You have used primers, not included in the program code. Insert the sample mannually in the mapping file.


## How-to compile

You should install [**Kotlin**](https://kotlinlang.org/) compiller and **JDK**:

https://kotlinlang.org/docs/tutorials/command-line.html

https://www.oracle.com/technetwork/java/javase/downloads/index.html

After installing run:

*kotlinc-jvm -include-runtime -d mapgen.jar mapgen.kt*


## File examples

You can find examples in "example/" directory.
