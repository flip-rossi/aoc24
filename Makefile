# https://makefiletutorial.com/#makefile-cookbook
SRC_DIRS := ./src

# C++ vars
CXX := g++
CXX_FLAGS := -std=c++23 -Wall -lm

CXX_SRC_DIR := ./src/main/cpp
CXX_OUT_DIR := ./target/cpp

CXX_SRCS := $(wildcard $(CXX_SRC_DIR)/*.cpp)
CXX_BINS := $(CXX_SRCS:$(CXX_SRC_DIR)/%.cpp=$(CXX_OUT_DIR)/%)

# Java vars
JAVA_BUILD_CMD := mvn compile
JRE := java
#same flags as MUIP '23
#JRE_FLAGS := -Xss128m -Xms512m -Xmx512m

JAVA_SRC_DIR := ./src/main/java
JAVA_OUT_DIR := ./target/java/classes

JAVA_SRCS := $(wildcard $(JAVA_SRC_DIR)/*.java)
JAVA_BINS := $(JAVA_SRCS:$(JAVA_SRC_DIR)/%.java=$(JAVA_OUT_DIR)/%.class)

# TODO Rust and OCaml?

ALL_BINS = $(CXX_BINS) $(JAVA_BINS)

.PHONY: all clean

all: $(ALL_BINS)

clean:
	rm -rf $(CXX_OUT_DIR)
	mvn clean

# C++ goals
cpp: $(CXX_BINS)

$(CXX_OUT_DIR):
	mkdir -p $(CXX_OUT_DIR)

$(CXX_BINS): $(CXX_OUT_DIR)/%: $(CXX_SRC_DIR)/%.cpp $(CXX_OUT_DIR)
	$(CXX) $(CXX_FLAGS) $< -o $@

# Compile specific exercise
%:: $(CXX_OUT_DIR)/% ;

# Java goals
java: $(JAVA_BINS)

$(JAVA_BINS): $(JAVA_OUT_DIR)/%.class: $(JAVA_SRC_DIR)/%.java
	$(JAVA_BUILD_CMD)

# Compile and run an exercise based on name
%:: $(JAVA_OUT_DIR)/%.class
	$(JRE) $(JRE_FLAGS) -cp $(JAVA_OUT_DIR) $@

