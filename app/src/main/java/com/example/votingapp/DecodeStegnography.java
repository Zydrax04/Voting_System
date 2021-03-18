package com.example.votingapp;

import android.graphics.Bitmap;
import java.io.File;

public class DecodeStegnography {
    private Bitmap image;
    private int width;
    private int height;
    private String encodedMsg;
    private String end = "0101110000110000";
    private boolean decoded = false;

    public DecodeStegnography(Bitmap bitmap){
        this.image = bitmap;
        int length = 0;
        this.encodedMsg = "";
        try {
            width = image.getWidth();
            height = image.getHeight();

            for(int i=0; i<height; i++) {

                for(int j=0; j<width; j++) {
                    //get pixel value
                    int p = image.getPixel(j,i);

                    //get alpha
                    int alpha = (p>>24) & 0xff;
                    //get red
                    int red = (p>>16) & 0xff;
                    //get green
                    int green = (p>>8) & 0xff;
                    //get blue
                    int blue = p & 0xff;

                    if(Math.abs(blue) % 2 == 1)
                        encodedMsg = encodedMsg.concat("1");
                    if(Math.abs(blue) % 2 == 0)
                        encodedMsg = encodedMsg.concat("0");
                    length++;
                    if(encodedMsg.length() > 16 && encodedMsg.substring(encodedMsg.length()-16).equals(end)){
                        encodedMsg = encodedMsg.substring(0, encodedMsg.length()-16);
                        decoded = true;
                        break;
                    }
                }

                if(decoded || length>150)
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getEncodedMsg(){
        return this.encodedMsg;
    }

    public boolean getDecoded(){
        return this.decoded;
    }
}
