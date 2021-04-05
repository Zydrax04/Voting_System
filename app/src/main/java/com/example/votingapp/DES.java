package com.example.votingapp;

import java.util.ArrayList;

public class DES {
    private String key = "3b3898371520f75e";
    private String binaryKey;
    private String binaryDataBlock;
    private String permutatedDataBlock;
    private int[] IP = new int[]{ 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
    private int[] IP_1 = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private int[] PC1 = new int[]{ 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
    private int[] PC2 = new int[]{ 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };
    private int[] E = new int[]{ 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
    private int[] P = new int[]{ 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private String permutatedKey;
    private ArrayList<String> C = new ArrayList<>();
    private ArrayList<String> D = new ArrayList<>();
    private ArrayList<String> K = new ArrayList<>();
    private ArrayList<String> L = new ArrayList<>();
    private ArrayList<String> R = new ArrayList<>();
    private int[] leftShifts = new int[]{0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private int[][][] S = {
            {
                    {}
            },
            { //S1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { //S2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { //S3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { //S4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { //S5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { //S6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { //S7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { //S8 finally
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    public DES(){

    }


    public String getBinaryForm(String hexValue){
        String result = "";
        for(int i = 0; i < hexValue.length(); i++){
            char c = hexValue.charAt(i);
            if(c == 'a' || c == 'A'){
                result = result.concat("1010");
            }
            if(c == 'b' || c == 'B'){
                result = result.concat("1011");
            }
            if(c == 'c' || c == 'C'){
                result = result.concat("1100");
            }
            if(c == 'd' || c == 'D'){
                result = result.concat("1101");
            }
            if(c == 'e' || c == 'E'){
                result = result.concat("1110");
            }
            if(c == 'f' || c == 'F'){
                result = result.concat("1111");
            }
            if(c == '0'){
                result = result.concat("0000");
            }
            if(c == '1'){
                result = result.concat("0001");
            }
            if(c == '2'){
                result = result.concat("0010");
            }
            if(c == '3'){
                result = result.concat("0011");
            }
            if(c == '4'){
                result = result.concat("0100");
            }
            if(c == '5'){
                result = result.concat("0101");
            }
            if(c == '6'){
                result = result.concat("0110");
            }
            if(c == '7'){
                result = result.concat("0111");
            }
            if(c == '8'){
                result = result.concat("1000");
            }
            if(c == '9'){
                result = result.concat("1001");
            }
        }
        return result;
    } //transforms hex to binary

    public String binaryToHex(String binary){ //transforms binary to Hex
        String result = "";
        for(int i = 0; i < binary.length(); i+=4){
            String value = binary.substring(i, i+4);
            if(value.equals("0000"))
                result = result.concat("0");
            if(value.equals("0001"))
                result = result.concat("1");
            if(value.equals("0010"))
                result = result.concat("2");
            if(value.equals("0011"))
                result = result.concat("3");
            if(value.equals("0100"))
                result = result.concat("4");
            if(value.equals("0101"))
                result = result.concat("5");
            if(value.equals("0110"))
                result = result.concat("6");
            if(value.equals("0111"))
                result = result.concat("7");
            if(value.equals("1000"))
                result = result.concat("8");
            if(value.equals("1001"))
                result = result.concat("9");
            if(value.equals("1010"))
                result = result.concat("a");
            if(value.equals("1011"))
                result = result.concat("b");
            if(value.equals("1100"))
                result = result.concat("c");
            if(value.equals("1101"))
                result = result.concat("d");
            if(value.equals("1110"))
                result = result.concat("e");
            if(value.equals("1111"))
                result = result.concat("f");
        }
        return result;
    }

    public void permutateKey(){ //permutate original key with PC1
        permutatedKey = "";
        for(int i = 0; i < PC1.length; i++){
            permutatedKey = permutatedKey.concat(String.valueOf(binaryKey.charAt(PC1[i]-1)));
        }
    }

    public String permutateB(String B){ //permutate B with P
        String result = "";
        for(int i = 0; i < P.length; i++){
            result = result.concat(String.valueOf(B.charAt(P[i]-1)));
        }
        return result;
    }

    public String permutateR16L16(String value){ //do the final permutation IP-1 on R[16]L[16]
        String result = "";
        for(int i = 0; i < IP_1.length; i++){
            result = result.concat(String.valueOf(value.charAt(IP_1[i]-1)));
        }
        return result;
    }

    public String expandR(int crt){ //function expands R[crt] with E
        String expandedR = "";
        for(int i = 0; i < E.length; i++){
            expandedR = expandedR.concat(String.valueOf(R.get(crt-1).charAt(E[i]-1)));
        }
        return expandedR;
    }

    public void setPermutatedDataBlock(){ //permutate the original data block with IP
        permutatedDataBlock = "";
        for(int i = 0; i < IP.length; i++){
            permutatedDataBlock = permutatedDataBlock.concat(String.valueOf(binaryDataBlock.charAt(IP[i]-1)));
        }
    }

    public String permutateBlockWithPermutation(String block, int[] perm){
        //permutate a data block with a given permutation
        String result = "";
        for(int i = 0; i < perm.length; i++){
            result = result.concat(String.valueOf(block.charAt(perm[i]-1)));
        }
        return result;
    }

    public void splitDataBlock(){ //split the data block in 2 halves
        L.clear();
        R.clear();
        L.add((String) permutatedDataBlock.subSequence(0, 32)); // L[0] is left half
        R.add((String) permutatedDataBlock.subSequence(32, 64)); // R[0] is right half
    }

    public String permutateSubKey(String subkey){ //function permutates subkey with PC2
        String result = "";
        for(int i = 0; i < PC2.length; i++){
            result = result.concat(String.valueOf(subkey.charAt(PC2[i]-1)));
        }
        return result;
    }

    public String circularShift(String value, int iterations){ //function does i iterations of left circular shift
        for(int i = 0; i < iterations; i++){
            char c = value.charAt(0); //first bit
            value = value.substring(1,value.length());
            value = value.concat(String.valueOf(c)); //add first bit to end
        }
        return value;
    }

    public void calculateSubKeys(){ //function calculates the 16 subkeys
        C.clear();
        D.clear();
        K.clear();
        C.add((String) permutatedKey.subSequence(0, 28)); // C[0] is left half
        D.add((String) permutatedKey.subSequence(28, 56)); // D[0] is right half
        K.add("this will never be used");  //K[0] so that I can count them from 1 to 16
        for(int i = 1; i <= 16; i++){
            int iterations = leftShifts[i];
            C.add(circularShift(C.get(i-1), iterations)); //calculate C[i]
            D.add(circularShift(D.get(i-1), iterations)); //calculate C[i]
            String CD = C.get(i).concat(D.get(i)); //C[i]D[i]
            K.add(permutateSubKey(CD)); //calculate K[i]
            //System.out.println(K.get(i));
        }
    }

    public int binaryToInt(String binaryValue){
        if(binaryValue.equals("01") || binaryValue.equals("0001"))
            return 1;
        if(binaryValue.equals("10") || binaryValue.equals("0010"))
            return 2;
        if(binaryValue.equals("11") || binaryValue.equals("0011"))
            return 3;
        if(binaryValue.equals("0100"))
            return 4;
        if(binaryValue.equals("0101"))
            return 5;
        if(binaryValue.equals("0110"))
            return 6;
        if(binaryValue.equals("0111"))
            return 7;
        if(binaryValue.equals("1000"))
            return 8;
        if(binaryValue.equals("1001"))
            return 9;
        if(binaryValue.equals("1010"))
            return 10;
        if(binaryValue.equals("1011"))
            return 11;
        if(binaryValue.equals("1100"))
            return 12;
        if(binaryValue.equals("1101"))
            return 13;
        if(binaryValue.equals("1110"))
            return 14;
        if(binaryValue.equals("1111"))
            return 15;
        else
            return 0; // 00 || 0000
    } //function transforms binary to Integer value

    public String intToBinary(int number){ //function transforms Integer to binary
        if(number == 0)
            return "0000";
        if(number == 1)
            return "0001";
        if(number == 2)
            return "0010";
        if(number == 3)
            return "0011";
        if(number == 4)
            return "0100";
        if(number == 5)
            return "0101";
        if(number == 6)
            return "0110";
        if(number == 7)
            return "0111";
        if(number == 8)
            return "1000";
        if(number == 9)
            return "1001";
        if(number == 10)
            return "1010";
        if(number == 11)
            return "1011";
        if(number == 12)
            return "1100";
        if(number == 13)
            return "1101";
        if(number == 14)
            return "1110";
        else // number = 153
            return "1111";
    }

    public String exclusiveOr(String value1, String value2){
        //function calculates exclusive or between 2 values and returns the result
        String result = "";
        for(int i = 0; i < value1.length(); i++){
            if(value1.charAt(i) == value2.charAt(i)) // 1 xor 1 = 0 xor 0 = 0
                result = result.concat("0");
            else
                result = result.concat("1");
        }
        return result;
    }

    public void applySubKeys(){
        for(int i = 1; i <=16; i++){
            String expandedCrtR;
            expandedCrtR = expandR(i); //32 bit R[i-1] -> 48 bit R[i-1]
            String xoredR = exclusiveOr(expandedCrtR, K.get(i)); //48 bit
            ArrayList <String> B = new ArrayList<>();
            B.add("this will never be used"); // B[0]
            String bitBlock = "";
            for(int j = 0; j < xoredR.length(); j++){ //break E(R[i-1]) xor K[i] in 6 bit blocks B[1], B[2], ..., B[8]
                if(j % 6 == 0 && j != 0){ // 6 bits
                    B.add(bitBlock);
                    bitBlock = "";
                }
                bitBlock = bitBlock.concat(String.valueOf(xoredR.charAt(j))); //add bit
            }
            B.add(bitBlock); //add last 6 bitblock
            //Apply S-boxes
            for(int j = 1; j < B.size(); j++){
                String binaryValue = String.valueOf(B.get(j).charAt(0)) + String.valueOf(B.get(j).charAt(5)); //1st bit and 6th bit
                int m = binaryToInt(binaryValue);
                binaryValue = B.get(j).substring(1,5); // 2nd to 5th bits
                int n = binaryToInt(binaryValue);
                B.set(j, intToBinary(S[j][m][n])); //substitute to 4 bits accordingly
            }
            String resultedB = "";
            for(int j = 1; j < B.size(); j++){ //concatenate B[1]->B[8]
                resultedB = resultedB.concat(B.get(j)); // 8 * 4 = 32 bits
            }
            resultedB = permutateB(resultedB); // permutate with P. 32 bits
            R.add(exclusiveOr(resultedB, L.get(i-1))); // R[i] = L[i-1] xor P(S[1](B[1])...S8(B[8])). 32 bits
            L.add(R.get(i-1)); // L[i] = R[i-1]

        }
    }


    public void generateSubKeys(){
        //key operations
        binaryKey = getBinaryForm(key); //get binary form of the key
        permutateKey();  //permutate the key
        calculateSubKeys(); //calculate the 16 subkeys
    }

    public String encrypt(String dataBlock){

        //Key operations
        generateSubKeys();
        String finalR16L16 = "";

        for(int i = 0; i < dataBlock.length(); i += 64) {
            //data block operations
            binaryDataBlock = dataBlock.substring(i, i+64);
            //System.out.println(binaryDataBlock + " (binary datablock)");
            setPermutatedDataBlock(); // permutate data block with IP
            splitDataBlock();
            applySubKeys();
            String R16L16 = R.get(16) + L.get(16);
            finalR16L16 = finalR16L16.concat(permutateR16L16(R16L16));
        }
        return finalR16L16;
    }

    private String decryptOneBlock(String cipherBlock){
        //dataBlock is binary
        R.clear();
        L.clear();
        generateSubKeys();
        for(int i = 0; i <= 16; i++){
            R.add(""); // R[16] = ""
            L.add(""); // L[16] = ""
        }
        String R16L16 = permutateBlockWithPermutation(cipherBlock, IP);
        R.set(16, R16L16.substring(0, 32)); //R16
        L.set(16, R16L16.substring(32, 64)); //L16

        for(int i = 16; i >= 1; i--){
            R.set(i-1, L.get(i)); // R[i-1] = L[i] -> 32 bits
            //f( L[i], K[i] )
            String expandedL = permutateBlockWithPermutation(L.get(i), E); //Expand L -> 48 bits
            String xoredL = exclusiveOr(expandedL, K.get(i)); // E(L[i]) xor K[i] -> 48 bits

            ArrayList <String> B = new ArrayList<>();
            B.add("this will never be used"); // B[0]
            String bitBlock = "";
            for(int j = 0; j < xoredL.length(); j++){ //break E(L[i]) xor K[i] in 6 bit blocks B[1], B[2], ..., B[8]
                if(j % 6 == 0 && j != 0){ // 6 bits
                    B.add(bitBlock);
                    bitBlock = "";
                }
                bitBlock = bitBlock.concat(String.valueOf(xoredL.charAt(j))); //add bit
            }
            B.add(bitBlock); //add last 6 bitblock
            //Apply S-boxes
            for(int j = 1; j < B.size(); j++){
                String binaryValue = String.valueOf(B.get(j).charAt(0)) + String.valueOf(B.get(j).charAt(5)); //1st bit and 6th bit
                int m = binaryToInt(binaryValue);
                binaryValue = B.get(j).substring(1,5); // 2nd to 5th bits
                int n = binaryToInt(binaryValue);
                B.set(j, intToBinary(S[j][m][n])); //substitute to 4 bits accordingly
            }
            String resultedB = "";
            for(int j = 1; j < B.size(); j++){ //concatenate B[1]->B[8]
                resultedB = resultedB.concat(B.get(j)); // 8 * 4 = 32 bits
            }
            resultedB = permutateB(resultedB); // permutate with P. 32 bits
            L.set(i-1, exclusiveOr(R.get(i), resultedB)); // L[i-1] = R[i] xor f( L[i], K[i] )
        }
        String L0R0 = L.get(0) + R.get(0);
        String plainBlock = permutateBlockWithPermutation(L0R0, IP_1);
        return plainBlock;
    }

    public String decrypt(String cipherText){
        String plainText = "";
        for(int i = 0; i < cipherText.length(); i += 64){
            plainText = plainText.concat(decryptOneBlock(cipherText.substring(i, i+64)));
        }
        return plainText;
    }
}
