/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.lucene.plus.knn.plugin.stats.suppliers;

import org.lucene.plus.knn.plugin.stats.KNNCounter;

import java.util.function.Supplier;

/**
 * Supplier for stats that need to keep count
 */
public class KNNCounterSupplier implements Supplier<Long> {
    private KNNCounter knnCounter;

    /**
     * Constructor
     *
     * @param knnCounter KNN Plugin Counter
     */
    public KNNCounterSupplier(KNNCounter knnCounter) {
        this.knnCounter = knnCounter;
    }

    @Override
    public Long get() {
        return knnCounter.getCount();
    }
}