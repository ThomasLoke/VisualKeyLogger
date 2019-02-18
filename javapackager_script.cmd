set packager=%1
set appclass=%2
set srcdir=%3
set outdir=%4
set outfile=%5
set srcfiles=%6

REM Build the jar file
%packager% -createjar -appclass %appclass% -srcdir %srcdir% -outdir %outdir% -outfile %outfile%

REM Make the bundled EXE file
%packager% -deploy -native exe -appclass %appclass% -srcfiles %srcfiles% -outdir %outdir% -outfile %outfile%