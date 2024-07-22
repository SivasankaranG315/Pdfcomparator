@echo off
set DIFFPDF_PATH="C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdcomparepdfcmd.exe"
set PDF1_PATH="C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdi1.pdf"
set PDF2_PATH="C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdi2.pdf"
set OUTPUT_DIR="C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmd"
set OUTPUT_FILE="result.pdf "

echo Running comparison command...

"C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdcomparepdfcmd.exe" -v -s -r "C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmd\result.pdf " "C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdi1.pdf" "C:UserslowersDownloadscomparepdfcmd-2.4.0comparepdfcmdi2.pdf"

echo Comparison completed.

pause