/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.leagues.exceptions;

import org.mule.module.apikit.exception.MuleRestException;

public class ConflictException extends MuleRestException {

    private static final long serialVersionUID = 3387516993124229969L;

    public ConflictException(String message) {
        super(message);
    }
}
