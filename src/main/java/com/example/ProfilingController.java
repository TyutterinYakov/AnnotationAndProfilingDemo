package com.example;

import com.example.annotation.PostProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfilingController implements ProfilingControllerMBean{

    @Value("${profiling.enabled}")
    private boolean enabled;

    public boolean isEnabled() {
        System.out.println("Профилирование: "+enabled);
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
