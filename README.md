# Remittance
Project is for performing a remittance from one account to another.

**Technical implementation details**
When starting, the program expects the following information:

1 is entered into the console - make remittances based on input files,

2 is entered into the console - calling the operation to display a list of all remittances from a report file,

3 is entered into the console - outputting the history of processed records from the report file by dates from ... to...,

4 is entered into the console - exit the program.

**Description of classes**

- Main: 

Entry point to the program. Implements information input and performs selected operations.

- InputFilesService:

the class performs operations on input files:

receives .txt format files from the Input directory, 

extracts information about remittances from the file,

moves this file to the archive and creates a list of information about remittances this file,

deletes file that do not contain information about remittances.

- AccountsFileService:

the class performs operations on a file containing account data: 

receives a .txt file from the “Information” directory, 

extracts information about the amounts accounts of remittances from the file,

updates data on these accounts.

- ReportFileService:

the class performs operations on the report file:

receives a .txt file from the “Information” directory or creates, 

records information about remittances and their status, 

extracts from the file the entire history of remittances or by date.





**Project data**
The Information folder contains a file with data about the account and its amount, a report file,
an input directory that stores input files, an archive directory that stores processed files. The program processes files in txt format.

**Additional functions**
Loading the results of remittances into the database.

**Class diagram**
The class diagram is provided in the documentation folder.



 
