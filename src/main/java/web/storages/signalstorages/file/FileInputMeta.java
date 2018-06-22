package web.storages.signalstorages.file;

import exceptions.ClassFromJSONIsNotExists;
import exceptions.SerializerForClassIsNotRegistered;
import synchronizer.utils.JSONHelper;
import synchronizer.utils.JSONHelperResult;
import web.signals.ISignal;
import web.storages.IInputMeta;
import web.storages.ISerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileInputMeta implements IInputMeta<String> {
    private File file;
    private HashMap<Class<? extends ISignal>,ISerializer<? extends ISignal,String>> map;

    public FileInputMeta(File file, HashMap<Class<? extends ISignal>, ISerializer<? extends ISignal, String>> map) {
        this.file = file;
        this.map = map;
    }

    @Override
    public <S extends ISignal> void registerSerializer(ISerializer<S, String> serializer, Class<S> clazz) {
        map.put(clazz,serializer);
    }

    @Override
    public HashMap<String, List<ISignal>> readInputs(String layerId) {
        HashMap<String,  List<ISignal>> result = new HashMap<>();
        StringBuilder sb=new StringBuilder();
        BufferedReader br=null;
        FileReader fr=null;
        try {
            fr=new FileReader(file);
            br=new BufferedReader(fr);
            String content=null;
            while ((content=br.readLine())!=null) {
                sb.append(content);
            }
        }catch (IOException  ex){

        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fr!=null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String json= sb.toString();
        int startIndex=json.indexOf('[');
        JSONHelperResult res=JSONHelper.getNextJSONObject(json,startIndex);
        while (res!=null){
            String className= res.getClassName();
            String jsonObject=res.getObject();
            String neuronID=JSONHelper.extractJsonField(jsonObject,"neuronId");
            startIndex=res.getIndex();
            try {
                Class cl=Class.forName(className);
                if(map.containsKey(cl)){
                ISerializer ser=map.get(cl);
                if(result.containsKey(neuronID)){
                    result.get(neuronID).add((ISignal) ser.deserialize(json));
                }else {
                    List<ISignal> l= new ArrayList<>();
                    l.add((ISignal) ser.deserialize(json));
                    result.put(neuronID,l);
                }
                    res=JSONHelper.getNextJSONObject(json,startIndex);
                }else {
                    throw new SerializerForClassIsNotRegistered("Serializer for class"+cl+"is not registered");
                }

            } catch (ClassNotFoundException e) {
              throw new ClassFromJSONIsNotExists("Class "+className+" from this json "+ jsonObject+" is not exists");
            }
        }

        return result;
    }

    @Override
    public void saveResults( HashMap<String, List<ISignal>> signals,String layerId) {
        StringBuilder  resultJson= new StringBuilder();
        resultJson.append("{\"inputs\":[");
        for(String nrId:signals.keySet()){
            StringBuilder signal=new StringBuilder();
            signal.append("{\"neuronId\":\"");
            signal.append(nrId);
            signal.append("\",\"signal\":");
            for (ISignal s:signals.get(nrId)){
                ISerializer serializer=map.get(s.getCurrentClass());
                StringBuilder resSignal= new StringBuilder(signal);
                resSignal.append(serializer.serialize(resSignal.toString()));
                resSignal.append("},");
                resultJson.append(resSignal.toString());
            }

        }
        resultJson.deleteCharAt(resultJson.length()-1);
        resultJson.append("]}");
        //TODO: add saving to file

    }
}