CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -I"$(IPOPT_INCLUDES)"

LDFLAGS = -shared

LIB_NAME = 	IpOpt

SOURCES =	$(wildcard *.cpp)

HEADERS = 	$(wildcard *.hpp)

OBJS = 		$(addprefix $(BUILD_DIR)/,$(SOURCES:%.cpp=%.obj))

LIBS = -Ld:/Dateien/SVN/ipopt/build/lib -lipopt -lcoinmumps -lpthread -lgfortran -lmingw32 -lmoldname -lmingwex -lmsvcrt -lquadmath -lm -ladvapi32 -lshell32 -luser32 -lkernel32 -lcoinmetis -lcoinlapack -lcoinblas -lm 

TARGET = $(LIB_NAME).dll

$(BUILD_DIR)/%.obj: %.cpp $(HEADERS) makefile | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c -o $@ $<

$(BUILD_DIR)/$(TARGET):	$(OBJS)
	$(CXX) $(LDFLAGS) -o $(BUILD_DIR)/$(TARGET) $(OBJS) $(LIBS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

all:	$(TARGET)

clean:
	rm -rf $(BUILD_DIR)