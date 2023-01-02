package com.narola.util;

public class CommonEnums {

    public enum APIResponseCode {

        FAIL("400"), SUCCESS("200");

        private String name = null;

        APIResponseCode(String value) {
            name = value;
        }

        public String getCode() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}