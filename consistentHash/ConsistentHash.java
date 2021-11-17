package com.example.demo1.consistentHash2;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

//这里T代表Node<String> 来使用
public class ConsistentHash<T> {
    private final HashServiceInterface hashServiceInterface;
    private final int virtualNumber;

    //SortedMap<一些虚拟节点映射到T节点的hash值,T>
    private final SortedMap<Long,T> virtualNodes = new TreeMap<>();

    public ConsistentHash(HashServiceInterface hashServiceInterface,
                          int virtualNumber,
                          Collection<T> nodes){
        this.hashServiceInterface = hashServiceInterface;
        this.virtualNumber = virtualNumber;

        for(T node:nodes){
            for(int i=0;i<virtualNumber;i++){
                //node.toString()返回node的ip
                //把node拓展成virtualNumber个 ip+i,形成hash值
                //所有hash值映射到这个node
                virtualNodes.put(this.hashServiceInterface.hash(node.toString()+i),node);
            }
        }
    }

    public T get(String key){

        Long hash = this.hashServiceInterface.hash(key);
        //virtualNodes集合里面没有key为hash
        //virtualNodes.tailMap(hash); 相当于返回key大于hash的所有键值对
        if(!virtualNodes.containsKey(hash)){
            SortedMap<Long,T> tailMap = virtualNodes.tailMap(hash);
            hash = tailMap.isEmpty() ? virtualNodes.firstKey():tailMap.firstKey();
        }

        return virtualNodes.get(hash);

    }


}
