package com.si.broker.orkestracija;

import com.si.broker.model.Endpoint;
import com.si.broker.services.IEndpointService;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Generator {

    private final IEndpointService endpointService;
    private final Context context;
    private final Executor executor;

    public Generator(IEndpointService endpointService) {
        this.endpointService = endpointService;
        this.context = new Context();
        this.executor = new Executor();
    }

    public List<Endpoint> generatePlans(List<Endpoint> endpoints) {
        context.definedEndpoints = endpoints;
        for (Endpoint e : context.definedEndpoints)
            context.availability.put(e, isValid(e, 4000));
        return executor.executePlan(context.availability);
    }

    private Boolean isValid(Endpoint endpoint, int timeout) {
        String url = endpoint.getService().getProvider().getHost() + ":" + endpoint.getService().getProvider().getPort() + "/" + endpoint.getService().getRoute() + "/" + endpoint.getRoute();
        url = url.replaceFirst("^https", "http");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
}
