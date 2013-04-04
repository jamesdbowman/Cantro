all:
	javac $(shell find ./cantro/* | grep .java$$)
