CXXFLAGS =	-O2 -g -Wall -fPIC -fmessage-length=0 -I"$(IPOPT_INCLUDES)"

LDFLAGS = -shared

LIB_NAME = 	IpOpt

SOURCES =	$(wildcard *.cpp)

HEADERS = 	$(wildcard *.hpp)

OBJS = 		$(addprefix $(BUILD_DIR)/,$(SOURCES:%.cpp=%.obj))

LIBS = -L$(IPOPT_LIB) -lipopt -lcoinmumps -lcoinmetis -lcoinlapack -lcoinblas -lpthread -lgfortran -lquadmath -lm

TARGET = $(LIB_PREFIX)$(LIB_NAME).$(LIB_EXTENSION)

$(BUILD_DIR)/%.obj: %.cpp $(HEADERS) makefile | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c -o $@ $<

$(BUILD_DIR)/$(TARGET):	$(OBJS)
	$(CXX) $(LDFLAGS) -o $(BUILD_DIR)/$(TARGET) $(OBJS) $(LIBS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

all:	$(TARGET)

clean:
	rm -rf $(BUILD_DIR)