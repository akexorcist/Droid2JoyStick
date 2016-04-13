#include "Joystick.h"

#define HAT_UP      0
#define HAT_DOWN    1
#define HAT_LEFT    2
#define HAT_RIGHT   3

const String COMMAND_BUTTON = "btn:";
const String COMMAND_HAT = "hat:";
const String COMMAND_AXIS = "axis:";

boolean isHatUpPressed = false;
boolean isHatDownPressed = false;
boolean isHatLeftPressed = false;
boolean isHatRightPressed = false;

void setup() {

  Serial.begin(9600);
  Serial1.begin(9600);
  Joystick.begin();
  delay(1000);
  Serial.println("Ready");
  pinMode(0, INPUT);
}

String command;

void loop() {
  // put your main code here, to run repeatedly:

  if(Serial.available()) {
    int data = Serial.read();
    if(data == 0x0D) {
    } else if(data == 0x0A) {
      execute(command);
      command = "";
    } else {
      command += (char) data;
    }
  }
  if(Serial1.available()) {
    int data = Serial1.read();
    if(data == 0x0D) {
    } else if(data == 0x0A) {
      execute(command);
      command = "";
    } else {
      command += (char) data;
    }
  }
}

void execute(String command) {
  Serial.println(command);
  if(command.startsWith(COMMAND_BUTTON)) {
    sendButtonState(command);
  } else if(command.startsWith(COMMAND_HAT)) {
    sendHatState(command);
  } else if(command.startsWith(COMMAND_AXIS)) {
    sendAxisState(command);
  }
  setDefaultJoystickValue();
}

void sendButtonState(String command) {
  // Command : "btn:id:value"
  // Example "btn:0:1" - Press Button 1
  byte firstSeperatorIndex = command.indexOf(":");
  command = command.substring(firstSeperatorIndex + 1, command.length());
  byte secondSeperatorIndex = command.indexOf(":");
  int buttonId = command.substring(0, secondSeperatorIndex).toInt();
  int buttonState = command.substring(secondSeperatorIndex + 1, command.length()).toInt();
  Joystick.setButton(buttonId, buttonState);
}

void sendHatState(String command) {
  // Command : "hat:id:value"
  // Example "hat:0:1" - Press Up Direction
  byte firstSeperatorIndex = command.indexOf(":");
  command = command.substring(firstSeperatorIndex + 1, command.length());
  byte secondSeperatorIndex = command.indexOf(":");
  int buttonId = command.substring(0, secondSeperatorIndex).toInt();
  int buttonState = command.substring(secondSeperatorIndex + 1, command.length()).toInt();
  if(buttonId == HAT_UP) { 
    isHatUpPressed = buttonState == 1;
  } else if(buttonId == HAT_DOWN) {
    isHatDownPressed = buttonState == 1;
  } else if(buttonId == HAT_LEFT) {
    isHatLeftPressed = buttonState == 1;
  } else if(buttonId == HAT_RIGHT) {
    isHatRightPressed = buttonState == 1;
  }
  int hatValue = -1;
  if((isHatUpPressed && isHatDownPressed) ||
      (isHatLeftPressed && isHatRightPressed)) {
    hatValue = -1;
  } else if(isHatUpPressed && isHatRightPressed) {
    hatValue = 45; 
  } else if(isHatUpPressed && isHatLeftPressed) {
    hatValue = 315; 
  } else if(isHatDownPressed && isHatRightPressed) {
    hatValue = 135; 
  } else if(isHatDownPressed && isHatLeftPressed) {
    hatValue = 225; 
  } else if(isHatUpPressed) {
    hatValue = 0; 
  } else if(isHatDownPressed) {
    hatValue = 180; 
  } else if(isHatLeftPressed) {
    hatValue = 270; 
  } else if(isHatRightPressed) {
    hatValue = 90; 
  }
  Joystick.setHatSwitch(0, hatValue);
}

void sendAxisState(String command) {
  // Command : "axis:id:valueX:valueY"
  // Example "axis:0:-10:20" - Left Axis (X = -10, Y = 20)
  byte firstSeperatorIndex = command.indexOf(":");
  command = command.substring(firstSeperatorIndex + 1, command.length());
  byte secondSeperatorIndex = command.indexOf(":");
  int axisId = command.substring(0, secondSeperatorIndex).toInt();
  command = command.substring(secondSeperatorIndex + 1, command.length());
  byte thirdSeperatorIndex = command.indexOf(":");
  int xValue = command.substring(0, thirdSeperatorIndex).toInt();
  int yValue = command.substring(thirdSeperatorIndex + 1, command.length()).toInt();
  if(axisId == 0) {
    Joystick.setXAxis(xValue);
    Joystick.setYAxis(yValue);
  } else if(axisId == 1) {
    Joystick.setZAxis(xValue);
    Joystick.setRudder(yValue + 127);
  }
}

void setDefaultJoystickValue() {
  Joystick.setXAxisRotation(180);
  Joystick.setYAxisRotation(180);
  Joystick.setZAxisRotation(180);
  Joystick.setThrottle(127);
}

