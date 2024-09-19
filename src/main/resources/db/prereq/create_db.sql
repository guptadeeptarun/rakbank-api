create user 'rakbank_rw' identified by 'changeme';
create database rakbank;
GRANT ALL ON rakbank.* TO 'rakbank_rw';