/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import org.jmock.Constraint;
import org.jmock.dynamic.Invocation;

public class MethodNameMatcher
    extends StatelessInvocationMatcher 
{
    private Constraint constraint;

    public MethodNameMatcher(Constraint constraint) {
        this.constraint = constraint;
    }

    public MethodNameMatcher(final String methodName) {
        this(new Constraint() {
            public boolean eval(Object o) {
                return methodName.equals(o);
            }
            public String toString() {
                return methodName;
            }
        });
    }

    public boolean matches(Invocation invocation) {
        return constraint.eval(invocation.getMethodName());
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append(constraint);
    }
}