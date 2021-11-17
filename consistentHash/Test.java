package com.example.demo1.consistentHash2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;

public class Test {
    private static final String ip = "192.169.172.";
    public static void main(String[] args) {

        //Map<真实节点ip，映射到该节点的记录数目>
        Map<String,Integer> map = new HashMap<>();

        List<Node<String>> nodes = new ArrayList<>();

        for(int i=1;i<=10;i++){
            String nodeIp = ip + i;
            map.put(nodeIp,0);

            Node<String> node = new Node<>(nodeIp,"node"+i);
            nodes.add(node);
        }

        HashServiceInterface hashServiceInterface = new HashService();
        int virtualNumber =500;

        ConsistentHash<Node<String>> consistentHash = new ConsistentHash<>(hashServiceInterface,virtualNumber,nodes);

        for(int i = 0 ;i<6666; i++){
            String data = UUID.randomUUID().toString() + i;
            Node node = consistentHash.get(data);
            map.put(node.getIp(),map.get(node.getIp())+1);
        }

        for(int i=1 ;i<=10 ; i++){


            System.out.println(ip+i+":"+map.get(ip+i));
        }
    }
}
