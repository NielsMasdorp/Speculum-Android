
package com.nielsmasdorp.speculum.models.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
@SuppressWarnings("unused")
public class GoogleMapsResponse {

    private List<Result> results = new ArrayList<>();

    public List<Result> getResults() {
        return results;
    }
}