@echo off
title Servidor MongoDB
echo ============================================================
echo  Iniciando MongoDB en localhost:27017...
echo  (Manten esta ventana abierta mientras ejecutes los ejercicios)
echo ============================================================
"C:\Users\Admin\scoop\apps\mongodb\current\bin\mongod.exe" --dbpath "C:\Users\Admin\scoop\apps\mongodb\data"
pause
