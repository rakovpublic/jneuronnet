package net.layers.impl;

import net.layers.ILayer;
import net.layers.IResultLayer;
import net.storages.IInputMeta;
import net.storages.ILayerMeta;


public class LayerBuilder {
    private ILayerMeta layerMeta;
    private IInputMeta meta;

    public LayerBuilder() {

    }

    public LayerBuilder withLayer(ILayerMeta layerMeta) {
        this.layerMeta = layerMeta;
        return this;
    }

    public LayerBuilder withInput(IInputMeta meta) {
        this.meta = meta;
        return this;
    }

    public LayerBuilder withNeuronRange(int start, int end) {
        //TODO:add implementation
        return this;
    }

    public ILayer build() {
        ILayer layer = new Layer(layerMeta.getID(), meta);
        layer.registerAll(layerMeta.getNeurons());
        return layer;
    }

    public IResultLayer buildResultLayer() {
        IResultLayer layer = new ResultLayer(layerMeta.getID(), meta);
        layer.registerAll(layerMeta.getNeurons());
        //TODO:add implementation
        return layer;
    }

}
