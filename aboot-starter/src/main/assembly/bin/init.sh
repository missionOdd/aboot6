#! /bin/shell
for file in `ls *.sh`
do
vi +':w ++ff=unix' +':q' ${file}
echo ${file} dos2unix success!
done
chmod 777 *.sh
echo chmod 777 success!
