package com.example.comp;

import com.example.annotation.DeprecatedClass;
import com.example.annotation.InjectRandomInt;
import com.example.annotation.PostProxy;
import com.example.annotation.Profiling;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profiling
@DeprecatedClass(newImpl = T1000.class)
public class TerminatorQuoter implements Quoter{

    @Value("${say.message}")
    private String message;
    @InjectRandomInt(min=2, max=7)
    private int repeat;

    @PostConstruct
    public void init(){
        System.out.println("Phase 2");
        System.out.println(repeat);
    }

    public TerminatorQuoter() {
        System.out.println("Phase 1");
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    @PostProxy
    public void sayQuote() {
        System.out.println("Phase 3");
        for(int i=0; i<repeat; i++) {
            System.out.println(message);
        }
    }



    @PostProxy
    public void testPhase3(){
        System.out.println("Test phase 3");
    }
}
