package com.example.votingapp;

import android.graphics.Bitmap;
import java.io.File;


public class EncodeStegnography {

    private Bitmap image;
    int width;
    int height;

    public EncodeStegnography(String code, Bitmap bitmap){
        try {
            image = bitmap.copy(bitmap.getConfig(), true);
            width = image.getWidth();
            height = image.getHeight();

            int k = 0;
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

                    if(code.charAt(k) == '0'){
                        if(Math.abs(blue) % 2 == 1)
                            blue += 1;
                    }
                    else if(code.charAt(k) == '1'){
                        if(Math.abs(blue) % 2 == 0)
                            blue += 1;
                    }
                    System.out.printf("old Pixel:" + p);
                    //set the pixel value
                    p = (alpha<<24) | (red<<16) | (green<<8) | blue;
                    System.out.println(" , new pixel: " + p);
                    //set new pixel value
                    image.setPixel(j,i,p);

                    k++;
                    if(k >= code.length())
                        break;
                }

                if(k >= code.length())
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Bitmap getImage(){
        return this.image;
    }
}
