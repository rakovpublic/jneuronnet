package net.neuron.impl;

import net.neuron.INeuron;
import net.storages.INeuronMeta;
import synchronizer.Context;
import synchronizer.IContext;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/***
 * Created by Rakovskyi Dmytro on 14.06.2018.
 */
public class NeuronRunnerService {

    private Queue<INeuron> neuronQueue;
    private static NeuronRunnerService service = new NeuronRunnerService();
    private static String neuronPool = "neuron.pool.size";
    private IContext context;

    private NeuronRunnerService() {

        neuronQueue = new ConcurrentLinkedQueue<>();
        context = Context.getContext();


    }


    public void addNeuron(INeuron neuron) {
        neuronQueue.add(neuron);
    }

    public void addNeurons(List<INeuronMeta<? extends INeuron>> metas) {
        for (INeuronMeta m : metas) {
            addNeuron(m.toNeuron());
        }
    }

    public void process() {
        int poolSize = Integer.parseInt(context.getProperty(neuronPool));
        for (int i = 0; i < poolSize; i++) {
            NeuronRunner ne = new NeuronRunner(this);
            Thread th = new Thread(ne);
            th.start();
        }
    }

    Queue<INeuron> getNeuronQueue() {
        return neuronQueue;
    }


    public static NeuronRunnerService getService() {
        return service;
    }
}
