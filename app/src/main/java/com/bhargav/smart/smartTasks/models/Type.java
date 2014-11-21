package com.bhargav.smart.smartTasks.models;

/**
 * Created by Admin on 06-10-2014.
 */
public class Type {
    public enum  name{
        SIMPLE_NOTE(0), CHECKLIST(1), HABIT(2);

        private int typeValue;

        name(int typeValue) {
            this.typeValue = typeValue;
        }

        public void settypeValue(int typeValue) {
            this.typeValue = typeValue;
        }

        public int getTypeValue() {
            return typeValue;
        }
    }

}
