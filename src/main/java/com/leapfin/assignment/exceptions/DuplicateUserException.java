package com.leapfin.assignment.exceptions;

public class DuplicateUserException extends Exception{
    public DuplicateUserException(String exc) {
        super("Specified username is already in use, please choe another username");
    }
}