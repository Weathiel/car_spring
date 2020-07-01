package app.entity.payloads;

import app.entity.Engine;

import java.util.HashSet;
import java.util.Set;

public class EnginesPayload {
    Set<Engine> engineSet = new HashSet<>();

    public Set<Engine> getEngineSet() {
        return engineSet;
    }

    public void setEngineSet(Set<Engine> engineSet) {
        this.engineSet = engineSet;
    }
}
