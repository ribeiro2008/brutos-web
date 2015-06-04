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

package org.brandao.brutos.annotation;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.brandao.brutos.annotation.helper.configuration.app1.App1Configuration;
import org.brandao.brutos.annotation.helper.configuration.fail.Fail1Configuration;
import org.brandao.brutos.annotation.web.test.MockAnnotationWebApplicationContext;
import org.brandao.brutos.test.MockRenderView;
import org.brandao.brutos.web.ConfigurableWebApplicationContext;
import org.brandao.brutos.web.ContextLoader;
import org.brandao.brutos.web.test.WebApplicationContextTester;
import org.brandao.brutos.web.test.WebApplicationTester;

/**
 *
 * @author Brandao
 */
public class ConfigurationTest  extends TestCase{
    
    public void test1() throws Throwable{
        WebApplicationContextTester.run(
            "/controller/myAction", 
            new WebApplicationTester() {

                public void prepareContext(Map<String, String> parameters) {
                    parameters.put(
                            ContextLoader.CONTEXT_CLASS,
                            MockAnnotationWebApplicationContext.class.getName()
                    );

                    parameters.put(
                            MockAnnotationWebApplicationContext.IGNORE_RESOURCES,
                            "true"
                    );
                }

                public void prepareSession(Map<String, String> parameters) {
                }
                
                public void prepareRequest(Map<String, String> parameters) {
                }

                public void checkResult(HttpServletRequest request, HttpServletResponse response, 
                        ServletContext context, ConfigurableWebApplicationContext applicationContext) {
                    
                    Assert.assertNotNull(applicationContext.getControllerManager().getController("/controller"));
                    MockRenderView render = (MockRenderView) applicationContext.getRenderView();
                    Assert.assertEquals("/myView.jsp", render.getView());
                }

                public void checkException(Throwable e) throws Throwable {
                    throw e;
                }
            },
            new Class[]{App1Configuration.class});
    }

    public void test2() throws Throwable{
        WebApplicationContextTester.run(
            "", 
            new WebApplicationTester() {

                public void prepareContext(Map<String, String> parameters) {
                    parameters.put(
                            ContextLoader.CONTEXT_CLASS,
                            MockAnnotationWebApplicationContext.class.getName()
                    );

                    parameters.put(
                            MockAnnotationWebApplicationContext.IGNORE_RESOURCES,
                            "true"
                    );
                }

                public void prepareSession(Map<String, String> parameters) {
                }
                
                public void prepareRequest(Map<String, String> parameters) {
                }

                public void checkResult(HttpServletRequest request, HttpServletResponse response, 
                        ServletContext context, ConfigurableWebApplicationContext applicationContext) {

                    Assert.assertNull(applicationContext.getControllerManager().getController("/controller"));
                }

                public void checkException(Throwable e) throws Throwable {
                    throw e;
                }
            },
            new Class[]{Fail1Configuration.class});
    }
    
}
