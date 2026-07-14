package com.duoc.guiatransporte.productor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "despacho.exchange";
    public static final String COLA_NORMAL = "cola.despachos.normal";
    public static final String COLA_DLQ = "cola.despachos.dlq";
    public static final String ROUTING_NORMAL = "despacho.crear";
    public static final String ROUTING_DLQ = "despacho.error";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange despachoExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue colaNormal() {
        return new Queue(COLA_NORMAL, true, false, false, Map.of(
                "x-dead-letter-exchange", EXCHANGE_NAME,
                "x-dead-letter-routing-key", ROUTING_DLQ
        ));
    }

    @Bean
    public Queue colaErrores() {
        return new Queue(COLA_DLQ, true);
    }

    @Bean
    public Binding bindingNormal() {
        return BindingBuilder.bind(colaNormal()).to(despachoExchange()).with(ROUTING_NORMAL);
    }

    @Bean
    public Binding bindingErrores() {
        return BindingBuilder.bind(colaErrores()).to(despachoExchange()).with(ROUTING_DLQ);
    }
}