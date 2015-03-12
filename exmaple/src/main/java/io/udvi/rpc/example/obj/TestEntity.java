package io.udvi.rpc.example.obj;

/**
 * Created by sureshreddyguntaka on 11/03/15.
 */
public class TestEntity implements Cloneable{
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    private String test;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
