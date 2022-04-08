package com.xhg.utils.emuns;

public enum AreaCode {



    YES(1),
    NO(0);
    
    private int code;

    public int getCode() {
        return code;
    }

    AreaCode(int code) {
        this.code = code;
    }

}
