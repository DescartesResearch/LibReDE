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

java -cp %classpath% -Djava.library.path=%basepath%\..\lib\native\win32 -Djna.library.path=%basepath%\..\lib\native\win32 tools.descartes.librede.frontend.Console %*