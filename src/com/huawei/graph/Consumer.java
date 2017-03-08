package com.huawei.graph;

/**
 * Created by hadoop on 05/03/17.
 */
public class Consumer{
    public long id;
    public long networkid;
    public long bandrequire;

    public Consumer(){
        this.id = -1;
        this.networkid = -1;
        this.bandrequire = -1;
    }

    public Consumer(long id, long networkid, long bandrequire) {
        this.id = id;
        this.networkid = networkid;
        this.bandrequire = bandrequire;
    }
}
