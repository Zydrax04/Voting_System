package com.example.votingapp;

public class DecodeBinary {
    private String code;

    public DecodeBinary(String code){
        this.code = code;
    }

    public String decodeMessage(){
        String message = "";
        int pow = 7;
        int sum = 0;
        int bit;
        for(int i = 0; i < code.length(); i++){
            if(pow < 0){
                char c = (char) sum;
                message =  message.concat(String.valueOf(c));
                sum = 0;
                pow = 7;
            }
            if(this.code.charAt(i) == '0')
                bit = 0;
            else
                bit = 1;
            sum += bit*Math.pow(2, pow);
            pow--;
        }
        if(pow < 0){
            char c = (char) sum;
            message = message.concat(String.valueOf(c));
        }
        return message;
    }
}
