package io.udvi.rpc.example.obj;

import io.udvi.rpc.common.annotation.Remote;
import org.springframework.stereotype.Component;

@Component
@Remote
public class TestInterfaceImpl implements TestInterface {


	@Override
	public String thisIsAnotherMehtod(String test) {
		System.out.println("Wow This is great ::"+ test);
		return test;
	}

	public String methodWithOutParams(){
		return "This is great";
	}
}
