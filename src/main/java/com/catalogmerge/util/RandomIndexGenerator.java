package com.catalogmerge.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomIndexGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(WriteToCsv.class);

    public static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars-1){
            sb.append(Integer.toUnsignedLong(r.nextInt()));
        }
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder(1);
        int randomLimitedInt = leftLimit + (int) 
                (r.nextFloat() * (rightLimit - leftLimit + 1));
              buffer.append((char) randomLimitedInt);
        String generatedString = buffer.toString();
        return generatedString.concat(sb.toString().substring(0, numchars));
    }
}
