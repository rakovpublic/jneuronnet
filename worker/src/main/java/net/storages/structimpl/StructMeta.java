package net.storages.structimpl;

import net.neuron.INeuron;
import net.signals.ISignal;
import net.storages.*;
import net.study.IStudyingRequest;

import java.util.HashMap;
import java.util.List;

public class StructMeta implements IStructMeta {

    private IInputMeta initInputMeta;
    private IInputMeta hiddenInputMeta;
    private ILayersMeta layersMeta;

    public StructMeta(IInputMeta initInputMeta, IInputMeta hiddenInputMeta, ILayersMeta layersMeta) {
        this.initInputMeta = initInputMeta;
        this.hiddenInputMeta = hiddenInputMeta;
        this.layersMeta = layersMeta;
    }

    @Override
    public List<ILayerMeta> getLayers() {
        return layersMeta.getLayers();
    }


    @Override
    public IInputMeta getInputs(int layerId) {
        if (layerId == 0) {
            return initInputMeta;
        }
        return hiddenInputMeta;
    }

    @Override
    public void saveResults(int layerId, HashMap<String, List<ISignal>> meta) {
        hiddenInputMeta.saveResults(meta, layerId);
    }

    @Override
    public void study(List<IStudyingRequest> requests) {
        //TODO:write optimization now work for small local config
        for (IStudyingRequest request : requests) {
            ILayerMeta lm = layersMeta.getLayerByID(request.getLayerId());
            INeuron ner = lm.getNeuronByID(request.getNeuronId());
            ner.getAxon().resetConnection(request.getNewConnections());
        }
        for (ILayerMeta meta : layersMeta.getLayers()) {
            meta.dumpLayer();
        }
        layersMeta.getResultLayer().dumpLayer();
    }

    @Override
    public IResultLayerMeta getResultLayer() {
        return layersMeta.getResultLayer();
    }


}
