@REM
@REM ==============================================
@REM  LibReDE : Library for Resource Demand Estimation
@REM ==============================================
@REM
@REM (c) Copyright 2013-2014, by Simon Spinner and Contributors.
@REM
@REM Project Info:   http://www.descartes-research.net/
@REM
@REM All rights reserved. This software is made available under the terms of the
@REM Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
@REM http://www.eclipse.org/legal/epl-v10.html
@REM
@REM This software is distributed in the hope that it will be useful, but
@REM WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
@REM or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
@REM for more details.
@REM
@REM You should have received a copy of the Eclipse Public License (EPL)
@REM along with this software; if not visit http://www.eclipse.org or write to
@REM Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
@REM Email: license (at) eclipse.org
@REM
@REM [Java is a trademark or registered trademark of Sun Microsystems, Inc.
@REM in the United States and other countries.]
@REM

@echo off

setlocal enabledelayedexpansion
set basepath=%~dp0
set classpath=

pushd .
cd %basepath%\..
for /r %%a in (lib\plugins\*) do set classpath=!classpath!;%%a
popd

@REM Determine whether we have 32-bit or 64-bit Java
java -d64 -version >nul 2>&1
if errorlevel 1 goto CHECK32
set arch=x86_64
goto EXEC
:CHECK32
where java >nul 2>&1
if errorlevel 1 goto NOJAVA
set arch=x86
goto EXEC
echo "No Java installation found. Check your PATH variable."
goto EXIT

:EXEC
java -cp %classpath% -Djava.library.path=%basepath%\..\lib\os\win32\%arch% -Djna.library.path=%basepath%\..\lib\os\win32\%arch% tools.descartes.librede.frontend.Console %*

:EXIT
