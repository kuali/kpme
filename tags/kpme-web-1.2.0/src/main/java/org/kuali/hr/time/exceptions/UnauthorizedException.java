/**
 * Copyright 2004-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.hr.time.exceptions;

public class UnauthorizedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1259114952273849615L;

    
    public UnauthorizedException() {
	super();
    }

    public UnauthorizedException(String message, Throwable cause) {
	super(message, cause);
    }

    public UnauthorizedException(String message) {
	super(message);
    }

    public UnauthorizedException(Throwable cause) {
	super(cause);
    }


}
