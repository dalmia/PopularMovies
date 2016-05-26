/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.passenger.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String MOVIE_QUERY = "Highest Rated";
    private static final String TEST_POSTER = "test";

    // content://com.passenger.popularmovies/movies"
    private static final Uri TEST_MOVIE_DIR = MoviesContract.MoviesEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_TAG_DIR = MoviesContract.MoviesEntry.buildMovieTag(MOVIE_QUERY);
    private static final Uri TEST_MOVIE_WITH_TAG_AND_POSTER_DIR = MoviesContract.MoviesEntry.buildMovieTagAndPoster(MOVIE_QUERY, TEST_POSTER);

    public void testUriMatcher() {
        UriMatcher testMatcher = MoviesProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MoviesProvider.MOVIE);
        assertEquals("Error: The MOVIE WITH LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_TAG_DIR), MoviesProvider.MOVIE_WITH_TAG);
        assertEquals("Error: The MOVIE WITH LOCATION AND DATE URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_TAG_AND_POSTER_DIR), MoviesProvider.MOVIE_WITH_TAG_AND_POSTER);

    }
}
