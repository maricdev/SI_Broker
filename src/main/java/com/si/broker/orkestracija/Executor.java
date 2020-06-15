package com.si.broker.orkestracija;

import com.si.broker.model.Endpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Executor {

    public List<Endpoint> executePlan(Map<Endpoint, Boolean> endpoints) {
        List<Endpoint> definedPlan = new ArrayList<>();

        Iterator<Endpoint> it = endpoints.keySet().iterator();

        while (it.hasNext()) {
            Endpoint key = it.next();
            if (endpoints.get(key) == true)
                definedPlan.add(key);
        }
        return definedPlan;
    }
}
