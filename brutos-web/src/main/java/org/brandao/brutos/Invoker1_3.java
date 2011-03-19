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

package org.brandao.brutos;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.brandao.brutos.interceptor.ImpInterceptorHandler;
import org.brandao.brutos.ioc.IOCProvider;
import org.brandao.brutos.logger.Logger;
import org.brandao.brutos.logger.LoggerProvider;
import org.brandao.brutos.mapping.Form;
import org.brandao.brutos.old.programatic.IOCManager;
import org.brandao.brutos.old.programatic.WebFrameManager;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.scope.Scopes;

/**
 * Classe usada para invocar a aplica��o.
 * 
 * @author Afonso Brandao
 */
public class Invoker1_3 extends Invoker{

    public Invoker1_3() {
    }

    public Invoker1_3( ControllerResolver controllerResolver, IOCProvider iocProvider,
            ControllerManager controllerManager, ActionResolver actionResolver,
            ApplicationContext applicationContext ){
            super( controllerResolver, iocProvider,
            controllerManager, actionResolver, applicationContext );
    }

    public boolean invoke( String requestId ){
        try{
            return invoke(BrutosContext.getCurrentInstance(),
                    (HttpServletResponse) ContextLoaderListener.currentContext);
        }
        catch( Exception e ){
            throw new BrutosException(e);
        }
    }
    /**
     * @deprecated 
     * @param brutosContext
     * @param response
     * @return .
     * @throws IOException
     */
    public boolean invoke( BrutosContext brutosContext, HttpServletResponse response ) throws IOException{

        //Form form = brutosContext.getController();
        WebFrameManager wfm = brutosContext.getWebFrameManager();

        Form form = brutosContext
                .getResolveController()
                    .getController(
                        wfm,
                        (HttpServletRequest)org.brandao.brutos.ContextLoaderListener.currentRequest.get()
                );

        long time = 0;
        if( form == null )
            //response.setStatus( response.SC_NOT_FOUND );
            return false;
        else
            brutosContext
                .getRequest()
                    .setAttribute( BrutosConstants.CONTROLLER , form);


        Scope requestScope = Scopes.get(ScopeType.REQUEST.toString());
        try{
            //compatibilidade com a vers�o 2.0
            requestScope.put( BrutosConstants.IOC_PROVIDER , this.iocProvider );
            requestScope.put( BrutosConstants.ROOT_APPLICATION_CONTEXT_ATTRIBUTE, brutosContext );

            if( logger.isDebugEnabled() ){
                logger.debug( "Received a new request" );
            }

            time = System.currentTimeMillis();

            IOCManager iocManager =
                    (IOCManager)brutosContext.getContext()
                        .getAttribute( BrutosConstants.IOC_MANAGER );

            ImpInterceptorHandler ih = new ImpInterceptorHandler();
            ih.setContext( brutosContext.getContext() );
            ih.setRequest( brutosContext.getRequest() );
            ih.setResource( iocManager.getInstance( form.getId() ) );
            ih.setResponse( response );
            ih.setURI( ih.getRequest().getRequestURI() );
            ih.setResourceAction(
                brutosContext
                    .getMethodResolver()
                        .getResourceMethod( brutosContext.getRequest() ) );

            if( logger.isDebugEnabled() ){
                logger.debug(
                    String.format(
                        "Controller: %s Method: %s",
                        form.getClass().getName() ,
                        ih.getResourceAction() == null?  "" : ih.getResourceAction().getMethod().getName() )
                );


            }
            form.proccessBrutosAction( ih );
        }
        finally{
            requestScope.remove( BrutosConstants.IOC_PROVIDER );
            if( logger.isDebugEnabled() )
                logger.debug(
                        String.format( "Request processed in %d ms",
                            (System.currentTimeMillis()-time) ) );
        }

        return true;
    }

}