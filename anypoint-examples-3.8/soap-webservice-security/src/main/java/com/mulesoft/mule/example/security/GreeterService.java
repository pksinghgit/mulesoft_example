/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package com.mulesoft.mule.example.security;

public class GreeterService implements Greeter
{
    public String greet(String name)
    {
        return "Hello " + name;
    }
}
