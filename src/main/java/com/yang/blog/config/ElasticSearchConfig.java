package com.yang.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.yang.blog.es.dao"})
public class ElasticSearchConfig {
//    @Value("${esearch.port}") int port;
//    @Value("${esearch.host}") String hostname;
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchTemplate(client());
//    }
//
//    @Bean
//    public Client client(){
//        TransportClient client= new TransportClient();
//        TransportAddress address = new InetSocketTransportAddress(hostname, port);
//        client.addTransportAddress(address);
//        return client;
//    }
}
