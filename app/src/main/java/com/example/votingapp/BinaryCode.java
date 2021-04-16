package com.example.votingapp;

import java.util.Stack;

public class BinaryCode {
    private Stack<Integer> stack;

    public BinaryCode() {
        stack = new Stack<Integer>();
    }

    public void binaryCode(String message){
        stack.clear();
        for(int i = message.length() - 1; i >= 0; i--){
            char c = message.charAt(i);
            byte value = (byte) c;
            //transform in binary
            int counter = 0;
            while (value != 0){
                stack.push(value % 2);
                value /= 2;
                counter++;
            }
            while (counter < 8) {
                stack.push(0);
                counter++;
            }
        }
    }

    public String giveStack(){
        Stack copyStack = this.stack;
        String message = "";
        while (!copyStack.empty()){
            message = message.concat(""  + copyStack.peek());
            copyStack.pop();
        }
        return message;
    }

    public Stack<Integer> getStack() {
        return stack;
    }
}
