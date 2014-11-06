FC = gfortran

FCFLAGS =	-O2 -g -Wall -fPIC -fmessage-length=0

LDFLAGS = 	-shared

LIB_NAME = 	NNLS

F77_SOURCES = $(wildcard *.FOR)

F90_SOURCES = $(wildcard *.F90)

OBJS = 		$(addprefix $(BUILD_DIR)/,$(F77_SOURCES:%.FOR=%.o) $(F90_SOURCES:%.F90=%.o))

TARGET =	$(LIB_PREFIX)$(LIB_NAME).$(LIB_EXTENSION)

$(BUILD_DIR)/%.o: %.FOR makefile | $(BUILD_DIR)
	$(FC) $(FCFLAGS) -c -o $@ $<

$(BUILD_DIR)/%.o: %.F90 makefile | $(BUILD_DIR)
	$(FC) $(FCFLAGS) -c -o $@ $<

$(BUILD_DIR)/$(TARGET):	$(OBJS)
	$(FC) $(LDFLAGS) -o $(BUILD_DIR)/$(TARGET) $(OBJS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

all:	$(TARGET)
	echo $(OBJS)
	
clean:
	rm -rf $(BUILD_DIR)
