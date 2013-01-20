/*
 * Brutos Web MVC http://www.brutosframework.com.br/
 * Copyright (C) 2009 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.brandao.brutos.web.http;

import java.io.IOException;
import java.util.Map;
import javax.servlet.*;
import org.brandao.brutos.*;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.web.*;

/**
 *
 * @author Afonso Brandao
 */
public class BrutosRequestFilter implements Filter{

    private FilterConfig filterConfig = null;
    private static ThreadLocal<FilterChain> currentFilter = new ThreadLocal<FilterChain>();
    private WebApplicationContext webApplicationContext;
    private WebInvoker invoker;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig   = filterConfig;
        
        webApplicationContext = ContextLoader.getCurrentWebApplicationContext();

        if( webApplicationContext == null ){
            throw new IllegalStateException(
                    "Unable to initialize the servlet was not configured for the application context root - " +
                    "make sure you have defined in your web.xml ContextLoader!"
            );
        }
        else
            invoker = (WebInvoker)((ConfigurableApplicationContext)webApplicationContext).getInvoker();

        Throwable ex = (Throwable)filterConfig.getServletContext().getAttribute( BrutosConstants.EXCEPTION );

        if( ex != null )
            throw new ServletException( ex );
        
    }

    public static FilterChain getCurrentFilterChain(){
        return currentFilter.get();
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        try{
            if( filterConfig == null )
                return;
            currentFilter.set(chain);
            invoker.invoker(request, response, chain);
        }
        finally{
            currentFilter.remove();
        }
    }

    public void destroy() {
        this.filterConfig = null;
    }

}
