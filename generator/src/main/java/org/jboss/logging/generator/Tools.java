/*
 *  JBoss, Home of Professional Open Source Copyright 2011, Red Hat, Inc., and
 *  individual contributors by the @authors tag. See the copyright.txt in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it under the
 *  terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This software is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this software; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 *  site: http://www.fsf.org.
 */
package org.jboss.logging.generator;

import org.jboss.logging.generator.apt.AptHelper;

import java.util.Arrays;
import java.util.ServiceLoader;

/**
 * This class is not thread safe. The static methods use lazy loading for static
 * variables.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a> - 21.Feb.2011
 */
public class Tools {

    private static volatile AptHelper aptHelper;
    private static volatile Annotations annotations;
    private static volatile Loggers loggers;
    private static final ServiceLoader<AptHelper> aptHelperLoader = ServiceLoader.load(AptHelper.class, Tools.class.getClassLoader());
    private static final ServiceLoader<Annotations> annotationsLoader = ServiceLoader.load(Annotations.class, Tools.class.getClassLoader());
    private static final ServiceLoader<Loggers> loggersLoader = ServiceLoader.load(Loggers.class, Tools.class.getClassLoader());

    private Tools() {
    }

    /**
     * Locates the first implementation of {@link Annotations}.
     *
     * @return the annotations to use.
     *
     * @throws IllegalStateException if the implementation could not be found.
     */
    public static Annotations annotations() {
        if (annotationsLoader == null) {
            throw servicesNotFound(Annotations.class);
        }
        Annotations result = annotations;
        if (result == null) {
            synchronized (annotationsLoader) {
                result = annotations;
                if (result == null) {
                    if (annotationsLoader.iterator().hasNext()) {
                        annotations = result = annotationsLoader.iterator().next();
                    } else {
                        throw servicesNotFound(Annotations.class);
                    }
                }
            }
        }
        return result;
    }


    /**
     * Locates the first implementation of {@link Loggers}.
     *
     * @return the loggers to use.
     *
     * @throws IllegalStateException if the implementation could not be found.
     */
    public static AptHelper aptHelper() {
        if (aptHelperLoader == null) {
            throw servicesNotFound(AptHelper.class);
        }
        AptHelper result = aptHelper;
        if (result == null) {
            synchronized (aptHelperLoader) {
                result = aptHelper;
                if (result == null) {
                    if (aptHelperLoader.iterator().hasNext()) {
                        aptHelper = result = aptHelperLoader.iterator().next();
                    } else {
                        throw servicesNotFound(AptHelper.class);
                    }
                }
            }
        }
        return result;
    }


    /**
     * Locates the first implementation of {@link Loggers}.
     *
     * @return the loggers to use.
     *
     * @throws IllegalStateException if the implementation could not be found.
     */
    public static Loggers loggers() {
        if (loggersLoader == null) {
            throw servicesNotFound(Loggers.class);
        }
        Loggers result = loggers;
        if (result == null) {
            synchronized (loggersLoader) {
                result = loggers;
                if (result == null) {
                    if (loggersLoader.iterator().hasNext()) {
                        loggers = result = loggersLoader.iterator().next();
                    } else {
                        throw servicesNotFound(Loggers.class);
                    }
                }
            }
        }
        return result;
    }

    private static IllegalStateException servicesNotFound(final Class<?> clazz) {
        final IllegalStateException result = new IllegalStateException(String.format("%1$s was not defined in META-INF/services/%1$s", clazz.getName()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
}
