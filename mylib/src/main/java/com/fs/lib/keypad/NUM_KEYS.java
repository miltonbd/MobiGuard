package com.fs.lib.keypad;

/**
 * Created by milton on 24/01/16.
 */
public enum NUM_KEYS {
    NUM_KEY_1("1"),NUM_KEY_2("2"),NUM_KEY_3("3"),
    NUM_KEY_4("4"),NUM_KEY_5("5"),NUM_KEY_6("6"),NUM_KEY_7("7"),NUM_KEY_8("8"),NUM_KEY_9("9"),NUM_KEY_0("0"),
    NUM_KEY_BACK("Back"),NUM_KEY_CLEAR("Clear");
    private String keyValue;

    NUM_KEYS(String keyValue){
        this.keyValue = keyValue;
    }
    public String getKeyValue() {
      return  this.keyValue;
    }
}
