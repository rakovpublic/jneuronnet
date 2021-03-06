package sample;

import net.neuron.INeuron;
import net.neuron.IResultNeuron;
import net.signals.IResultSignal;

public class SimpleResultNeuron extends SimpleNeuron implements IResultNeuron<SimpleResult> {
    private Class<? extends INeuron> currentNeuronClass=SimpleResultNeuron.class;
    @Override
    public Class<? extends INeuron> getCurrentNeuronClass() {
        return currentNeuronClass;
    }

    @Override
    public void activate() {
        super.activate();
        result.clear();
        result.add(new SimpleResult());
    }


    @Override
    public SimpleResult getFinalResult() {
        return new SimpleResult();
    }
}
