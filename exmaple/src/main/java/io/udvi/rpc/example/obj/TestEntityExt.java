package io.udvi.rpc.example.obj;

/**
 * Created by sureshreddyguntaka on 11/03/15.
 */
public class TestEntityExt extends TestEntity{

    private String testExt;

    public String getTestExt() {
        return testExt;
    }

    public void setTestExt(String testExt) {
        this.testExt = testExt;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
