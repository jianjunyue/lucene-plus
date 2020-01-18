/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lucene.search.similarities;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lucene.search.Explanation;

/**
 * Bayesian smoothing using Dirichlet priors. From Chengxiang Zhai and John
 * Lafferty. 2001. A study of smoothing methods for language models applied to
 * Ad Hoc information retrieval. In Proceedings of the 24th annual international
 * ACM SIGIR conference on Research and development in information retrieval
 * (SIGIR '01). ACM, New York, NY, USA, 334-342.
 * <p>
 * The formula as defined the paper assigns a negative score to documents that
 * contain the term, but with fewer occurrences than predicted by the collection
 * language model. The Lucene implementation returns {@code 0} for such
 * documents.
 * </p>
 * 
 * @lucene.experimental
 */
public class LMDirichletSimilarity extends LMSimilarity {
  /** The &mu; parameter. */
  private final float mu;
  
  /** Instantiates the similarity with the provided &mu; parameter. */
  public LMDirichletSimilarity(CollectionModel collectionModel, float mu) {
    super(collectionModel);
    if (Float.isFinite(mu) == false || mu < 0) {
      throw new IllegalArgumentException("illegal mu value: " + mu + ", must be a non-negative finite value");
    }
    this.mu = mu;
  }
  
  /** Instantiates the similarity with the provided &mu; parameter. */
  public LMDirichletSimilarity(float mu) {
    if (Float.isFinite(mu) == false || mu < 0) {
      throw new IllegalArgumentException("illegal mu value: " + mu + ", must be a non-negative finite value");
    }
    this.mu = mu;
  }

  /** Instantiates the similarity with the default &mu; value of 2000. */
  public LMDirichletSimilarity(CollectionModel collectionModel) {
    this(collectionModel, 2000);
  }
  
  /** Instantiates the similarity with the default &mu; value of 2000. */
  public LMDirichletSimilarity() {
    this(2000);
  }
  
  @Override
  protected double score(BasicStats stats, double freq, double docLen) {
    double score = stats.getBoost() * (Math.log(1 + freq /
        (mu * ((LMStats)stats).getCollectionProbability())) +
        Math.log(mu / (docLen + mu)));
    return score > 0.0d ? score : 0.0d;
  }

  @Override
  protected void explain(List<Explanation> subs, BasicStats stats,
      double freq, double docLen) {
    if (stats.getBoost() != 1.0d) {
      subs.add(Explanation.match((float) stats.getBoost(), "query boost"));
    }
    double p = ((LMStats)stats).getCollectionProbability();
    Explanation explP = Explanation.match((float) p,
        "P, probability that the current term is generated by the collection");
    Explanation explFreq = Explanation.match((float) freq,
        "freq, number of occurrences of term in the document");

    subs.add(Explanation.match(mu, "mu"));
    Explanation weightExpl = Explanation.match(
        (float)Math.log(1 + freq /
        (mu * ((LMStats)stats).getCollectionProbability())),
        "term weight, computed as log(1 + freq /(mu * P)) from:",
        explFreq,
        explP);
    subs.add(weightExpl);
    subs.add(Explanation.match(
        (float)Math.log(mu / (docLen + mu)),
        "document norm, computed as log(mu / (dl + mu))"));
    subs.add(Explanation.match((float) docLen,"dl, length of field"));
    super.explain(subs, stats, freq, docLen);
  }

  @Override
  protected Explanation explain(
      BasicStats stats, Explanation freq, double docLen) {
    List<Explanation> subs = new ArrayList<>();
    explain(subs, stats, freq.getValue().doubleValue(), docLen);

    return Explanation.match(
        (float) score(stats, freq.getValue().doubleValue(), docLen),
        "score(" + getClass().getSimpleName() + ", freq=" +
            freq.getValue() +"), computed as boost * " +
            "(term weight + document norm) from:",
        subs);
  }

  /** Returns the &mu; parameter. */
  public float getMu() {
    return mu;
  }
  
  @Override
  public String getName() {
    return String.format(Locale.ROOT, "Dirichlet(%f)", getMu());
  }
}
