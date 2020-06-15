package com.si.broker.orkestracija;

import com.si.broker.model.Endpoint;

import java.util.List;
import java.util.Map;

public class Context {

    public List<Endpoint> definedEndpoints;

    public Map<Endpoint, Boolean> availability;
}
