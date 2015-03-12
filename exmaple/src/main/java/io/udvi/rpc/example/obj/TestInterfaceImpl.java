package io.udvi.rpc.example.obj;

import io.udvi.rpc.common.annotation.Remote;
import org.springframework.stereotype.Component;

@Component
public class TestInterfaceImpl implements TestInterface {


	@Override
	public String thisIsAnotherMehtod(String test) {
		System.out.println("Wow This is great ::"+ test);
		return test;
	}

	@Override
	public String methodWithOutParams(){
		System.out.println("This is great");
		return "This is great";
	}

	@Override
	public void methodWithOurReturnAndParams(){
		System.out.println("This is even great");
	}

	@Override
	public TestEntity getTestEntity() {
		TestEntity te = null;
		TestEntityExt tee = new TestEntityExt();
		tee.setTestExt("testExt");
		tee.setTest("test");
		try {
			te = (TestEntity)tee.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return te;
	}
}
