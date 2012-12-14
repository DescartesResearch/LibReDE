CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -I"$(BAYES_PLUS_PLUS_INCLUDES)" -I"$(BOOST_ROOT)"

LDFLAGS = 	-shared -L"$(BAYES_PLUS_PLUS_LIB)"

#OBJS =		$(BUILD_DIR)\bayesFltWrappers.hpp

LIB_NAME = 	BayesPlusPlus

SOURCES =	$(wildcard *.cpp)

HEADERS = 	$(wildcard *.hpp)

OBJS = 		$(addprefix $(BUILD_DIR)/,$(SOURCES:%.cpp=%.obj))

LIBS =		-lBayesFilter

TARGET =	$(LIB_NAME).dll

$(BUILD_DIR)/%.obj: %.cpp $(HEADERS) makefile | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c -o $@ $<

$(BUILD_DIR)/$(TARGET):	$(OBJS)
	$(CXX) $(LDFLAGS) -o $(BUILD_DIR)/$(TARGET) $(OBJS) $(LIBS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

all:	$(TARGET)

clean:
	rm -rf $(BUILD_DIR)