package io.udvi.rpc.common;

/**
 * Created by sureshreddyguntaka on 05/03/15.
 */
public interface RPCSerializer {

    char KRYO = 0;
    char JSON = 1;
    char MSGPACK = 2;
    char BSON = 3;
}
