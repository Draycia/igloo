/*
 * This file is part of igloo, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.github.api.v3.implementation;

import com.google.common.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.kyori.github.api.v3.PullRequestReview;
import net.kyori.github.api.v3.PullRequestReviews;
import org.checkerframework.checker.nullness.qual.NonNull;

final class PullRequestReviewsImpl implements PullRequestReviews {
  @SuppressWarnings("UnstableApiUsage")
  private static final TypeToken<List<Partial.PullRequestReview>> GET_TYPE = new TypeToken<List<Partial.PullRequestReview>>() {};
  final HTTP.RequestTemplate request;

  PullRequestReviewsImpl(final HTTP.RequestTemplate request) {
    this.request = request;
  }

  @Override
  public @NonNull List<PullRequestReview> get() throws IOException {
    final List<Partial.PullRequestReview> partials = this.request.get().as(GET_TYPE);
    final List<PullRequestReview> reviews = new ArrayList<>(partials.size());
    for (final Partial.PullRequestReview partial : partials) {
      reviews.add(new PullRequestReviewImpl(new UserImpl(partial.user.login, partial.user.name, partial.user.avatar_url), partial.state, partial.body));
    }
    return reviews;
  }

  @Override
  public @NonNull PullRequestReview create(final PullRequestReview.@NonNull Create create) throws IOException {
    final Partial.PullRequestReview partial = this.request.post(create).as(Partial.PullRequestReview.class);
    return new PullRequestReviewImpl(new UserImpl(partial.user.login, partial.user.name, partial.user.avatar_url), partial.state, partial.body);
  }
}
