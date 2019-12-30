# May fail if more than one version of JAVA installed.
export JAVA_HOME=$(/usr/libexec/java_home)
gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -I"." -dynamiclib -o libhx13.dylib hashblock.cpp aes_helper.c blake.c echo.c hamsi.c jh.c shavite.c bmw.c fugue.c keccak.c simd.c cubehash.c hamsi_helper.c luffa.c skein.c groestl.c
