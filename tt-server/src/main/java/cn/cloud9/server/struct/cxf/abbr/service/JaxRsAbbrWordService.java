package cn.cloud9.server.struct.cxf.abbr.service;

import cn.cloud9.server.struct.cxf.abbr.AbbrWordDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@WebService
public interface JaxRsAbbrWordService {

    @POST
    @Path("/save")
    boolean saveWord(AbbrWordDTO word);

    @DELETE
    @Path("/delete/{id}")
    boolean deleteWord(@PathParam("id") Integer code);

    @PUT
    @Path("/update")
    boolean updateWord(AbbrWordDTO word);

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    List<AbbrWordDTO> listWords();

    @GET
    @Path("/page")
    @Produces({MediaType.APPLICATION_JSON})
    Page<AbbrWordDTO> pageWords(AbbrWordDTO word);

    @GET
    @Path("/find/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    AbbrWordDTO findWordById(@PathParam("id") Integer code);
}
