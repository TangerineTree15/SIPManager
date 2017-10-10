/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturaltel.config;

/**
 *
 * @author LaLaTsai
 */
public class InternalException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3282093384286817561L;

    public InternalException() {
        super();
    }

    public InternalException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public InternalException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InternalException(String arg0) {
        super(arg0);
    }

    public InternalException(Throwable arg0) {
        super(arg0);
    }
}
