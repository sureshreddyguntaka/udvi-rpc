package io.udvi.rpc.example.obj;

import io.udvi.rpc.common.annotation.Remote;

/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Remote
public interface TestInterface {

    public String thisIsAnotherMehtod(String test);

    public String methodWithOutParams();

    public void methodWithOurReturnAndParams();

    public TestEntity getTestEntity();
}
