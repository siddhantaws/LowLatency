/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.jcstress.infra.results;

import org.openjdk.jcstress.annotations.Result;
import sun.misc.Contended;

import java.io.Serializable;

@Result
public class ByteResult4 implements Serializable {

    @Contended
    public byte r1;

    @Contended
    public byte r2;

    @Contended
    public byte r3;

    @Contended
    public byte r4;

    @Override
    public String toString() {
        return "[" + r1 + ", " + r2 + ", " + r3 + ", " + r4 + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByteResult4 that = (ByteResult4) o;

        if (r1 != that.r1) return false;
        if (r2 != that.r2) return false;
        if (r3 != that.r3) return false;
        if (r4 != that.r4) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = r1;
        result = 31 * result + r2;
        result = 31 * result + r3;
        result = 31 * result + r4;
        return result;
    }
}
