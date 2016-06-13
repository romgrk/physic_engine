#
# Makefile
# romgrk, 2016-06-13 13:05
#

all:
	@echo "Running makefile" && make build

build:
	javac -sourcepath ./src -d bin src/*/*.java

run:
	java -cp ./bin start.Main

# vim:ft=make
#
