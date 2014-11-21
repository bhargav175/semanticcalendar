package com.bhargav.smart.smartTasks.models;

/**
 * Created by Admin on 06-10-2014.
 */
public class State {
    public enum  name{
        NOT_STARTED(0), STARTED(1), INCOMPLETE(2),COMPLETED_UNSUCCESSFULLY(3),COMPLETED_SUCCESSFULLY(4);

        private int stateValue;
        name(int stateValue) {
            this.stateValue = stateValue;
        }

        public void setStateValue(int stateValue) {
            this.stateValue = stateValue;
        }

        public int getStateValue() {
            return stateValue;
        }
    }

}
