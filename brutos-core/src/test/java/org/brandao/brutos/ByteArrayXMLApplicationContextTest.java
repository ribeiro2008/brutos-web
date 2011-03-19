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

import java.util.Properties;
import junit.framework.TestCase;
import org.brandao.brutos.test.MockMvcRequestFactory;
import org.brandao.brutos.test.MockMvcResponseFactory;
import org.brandao.brutos.test.MockViewProvider;

/**
 *
 * @author Brandao
 */
public class ByteArrayXMLApplicationContextTest extends TestCase{

    private Properties config;

    public ByteArrayXMLApplicationContextTest(){
        config = new Properties();

        config.setProperty(
            "org.brandao.brutos.controller.request_factory",
            MockMvcRequestFactory.class.getName());

        config.setProperty(
            "org.brandao.brutos.controller.response_factory",
            MockMvcResponseFactory.class.getName());

        config.setProperty( "org.brandao.brutos.view.provider",
            MockViewProvider.class.getName());
    }

    public void testMultipleByteArray(){
        byte[] byteArray =
                XMLHelper.getByteArray(XMLHelper.getSimpleXMLApplicationContext());

        byte[][] array = new byte[][]{byteArray,byteArray};
        ByteArrayXMLApplicationContext app =
            new ByteArrayXMLApplicationContext(array);

        app.configure(config);
    }

    public void testByteArrayApplicationContext(){
        byte[] byteArray =
                XMLHelper.getByteArray(XMLHelper.getSimpleXMLApplicationContext());

        ByteArrayXMLApplicationContext app =
            new ByteArrayXMLApplicationContext(byteArray);

        app.configure(config);
    }

    public void testByteArrayApplicationContextwithParent(){
        ApplicationContext parent = new ApplicationContext(){};
        byte[] byteArray =
                XMLHelper.getByteArray(XMLHelper.getSimpleXMLApplicationContext());

        ByteArrayXMLApplicationContext app =
            new ByteArrayXMLApplicationContext(byteArray,parent);

        app.configure(config);
    }

}
