package com.alexcorrigan;

public class MessageBufferUtils {

    public static boolean charIndicatesFixField(StringBuffer messageBuffer, int c) {
        return c == '=' && messageBuffer.length() > 3;
    }

    public static boolean haveReachedEndOfMessage(StringBuffer messageBuffer) {
        boolean foundEnd;
        if (messageBuffer.charAt(messageBuffer.length() - 4) == 1 && messageBuffer.charAt(messageBuffer.length() - 2) == '0' &&  messageBuffer.charAt(messageBuffer.length() - 3) == '1' ) {
            foundEnd = true;
        } else {
            foundEnd = false;
        }
        return foundEnd;
    }

}
