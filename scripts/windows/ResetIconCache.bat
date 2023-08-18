@echo off
:: Erases all icon cache and resets Windows Explorer.
:: (Unknown author)

taskkill /f /im explorer.exe
cd /d %userprofile%\AppData\Local
del IconCache.db /a
start explorer.exe
