/*
 * Brutos Web MVC http://brutos.sourceforge.net/
 * Copyright (C) 2009 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * This library is free software. You can redistribute it 
 * and/or modify it under the terms of the GNU General Public
 * License (GPL) version 3.0 or (at your option) any later 
 * version.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/gpl.html 
 * 
 * Distributed WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied.
 *
 */

package org.brandao.brutos.ioc.picocontainer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Afonso Brandao
 */
public class SessionScope implements Scope{
    
    public SessionScope() {
    }

    public void put(String name, Object value) {
        ServletRequest request = PicoContainerContextLoaderListener.currentRequest.get();

        if( request == null )
            throw new RuntimeException( "request scope not registred" );

        HttpSession session = ((HttpServletRequest)request).getSession();
        session.setAttribute( name, value );
    }

    public Object get(String name) {
        ServletRequest request = PicoContainerContextLoaderListener.currentRequest.get();

        if( request == null )
            throw new RuntimeException( "request scope not registred" );

        HttpSession session = ((HttpServletRequest)request).getSession();
        
        return session.getAttribute( name );
    }
    
}