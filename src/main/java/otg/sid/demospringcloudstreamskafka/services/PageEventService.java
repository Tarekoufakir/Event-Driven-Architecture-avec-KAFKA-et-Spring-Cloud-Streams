package otg.sid.demospringcloudstreamskafka.services;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import otg.sid.demospringcloudstreamskafka.Entities.PageEvent;

import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PageEventService {

    @Bean
    public Consumer<PageEvent> pageEventConsumer() {
        return (input) -> {
            System.out.println("**********************");
            System.out.println(input.toString());
            System.out.println("**********************");
        };
    }
        @Bean
        public Supplier<PageEvent> pageEventSupplier(){
            return ()-> new PageEvent(

                    Math.random()>0.5?"P1":"P2",
                    Math.random()>0.5?"U1":"U2",
                    new Date(),
                    new Random().nextInt(9000));

        }
    @Bean
    public Function<PageEvent,PageEvent> pageEventFunction(){
        return (input)->{
            input.setName("L:"+input.getName().length());
            input.setUser("UUUUUU");
            return input;
        };
    }
    @Bean
    public Function<KStream<String,PageEvent>, KStream<String,Long>> kStreamKStreamFunction(){
        return  (input) ->{
            return  input
                    .filter((k,v)->v.getDuration()>100)
                    .map((k,v)->new KeyValue<>(v.getName(),0L))
                    .groupBy((k,v)->k,Grouped.with(Serdes.String(),Serdes.Long()))
                    .count()
                    .toStream();
        };
    }


}

