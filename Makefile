#Makefile
install:
	./gradlew clean install

run:
	./gradlew run

check-updates:
	./gradlew dependencyUpdates

lint:
	./gradlew checkstyleMain checkstyleTest


build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

.PHONY: build