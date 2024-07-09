package cn.cloud9.server.struct.jersey;

import cn.cloud9.server.test.model.UserModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/jersey-demo")
public class JerseyDemo {

    //path注解指定路径,get注解指定访问方式,produces注解指定了返回值类型，这里返回JSON
    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel get(){
        UserModel userModel = new UserModel();
        userModel.setId(1001);
        userModel.setPassword("123456");
        userModel.setUsername("OnCloud9");
        return userModel;
    }
}
