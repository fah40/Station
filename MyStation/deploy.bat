@echo off
setlocal

@rem Définir le chemin vers WildFly
set "TOMCAT_PATH=D:\WORK\S5\PRGOG\wildfly-26.1.2.Final\standalone\deployments"
@REM set "TOMCAT_PATH=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"

@rem Obtenir le nom du répertoire principal
for %%I in ("%CD%") do set DIRECTORY=%%~nI

@rem Supprimer le dossier "temp" s'il existe déjà
if exist "temp" (
    rmdir /s /q .\temp
)

@rem Créer les dossiers nécessaires dans "temp"
mkdir .\temp
mkdir .\temp\WEB-INF
mkdir .\temp\WEB-INF\classes
mkdir .\temp\java
mkdir .\temp\WEB-INF\lib

@rem Copier tous les fichiers du répertoire "web" vers "temp"
Xcopy web .\temp /E /H /C /I /Y

@rem Copier web.xml dans WEB-INF
copy config\web.xml .\temp\WEB-INF

@rem Copier les bibliothèques dans "temp\WEB-INF\lib"
Xcopy lib .\temp\WEB-INF\lib /E /H /C /I /Y

@rem Définir les chemins source et destination
set "source_folder=.\src"
set "destination_folder=.\temp\java"

@rem Boucle sur les sous-dossiers pour copier les fichiers .java
for /D %%d in ("%source_folder%\*") do (
    for %%f in ("%%d\*.java") do (
        copy "%%f" "%destination_folder%"
    )
)

@rem Compiler les fichiers .java dans "WEB-INF\classes"
javac -d .\temp\WEB-INF\classes -cp "lib\*" .\temp\java\*.java

@rem Supprimer le dossier "java" après la compilation
rmdir /s /q .\temp\java

@rem Convertir "temp" en fichier .war et le déplacer vers le serveur WildFly
cd temp
jar -cvf "%DIRECTORY%.war" *
copy "%DIRECTORY%.war" "%TOMCAT_PATH%"

pause
