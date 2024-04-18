package com.aqarati.property.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticClientConfig extends ElasticsearchConfiguration  {

    private final String userName="";
    private final String password="";

    @Value("${elastic.host}")
    private final String hostAndPort="localhost:9200";

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().
                connectedTo(hostAndPort).
                //usingSsl("http_ca.crt").
//                withBasicAuth(userName,password).
                build();
    }
}
