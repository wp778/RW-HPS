package com.github.dr.rwserver.util.log.exp;

public class NetException extends Exception {
    public NetException(String type) {
		super(com.github.dr.rwserver.util.log.ErrorCode.valueOf(type).getError());
	}
}