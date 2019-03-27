/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wfs.perf.disruptor.support;

public enum Operation
{
    ADDITION
        {
            @Override
            public long op(final long lhs, final long rhs)
            {
                return lhs + rhs;
            }
        },

    SUBTRACTION
        {
            @Override
            public long op(final long lhs, final long rhs)
            {
                return lhs - rhs;
            }
        },

    AND
        {
            @Override
            public long op(final long lhs, final long rhs)
            {
                return lhs & rhs;
            }
        };

    public abstract long op(long lhs, long rhs);
}
