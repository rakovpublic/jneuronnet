package net.storages.db;

import net.neuron.INeuron;
import net.storages.ILayerMeta;

import java.util.Collection;
import java.util.List;

public class DBLayerMeta implements ILayerMeta {


    @Override
    public int getID() {
        return 0;
    }

    @Override
    public List<? extends INeuron> getNeurons() {
        return null;
    }


    @Override
    public INeuron getNeuronByID(Long id) {
        return null;
    }

    @Override
    public void saveNeurons(Collection<? extends INeuron> neuronMetas) {

    }


    @Override
    public void dumpLayer() {

    }

    @Override
    public Long getSize() {
        return null;
    }
}
