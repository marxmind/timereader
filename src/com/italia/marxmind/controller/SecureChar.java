package com.italia.marxmind.controller;

import java.util.Base64;

public class SecureChar
{
    public static String encode(final String val) {
        try {
            final String encoded = Base64.getEncoder().encodeToString(val.getBytes(GlobalVar.SECURITY_ENCRYPTION_FORMAT));
            return encoded;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static String decode(final String val) {
        try {
            final byte[] barr = Base64.getDecoder().decode(val);
            return new String(barr, GlobalVar.SECURITY_ENCRYPTION_FORMAT);
        }
        catch (Exception ex) {
            return null;
        }
    }
}